/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.coursework_w2086242;

import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author saboo
 */
public class SensorReadingResource {

    private String sensorId;

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<SensorReading> getReadingHistory() {
        return DataStore.readings.stream()
                .filter(r -> r.getId().equals(this.sensorId))
                .collect(Collectors.toList());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response appendReading(SensorReading newReading) {
        if (newReading == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Reading data is missing").build();
        }

        Sensor parentSensor = DataStore.sensors.get(this.sensorId);

        if (parentSensor != null && "MAINTENANCE".equalsIgnoreCase(parentSensor.getStatus())) {
            throw new SensorUnavailableException("The sensor is currently in MAINTENANCE mode and cannot accept new readings.");
        }

        newReading.setId(this.sensorId);
        DataStore.readings.add(newReading);

        if (parentSensor != null) {
            parentSensor.setCurrentValue(newReading.getValue());
        }

        return Response.status(Response.Status.CREATED).entity(newReading).build();
    }
}
