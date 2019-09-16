package com.rest;
// Created by Руслан on 15.09.2019.

import com.Controller;
import com.google.gson.Gson;
import com.view.Record;
import com.view.View;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

@Path("task")
public class Service {
    private Controller controller = new Controller();

    /**
     * Method handling HTTP GET requests.
     */
    @GET
    public Response get(@QueryParam("id") String id) {
        try {
            UUID guid = UUID.fromString(id);
            Record record = controller.getRecord(guid);
            return Response.status(HttpServletResponse.SC_ACCEPTED).entity(serializeAsString(record)).build();

        } catch (IllegalArgumentException exception) {
            return Response.status(HttpServletResponse.SC_BAD_REQUEST).entity("Malformed GUID").build();

        } catch (NullPointerException e) {
            return Response.status(HttpServletResponse.SC_NOT_FOUND).entity("Task with that GUID was not found").build();

        } catch (Exception e) {
            return Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).entity("Something went wrong. Please check the trace logs, or contact irus2@ya.ru.").build();
        }
    }

    /**
     * Method handling HTTP POST requests.
     */
    @POST
    public Response post() {
        Record record = controller.createRecord();

        if (record != null) {
            return Response.status(HttpServletResponse.SC_ACCEPTED).entity(record.getGuid()).build();

        } else {
            return Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).entity("Something went wrong. Please check the trace logs, or contact irus2@ya.ru.").build();

        }

    }

    /**
     * Метод сериализует Java объект в json строку
     *
     * @param obj - объект
     * @return - сериализованная строка
     */
    private String serializeAsString(Object obj) throws Exception {
        if (obj == null) {
            throw new NullPointerException("Object is NULL");
        }

        View view = new View(obj);
        Gson gson = new Gson();
        return gson.toJson(view);
    }
}
