package com.wang.boot.zookeeper.zkchapter4lock;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @description: zk 分布式锁的实现
 * @date: 2021/4/7 22:40
 * @author: wei·man cui
 */
@Slf4j
public class DistributeLockZooKeeper implements DistributedLockZk {

    private final CuratorFramework client;

    /**
     * 根路径
     */
    private String rootPath;

    /**
     * 锁前缀
     */
    private String lockNamePre;

    /**
     * 用于保存某个客户端在 locker 下面创建成功的顺序节点，
     * 用于后续相关操作使用（如判断）
     */
    private String currentLockPath;

    /**
     * 最大重试次数
     */
    private static final Integer MAX_RETRY_TIMES = 10;

    public DistributeLockZooKeeper(CuratorFramework client, String rootPath, String lockNamePre) {
        this.client = client;
        this.rootPath = rootPath;
        this.lockNamePre = lockNamePre;
        init();
    }

    private void init() {
        try {
            Stat stat = client.checkExists().forPath(rootPath);
            if (Objects.isNull(stat)) {
                client.create().withMode(CreateMode.PERSISTENT).forPath(rootPath);
            }
        } catch (Exception e) {
            log.error("create rootPath error", e);
        }
    }

    private String createLockNode(String path) {
        try {
            Stat stat = client.checkExists().forPath(rootPath);
            // 判断一下根目录是否存在
            if (Objects.isNull(stat)) {
                client.create().withMode(CreateMode.PERSISTENT).forPath(rootPath);
            }
            return client.create().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath(path);
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    private String getLockNodeNumber(String str, String lockName) {
        int index = str.lastIndexOf(lockName);
        if (index >= 0) {
            index += lockName.length();
            return index <= str.length() ? str.substring(index) : "";
        }
        return str;
    }

    private List<String> getSortedChildren() {
        try {
            List<String> children = client.getChildren().forPath(rootPath);

            if (!CollectionUtils.isEmpty(children)) {
                children.sort(Comparator.comparing(lhs -> getLockNodeNumber(lhs, lockNamePre)));
            }
            log.info("sort childRen:{}", children);
            return children;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean waitToLock(long startMillis, Long millisToWait) throws Exception {
        boolean haveTheLock = false;
        boolean doDelete = false;

        try {
            while (!haveTheLock) {
                log.info("get Lock Begin");
                // 该方法实现获取locker节点下的所有顺序节点，并且从小到大排序,
                List<String> children = getSortedChildren();
                String sequenceNodeName = currentLockPath.substring(rootPath.length() + 1);

                // 计算刚才客户端创建的顺序节点在locker的所有子节点中排序位置，如果是排序为0，则表示获取到了锁
                int ourIndex = children.indexOf(sequenceNodeName);

                /*
                 * 如果在getSortedChildren中没有找到之前创建的[临时]顺序节点，这表示可能由于网络闪断而导致
                 * Zookeeper认为连接断开而删除了我们创建的节点，此时需要抛出异常，让上一级去处理
                 * 上一级的做法是捕获该异常，并且执行重试指定的次数 见后面的 attemptLock方法
                 */
                if (ourIndex < 0) {
                    log.error("not find node:{}", sequenceNodeName);
                    throw new Exception("节点没有找到: " + sequenceNodeName);
                }

                // 如果当前客户端创建的节点在locker子节点列表中位置大于0，表示其它客户端已经获取了锁
                // 此时当前客户端需要等待其它客户端释放锁，
                boolean isGetTheLock = ourIndex == 0;

                // 如何判断其它客户端是否已经释放了锁？从子节点列表中获取到比自己次小的哪个节点，并对其建立监听
                String pathToWatch = isGetTheLock ? null : children.get(ourIndex - 1);

                if (isGetTheLock) {
                    log.info("get the lock,currentLockPath:{}", currentLockPath);
                    haveTheLock = true;
                } else {
                    // 如果次小的节点被删除了，则表示当前客户端的节点应该是最小的了，所以使用CountDownLatch来实现等待
                    String previousSequencePath = rootPath.concat("/").concat(pathToWatch);
                    final CountDownLatch latch = new CountDownLatch(1);
                    final Watcher previousListener = event -> {
                        if (event.getType() == Watcher.Event.EventType.NodeDeleted) {
                            latch.countDown();
                        }
                    };

                    // 如果节点不存在会出现异常
                    client.checkExists().usingWatcher(previousListener).forPath(previousSequencePath);

                    // 如果有超时时间，刚到超时时间就返回
                    if (millisToWait != null) {
                        millisToWait -= (System.currentTimeMillis() - startMillis);
                        startMillis = System.currentTimeMillis();
                        if (millisToWait <= 0) {
                            // timed out - delete our node
                            doDelete = true;
                            break;
                        }

                        latch.await(millisToWait, TimeUnit.MICROSECONDS);
                    } else {
                        latch.await();
                    }
                }
            }
        } catch (Exception e) {
            // 发生异常需要删除节点
            log.error("waitToLock exception", e);
            doDelete = true;
            throw e;
        } finally {
            // 如果需要删除节点
            if (doDelete) {
                unLock();
            }
        }
        log.info("get Lock end,haveTheLock=" + haveTheLock);
        return haveTheLock;
    }


    private Boolean attemptLock(long time, TimeUnit unit) {
        final long startMillis = System.currentTimeMillis();
        final Long millisToWait = (unit != null) ? unit.toMillis(time) : null;

        boolean hasTheLock = false;
        boolean isDone = false;
        int retryCount = 0;

        // 网络闪断需要重试一试，最大重试次数 MAX_RETRY_COUNT
        while (!isDone) {
            isDone = true;
            try {
                currentLockPath = createLockNode(rootPath.concat("/").concat(lockNamePre));
                hasTheLock = waitToLock(startMillis, millisToWait);

            } catch (Exception e) {
                if (retryCount++ < MAX_RETRY_TIMES) {
                    isDone = false;
                } else {
                    throw new RuntimeException(e.toString());
                }
            }
        }
        return hasTheLock;
    }

    @Override
    public boolean tryLock() {
        log.info("tryLock Lock Begin");
        // 该方法实现获取locker节点下的所有顺序节点，并且从小到大排序,
        List<String> children = getSortedChildren();
        String sequenceNodeName = currentLockPath.substring(rootPath.length() + 1);

        // 计算刚才客户端创建的顺序节点在locker的所有子节点中排序位置，如果是排序为0，则表示获取到了锁
        int ourIndex = children.indexOf(sequenceNodeName);

        if (ourIndex < 0) {
            log.error("not find node:{}", sequenceNodeName);
            throw new RuntimeException("节点没有找到: " + sequenceNodeName);
        }

        // 如果当前客户端创建的节点在locker子节点列表中位置大于0，表示其它客户端已经获取了锁
        return ourIndex == 0;
    }

    @Override
    public void lock() {
        // -1,null表示阻塞等待，不设置超时时间
        attemptLock(-1, null);
    }


    @Override
    public boolean lock(long time, TimeUnit unit) {
        if (time <= 0) {
            throw new RuntimeException("Lock wait for time must greater than 0");
        }
        if (Objects.isNull(unit)) {
            throw new RuntimeException("TimeUnit can not be null");
        }
        return attemptLock(time, unit);
    }

    @Override
    public void unLock() {
        try {
            client.delete().forPath(currentLockPath);
        } catch (Exception e) {
            log.error("unLock error", e);
        }
    }
}
