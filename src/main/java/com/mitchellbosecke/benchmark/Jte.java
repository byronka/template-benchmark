package com.mitchellbosecke.benchmark;


import com.mitchellbosecke.benchmark.model.Stock;
import org.jusecase.jte.TemplateEngine;
import org.jusecase.jte.output.StringOutput;
import org.jusecase.jte.output.StringOutputPool;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Setup;

import java.nio.file.Path;
import java.util.List;

/**
 * Benchmark for jte templates.
 * <p>
 * https://github.com/casid/jte
 *
 * @author casid
 */
public class Jte extends BaseBenchmark {
    private final StringOutputPool stringOutputPool = new StringOutputPool();
    private TemplateEngine templateEngine;
    private List<Stock> items;

    @Setup
    public void setup() {
        items = Stock.dummyItems();

        templateEngine = TemplateEngine.createPrecompiled(Path.of("jte"));
        templateEngine.prepareForRendering("stocks.jte");
    }

    @Benchmark
    public String benchmark() {
        StringOutput output = stringOutputPool.get();
        templateEngine.render("stocks.jte", items, output);
        return output.toString();
    }
}
