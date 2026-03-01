package com.mitchellbosecke.benchmark.utf8;

import java.io.IOException;
import java.util.List;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Setup;

import com.mitchellbosecke.benchmark.BaseBenchmark;
import com.mitchellbosecke.benchmark.model.Stock;
import com.mitchellbosecke.benchmark.output.OutputKind;
import com.mitchellbosecke.benchmark.output.RockerAdapter;

import freemarker.template.TemplateException;

/**
 * Benchmark for Rocker template engine by Fizzed.
 * 
 * https://github.com/fizzed/rocker
 * 
 * @author joelauer
 */
public class RockerUtf8 extends BaseBenchmark {

    private List<Stock> items;
    @Param
    public OutputKind output;

    @Setup
    public void setup() throws IOException {
        // no config needed, replicate stocks from context
        this.items = Stock.dummyItems();
    }

    @Benchmark
    public byte[] benchmark() throws TemplateException, IOException {
        try (var out = output.create()) {
            var adapter = new RockerAdapter(out);
            return templates.stocks.template(this.items).render(adapter).getOutput().output();
        }
    }

}
