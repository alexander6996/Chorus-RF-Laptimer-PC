package com.chorusrflaptimer.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

import com.chorusrflaptimer.ConnectionListener;

/**
 * @author Alexander Rios <br> 15/09/2018
 */
public class UtilBluetooth {

	final static Object lock = new Object();
	public static ArrayList<RemoteDevice> devicesBluetooth;
	public static ArrayList<String[]> devices;
	
	public static final String SERIAL_DATA_DELIMITER = "\n";
	
	public static final int DELAY = 100;
	
	private String urlBluetooth = "btspp://00211300A406:1;authenticate=false;encrypt=false;master=false";

	private UtilBluetoothServerRead bluetoothServerRead;
	
	private StreamConnection streamConnection;
	private OutputStream os;
	private InputStream is;
	private ConnectionListener listenerApp;
	
	public static void main(String[] args) {
		UtilBluetooth bluetooth = new UtilBluetooth();
		//bluetooth.getDevices();
		//bluetooth.sendMSG("R*v"+SERIAL_DATA_DELIMITER);
	}
	
	public void setURL(String id) {
		urlBluetooth = "btspp://"+id+":1;authenticate=false;encrypt=false;master=false";
	}
	
	public void connect() {
		try {
			streamConnection = (StreamConnection) Connector.open(urlBluetooth);
			os = streamConnection.openOutputStream();
			is = streamConnection.openInputStream();
			
			bluetoothServerRead = new UtilBluetoothServerRead(is, listenerApp);
			bluetoothServerRead.start();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void disconnect() {
		if(os != null) {
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(is != null) {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(bluetoothServerRead != null) {
			try {
				bluetoothServerRead.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void send(String data) {
		try {
			//System.out.println("ENVIADO:\n"+data);
			if(streamConnection == null) {
				return;
			}
			os.write((data).getBytes());
			os.flush();
			Thread.sleep(100);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void setConnectionListener(ConnectionListener listener) {
		this.listenerApp = listener;
	}
	
	public ArrayList<String[]> getDevices() {
		devicesBluetooth = new ArrayList<RemoteDevice>();
		devices = new ArrayList<String[]>();
		try {
			synchronized (lock) {
				LocalDevice localDevice = LocalDevice.getLocalDevice();
				DiscoveryAgent agent = localDevice.getDiscoveryAgent();
				boolean started = agent.startInquiry(DiscoveryAgent.GIAC, listener);
				if (started) {
					System.out.println("Empieza el proceso de busqueda ...");
					lock.wait();
				} else {
					System.out.println("El proceso de busqueda no ha encontrado nada!");
				}
			}
			System.out.println("Device Inquiry Completed. ");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return devices;
	}
	
	/**
	 * The DiscoveryListener interface allows an application to receive device
	 * discovery and service discovery events.
	 */
	static DiscoveryListener listener = new DiscoveryListener() {
		/**
		 * Called when a device is found during an inquiry.
		 */

		public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
			String name;
			try {
				name = btDevice.getFriendlyName(false);
				devices.add(new String[] { btDevice.getFriendlyName(true), btDevice.getBluetoothAddress() });
				System.out.println("device found: " + btDevice.getFriendlyName(false));
				System.out.println("device found: " + btDevice.getFriendlyName(true));
				System.out.println("device found: " + btDevice.getBluetoothAddress());
			} catch (Exception e) {
				name = btDevice.getBluetoothAddress();
			}
			
			devicesBluetooth.add(btDevice);

		}

		/**
		 * Called when an inquiry is completed.
		 */

		public void inquiryCompleted(int discType) {
			System.out.println("Busqueda Finalizada!");
			synchronized (lock) {
				lock.notifyAll();
			}
		}

		/**
		 * Called when a service search is completed or was terminated because of an
		 * error.
		 */
		public void serviceSearchCompleted(int transID, int respCode) {
			synchronized (lock) {
				lock.notifyAll();
			}
		}

		/**
		 * Called when service(s) are found during a service search.
		 */
		public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
		}
	};
}
