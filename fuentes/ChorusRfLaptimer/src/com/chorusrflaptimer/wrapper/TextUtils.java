package com.chorusrflaptimer.wrapper;

import java.util.ArrayList;

/**
 * @author Alexander Rios <br>
 *         15/09/2018
 */
public class TextUtils {

	public static String join(String value, ArrayList<String> valueList) {
		String valueTemp = "";
		try {
			if (valueList == null || valueList.isEmpty()) {
				return value;
			}
			for (String element : valueList) {
				valueTemp += element + value;
			}
			return valueTemp;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	public static String[] split(String value, String tag) {
		try {
			return value.split(tag);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
