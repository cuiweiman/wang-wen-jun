package com.wang.guava.eventbus.monitor;

import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;

/**
 * @description:
 * @date: 2020/8/10 23:10
 * @author: wei·man cui
 */
@Slf4j
public class FileChangeListener {

    @Subscribe
    public void onChange(FileChangeEvent event) {
        log.info("{} - {}", event.getPath(), event.getKind());
    }

}
