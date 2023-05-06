package actions;

import java.math.BigDecimal;

public class precisionOperations {
	public static double add(double num1, double num2) {
		return BigDecimal.valueOf(num1).add(BigDecimal.valueOf(num2)).doubleValue();
	}
	public static double subtract(double num1, double num2) {
		return BigDecimal.valueOf(num1).subtract(BigDecimal.valueOf(num2)).doubleValue();
	}
}
