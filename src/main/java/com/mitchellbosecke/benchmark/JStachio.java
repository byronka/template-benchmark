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
import io.jstach.jstachio.Appender;
import io.jstach.jstachio.Escaper;
import io.jstach.jstachio.Formatter;
import io.jstach.jstachio.Output.EncodedOutput;
import io.jstach.jstachio.escapers.Html;
import io.jstach.jstachio.escapers.PlainText;
import io.jstach.jstachio.formatters.DefaultFormatter;
import io.jstach.jstachio.formatters.SpecFormatter;

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
    @JStacheFlags(flags = Flag.NO_NULL_CHECKING)
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
    
    private static Escaper escaper = Html.of();
    private static Formatter formatter = SpecFormatter.provider();
    
    public static void execute(EncodedOutput<? extends RuntimeException> o, StocksModel model) {
        JStachioStocksTemplate.encode(model, o, formatter, escaper, Appender.defaultAppender());
    }

}
