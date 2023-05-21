package com.mitchellbosecke.benchmark.utf8;

import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Setup;

import com.mitchellbosecke.benchmark.BaseBenchmark;
import com.mitchellbosecke.benchmark.JStachio.StocksModel;
import com.mitchellbosecke.benchmark.JStachioStocksTemplate;
import com.mitchellbosecke.benchmark.model.Stock;

import io.jstach.jstachio.escapers.Html;
import io.jstach.jstachio.formatters.DefaultFormatter;

public class JStachioUtf8 extends BaseBenchmark {

    private static final Supplier<ByteBufferedOutputStream> buffer = () -> new ByteBufferedOutputStream(1024 * 8);

    private List<Stock> items;
    private StocksModel model;
    private JStachioStocksTemplate template;
    
    @Setup
    public void setup() {
        items = Stock.dummyItems();
        model = new StocksModel(items);
        template = new JStachioStocksTemplate(DefaultFormatter.of(), Html.of());
    }

    @Benchmark
    public byte[] benchmark() throws IOException {
        ByteBufferedOutputStream sb = buffer.get();
        sb.reset();
        template.write(model, sb);
        return sb.toByteArray();
    }

}
