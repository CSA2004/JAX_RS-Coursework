/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.coursework_w2086242;

/**
 *
 * @author saboo
 */
public class Sensor {
    private String id, type, status, roomId;
    private double currentValue;

    public Sensor() {
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public String getRoomId() {
        return roomId;
    }

    public double getCurrentValue() {
        return currentValue;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public void setCurrentValue(double currentValue) {
        this.currentValue = currentValue;
    }
    
    
}
