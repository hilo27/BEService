package com;
// Created by ������ on 16.09.2019.

import com.sun.istack.internal.Nullable;
import com.view.Record;
import com.view.Record.*;
import com.view.Worker;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import java.sql.*;
import java.util.UUID;

public class Controller {
    private static final Logger log = LoggerFactory.getLogger(Controller.class);

    private String CLASS = "org.postgresql.Driver";
    private String URL = "jdbc:postgresql://localhost:5432/task_db";
    private String USERNAME = "postgres";
    private String PASSWORD = "postgres";

    private Connection connection = null;
    private PreparedStatement statement = null;

    /**
     * ����� ������ ������ � �� c� ��������������� GUID, ������� �������� � �������� �created�
     *
     * @return - {@link Record}
     */
    @Nullable
    public Record createRecord() {
        try {
            Record record = new Record();
            saveInDB(record);

            Worker.getInstance().add(record);

            return record;

        } catch (SQLException e) {
            log.error("������ ���� ������", e);

        } catch (Exception e) {
            log.error("���-�� ����� �� ���", e);
        }

        return null;
    }

    /**
     * ����� ��������� ������ � ��
     *
     * @return - {@link Record}
     */
    @Nullable
    public Record updateRecord(Record record) {
        try {
            if (record != null) {
                updateInDB(record);
                return record;
            }

        } catch (SQLException e) {
            log.error("������ ���� ������", e);

        } catch (Exception e) {
            log.error("���-�� ����� �� ���", e);
        }

        return null;
    }

    private void saveInDB(Record record) throws Exception {
        Class.forName(CLASS);
        connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        statement = connection.prepareStatement(record.getCreateStatement());
        statement.executeUpdate();
    }

    private void updateInDB(Record record) throws Exception {
        Class.forName(CLASS);
        connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        statement = connection.prepareStatement(record.getUpdateStatement());
        statement.executeUpdate();
    }

    /**
     * ����� ������ ������ �� �� �� ���������� guid
     *
     * @param guid - ������������� ������
     */
    @Nullable
    public Record getRecord(UUID guid) throws Exception {
        Class.forName(CLASS);
        connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        //noinspection SqlDialectInspection
        statement = connection.prepareStatement("SELECT * FROM public.task_table WHERE guid = '" + guid.toString() + "'");
        ResultSet resultSet = statement.executeQuery();

        Record record = null;

        if (resultSet.next()) {
            record = new Record();
            record.setGuid(guid.toString());
            record.setStatus(resultSet.getString(Column.STATUS));
            record.setTimestamp(resultSet.getString(Column.TIMESTAMP));
        }

        return record;
    }

}
