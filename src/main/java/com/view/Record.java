package com.view;
// Created by Руслан on 16.09.2019.

import com.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;


/**
 * Класс являющийся представление записи в БД
 */
public class Record implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(Record.class);

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        try {
            Controller controller = new Controller();
            this.setStatus(Status.RUNNING);
            this.setNewTime();
            controller.updateRecord(this);

            Thread.sleep(9000);

            controller = new Controller();
            this.setStatus(Status.FINISHED);
            this.setNewTime();
            controller.updateRecord(this);

        } catch (InterruptedException e) {
            log.error("Thread interrupted");
        } catch (Exception e) {
            log.error("Cant");
        }
    }

    public interface Column {
        String GUID = "guid";
        String STATUS = "status";
        String TIMESTAMP = "timestamp";
    }

    public interface Status {
        String CREATED = "CREATED";
        String RUNNING = "RUNNING";
        String FINISHED = "FINISHED";
    }

    private String guid;
    private String timestamp;
    private String status;

    public Record() {
        this.guid = UUID.randomUUID().toString();
        this.timestamp = setNewTime();
        this.status = Status.CREATED;
    }

    // TODO (Руслан): я отдаю себе отчёт, что это полная хрень, но увы нет времени :(
    public String getCreateStatement() {
        return "INSERT INTO task_table (" + Column.GUID + ", " + Column.TIMESTAMP + ", " + Column.STATUS + " )" +
                "VALUES ('" + guid + "','" + timestamp + "', '" + status + "');";
    }

    // TODO (Руслан): я отдаю себе отчёт, что это полная хрень, но увы нет времени :(
    public String getUpdateStatement() {
        return "UPDATE task_table SET " +
                Column.STATUS + " = '" + status + "', " +
                Column.TIMESTAMP + " = '" + timestamp +"'" +
                " WHERE " + Column.GUID + " = '" + guid + "';";
    }

    public String getGuid() {
        return guid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String setNewTime() {
        this.timestamp = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_DATE_TIME);
        return this.timestamp;
    }
}
