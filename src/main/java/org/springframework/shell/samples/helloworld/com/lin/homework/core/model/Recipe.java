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

    public Range<Date> getPeriod() {
        return period;
    }

    public Set<Material> getIngredients() {
        return ingredients;
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

    public Recipe addIngredients(Set<Material> ingredients) {
        if (ingredients != null) {
            this.ingredients.addAll(ingredients);
        }

        return this;
    }
}
