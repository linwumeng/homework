package org.springframework.shell.samples.helloworld.com.lin.homework.core.model;

import com.google.common.base.Preconditions;
import com.google.common.collect.Range;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by linwum on 2016/8/30.
 */
public class Recipe implements Comparable<Recipe> {

    private Set<Material> ingredients = new HashSet<>();
    private Range<Date> period;

    public Recipe(Range<Date> period, Material ingredient) {
        Preconditions.checkArgument(period != null);
        Preconditions.checkArgument(ingredient != null);

        this.period = period;
        this.ingredients.add(ingredient);
    }

    public Recipe(Date beginDate, Date endDate, Set<Material> ingredients) {
        Preconditions.checkArgument(ingredients != null && !ingredients.isEmpty());

        this.period = Range.closedOpen(beginDate, endDate);
        this.ingredients.addAll(ingredients);
    }

    public Recipe(Date beginDate, Date endDate, Material ingredient) {
        this(Range.closedOpen(beginDate, endDate), ingredient);
    }

    public Recipe(Date beginDate, Date endDate, Set<Material>... ingredients) {
        // 3 is a magic number that we assume a product is composed by 3 material averagely.
        this.ingredients = new HashSet<>(ingredients.length * 3 + 1);
        for(Set<Material> m : ingredients) {
            this.ingredients.addAll(m);
        }

        Preconditions.checkArgument(!this.ingredients.isEmpty());

        this.period = Range.closedOpen(beginDate, endDate);
    }

    public Range<Date> getPeriod() {
        return period;
    }

    public Set<Material> getIngredients() {
        return ingredients;
    }

    public int number() {
        int number = Integer.MAX_VALUE;

        for(Material m : ingredients) {
            int amount = m.supply(period) / m.getNeedAmount();
            number = Math.min(number, amount);

            if (0 == number) {
                break;
            }
        }

        return number;
    }

    public void consume(int number) {

        for(Material m : ingredients) {
            m.consume(period, number);
        }

    }

    @Override
    public int compareTo(Recipe that) {
        return this.period.lowerEndpoint().compareTo(that.period.lowerEndpoint());
    }

    public boolean after(Recipe that) {
        // Use >= for the upper boundary is exclusive.
        return period.lowerEndpoint().getTime() >= that.period.upperEndpoint().getTime();
    }

    public boolean before(Recipe that) {
        // Use <= for the upper boundary is exclusive.
        return period.upperEndpoint().getTime() <= that.period.lowerEndpoint().getTime();
    }

    public boolean enclose(Recipe that) {
        return period.lowerEndpoint().getTime() <= that.period.lowerEndpoint().getTime()
                && period.upperEndpoint().getTime() >= that.period.upperEndpoint().getTime();
    }

    public boolean overlap(Recipe that) {
        Recipe leading,ending;
        if (that.getPeriod().lowerEndpoint().getTime() >= this.getPeriod().lowerEndpoint().getTime()) {
            leading = this;
            ending = that;
        } else {
            leading = that;
            ending = this;
        }

        if (leading.getPeriod().upperEndpoint().getTime() == ending.getPeriod().lowerEndpoint().getTime()) {
            // adjacent
            return false;
        }

        if (leading.getPeriod().upperEndpoint().getTime() > ending.getPeriod().lowerEndpoint().getTime()
                && leading.getPeriod().upperEndpoint().getTime() < ending.getPeriod().upperEndpoint().getTime()) {
            return true;
        }

        return false;
    }

    public Recipe addIngredients(Set<Material> ingredients) {
        if (ingredients != null) {
            this.ingredients.addAll(ingredients);
        }

        return this;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "period=" + period +
                '}';
    }
}
