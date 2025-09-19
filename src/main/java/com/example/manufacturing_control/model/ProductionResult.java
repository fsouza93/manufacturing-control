package com.example.manufacturing_control.model;

import java.util.List;

public class ProductionResult {

    private List<String> productionOrder;
    private List<ProductionStep> productionSteps;

    // Totais gerais
    private int totalProductionTime;  // mantém para quem quiser ver o makespan
    private int fabricationTime;      // soma das durações de fabricação
    private int assemblyTime;         // soma M1 + M2

    // NOVO: totais separados por montagem
    private int assembly1Time;
    private int assembly2Time;

    // Ociosidade
    private int idleTime; // M1 + M2
    private int idleM1;   // NOVO
    private int idleM2;   // NOVO

    // Quantidade de etapas de montagem (1 ou 2)
    private int numberOfAssemblyStages;

    // Cumulativos "clássicos"
    private List<Integer> fabricationCumulativeTimes;
    private List<Integer> assembly1CumulativeTimes;
    private List<Integer> assembly2CumulativeTimes;

    // Listas para exibição "tipo Excel" com OC nas Montagens
    private List<String>  assembly1DisplayLabels;
    private List<Integer> assembly1DisplayCumTimes;
    private List<String>  assembly2DisplayLabels;
    private List<Integer> assembly2DisplayCumTimes;

    // ---------- Step ----------
    public static class ProductionStep {
        private String productName;
        private String stage;
        private int startTime;
        private int endTime;
        private int duration;

        public ProductionStep(String productName, String stage, int startTime, int endTime) {
            this.productName = productName;
            this.stage = stage;
            this.startTime = startTime;
            this.endTime = endTime;
            this.duration = endTime - startTime;
        }

        public String getProductName() { return productName; }
        public String getStage()       { return stage; }
        public int getStartTime()      { return startTime; }
        public int getEndTime()        { return endTime; }
        public int getDuration()       { return duration; }

        public void setProductName(String productName) { this.productName = productName; }
        public void setStage(String stage)             { this.stage = stage; }
        public void setStartTime(int startTime)        { this.startTime = startTime; }
        public void setEndTime(int endTime)            { this.endTime = endTime; }
        public void setDuration(int duration)          { this.duration = duration; }
    }

    // ---------- Getters/Setters ----------
    public List<String> getProductionOrder() { return productionOrder; }
    public void setProductionOrder(List<String> productionOrder) { this.productionOrder = productionOrder; }

    public List<ProductionStep> getProductionSteps() { return productionSteps; }
    public void setProductionSteps(List<ProductionStep> productionSteps) { this.productionSteps = productionSteps; }

    public int getTotalProductionTime() { return totalProductionTime; }
    public void setTotalProductionTime(int totalProductionTime) { this.totalProductionTime = totalProductionTime; }

    public int getFabricationTime() { return fabricationTime; }
    public void setFabricationTime(int fabricationTime) { this.fabricationTime = fabricationTime; }

    public int getAssemblyTime() { return assemblyTime; }
    public void setAssemblyTime(int assemblyTime) { this.assemblyTime = assemblyTime; }

    public int getAssembly1Time() { return assembly1Time; }
    public void setAssembly1Time(int assembly1Time) { this.assembly1Time = assembly1Time; }

    public int getAssembly2Time() { return assembly2Time; }
    public void setAssembly2Time(int assembly2Time) { this.assembly2Time = assembly2Time; }

    public int getIdleTime() { return idleTime; }
    public void setIdleTime(int idleTime) { this.idleTime = idleTime; }

    public int getIdleM1() { return idleM1; }
    public void setIdleM1(int idleM1) { this.idleM1 = idleM1; }

    public int getIdleM2() { return idleM2; }
    public void setIdleM2(int idleM2) { this.idleM2 = idleM2; }

    public int getNumberOfAssemblyStages() { return numberOfAssemblyStages; }
    public void setNumberOfAssemblyStages(int numberOfAssemblyStages) { this.numberOfAssemblyStages = numberOfAssemblyStages; }

    public List<Integer> getFabricationCumulativeTimes() { return fabricationCumulativeTimes; }
    public void setFabricationCumulativeTimes(List<Integer> fabricationCumulativeTimes) { this.fabricationCumulativeTimes = fabricationCumulativeTimes; }

    public List<Integer> getAssembly1CumulativeTimes() { return assembly1CumulativeTimes; }
    public void setAssembly1CumulativeTimes(List<Integer> assembly1CumulativeTimes) { this.assembly1CumulativeTimes = assembly1CumulativeTimes; }

    public List<Integer> getAssembly2CumulativeTimes() { return assembly2CumulativeTimes; }
    public void setAssembly2CumulativeTimes(List<Integer> assembly2CumulativeTimes) { this.assembly2CumulativeTimes = assembly2CumulativeTimes; }

    public List<String> getAssembly1DisplayLabels() { return assembly1DisplayLabels; }
    public void setAssembly1DisplayLabels(List<String> assembly1DisplayLabels) { this.assembly1DisplayLabels = assembly1DisplayLabels; }

    public List<Integer> getAssembly1DisplayCumTimes() { return assembly1DisplayCumTimes; }
    public void setAssembly1DisplayCumTimes(List<Integer> assembly1DisplayCumTimes) { this.assembly1DisplayCumTimes = assembly1DisplayCumTimes; }

    public List<String> getAssembly2DisplayLabels() { return assembly2DisplayLabels; }
    public void setAssembly2DisplayLabels(List<String> assembly2DisplayLabels) { this.assembly2DisplayLabels = assembly2DisplayLabels; }

    public List<Integer> getAssembly2DisplayCumTimes() { return assembly2DisplayCumTimes; }
    public void setAssembly2DisplayCumTimes(List<Integer> assembly2DisplayCumTimes) { this.assembly2DisplayCumTimes = assembly2DisplayCumTimes; }
}
