package org.springframework.shell.samples.helloworld.com.lin.homework.core.model;

import com.google.common.base.Preconditions;
import org.springframework.shell.samples.helloworld.com.lin.homework.core.SupplyManager;

import java.util.Arrays;
import java.util.Set;
import java.util.SortedSet;

/**
 * Created by linwum on 2016/8/30.
 */
public class Product {

    private String id;
    private Set<Material> materials;
    private SupplyManager supplyManager;

    public Product(String id, Material... materials) {
        Preconditions.checkArgument(id != null);
        Preconditions.checkArgument(materials != null && materials.length > 0);

        this.id = id;
        this.materials.addAll(Arrays.asList(materials));
    }

    public SortedSet<Recipe> reportAvailability() {
        supplyManager = new SupplyManager(this);

        return supplyManager.check();
    }

    public Set<Material> getMaterial() {
        return materials;
    }
}
