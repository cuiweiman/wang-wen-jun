package com.wang.boot.zookeeper.zkchapter2;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.*;
import java.util.Objects;

/**
 * @description:
 * @date: 2021/4/5 23:24
 * @author: wei·man cui
 */
public class Executor implements Watcher, Runnable, DataMonitor.DataMonitorListener {
    private String zNode;
    private DataMonitor dataMonitor;
    private ZooKeeper zk;
    private String pathName;
    private String[] exec;
    private Process child;

    public Executor(String hostPort, String zNode, String fileName, String[] exec) throws IOException {
        this.pathName = fileName;
        this.exec = exec;
        zk = new ZooKeeper(hostPort, 3000, this);
        dataMonitor = new DataMonitor(zk, zNode, this);
    }

    public static void main(String[] args) {
        if (args.length < 4) {
            System.out.println("USAGE:Executor hostPort zNode pathname program [args ...]");
            System.exit(2);
        }
        String hostPort = args[0];
        String zNode = args[1];
        String fileName = args[2];
        String[] exec = new String[args.length - 3];
        System.arraycopy(args, 3, exec, 0, exec.length);
        try {
            new Executor(hostPort, zNode, fileName, exec).run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ZNode 发送变化时，都会触发 此方法
     *
     * @param watchedEvent 发生监听事件
     */
    @Override
    public void process(WatchedEvent watchedEvent) {
        dataMonitor.handler(watchedEvent);
    }

    @Override
    public void run() {
        try {
            synchronized (this) {
                while (!dataMonitor.dead) {
                    wait();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void exists(byte[] data) {
        if (Objects.isNull(data)) {
            if (child != null) {
                System.out.println("Killing handler");
                child.destroy();
                try {
                    child.waitFor();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            child = null;
        } else {
            if (child != null) {
                System.out.println("Stopping child");
                child.destroy();
                try {
                    child.waitFor();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                FileOutputStream fos = new FileOutputStream(new File(pathName));
                fos.write(data);
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                System.out.println("Starting child");
                child = Runtime.getRuntime().exec(exec);
                new StreamWriter(System.out, child.getInputStream());
                new StreamWriter(System.err, child.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void closing(int rc) {
        synchronized (this) {
            notifyAll();
        }
    }

    static class StreamWriter extends Thread {
        OutputStream os;
        InputStream is;

        public StreamWriter(OutputStream os, InputStream is) {
            this.os = os;
            this.is = is;
        }

    }
}
