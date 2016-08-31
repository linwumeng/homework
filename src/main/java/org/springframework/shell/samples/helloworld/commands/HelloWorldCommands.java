package org.springframework.shell.samples.helloworld.commands;

import com.google.common.collect.Range;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.shell.samples.helloworld.com.lin.homework.core.ProductManager;
import org.springframework.shell.samples.helloworld.com.lin.homework.core.impl.FileProductManager;
import org.springframework.shell.samples.helloworld.com.lin.homework.core.model.Product;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

@Component
public class HelloWorldCommands implements CommandMarker {

	private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@CliCommand(value = "pt", help = "Print product availabilities")
	public String query(
			@CliOption(key = { "product" }, mandatory = true, help = "The product file") final String productFile,
			@CliOption(key = { "supply" }, mandatory = true, help = "The supply file") final String supplyFile,
			@CliOption(key = { "id" }, mandatory = true, help = "The product id") final String id
	) throws ParseException, IOException {
		ProductManager productManager = new FileProductManager(productFile, supplyFile);

		Product product = productManager.findByCode(id);
		String output = "Not available";

		if (null != product) {
			List<Map<Range, Integer>> items = product.reportAvailability();
			StringBuilder builder = new StringBuilder();

			for (Map<Range, Integer> item : items) {
				builder.append(id);
				builder.append(" ");
				builder.append(df.format(item.keySet().iterator().next().lowerEndpoint()));
				builder.append(" ");
				builder.append(df.format(item.keySet().iterator().next().upperEndpoint()));
				builder.append(" ");
				builder.append(item.values().iterator().next());
				builder.append(System.lineSeparator());
			}

			output = builder.toString();
		}

		return output;
	}

}
