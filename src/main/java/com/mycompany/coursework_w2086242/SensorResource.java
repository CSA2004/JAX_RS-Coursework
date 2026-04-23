/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.coursework_w2086242;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author saboo
 */
@Path("/sensors")
public class SensorResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerSensor(Sensor newSensor) {
        if (newSensor == null || newSensor.getId() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid sensor data").build();
        }

        Room parentRoom = DataStore.rooms.get(newSensor.getRoomId());

        if (parentRoom == null) {
            throw new LinkedResourceNotFoundException("Validation Failed: The roomId '" + newSensor.getRoomId() + "' does not exist.");
        }

        DataStore.sensors.put(newSensor.getId(), newSensor);

        if (!parentRoom.getSensorIds().contains(newSensor.getId())) {
            parentRoom.getSensorIds().add(newSensor.getId());
        }

        return Response.status(Response.Status.CREATED).entity(newSensor).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Sensor> getSensors(@QueryParam("type") String type) {
        List<Sensor> allSensors = new ArrayList<>(DataStore.sensors.values());

        if (type == null || type.isEmpty()) {
            return allSensors;
        }

        List<Sensor> filteredSensors = new ArrayList<>();
        for (Sensor s : allSensors) {
            if (type.equalsIgnoreCase(s.getType())) {
                filteredSensors.add(s);
            }
        }

        return filteredSensors;
    }

    @Path("/{sensorId}/read")
    public SensorReadingResource getReadingResource(@PathParam("sensorId") String sensorId) {
        if (!DataStore.sensors.containsKey(sensorId)) {
            throw new WebApplicationException("Sensor not found", Response.Status.NOT_FOUND);
        }
        return new SensorReadingResource(sensorId);
    }
}
