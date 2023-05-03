package com.mitchellbosecke.benchmark;

import java.util.ArrayList;
import java.util.List;


import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Setup;

import com.mitchellbosecke.benchmark.model.Stock;

import io.jstach.jstache.JStache;
import io.jstach.jstachio.Appender;
import io.jstach.jstachio.Formatter;
import io.jstach.jstachio.escapers.PlainText;
import io.jstach.jstachio.formatters.DefaultFormatter;

public class JStachio extends BaseBenchmark {

    // Let us cheat like rocker and jte
    private static final ThreadLocal<StringBuilder> buffer = ThreadLocal.withInitial(() -> new StringBuilder(1024 * 8));
    
    private List<Stock> items;
    private StocksModel model;
    
    Appender<StringBuilder> appender;
    Appender<Appendable> escaper;
    Formatter formatter;
    
    @Setup
    public void setup() {
        items = Stock.dummyItems();
        List<StockView> vs = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            int j = i + 1;
            StockView v = new StockView(j, j == 1, j == items.size(), items.get(i));
            vs.add(v);
        }
        
        appender = Appender.stringAppender();
        escaper = PlainText.provider();
        formatter = DefaultFormatter.provider();
        
        model = new StocksModel(vs);
    }

    @Benchmark
    public String benchmark() {
        StringBuilder sb = buffer.get();
        sb.setLength(0);

        try {
           StocksModelRenderer.render(model, sb, formatter, escaper, appender);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();

    }
    
    @JStache(path = "templates/stocks.mustache.html")
    public static class StocksModel {

        public final List<StockView> items;

        public StocksModel(List<StockView> items) {
            super();
            this.items = items;
        }

    }

    static class StockView {

        public final int index;

        public final boolean first;

        public final boolean last;

        public final Stock value;

        public final String negativeClass;

        public final String rowClass;

        public StockView(int index, boolean first, boolean last, Stock value) {
            this.index = index;
            this.first = first;
            this.last = last;
            this.value = value;
            this.negativeClass = value.getChange() > 0 ? "" : "class=\"minus\"";
            this.rowClass = index % 2 == 0 ? "even" : "odd";
        }
    }

}
