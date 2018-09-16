package com.chorusrflaptimer.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

import com.chorusrflaptimer.AppState;

/**
 * @author Alexander Rios <br> 15/09/2018
 */
public class UtilMessage {

	private static HashMap<String, Properties> properties;
	
	private static void load() {
		try {
			properties = new HashMap<String, Properties>();
			
			loadMsg("en");
			loadMsg("es");
			loadMsg("de");
			loadMsg("it");
			loadMsg("ru");
			
		} catch (Exception io) {
			io.printStackTrace();
		} finally {
		}
	}
	
	private static void loadMsg(String language) {
		InputStream inStream = null;
		try {
			Properties propertiesTemp = new Properties();
			inStream = new FileInputStream("msg_"+language+".properties");
			propertiesTemp.load(inStream);
			properties.put(language, propertiesTemp);
		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (inStream != null) {
				try {
					inStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static String getMsg(String key) {
		try {
			return getProperties().get(AppState.getInstance().languageApp).getProperty(key);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static String getMsg(String key, String defaultValue) {
		try {
			String value = getProperties().get(AppState.getInstance().languageApp).getProperty(key);
			if(value != null && !value.isEmpty()) {
				return value;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return defaultValue;
	}
	
	private static HashMap<String, Properties> getProperties() {
		if(properties == null) {
			load();
		}
		return properties;
	}
}
