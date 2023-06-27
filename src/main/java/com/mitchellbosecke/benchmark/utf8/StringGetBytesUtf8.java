package com.mitchellbosecke.benchmark.utf8;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Setup;

import com.mitchellbosecke.benchmark.BaseBenchmark;
import com.mitchellbosecke.benchmark.JStachio.StocksModel;
import com.mitchellbosecke.benchmark.JStachioStocksTemplate;
import com.mitchellbosecke.benchmark.model.Stock;
import com.mitchellbosecke.benchmark.output.OutputKind;

import io.jstach.jstachio.Appender;
import io.jstach.jstachio.Escaper;
import io.jstach.jstachio.Formatter;
import io.jstach.jstachio.Output;
import io.jstach.jstachio.escapers.Html;
import io.jstach.jstachio.formatters.DefaultFormatter;

public class StringGetBytesUtf8 extends BaseBenchmark {

    private List<Stock> items;
    private StocksModel model;
    private JStachioStocksTemplate template;
    private Formatter formatter = DefaultFormatter.of();
    private Escaper escaper = Html.provider();
    private Appender appender = Appender.defaultAppender();
    
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
        //JStachioStocksTemplate.render(model, Output.of(sb), formatter, escaper, appender);
        return template.execute(model, sb).toString().getBytes(StandardCharsets.UTF_8);
        //return sb.toString().getBytes(StandardCharsets.UTF_8);
    }
}
