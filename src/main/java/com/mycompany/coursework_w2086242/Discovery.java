/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.coursework_w2086242;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author saboo
 */
public class Discovery {

    private String version = "v1.0";
    private String adminContact = "admin@smartcampus.edu";
    private Map<String, String> resources = new HashMap<>();

    public Discovery() {
        resources.put("rooms", "Coursework_w2086242/api/v1/rooms");
        resources.put("sensors", "Coursework_w2086242/api/v1/sensors");   
    }

    public String getVersion() {
        return version;
    }

    public String getAdminContact() {
        return adminContact;
    }

    public Map<String, String> getResources() {
        return resources;
    }
    
}
