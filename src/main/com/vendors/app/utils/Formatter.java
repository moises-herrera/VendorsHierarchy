package main.com.vendors.app.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Formatter {

    public static String formatDecimals(double number) {
        NumberFormat formatter = new DecimalFormat("#.##");
        return formatter.format(number);
    }
}
