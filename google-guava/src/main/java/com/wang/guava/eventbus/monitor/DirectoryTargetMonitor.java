package com.wang.guava.eventbus.monitor;

import com.google.common.eventbus.EventBus;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.*;

/**
 * @description:
 * @date: 2020/8/10 22:53
 * @author: wei·man cui
 */
@Slf4j
public class DirectoryTargetMonitor implements TargetMonitor {

    private WatchService watchService;

    private final EventBus eventBus;

    private final Path path;

    private volatile boolean start = false;

    public DirectoryTargetMonitor(EventBus eventBus, String path) {
        this(eventBus, path, "");
    }

    public DirectoryTargetMonitor(EventBus eventBus, String path, String... morePaths) {
        this.eventBus = eventBus;
        this.path = Paths.get(path, morePaths);
    }

    @Override
    public void startMonitor() throws Exception {
        this.watchService = FileSystems.getDefault().newWatchService();
        this.path.register(watchService,
                StandardWatchEventKinds.ENTRY_MODIFY,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_DELETE);
        log.info("The directory {} is monitoring...", path);
        this.start = true;
        while (start) {
            WatchKey watchKey = null;
            try {
                watchKey = watchService.take();
                watchKey.pollEvents().forEach(event -> {
                    WatchEvent.Kind<?> kind = event.kind();
                    Path path = (Path) event.context();
                    Path child = DirectoryTargetMonitor.this.path.resolve(path);
                    eventBus.post(new FileChangeEvent(child, kind));
                });
            } catch (Exception e) {
                this.start = false;
            } finally {
                if (watchKey != null) {
                    watchKey.reset();
                }
            }
        }
    }

    @Override
    public void stopMonitor() throws Exception {
        log.info("The directory {} monitor will be stop... ", path);
        Thread.currentThread().interrupt();
        this.start = false;
        this.watchService.close();
        log.info("The directory {} monitor has been stopped!", path);
    }
}
