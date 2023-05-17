package com.mitchellbosecke.benchmark;

import java.util.List;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Setup;

import com.mitchellbosecke.benchmark.model.Stock;

import io.jstach.jstache.JStache;
import io.jstach.jstache.JStacheConfig;
import io.jstach.jstache.JStacheFlags;
import io.jstach.jstache.JStacheFlags.Flag;
import io.jstach.jstache.JStacheLambda;
import io.jstach.jstachio.escapers.PlainText;

public class JStachio extends BaseBenchmark {

    // Let us cheat like rocker and jte
    private static final ThreadLocal<StringBuilder> buffer = ThreadLocal.withInitial(() -> new StringBuilder(1024 * 8));
    
    private List<Stock> items;
    private StocksModel model;
    private JStachioStocksTemplate template;
    
    @Setup
    public void setup() {
        items = Stock.dummyItems();
        model = new StocksModel(items);
        template = JStachioStocksTemplate.of();
    }

    @Benchmark
    public String benchmark() {
        StringBuilder sb = buffer.get();
        sb.setLength(0);
        return template.execute(model, sb).toString();
    }
    
    @JStache(path = "templates/stocks.jstachio.html", 
            name="JStachioStocksTemplate")
    @JStacheConfig(contentType=PlainText.class)
    @JStacheFlags(flags = Flag.PRE_ENCODE)
    public static class StocksModel {

        public final List<Stock> items;

        public StocksModel(List<Stock> items) {
            this.items = items;
        }
        
        @JStacheLambda
        public boolean isPositive(Stock stock) {
            return stock.getChange() > 0;
        }
        
        @JStacheLambda
        public boolean isEven(int index) {
            return index % 2 == 0;
        }

    }

}
