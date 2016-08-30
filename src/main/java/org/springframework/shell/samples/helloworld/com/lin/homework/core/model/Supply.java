package org.springframework.shell.samples.helloworld.com.lin.homework.core.model;

import com.google.common.base.Objects;
import com.google.common.collect.Range;

import java.util.Date;
import java.util.Set;

/**
 * Created by linwum on 2016/8/30.
 */
public class Supply implements Comparable<Supply> {

    private Range<Date> period;
    private int amount;
    private Material material;

    public Supply(Date beginDate, Date endDate, int amount, Material material) {
        this.period = Range.closedOpen(beginDate, endDate);
        this.amount = amount;
        this.material = material;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Supply)) return false;
        Supply supply = (Supply) o;
        return amount == supply.amount &&
                Objects.equal(period, supply.period);
    }

    public Range getPeriod() {
        return period;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(period, amount);
    }

    @Override
    public int compareTo(Supply that) {
        return this.period.lowerEndpoint().compareTo(that.period.lowerEndpoint());
    }

}
