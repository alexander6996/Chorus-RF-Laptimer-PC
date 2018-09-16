package com.chorusrflaptimer.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import com.chorusrflaptimer.ConnectionListener;

/**
 * @author Alexander Rios <br> 15/09/2018
 */
public class UtilBluetoothServerRead extends Thread {

	InputStream is;
	ConnectionListener listener;

	public UtilBluetoothServerRead(InputStream is, ConnectionListener listener) {
		this.is = is;
		this.listener = listener;
	}

	public void run() {

		String bufferCommand = "";

		while (true) {
			try {

				ByteArrayOutputStream buffer = new ByteArrayOutputStream();
				int nRead;
				byte[] data = new byte[64];

				while ((nRead = is.read(data, 0, data.length)) != -1) {
					buffer.write(data, 0, nRead);
					buffer.flush();

					String respuesta = new String(buffer.toByteArray());

					//System.out.println("RECIBIDO:\n" + respuesta);

					char[] msj = respuesta.toCharArray();

					for (int index = 0; index < msj.length; index++) {
						if (msj[index] == '\n') {
							listener.onDataReceived(bufferCommand);
							bufferCommand = "";
						} else {
							bufferCommand += msj[index];
						}
					}

					data = new byte[64];
					buffer.reset();
					Thread.sleep(100);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
