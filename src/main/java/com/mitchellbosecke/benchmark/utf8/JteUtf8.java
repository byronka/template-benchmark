package com.mitchellbosecke.benchmark.utf8;


import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Setup;

import com.mitchellbosecke.benchmark.BaseBenchmark;
import com.mitchellbosecke.benchmark.model.Stock;
import com.mitchellbosecke.benchmark.output.JteAdapter;
import com.mitchellbosecke.benchmark.output.OutputKind;

import gg.jte.ContentType;
import gg.jte.TemplateEngine;

/**
 * Benchmark for jte templates.
 * <p>
 * https://github.com/casid/jte
 *
 * @author casid
 */
public class JteUtf8 extends BaseBenchmark {

    private TemplateEngine templateEngine;
    private List<Stock> items;
    @Param
    public OutputKind output;

    @Setup
    public void setup() {
        items = Stock.dummyItems();
        templateEngine = TemplateEngine.createPrecompiled(Path.of("jte-classes"), ContentType.Html);
        templateEngine.setBinaryStaticContent(true);
        templateEngine.prepareForRendering("stocks.jte");
    }

    @Benchmark
    public byte[] benchmark() throws IOException {
        try(var out = output.create()) {
            JteAdapter adapter = new JteAdapter(out);
            templateEngine.render("stocks.jte", items, adapter);
            return adapter.getOutput().output();
        }
    }
}
