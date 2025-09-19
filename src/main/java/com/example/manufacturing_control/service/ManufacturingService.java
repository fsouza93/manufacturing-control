package com.example.manufacturing_control.service;

import java.util.*;
import com.example.manufacturing_control.model.Product;
import com.example.manufacturing_control.model.ProductionLine;
import com.example.manufacturing_control.model.ProductionResult;
import org.springframework.stereotype.Service;

@Service
public class ManufacturingService {

    public ProductionResult calculateProductionSchedule(ProductionLine productionLine) {
        final List<Product> products = productionLine.getProducts();
        final int n = products.size();
        final int stages = Math.min(Math.max(productionLine.getNumberOfAssemblyStages(), 1), 2); // 1..2

        // 1) ORDEM JOHNSON: Fabricação (M1) x Montagem 1 (M2 na regra)
        List<Product> ordered = applyJohnsonRuleFabVsM1(products);

        ProductionResult result = new ProductionResult();
        List<String> orderNames = new ArrayList<>();
        List<ProductionResult.ProductionStep> steps = new ArrayList<>();

        // 2) FABRICAÇÃO
        int fabClock = 0;
        List<Integer> fabCum = new ArrayList<>(n);
        Map<String,Integer> endFab = new HashMap<>();

        for (Product p : ordered) {
            orderNames.add(p.getName());
            int start = fabClock;
            int end   = start + p.getFabricationTime();
            fabClock  = end;

            steps.add(new ProductionResult.ProductionStep(p.getName(), "Fabricação", start, end));
            fabCum.add(end);
            endFab.put(p.getName(), end);
        }

        // 3) MONTAGEM 1 (flow-shop)
        int m1Clock = 0;
        List<Integer> asm1Cum = new ArrayList<>(n);
        Map<String,Integer> endM1 = new HashMap<>();
        List<String>  m1Labels = new ArrayList<>();
        List<Integer> m1CumDisp = new ArrayList<>();
        int m1DisplayClock = 0;

        for (Product p : ordered) {
            int dur = p.getAssemblyTimes().get(0);
            int earliest = endFab.get(p.getName());
            int start = Math.max(m1Clock, earliest);
            int end   = start + dur;

            // real
            steps.add(new ProductionResult.ProductionStep(p.getName(), "Montagem 1", start, end));
            m1Clock = end;
            endM1.put(p.getName(), end);
            asm1Cum.add(end);

            // exibição com OC
            int idle = start - m1DisplayClock;
            if (idle > 0) {
                m1DisplayClock += idle;
                m1Labels.add("OC" + idle);
                m1CumDisp.add(m1DisplayClock);
            }
            m1DisplayClock += dur;
            m1Labels.add("Produto " + p.getName().replace("Produto ", "") + " (" + dur + ")");
            m1CumDisp.add(m1DisplayClock);
        }

        // 4) MONTAGEM 2 (fila por disponibilidade; apenas quem tem M2>0)
        int m2Clock = 0;
        List<Integer> asm2Cum = new ArrayList<>();
        List<String>  m2Labels = new ArrayList<>();
        List<Integer> m2CumDisp = new ArrayList<>();

        if (stages == 2) {
            List<Product> withM2 = new ArrayList<>();
            for (Product p : ordered) {
                int d2 = (p.getAssemblyTimes().size() > 1) ? p.getAssemblyTimes().get(1) : 0;
                if (d2 > 0) withM2.add(p);
            }

            // ordenar pela disponibilidade (fim da M1)
            withM2.sort(Comparator.comparingInt(p -> endM1.get(p.getName())));

            int m2DisplayClock = 0;
            for (Product p : withM2) {
                int d2 = p.getAssemblyTimes().get(1);
                int earliest = endM1.get(p.getName());
                int start = Math.max(m2Clock, earliest);
                int end   = start + d2;

                // real
                steps.add(new ProductionResult.ProductionStep(p.getName(), "Montagem 2", start, end));
                m2Clock = end;
                asm2Cum.add(end);

                // exibição com OC
                int idle = start - m2DisplayClock;
                if (idle > 0) {
                    m2DisplayClock += idle;
                    m2Labels.add("OC" + idle);
                    m2CumDisp.add(m2DisplayClock);
                }
                m2DisplayClock += d2;
                m2Labels.add("Produto " + p.getName().replace("Produto ", "") + " (" + d2 + ")");
                m2CumDisp.add(m2DisplayClock);
            }
        }

        // 5) Métricas
        int makespan = (stages == 2 && !asm2Cum.isEmpty()) ? m2Clock : m1Clock;

        int totalFab = steps.stream()
            .filter(s -> s.getStage().equals("Fabricação"))
            .mapToInt(ProductionResult.ProductionStep::getDuration).sum();

        int totalAsm1 = steps.stream()
            .filter(s -> s.getStage().equals("Montagem 1"))
            .mapToInt(ProductionResult.ProductionStep::getDuration).sum();

        int totalAsm2 = steps.stream()
            .filter(s -> s.getStage().equals("Montagem 2"))
            .mapToInt(ProductionResult.ProductionStep::getDuration).sum();

        int totalAsm = totalAsm1 + totalAsm2;

        // ociosidade M1
        int idleM1 = 0, prevEnd = 0;
        for (Product p : ordered) {
            var s = findStep(steps, p.getName(), "Montagem 1");
            idleM1 += Math.max(0, s.getStartTime() - prevEnd);
            prevEnd = s.getEndTime();
        }

        // ociosidade M2
        int idleM2 = 0;
        List<ProductionResult.ProductionStep> s2 = new ArrayList<>();
        for (var s : steps) if ("Montagem 2".equals(s.getStage())) s2.add(s);
        s2.sort(Comparator.comparingInt(ProductionResult.ProductionStep::getStartTime));
        prevEnd = 0;
        for (var s : s2) {
            idleM2 += Math.max(0, s.getStartTime() - prevEnd);
            prevEnd = s.getEndTime();
        }

        int idleTotal = idleM1 + idleM2;

        // 6) Resultado
        result.setProductionOrder(orderNames);
        result.setProductionSteps(steps);
        result.setTotalProductionTime(makespan);

        result.setFabricationTime(totalFab);
        result.setAssemblyTime(totalAsm);
        result.setAssembly1Time(totalAsm1);
        result.setAssembly2Time(totalAsm2);

        result.setIdleM1(idleM1);
        result.setIdleM2(idleM2);
        result.setIdleTime(idleTotal);

        result.setNumberOfAssemblyStages(stages);
        result.setFabricationCumulativeTimes(fabCum);
        result.setAssembly1CumulativeTimes(asm1Cum);
        result.setAssembly2CumulativeTimes(asm2Cum);

        result.setAssembly1DisplayLabels(m1Labels);
        result.setAssembly1DisplayCumTimes(m1CumDisp);
        result.setAssembly2DisplayLabels(m2Labels);
        result.setAssembly2DisplayCumTimes(m2CumDisp);

        return result;
    }

