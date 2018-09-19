package com.chorusrflaptimer.gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.io.IOException;
import java.util.ArrayList;

import javax.bluetooth.RemoteDevice;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

import com.chorusrflaptimer.AppPreferences;
import com.chorusrflaptimer.AppState;
import com.chorusrflaptimer.ConnectionListener;
import com.chorusrflaptimer.Utils;
import com.chorusrflaptimer.util.BTService;
import com.chorusrflaptimer.util.UtilBluetooth;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;
import javax.swing.ImageIcon;

/**
 * @author Alexander Rios <br> 15/09/2018
 */
public class ConexionWindow extends JFrame {

	private JPanel contentPane;

	JComboBox tipoCmb;

	JButton conectarBtn;

	JComboBox devicesCmb;
	
	BTService bt;
	
	ArrayList<String[]> devices;
	
	MainWindow mainWindow;

	/**
	 * Create the frame.
	 */
	public ConexionWindow() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(ConexionWindow.class.getResource("/com/chorusrflaptimer/img/ic_connection.png")));
		setTitle("");
		setResizable(false);
		setBounds(100, 100, 272, 164);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		tipoCmb = new JComboBox();
		tipoCmb.addItem("Bluetooth");
		//tipoCmb.addItem("Wifi");
		tipoCmb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selection = tipoCmb.getSelectedIndex();
				if(selection == 0) {
					devicesCmb.setEnabled(true);
					button.setEnabled(true);
				}else {
					devicesCmb.setEnabled(false);
					button.setEnabled(false);
				}
			}
		});

		conectarBtn = new JButton("Connect");
		conectarBtn.setIcon(new ImageIcon(ConexionWindow.class.getResource("/com/chorusrflaptimer/img/connect.png")));
		conectarBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				connect();
			}
		});

		devicesCmb = new JComboBox();

		JButton btnDesconectar = new JButton("Disconnect");
		btnDesconectar.setIcon(new ImageIcon(ConexionWindow.class.getResource("/com/chorusrflaptimer/img/disconnect.png")));
		btnDesconectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				disconect();
			}
		});
		
		button = new JButton("");
		button.setIcon(new ImageIcon(ConexionWindow.class.getResource("/com/chorusrflaptimer/img/load.png")));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Loading...", "", JOptionPane.INFORMATION_MESSAGE);
				UtilBluetooth bluetooth = bt.getBluetooth();
				devices = bluetooth.getDevices();
				devicesCmb.removeAllItems();
				for (String[] device : devices) {
					try {
						devicesCmb.addItem(device[0]);
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}				
			}
		});
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(tipoCmb, 0, 232, Short.MAX_VALUE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(conectarBtn, GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnDesconectar))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(devicesCmb, GroupLayout.PREFERRED_SIZE, 191, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(button, GroupLayout.PREFERRED_SIZE, 34, Short.MAX_VALUE)))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(tipoCmb, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(button)
						.addComponent(devicesCmb, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(conectarBtn)
						.addComponent(btnDesconectar)))
		);
		contentPane.setLayout(gl_contentPane);

		setLocationRelativeTo(null);
		init();
	}
	
	private void init() {
		initBluetooth();
		initUSB();
		initUDP();
	}

	public void load() {
		devices = new ArrayList<String[]>();
		if(AppState.getInstance().bluetoothLastConnect != null) {
			String[] value = new String[] {AppState.getInstance().bluetoothLastConnect[0], AppState.getInstance().bluetoothLastConnect[1]};
			devices.add(value);
		}
		devicesCmb.removeAllItems();
		for (String[] device : devices) {
			try {
				devicesCmb.addItem(device[0]);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	public void connect() {
		int selection = tipoCmb.getSelectedIndex();
		if(selection == 0) {
			//Bluetooth
			AppState.getInstance().conn = bt;
			int index = devicesCmb.getSelectedIndex();
			bt.getBluetooth().setURL(devices.get(index)[1]);

			AppState.getInstance().conn.connect();
			connectionListener.onConnected("");
			
			mainWindow.connectionButton.setBackground(Color.GREEN);
			
			AppState.getInstance().bluetoothLastConnect = new String[] {devices.get(index)[0], devices.get(index)[1]};
			AppPreferences.save(AppPreferences.BLUETOOTH_LAST_CONNECT);
			AppPreferences.applyAll();

		}else if(selection == 1) {
			//Wifi
			//AppState.getInstance().conn = udp;
		}
		
		setVisible(false);
		mainWindow.updatePanels();
	}

	public void disconect() {
		AppState.getInstance().conn.disconnect();
		connectionListener.onDisconnected();
		mainWindow.updatePanels();
		mainWindow.connectionButton.setBackground(Color.GRAY);
	}

	void initBluetooth() {
		bt = new BTService();
		bt.setConnectionListener(connectionListener);
	}

	void initUSB() {
		// usb = new USBService((UsbManager) getSystemService(Context.USB_SERVICE));
		// usb.setConnectionListener(this);
	}

	void initUDP() {
		// udp = new UDPService();
		// udp.setConnectionListener(this);
	}

	ConnectionListener connectionListener = new ConnectionListener() {

		@Override
		public void onDisconnected() {
			// AppState.getInstance().speakMessage(R.string.disconnected);
			AppState.getInstance().onDisconnected();
		}

		@Override
		public void onDataReceived(String message) {
			String parsedMsg;
			try {
				parsedMsg = Utils.btDataChunkParser(message);
			} catch (Exception e) {
				parsedMsg = e.toString();
			}
		}

		@Override
		public void onConnectionFailed(String errorMsg) {
			AppState.getInstance().conn = null;
			// Toast.makeText(getApplicationContext(),
			// getString(R.string.connection_failed), Toast.LENGTH_SHORT).show();
			// AppState.getInstance().speakMessage(R.string.connection_failed);
		}

		@Override
		public void onConnected(String name) {
			// AppState.getInstance().speakMessage(R.string.connected);
			AppState.getInstance().onConnected();
		}
	};
	private JButton button;
	
	public void setMainWindow(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
	}
}
