package com.mitchellbosecke.benchmark;

import java.io.IOException;
import java.util.List;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Setup;

import com.fizzed.rocker.RockerOutputFactory;
import com.fizzed.rocker.runtime.ArrayOfByteArraysOutput;
import com.mitchellbosecke.benchmark.model.Stock;

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
    private RockerOutputFactory<ArrayOfByteArraysOutput> factory;

    @Setup
    public void setup() throws IOException {
        // no config needed, replicate stocks from context
        this.items = Stock.dummyItems();
        factory = ArrayOfByteArraysOutput.FACTORY;
    }

    @Benchmark
    public byte[] benchmark() throws TemplateException, IOException {
        return templates.stocks
                .template(this.items)
                .render(factory)
                .toByteArray();
    }

}
