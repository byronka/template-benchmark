package com.mitchellbosecke.benchmark.utf8;

import java.util.List;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Setup;

import com.mitchellbosecke.benchmark.BaseBenchmark;
import com.mitchellbosecke.benchmark.JStachio.StocksModel;
import com.mitchellbosecke.benchmark.JStachioStocksTemplate;
import com.mitchellbosecke.benchmark.model.Stock;
import com.mitchellbosecke.benchmark.output.Utf8Output;

import io.jstach.jstachio.Escaper;
import io.jstach.jstachio.Formatter;
import io.jstach.jstachio.escapers.Html;
import io.jstach.jstachio.formatters.DefaultFormatter;

public class StringGetBytesUtf8 extends BaseBenchmark {

    private List<Stock> items;
    private StocksModel model;
    private JStachioStocksTemplate template;
    private Formatter formatter = DefaultFormatter.of();
    private Escaper escaper = Html.provider();
    
    @Param({"N/A"})
    String output;
    
    @Setup
    public void setup() {
        items = Stock.dummyItems();
        model = new StocksModel(items);
        template = new JStachioStocksTemplate(formatter, escaper);
    }

    @Benchmark
    public byte[] benchmark() {
        StringBuilder sb = new StringBuilder(8 * 1024);
        return Utf8Output.output(template.execute(model, sb));
    }
}
