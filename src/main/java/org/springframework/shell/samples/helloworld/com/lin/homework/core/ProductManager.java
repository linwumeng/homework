package org.springframework.shell.samples.helloworld.com.lin.homework.core;

import org.springframework.shell.samples.helloworld.com.lin.homework.core.model.Product;

/**
 * Created by linwumeng on 8/31/16.
 */
public interface ProductManager {
    Product findByCode(String code);
}
