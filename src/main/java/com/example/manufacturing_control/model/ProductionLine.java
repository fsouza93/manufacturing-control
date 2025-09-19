package com.example.manufacturing_control.model;

import java.util.List;
import java.util.ArrayList;


public class ProductionLine {
    private int numberOfProducts;
    private int numberOfAssemblyStages;
    private List<Product> products;
    
    public ProductionLine() {
        this.products = new ArrayList<>();
    }
    
    public void initializeProducts() {
        products.clear();
        for (int i = 0; i < numberOfProducts; i++) {
            Product product = new Product("Produto " + (char)('A' + i));
            // Inicializar lista de tempos de montagem
            for (int j = 0; j < numberOfAssemblyStages; j++) {
                product.getAssemblyTimes().add(0);
            }
            products.add(product);
        }
    }
    
    // Getters and Setters
    public int getNumberOfProducts() {
        return numberOfProducts;
    }
    
    public void setNumberOfProducts(int numberOfProducts) {
        this.numberOfProducts = numberOfProducts;
    }
    
    public int getNumberOfAssemblyStages() {
        return numberOfAssemblyStages;
    }
    
    public void setNumberOfAssemblyStages(int numberOfAssemblyStages) {
        this.numberOfAssemblyStages = numberOfAssemblyStages;
    }
    
    public List<Product> getProducts() {
        return products;
    }
    
    public void setProducts(List<Product> products) {
        this.products = products;
    }
}