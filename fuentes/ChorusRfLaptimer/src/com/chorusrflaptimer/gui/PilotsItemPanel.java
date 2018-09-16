package com.chorusrflaptimer.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import com.chorusrflaptimer.AppState;
import com.chorusrflaptimer.DeviceState;
import com.chorusrflaptimer.Utils;
import com.chorusrflaptimer.util.UtilMessage;

/**
 * @author Alexander Rios <br> 15/09/2018
 */
@SuppressWarnings("serial")
public class PilotsItemPanel extends JPanel {

	ArrayList<DeviceState> deviceStates;

	public static final int RSSI_LONG_STEP = 20;

	int position;
	String deviceId;
	public JCheckBox txtChannelLabel;
	public JProgressBar rssiBar;
	public JLabel rssiLabel = new JLabel("0");
	public JLabel spacePilotColor;
	public JTextField edPilotName;
	public JLabel edPilotNameLbl;
	public JLabel umbralRssiLbl;
	public JButton btnSetThr;
	public JTextField txtThresh;

	/**
	 * Create the panel.
	 */
	public PilotsItemPanel(int position, ArrayList<DeviceState> deviceStates) {
		this.position = position;
		this.deviceStates = deviceStates;
		deviceId = String.format("%X", position);

		setBorder(new LineBorder(Color.GRAY, 2));

		rssiBar = new JProgressBar();
		rssiBar.setBounds(10, 132, 110, 14);

		rssiLabel = new JLabel("0000");
		rssiLabel.setBounds(40, 115, 40, 16);
		rssiLabel.setHorizontalAlignment(SwingConstants.CENTER);
		setLayout(null);

		spacePilotColor = new JLabel(" ");
		spacePilotColor.setBounds(5, 5, 250, 15);
		spacePilotColor.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
		add(spacePilotColor);
		add(rssiBar);
		add(rssiLabel);

		setSize(263, 154);

		txtChannelLabel = new JCheckBox("Canal: R2 (5296 MHz)");
		txtChannelLabel.setBounds(5, 27, 250, 23);
		add(txtChannelLabel);

		edPilotNameLbl = new JLabel("Nombre Piloto:");
		edPilotNameLbl.setBounds(5, 57, 104, 14);
		add(edPilotNameLbl);

		edPilotName = new JTextField();
		edPilotName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					AppState.getInstance().changeDevicePilot(position, edPilotName.getText());
				}
			}
		});
		edPilotName.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				AppState.getInstance().changeDevicePilot(position, edPilotName.getText());
			}
		});
		edPilotName.setBounds(110, 54, 143, 22);
		add(edPilotName);
		edPilotName.setColumns(10);

		umbralRssiLbl = new JLabel("Umbral RSSI");
		umbralRssiLbl.setBounds(5, 85, 104, 14);
		add(umbralRssiLbl);

		btnSetThr = new JButton("Limpiar");
		btnSetThr.setBounds(140, 108, 74, 23);
		add(btnSetThr);
		
		txtThresh = new JTextField();
		txtThresh.setBounds(110, 81, 143, 22);
		add(txtThresh);
		txtThresh.setColumns(10);

		load();
	}

	private void load() {

		txtThresh.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					
			        boolean isValid = true;
			        int newThresh = 0;
			        try {
			            newThresh = Integer.parseInt(txtThresh.getText());
			            if (newThresh < 0 || newThresh > AppState.MAX_RSSI) {
			                isValid = false;
			            }
			        } catch (NumberFormatException x) {
			            isValid = false;
			            txtThresh.setText("0");
			        }
			        if (isValid) {
			        	AppState.getInstance().sendBtCommand("R" + deviceId + "T" + String.format("%04X", newThresh));
			        }else {
			        	txtThresh.setText("0");
			        }
					
				}
			}
		});
		
		// btnDecThr.addMouseListener(new MouseAdapter() {
		// @Override
		// public void mouseClicked(MouseEvent e) {
		// int newThresh = ds.threshold - 1;
		// if (newThresh < 0) {
		// newThresh = 0;
		// }
		// AppState.getInstance().sendBtCommand("R" + deviceId + "T" +
		// String.format("%04X", newThresh));
		// }
		// });

