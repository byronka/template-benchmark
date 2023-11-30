package com.mitchellbosecke.benchmark;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.Nullable;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Setup;

import com.mitchellbosecke.benchmark.model.Stock;

import io.jstach.jstache.JStache;
import io.jstach.jstache.JStacheConfig;
import io.jstach.jstache.JStacheLambda;
import io.jstach.jstachio.context.ContextNode;
import io.jstach.jstachio.context.ObjectContext;
import io.jstach.jstachio.escapers.PlainText;

public class JStachioContext extends BaseBenchmark {
    
    StocksContext model;
    StocksContextRenderer template;
    
    private static final ThreadLocal<StringBuilder> buffer = ThreadLocal.withInitial(() -> new StringBuilder(1024 * 8));

    
    @Setup
    public void setup() {
        var original = Stock.dummyItems();
        List<Map<String,Object>> items = new ArrayList<>();
        for (var i : original) {
            Map<String,Object> item = new HashMap<>();
            item.put("change", i.getChange());
            item.put("name", i.getName());
            item.put("name2", i.getName2());
            item.put("price", i.getPrice());
            item.put("ration", i.getRatio());
            item.put("symbol", i.getSymbol());
            item.put("url", i.getUrl());
            items.add(item);
        }
        model = new StocksContext(Map.<String,Object>of("items", items));
        template = StocksContextRenderer.of();
    }
    
    @Benchmark
    public String benchmark() {
        StringBuilder sb = buffer.get();
        sb.setLength(0);
        return template.execute(model, sb).toString();
    }
    
    @JStache(path = "templates/stocks.jstachio.html")
    @JStacheConfig(contentType = PlainText.class)
    public class StocksContext extends ObjectContext {

        private final Map<String, Object> model;
        
        public StocksContext(Map<String, Object> model) {
            super();
            this.model = model;
        }

        @Override
        public @Nullable Object getValue(String key) {
            return model.get(key);
        }
        
        
        @JStacheLambda
        public boolean isPositive(ContextNode stock) {
            return ((double)stock.get("change").object()) > 0;
        }
        
        @JStacheLambda
        public boolean isEven(int index) {
            return index % 2 == 0;
        }
        
    }

}
