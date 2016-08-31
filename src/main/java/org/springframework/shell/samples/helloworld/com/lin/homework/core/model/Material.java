package org.springframework.shell.samples.helloworld.com.lin.homework.core.model;

import com.google.common.base.Preconditions;
import com.google.common.collect.Range;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by linwum on 2016/8/30.
 */
public class Material {

    private String id;
    private Set<Supply> supplies;
    private final int needAmount;

    public Material(String id, int needAmount) {
        this.id = id;
        this.needAmount = needAmount;
        this.supplies = new HashSet<>();
    }

    public Set<Supply> getSupplies() {
        return supplies;
    }

    public int getNeedAmount() {
        return needAmount;
    }

    public void addSupply(Supply supply) {
        Preconditions.checkArgument(null != supply);
        supplies.add(supply);
        supply.setMaterial(this);
    }

    public int supply(Range<Date> period) {
        int total = 0;

        for (Supply s : supplies) {
            total += s.supply(period);
        }

        return total;
    }

    public void consume(Range<Date> period, int number) {
        for (Supply s : supplies) {
            s.consume(period, number * needAmount);
        }
    }

    public void addSupplies(List<Supply> supplies) {
        Preconditions.checkArgument(supplies != null);

        for (Supply s: supplies) {
            addSupply(s);
        }
    }
}
