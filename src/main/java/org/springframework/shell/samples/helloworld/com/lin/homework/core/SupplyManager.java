package org.springframework.shell.samples.helloworld.com.lin.homework.core;

import com.google.common.base.Preconditions;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Range;
import org.springframework.shell.samples.helloworld.com.lin.homework.core.model.Material;
import org.springframework.shell.samples.helloworld.com.lin.homework.core.model.Product;
import org.springframework.shell.samples.helloworld.com.lin.homework.core.model.Recipe;
import org.springframework.shell.samples.helloworld.com.lin.homework.core.model.Supply;

import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Consumer;

/**
 * Created by linwum on 2016/8/30.
 */
public class SupplyManager {

    private Product product;
    private NavigableSet<Recipe> recipes = new TreeSet<>();

    public SupplyManager(Product product) {
        Preconditions.checkArgument(product != null);

        this.product = product;
    }

    public SortedSet<Recipe> check() {
        FluentIterable.from(product.getMaterial()).forEach(new Consumer<Material>() {
            @Override
            public void accept(Material material) {
                Set<Supply> supplies = material.getSupplies();

                FluentIterable.from(supplies).forEach(new Consumer<Supply>() {
                    @Override
                    public void accept(Supply supply) {
                        Range period = supply.getPeriod();

                        Recipe recipe = new Recipe(supply.getPeriod(), material);
                        Recipe floor = recipes.floor(recipe);
                        Recipe ceilling = recipes.ceiling(recipe);

                        merge(recipe, floor, ceilling);
                    }
                });
            }
        });

        return null;
    }

    private void merge(Recipe mergee, Recipe floor, Recipe ceilling) {
        if ( (null == floor && null == ceilling)
                || ( mergee.after(floor) && mergee.before(ceilling))) {
            recipes.add(mergee);
        } else if (null == floor && ceilling != null && mergee.before(ceilling)) {
            recipes.add(mergee);
        } else if (null == ceilling && floor != null && mergee.after(floor)) {
            recipes.add(mergee);
        } else if (floor != null && ceilling != null && floor == ceilling) {
            Recipe newFloor = new Recipe(floor.getPeriod().lowerEndpoint(),
                                    mergee.getPeriod().lowerEndpoint(),
                                    floor.getIngredients()
                              );
            Recipe newCeilling = new Recipe(mergee.getPeriod().upperEndpoint(),
                                    ceilling.getPeriod().upperEndpoint(),
                                    ceilling.getIngredients()
                              );
            mergee.addIngredients(floor.getIngredients());

            recipes.remove(ceilling);
            recipes.add(newFloor);
            recipes.add(mergee);
            recipes.add(newCeilling);
        } else if ( (null == floor || mergee.after(floor)) && ceilling != null && !ceilling.after(mergee)) {

        }
    }

}
