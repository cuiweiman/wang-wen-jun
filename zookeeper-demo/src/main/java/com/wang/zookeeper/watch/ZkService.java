package com.wang.zookeeper.watch;

import lombok.RequiredArgsConstructor;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @description:
 * @author: cuiweiman
 * @date: 2022/3/1 23:44
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ZkService {

    private final ZooKeeper zooKeeper;

    public String getNodeValue(String path, Boolean watch) {
        try {
            byte[] data = zooKeeper.getData(path, watch, null);
            return new String(data, StandardCharsets.UTF_8);
        } catch (KeeperException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String putNodeTtl(String path, String data, Long ttl) {
        try {
            return zooKeeper.create(path,
                    data.getBytes(StandardCharsets.UTF_8),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.PERSISTENT_WITH_TTL,
                    new Stat(),
                    ttl
            );
        } catch (KeeperException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean exists(String path) {
        try {
            Stat stat = zooKeeper.exists(path, false);
            return Objects.nonNull(stat);
        } catch (KeeperException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
