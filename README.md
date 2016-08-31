#Summary
This project provides a shell to live query product using data loading from files. It answers the question that how many produce is available for the given time slot.

#Execution
This is a gradle project using JDK 7. You can run it by
$>./gradlew -q run

##Then you will see the shell prompt as

    =======================================
    *                                     *
    *       Product Availability          *
    *                                     *
    =======================================
    Version:0.0.1
    Welcome to ProductAvailability CLI
    product-shell>

##Use the command below to query
    product-shell>pt --product /Users/linwumeng/homework/product.csv --supply /Users/linwumeng/homework/supply.csv --id 98100201
