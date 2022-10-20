package com.mitchellbosecke.benchmark;

import java.util.ArrayList;
import java.util.List;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Setup;

import com.mitchellbosecke.benchmark.model.Stock;

import io.jstach.Appender;
import io.jstach.Formatter;
import io.jstach.annotation.JStache;
import io.jstach.escapers.PlainText;

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
        
        appender = Appender.StringAppender.INSTANCE;
        escaper = PlainText.provides();
        formatter = Formatter.DefaultFormatter.INSTANCE;
        
        model = new StocksModel(vs);
    }

    @Benchmark
    public String benchmark() {
        StringBuilder sb = buffer.get();
        sb.setLength(0);

        try {
            StocksModelRendererDefinition.render(model, sb, appender, escaper, formatter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();

    }
    
    @JStache(path = "templates/stocks.mustache.html", templateFormat = PlainText.class)
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
