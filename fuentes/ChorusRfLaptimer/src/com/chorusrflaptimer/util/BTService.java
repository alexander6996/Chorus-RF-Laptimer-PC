package com.chorusrflaptimer.util;

import com.chorusrflaptimer.Connection;
import com.chorusrflaptimer.ConnectionListener;

public class BTService implements Connection {

	private UtilBluetooth bluetooth;
	
	public BTService() {
		bluetooth = new UtilBluetooth();
	}
	
	@Override
	public void connect() {
		bluetooth.connect();
	}

	@Override
	public void disconnect() {
		bluetooth.disconnect();
	}

	@Override
	public void send(String data) {
		bluetooth.send(data);
	}

	@Override
	public void setConnectionListener(ConnectionListener listener) {
		bluetooth.setConnectionListener(listener);
	}
	
	public UtilBluetooth getBluetooth() {
		return bluetooth;
	}

}
