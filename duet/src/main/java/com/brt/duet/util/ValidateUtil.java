package com.brt.duet.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateUtil {
	
	public static boolean match(String regex, String str) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}
	
	public static boolean checkLength(String str, int minLength, int maxLength) {
		if (str != null) {
			int len = str.length();
			if (minLength == 0) {
				return len <= maxLength;
			} else if (maxLength == 0) {
				return len >= minLength;
			} else {
				return (len >= minLength && len <= maxLength);
			}
		}
		return false;
	}
	
	public static void main(String[] args) {
		System.out.println(match("^[1-9][0-9]{0,3}$","9999"));
	}
}
