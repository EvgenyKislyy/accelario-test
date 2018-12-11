package com.accelario.test;

public class CreditCardValidator {

	public static boolean checkCreditCardNumber(String ccNumber) {
		if (ccNumber.chars().allMatch(Character::isDigit) && ccNumber.length() >= 13 && ccNumber.length() <=19) {
			int sum = 0;
			boolean alternate = false;
			for (int i = ccNumber.length() - 1; i >= 0; i--) {
				int n = Integer.parseInt(ccNumber.substring(i, i + 1));
				if (alternate) {
					n *= 2;
					if (n > 9) {
						n = (n % 10) + 1;
					}
				}
				sum += n;
				alternate = !alternate;
			}
			return (sum % 10 == 0);
		} else {
			return false;
		}
	}
}
