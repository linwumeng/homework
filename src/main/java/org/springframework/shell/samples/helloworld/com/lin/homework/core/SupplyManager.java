package org.springframework.shell.samples.helloworld.com.lin.homework.core;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import org.apache.log4j.Logger;
import org.springframework.shell.samples.helloworld.com.lin.homework.core.model.Material;
import org.springframework.shell.samples.helloworld.com.lin.homework.core.model.Product;
import org.springframework.shell.samples.helloworld.com.lin.homework.core.model.Recipe;
import org.springframework.shell.samples.helloworld.com.lin.homework.core.model.Supply;

import java.util.Comparator;
import java.util.NavigableSet;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by linwum on 2016/8/30.
 */
public class SupplyManager {

    private static Logger LOGGER = Logger.getLogger(SupplyManager.class);
    
    private Product product;
    private NavigableSet<Recipe> recipes = new TreeSet<>();

    public SupplyManager(Product product) {
        Preconditions.checkArgument(product != null);

        this.product = product;
    }

    public SortedSet<Recipe> check() {
        for (Material m : product.getMaterial()) {
            for(Supply s: m.getSupplies()) {
                Recipe recipe = new Recipe(s.getPeriod(), m);
                Recipe floor = recipes.floor(recipe);
                Recipe ceiling = recipes.ceiling(recipe);

                merge(recipe, floor, ceiling);
            }
        }

        return FluentIterable.from(recipes).filter(new Predicate<Recipe>() {
            @Override
            public boolean apply(Recipe recipe) {
                return recipe.getIngredients().size() == product.getMaterial().size();
            }
        }).toSortedSet(new Comparator<Recipe>() {
            @Override
            public int compare(Recipe o1, Recipe o2) {
                return o1.compareTo(o2);
            }
        });
    }

    //Assume floor and ceiling are not overlapping
    private void merge(Recipe mergee, Recipe floor, Recipe ceiling) {
        LOGGER.debug(String.format("mergee:%s, floor:%s, ceiling:%s", mergee, floor, ceiling));


        if ( recipes.isEmpty() ) {
            LOGGER.debug(String.format("add empty %s", mergee));
            recipes.add(mergee);
        } else if ((null == floor || mergee.after(floor)) && (null == ceiling || mergee.before(ceiling))) {
            // f |===|
            // c        |=====|
            // m     |==|
            LOGGER.debug(String.format("add gap %s,%s,%s", floor, mergee, ceiling));
            recipes.add(mergee);
        } else if (null != floor && floor.enclose(mergee)) {
            // f |=====|
            // c       |===|
            // m   |==|
            LOGGER.debug(String.format("add enclosed %s,%s", mergee, floor));
            enclosedMerge(mergee, floor, floor);
        } else if (null != ceiling && mergee.enclose(ceiling)) {
            // f |===|
            // c     |==|
            // m     |=====|
            LOGGER.debug(String.format("add enclosing %s,%s", ceiling, mergee));
            enclosingMerge(ceiling, mergee, ceiling);
        } else if (null != floor && floor.overlap(mergee)) {
            // f |=====|
            // m     |=====|
            LOGGER.debug(String.format("add floor overlap %s,%s", floor, mergee));
            leftMerge(floor, mergee, floor);
        } else if (null != ceiling && ceiling.overlap(mergee)) {
            // c     |=====|
            // m |=====|
            LOGGER.debug(String.format("add ceiling overlap %s,%s", mergee, ceiling));
            rightMerge(mergee, ceiling, ceiling);
        }

    }

    private void leftMerge(Recipe leading, Recipe ending, Recipe victim) {
        recipes.remove(victim);
        Recipe inner = new Recipe(ending.getPeriod().lowerEndpoint(),
                leading.getPeriod().upperEndpoint(),
                leading.getIngredients(),
                ending.getIngredients()
        );
        addRecipe(inner);

        if (leading.getPeriod().lowerEndpoint().getTime() < ending.getPeriod().lowerEndpoint().getTime()) {
            Recipe left = new Recipe(leading.getPeriod().lowerEndpoint(),
                    ending.getPeriod().lowerEndpoint(),
                    leading.getIngredients()
            );
            addRecipe(left);
        }

        if (leading.getPeriod().upperEndpoint().getTime() < ending.getPeriod().upperEndpoint().getTime()) {
            Recipe right = new Recipe(leading.getPeriod().upperEndpoint(),
                    ending.getPeriod().upperEndpoint(),
                    ending.getIngredients()
            );
            merge(right, inner, recipes.ceiling(right));
        }
    }

    private void rightMerge(Recipe leading, Recipe ending, Recipe victim) {
        recipes.remove(victim);
        Recipe inner = new Recipe(ending.getPeriod().lowerEndpoint(),
                leading.getPeriod().upperEndpoint(),
                leading.getIngredients(),
                ending.getIngredients()
        );
        addRecipe(inner);

        if (leading.getPeriod().lowerEndpoint().getTime() < ending.getPeriod().lowerEndpoint().getTime()) {
            Recipe left = new Recipe(leading.getPeriod().lowerEndpoint(),
                    ending.getPeriod().lowerEndpoint(),
                    leading.getIngredients()
            );
            addRecipe(left);
        }

        if (leading.getPeriod().upperEndpoint().getTime() < ending.getPeriod().upperEndpoint().getTime()) {
            Recipe right = new Recipe(leading.getPeriod().upperEndpoint(),
                    ending.getPeriod().upperEndpoint(),
                    ending.getIngredients()
            );
            addRecipe(right);
        }
    }

    private void enclosedMerge(Recipe inner, Recipe outter, Recipe victim) {
        recipes.remove(victim);

        inner.addIngredients(outter.getIngredients());
        addRecipe(inner);

        if (outter.getPeriod().lowerEndpoint().getTime() < inner.getPeriod().lowerEndpoint().getTime()) {
            Recipe left = new Recipe(outter.getPeriod().lowerEndpoint(),
                                     inner.getPeriod().lowerEndpoint(),
                                     outter.getIngredients()
                                    );
            addRecipe(left);
        }

        if (inner.getPeriod().upperEndpoint().getTime() < outter.getPeriod().upperEndpoint().getTime()) {
            Recipe right = (new Recipe(inner.getPeriod().upperEndpoint(),
                    outter.getPeriod().upperEndpoint(),
                    outter.getIngredients()
            ));
            addRecipe(right);
        }
    }

    private void enclosingMerge(Recipe inner, Recipe outter, Recipe victim) {
        recipes.remove(victim);

        inner.addIngredients(outter.getIngredients());
        addRecipe(inner);

        if (outter.getPeriod().lowerEndpoint().getTime() < inner.getPeriod().lowerEndpoint().getTime()) {
            Recipe left = new Recipe(outter.getPeriod().lowerEndpoint(),
                    inner.getPeriod().lowerEndpoint(),
                    outter.getIngredients()
            );
            addRecipe(left);
        }

        if (inner.getPeriod().upperEndpoint().getTime() < outter.getPeriod().upperEndpoint().getTime()) {
            Recipe right = (new Recipe(inner.getPeriod().upperEndpoint(),
                    outter.getPeriod().upperEndpoint(),
                    outter.getIngredients()
            ));
            merge(right, inner, recipes.ceiling(right));
        }
    }

    private void addRecipe(Recipe recipe) {
        // m |
        if ( recipe.getPeriod().lowerEndpoint().equals(recipe.getPeriod().upperEndpoint())) {
            LOGGER.debug(String.format("skip time point %s", recipe));
            // Do nothing;
            return;
        }

        recipes.add(recipe);
    }

}
