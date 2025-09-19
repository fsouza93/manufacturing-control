package com.example.manufacturing_control.model;

import java.util.List;
import java.util.ArrayList;

public class Product {
    private String name;
    private int fabricationTime;
    private List<Integer> assemblyTimes;
    
    public Product() {
        this.assemblyTimes = new ArrayList<>();
    }
    
    public Product(String name) {
        this.name = name;
        this.assemblyTimes = new ArrayList<>();
    }
    
    // Getters and Setters
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getFabricationTime() {
        return fabricationTime;
    }
    
    public void setFabricationTime(int fabricationTime) {
        this.fabricationTime = fabricationTime;
    }
    
    public List<Integer> getAssemblyTimes() {
        return assemblyTimes;
    }
    
    public void setAssemblyTimes(List<Integer> assemblyTimes) {
        this.assemblyTimes = assemblyTimes;
    }
    
    public int getTotalTime() {
        int total = fabricationTime;
        for (Integer time : assemblyTimes) {
            total += time;
        }
        return total;
    }
}