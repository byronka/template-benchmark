package com.mitchellbosecke.benchmark.utf8;

import java.io.IOException;
import java.util.List;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Setup;

import com.mitchellbosecke.benchmark.BaseBenchmark;
import com.mitchellbosecke.benchmark.JStachio;
import com.mitchellbosecke.benchmark.JStachio.StocksModel;
import com.mitchellbosecke.benchmark.JStachioStocksTemplate;
import com.mitchellbosecke.benchmark.model.Stock;
import com.mitchellbosecke.benchmark.output.JStachioAdapter;
import com.mitchellbosecke.benchmark.output.OutputKind;

import io.jstach.jstachio.escapers.Html;
import io.jstach.jstachio.formatters.SpecFormatter;

public class JStachioUtf8 extends BaseBenchmark {

    //private static final Supplier<ByteBufferedOutputStream> buffer = () -> new ByteBufferedOutputStream(1024 * 8);

    private List<Stock> items;
    private StocksModel model;
    private JStachioStocksTemplate template;
    
    @Param
    public OutputKind output;
    
    @Setup
    public void setup() {
        items = Stock.dummyItems();
        model = new StocksModel(items);
        template = new JStachioStocksTemplate(SpecFormatter.provider(), Html.of());
    }

    @Benchmark
    public byte[] benchmark() throws IOException {
        try (var out = output.create()) {
            JStachioAdapter adapter = new JStachioAdapter(output.create());
            //template.write(model, adapter);
            JStachio.execute(adapter, model);
            return adapter.getOutput().toByteArray();
        }
    }

}
