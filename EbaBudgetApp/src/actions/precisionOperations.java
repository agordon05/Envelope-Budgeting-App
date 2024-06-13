package actions;

import java.math.BigDecimal;

public class precisionOperations {
	public static BigDecimal add(double num1, double num2) {
		return BigDecimal.valueOf(num1).add(BigDecimal.valueOf(num2));
	}
	public static BigDecimal subtract(double num1, double num2) {
		return BigDecimal.valueOf(num1).subtract(BigDecimal.valueOf(num2));
	}
	public static BigDecimal multiply(double num1, double num2) {
		return BigDecimal.valueOf(num1).multiply(BigDecimal.valueOf(num2));
	}
	public static BigDecimal divide(double num1, double num2) {
		return BigDecimal.valueOf(num1).divide(BigDecimal.valueOf(num2));
	}
	
	public static BigDecimal add(BigDecimal num1, BigDecimal num2) {
		return num1.add(num2);
	}
	public static BigDecimal subtract(BigDecimal num1, BigDecimal num2) {
		return num1.subtract(num2);
	}
	public static BigDecimal multiply(BigDecimal num1, BigDecimal num2) {
		return num1.multiply(num2);
	}
	public static BigDecimal divide(BigDecimal num1, BigDecimal num2) {
		return num1.divide(num2);
	}
}
