package org.springframework.shell.samples.helloworld.com.lin.homework.core;

import com.google.common.collect.Range;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.springframework.shell.samples.helloworld.com.lin.homework.core.model.Material;
import org.springframework.shell.samples.helloworld.com.lin.homework.core.model.Product;
import org.springframework.shell.samples.helloworld.com.lin.homework.core.model.Supply;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * Created by linwumeng on 9/1/16.
 */
public class SupplyManagerTest extends TestCase {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public void testCheck() throws Exception {
        Supply s1 = new Supply(sdf.parse("2015-01-01"), sdf.parse("2015-01-31"), 23);
        Supply s2 = new Supply(sdf.parse("2015-03-01"), sdf.parse("2015-03-31"), 27);

        Material m1 = new Material("m1", 5);
        m1.addSupply(s1);
        m1.addSupply(s2);

        Supply s3 = new Supply(sdf.parse("2015-01-13"), sdf.parse("2015-02-21"), 37);
        Material m2 = new Material("m2", 7);
        m2.addSupply(s3);

        Product product = new Product("Test");
        product.addMaterial(m1);
        product.addMaterial(m2);

        List<Map<Range, Integer>> result = product.reportAvailability();
        Assert.assertEquals(result.size(), 1);
        Assert.assertEquals(result.get(0).values().iterator().next().intValue(), 4);
    }
}