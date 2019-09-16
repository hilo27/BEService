package com.view;
// Created by –услан on 16.09.2019.

/**
 *  ласс €вл€ющийс€ представление записи дл€ вывода пользователю
 */
public class View {
    private String status;
    private String timestamp;

    public View(Object obj) {
        if (obj instanceof Record) {
            this.status = ((Record) obj).getStatus();
            this.timestamp = ((Record) obj).getTimestamp();
        } else {
            throw new IllegalArgumentException("Expected Record object");
        }
    }

    public String getStatus() {
        return status;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
