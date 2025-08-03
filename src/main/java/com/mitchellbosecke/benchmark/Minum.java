package com.mitchellbosecke.benchmark;


import com.mitchellbosecke.benchmark.model.Stock;
import com.renomad.minum.templating.TemplateProcessor;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Setup;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Minum extends BaseBenchmark {
    private List<Stock> items;
    private TemplateProcessor stockPrices;
    List<Map<String,String>> stockPricesList;

    @Setup
    public void setup() throws IOException, InterruptedException {
        items = Stock.dummyItems();
        String innerTemplate = Files.readString(Path.of("src/main/resources/templates/individual_stock.html"));
        var individualStockProcessor = TemplateProcessor.buildProcessor(innerTemplate);
        String outerTemplate = Files.readString(Path.of("src/main/resources/templates/stock_prices.html"));
        stockPrices = TemplateProcessor.buildProcessor(outerTemplate);
        stockPrices.registerInnerTemplate("individual_stocks", individualStockProcessor);

        // create reusable maps
        stockPricesList = new ArrayList<>();

        // fill the maps with data from stocks
        for (int i = 0; i < items.size(); i++) {
            Stock stock = items.get(i);
            HashMap<String, String> stockPricesMap = new HashMap<>();
            stockPricesMap.put("class", i % 2 == 1 ? "even" : "odd"); // the example I used start with odd, so ...
            stockPricesMap.put("index", String.valueOf(i + 1));
            stockPricesMap.put("symbol", stock.getSymbol());
            stockPricesMap.put("url", stock.getUrl());
            stockPricesMap.put("name", stock.getName());
            stockPricesMap.put("price", String.valueOf(stock.getPrice()));
            stockPricesMap.put("is_negative_change", stock.getChange() < 0 ? " class=\"minus\"" : "");
            stockPricesMap.put("is_negative_ratio", stock.getRatio() < 0 ? " class=\"minus\"" : "");
            stockPricesMap.put("change", String.valueOf(stock.getChange()));
            stockPricesMap.put("ratio", String.valueOf(stock.getRatio()));
            stockPricesList.add(stockPricesMap);
        }

    }

    @Benchmark
    public String benchmark() {
        stockPrices
                .getInnerTemplate("individual_stocks")
                .registerData(stockPricesList);
        return stockPrices.renderTemplate(false);
    }

}
