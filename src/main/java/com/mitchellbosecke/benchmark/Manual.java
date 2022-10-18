package com.mitchellbosecke.benchmark;

import java.util.ArrayList;
import java.util.List;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Setup;

import com.mitchellbosecke.benchmark.JStachio.StockView;
import com.mitchellbosecke.benchmark.JStachio.StocksModel;
import com.mitchellbosecke.benchmark.model.Stock;

public class Manual extends BaseBenchmark {
    
    private static final ThreadLocal<MyFormatter> buffer = ThreadLocal.withInitial(() -> new MyFormatter());
    
    private List<Stock> items;
    private StocksModel model;
    
    
    @Setup
    public void setup() {
        items = Stock.dummyItems();
        List<StockView> vs = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            int j = i + 1;
            StockView v = new StockView(j, j == 1, j == items.size(), items.get(i));
            vs.add(v);
        }
        
        model = new StocksModel(vs);
    }
    
    static class MyFormatter {
        private final StringBuilder buffer = new StringBuilder(1024 * 8);

        public void reset() {
            buffer.setLength(0);
        }
        
        public StringBuilder getBuffer() {
            return buffer;
        }
        
        public void append(String s) {
            buffer.append(s);
        }

        public void format(String s) {
            buffer.append(s);
            
        }

        public void format(int i) {
            buffer.append(i);
        }

        public void format(double d) {
            buffer.append(d);
            
        }
    }

    @Benchmark
    public String benchmark() {
        MyFormatter appender = buffer.get();
        appender.reset();
        try {
            ManualTemplate.render(model, appender);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appender.getBuffer().toString();

    }
    

}
