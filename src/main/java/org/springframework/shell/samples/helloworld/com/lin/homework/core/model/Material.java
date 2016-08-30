package org.springframework.shell.samples.helloworld.com.lin.homework.core.model;

import java.util.ServiceConfigurationError;
import java.util.Set;

/**
 * Created by linwum on 2016/8/30.
 */
public class Material {

    private String id;
    private Set<Supply> supplies;
    private int needAmount;

    public Set<Supply> getSupplies() {
        return supplies;
    }
}
