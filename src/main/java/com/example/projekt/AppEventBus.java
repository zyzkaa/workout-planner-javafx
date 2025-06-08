package com.example.projekt;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import lombok.Getter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppEventBus {
    private static final ExecutorService executor = Executors.newCachedThreadPool();

    @Getter
    private static final EventBus asyncBus = new AsyncEventBus(executor);

}
