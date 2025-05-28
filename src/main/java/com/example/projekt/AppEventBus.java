package com.example.projekt;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import lombok.Getter;

public class AppEventBus {
    private static final ExecutorService executor = Executors.newCachedThreadPool();

    @Getter
    private static final EventBus asyncBus = new AsyncEventBus(executor);

}
