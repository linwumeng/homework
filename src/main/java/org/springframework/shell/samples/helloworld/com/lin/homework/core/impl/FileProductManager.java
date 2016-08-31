package org.springframework.shell.samples.helloworld.com.lin.homework.core.impl;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.shell.samples.helloworld.com.lin.homework.core.ProductManager;
import org.springframework.shell.samples.helloworld.com.lin.homework.core.model.Material;
import org.springframework.shell.samples.helloworld.com.lin.homework.core.model.Product;
import org.springframework.shell.samples.helloworld.com.lin.homework.core.model.Supply;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by linwumeng on 8/31/16.
 */
public class FileProductManager implements ProductManager {

    private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private Reader supplyReader;
    private Reader productReader;

    public FileProductManager(String product, String supply) throws FileNotFoundException {
        productReader = new FileReader(product);
        supplyReader = new FileReader(supply);
    }

    @Override
    public Product findByCode(String code) {
        try {
            Map<String, Product> products = readProduct();
            return products.get(code);
        } catch (Exception e) {
            // log the error
        }

        return null;
    }

    private Map<String, List<Supply>> readSupply() throws IOException, ParseException {
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withHeader("id","start","end","amount").withSkipHeaderRecord().parse(supplyReader);
        Map<String, List<Supply>> supplies = new HashMap<>();

        for (CSVRecord record : records) {
            String id = record.get("id");
            String start = record.get("start");
            String end = record.get("end");
            int amount = Integer.parseInt(record.get("amount"));

            Supply s = new Supply(df.parse(start), df.parse(end), amount);

            if (supplies.get(id) == null) {
                supplies.put(id, new LinkedList<Supply>());
            }
            supplies.get(id).add(s);
        }

        return supplies;
    }

    private Map<String, Product> readProduct() throws IOException, ParseException {
        Map<String, List<Supply>> supplies = readSupply();
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withHeader("id","material","amount").withSkipHeaderRecord().parse(productReader);

        Map<String, Product> products = new HashMap<>();
        for (CSVRecord record : records) {
            String id = record.get("id");
            String name = record.get("material");
            int amount = Integer.parseInt(record.get("amount"));

            if (null == products.get(id)) {
                products.put(id, new Product(id));
            }
            Material material = new Material(name, amount);
            if (null != supplies.get(name)) {
                material.addSupplies(supplies.get(name));
            }

            products.get(id).addMaterial(material);
        }

        return products;
    }
}
