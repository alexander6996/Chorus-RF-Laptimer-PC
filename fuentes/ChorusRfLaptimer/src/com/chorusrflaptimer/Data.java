package com.chorusrflaptimer;

public class Data {

	private static Data data;
	
	private Data() {
	}
	
	public static Data getInstance() {
		if(data == null) {
			data = new Data();
		}
		return data;
	}

	public static void setData(Data data) {
		Data.data = data;
	}

}
