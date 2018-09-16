package com.chorusrflaptimer.wrapper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Alexander Rios <br> 15/09/2018
 */
public class SharedPreferences {
	
	private static Properties properties;
	
	private static SharedPreferences instance;
	
	public static SharedPreferences getInstance() {
		if(instance == null) {
			instance = new SharedPreferences();
		}
		return instance;
	}
	
	public static void load() {
		properties = new Properties();
		InputStream inStream = null;
		try {
			inStream = new FileInputStream("config.properties");
			properties.load(inStream);
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
	
	public static String getString(String key, String defaultValue) {
		try {
			String valueTemp = getProperties().getProperty(key);
			if(valueTemp != null) {
				return valueTemp;
			}else {
				return defaultValue;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return defaultValue;
	}
	
	public static boolean getBoolean(String key, boolean defaultValue) {
		try {
			String valueTemp = getProperties().getProperty(key);
			if(valueTemp != null) {
				return new Boolean(getProperties().getProperty(key));
			}else {
				return defaultValue;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return defaultValue;
	}
	
	public static int getInt(String key, int defaultValue) {
		try {
			String valueTemp = getProperties().getProperty(key);
			if(valueTemp != null) {
				return new Integer(getProperties().getProperty(key));
			}else {
				return defaultValue;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return defaultValue;
	}
	
	public static void put(String key, boolean value) {
		try {
			getProperties().put(key, ""+value);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void put(String key, int value) {
		try {
			getProperties().put(key, ""+value);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void put(String key, String value) {
		try {
			getProperties().put(key, ""+value);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void putString(String key, String value) {
		put(key, value);
	}
	
	public static void putBoolean(String key, boolean value) {
		put(key, value);
	}
	
	public static void putInt(String key, int value) {
		put(key, value);
	}
	
	public static void apply() {
		try {
			getProperties().store(new FileOutputStream("config.properties"), null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static Properties getProperties() {
		if(properties == null) {
			load();
		}
		return properties;
	}

}
