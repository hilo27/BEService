package com.view;
// Created by ������ on 16.09.2019.

import jersey.repackaged.com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Worker {
    private ExecutorService executor = Executors.newFixedThreadPool(15, new ThreadFactoryBuilder().setNameFormat("Thread-%d").build());
    private static Worker instance;

    private Worker() {
    }

    /**
     * �������� ��� �������
     *
     * @return �������
     */
    public static Worker getInstance() {
        synchronized (Worker.class) {
            if (instance == null) {
                instance = new Worker();
            }
        }
        return instance;
    }

    public void add(Record record) {
        executor.execute(record);
    }

}
