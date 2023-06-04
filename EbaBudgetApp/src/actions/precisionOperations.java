package actions;

import java.math.BigDecimal;

public class precisionOperations {
	public static double add(double num1, double num2) {
		return BigDecimal.valueOf(num1).add(BigDecimal.valueOf(num2)).doubleValue();
	}
	public static double subtract(double num1, double num2) {
		return BigDecimal.valueOf(num1).subtract(BigDecimal.valueOf(num2)).doubleValue();
	}
	public static double multiply(double num1, double num2) {
		return BigDecimal.valueOf(num1).multiply(BigDecimal.valueOf(num2)).doubleValue();
	}
	public static double divide(double num1, double num2) {
		return BigDecimal.valueOf(num1).divide(BigDecimal.valueOf(num2)).doubleValue();
	}
}
