package org.springframework.shell.samples.helloworld.com.lin.homework.core.model;

import com.google.common.base.Preconditions;
import com.google.common.collect.Range;
import org.springframework.shell.samples.helloworld.com.lin.homework.core.SupplyManager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

/**
 * Created by linwum on 2016/8/30.
 */
public class Product {

    private String id;
    private Set<Material> materials;
    private SupplyManager supplyManager;

    public Product(String id) {
        Preconditions.checkArgument(id != null);

        this.id = id;
        materials = new HashSet<>();
    }

    public void addMaterial(Material material) {
        Preconditions.checkArgument(null != material);

        materials.add(material);
    }

    public List<Map<Range, Integer>> reportAvailability() {
        supplyManager = new SupplyManager(this);
        List<Map<Range, Integer>> list = new LinkedList<>();

        SortedSet<Recipe> recipes = supplyManager.check();
        for (Iterator<Recipe> iter = recipes.iterator(); iter.hasNext();) {
            Recipe recipe = iter.next();
            Integer number = recipe.number();
            if (number > 0) {
                Map<Range, Integer> entry = new HashMap<>(1);
                entry.put(recipe.getPeriod(), number);
                list.add(entry);

                recipe.consume(number);
            }
        }

//        System.out.println(list);

        return list;
    }

    public Set<Material> getMaterial() {
        return materials;
    }
}
