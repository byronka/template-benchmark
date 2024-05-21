package com.mitchellbosecke.benchmark;


import com.mitchellbosecke.benchmark.model.Stock;
import com.renomad.minum.templating.TemplateProcessor;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Setup;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class Minum extends BaseBenchmark {
    private List<Stock> items;
    private TemplateProcessor individualStockProcessor;
    private TemplateProcessor stockPrices;

    @Setup
    public void setup() throws IOException {
        items = Stock.dummyItems();
        String innerTemplate = Files.readString(Path.of("src/main/resources/templates/individual_stock.html"));
        individualStockProcessor = TemplateProcessor.buildProcessor(innerTemplate);
        String outerTemplate = Files.readString(Path.of("src/main/resources/templates/stock_prices.html"));
        stockPrices = TemplateProcessor.buildProcessor(outerTemplate);
    }

    @Benchmark
    public String benchmark() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            Stock stock = items.get(i);
            String renderedIndividualStock = individualStockProcessor.renderTemplate(Map.of(
                    "class", i % 2 == 1 ? "even" : "odd",
                    "index", String.valueOf(i + 1),
                    "symbol", stock.getSymbol(),
                    "url", stock.getUrl(),
                    "name", stock.getName(),
                    "price", String.valueOf(stock.getPrice()),
                    "is_negative_change", stock.getChange() < 0 ? " class=\"minus\"" : "",
                    "is_negative_ratio", stock.getRatio() < 0 ? " class=\"minus\"" : "",
                    "change", String.valueOf(stock.getChange()),
                    "ratio", String.valueOf(stock.getRatio())
            ));
            sb.append(renderedIndividualStock);
            if (i < items.size() - 1) sb.append('\n');
        }
        return stockPrices.renderTemplate(Map.of("individual_stocks", sb.toString()));
    }


}
