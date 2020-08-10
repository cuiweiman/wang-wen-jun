package com.wang.guava.eventbus.monitor;

import lombok.Getter;

import java.nio.file.Path;
import java.nio.file.WatchEvent;

/**
 * @description:
 * @date: 2020/8/10 23:06
 * @author: wei·man cui
 */
@Getter
public class FileChangeEvent {

    private final Path path;

    private final WatchEvent.Kind<?> kind;

    public FileChangeEvent(Path path, WatchEvent.Kind<?> kind) {
        this.path = path;
        this.kind = kind;
    }
}