    // Johnson para 2 máquinas: M1 = Fabricação, M2 = Montagem 1
    // Empates ajustados para casar com seu exemplo prático:
    private List<Product> applyJohnsonRuleFabVsM1(List<Product> products) {
        List<Product> rem = new ArrayList<>(products);
        Product[] res = new Product[products.size()];
        int L = 0, R = res.length - 1;

        while (!rem.isEmpty()) {
            int min = Integer.MAX_VALUE;
            for (Product p : rem) {
                min = Math.min(min, Math.min(p.getFabricationTime(), p.getAssemblyTimes().get(0)));
            }
            List<Product> minOnM1 = new ArrayList<>();
            List<Product> minOnM2 = new ArrayList<>();
            for (Product p : rem) {
                if (p.getFabricationTime() == min) minOnM1.add(p);
                else if (p.getAssemblyTimes().get(0) == min) minOnM2.add(p);
            }

            if (!minOnM1.isEmpty()) {
                // desempate: maior M2 primeiro
                minOnM1.sort((a,b) -> Integer.compare(b.getAssemblyTimes().get(0), a.getAssemblyTimes().get(0)));
                Product chosen = minOnM1.get(0);
                res[L++] = chosen;
                rem.remove(chosen);
            } else {
                // mínimo na M2: desempate por menor M1 primeiro
                minOnM2.sort(Comparator.comparingInt(Product::getFabricationTime));
                Product chosen = minOnM2.get(0);
                res[R--] = chosen;
                rem.remove(chosen);
            }
        }
        return Arrays.asList(res);
    }

    private ProductionResult.ProductionStep findStep(List<ProductionResult.ProductionStep> steps,
                                                     String product, String stage) {
        for (ProductionResult.ProductionStep s : steps) {
            if (s.getProductName().equals(product) && s.getStage().equals(stage)) return s;
        }
        throw new IllegalStateException("Step not found: " + product + " / " + stage);
    }
}