//		btnDecThr.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				final DeviceState ds = AppState.getInstance().deviceStates.get(position);
//				int newThresh = ds.threshold - RSSI_LONG_STEP;
//				if (newThresh < 0) {
//					newThresh = 0;
//				}
//				AppState.getInstance().sendBtCommand("R" + deviceId + "T" + String.format("%04X", newThresh));
//			}
//		});

		// btnIncThr.addMouseListener(new MouseAdapter() {
		// @Override
		// public void mouseClicked(MouseEvent e) {
		// int newThresh = ds.threshold + 1;
		// AppState.getInstance().sendBtCommand("R" + deviceId + "T" +
		// String.format("%04X", newThresh));
		// }
		// });

//		btnIncThr.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				final DeviceState ds = AppState.getInstance().deviceStates.get(position);
//				int newThresh = ds.threshold + RSSI_LONG_STEP;
//				if (newThresh > AppState.MAX_RSSI) {
//					newThresh = AppState.MAX_RSSI;
//				}
//				AppState.getInstance().sendBtCommand("R" + deviceId + "T" + String.format("%04X", newThresh));
//			}
//		});
//
		btnSetThr.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final DeviceState ds = AppState.getInstance().deviceStates.get(position);
				if (ds.threshold == 0) {
					AppState.getInstance().sendBtCommand("R" + deviceId + "H1");
				} else {
					AppState.getInstance().sendBtCommand("R" + deviceId + "T0000");
				}
			}
		});

		if (position >= AppState.getInstance().deviceStates.size()) {
			return;
		}

		loadData();
	}
	
	public void loadData() {
		final DeviceState ds = AppState.getInstance().deviceStates.get(position);
		String ch = AppState.getInstance().getChannelText(position);
		String band = AppState.getInstance().getBandText(position);
		Boolean isEnabled = AppState.getInstance().getIsPilotEnabled(position);
		// int thresholdSetupState =
		// AppState.getInstance().getThresholdSetupState(position);

		// if (thresholdSetupState > 0) {
		// //showThresholdSetupProgress;
		// spinThresholdSetup.setVisible(true);
		// txtThresh.setVisible(true);
		// int colorId = (thresholdSetupState == 1) ? R.color.colorWarn :
		// R.color.colorPrimary;
		// int color = ContextCompat.getColor(mContext, colorId);
		// spinThresholdSetup.getIndeterminateDrawable().setColorFilter(color,
		// PorterDuff.Mode.SRC_IN);
		// } else {
		// //hideThresholdSetupProgress;
		// spinThresholdSetup.setVisible(false);
		// txtThresh.setVisible(false);
		// }

		String mhzString = UtilMessage.getMsg("mhz_string");
		String freq = AppState.getInstance().getFrequencyText(position) + " " + mhzString;

		spacePilotColor.setOpaque(true);
		spacePilotColor.setBackground(new Color(Utils.getBackgroundColorItem(position)));
		txtChannelLabel.setText(String.format(UtilMessage.getMsg("channel_descriptor"), band, ch, freq));
		txtChannelLabel.setSelected(isEnabled);

		if (ds.threshold == 0) {
			btnSetThr.setText(UtilMessage.getMsg("setup_set_threshold"));
		} else {
			btnSetThr.setText(UtilMessage.getMsg("setup_clear_threshold"));
		}

		edPilotName.setText(ds.pilotName);

		rssiBar.setMaximum(AppState.RSSI_SPAN);
	}

	public void loadMsg() {

		if (position >= AppState.getInstance().deviceStates.size()) {
			return;
		}

		String ch = AppState.getInstance().getChannelText(position);
		String band = AppState.getInstance().getBandText(position);

		String mhzString = UtilMessage.getMsg("mhz_string");
		String freq = AppState.getInstance().getFrequencyText(position) + " " + mhzString;

		txtChannelLabel.setText(String.format(UtilMessage.getMsg("channel_descriptor"), band, ch, freq));

		edPilotNameLbl.setText(UtilMessage.getMsg("setup_pilot_name"));
		umbralRssiLbl.setText(UtilMessage.getMsg("setup_rssi_threshold"));
		btnSetThr.setText(UtilMessage.getMsg("setup_clear_threshold"));
	}
}
