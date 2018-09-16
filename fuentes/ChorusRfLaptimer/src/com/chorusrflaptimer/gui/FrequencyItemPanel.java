package com.chorusrflaptimer.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
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
public class FrequencyItemPanel extends JPanel {

	ArrayList<DeviceState> deviceStates;
	
	int position;
	String deviceId;
	
	public JLabel txtDeviceLabel;
	public JProgressBar rssiBar;
	public JLabel rssiLabel = new JLabel("0");
	public JLabel lblRfBand;
	public JButton btnDecBand;
	public JLabel txtBand;
	public JButton btnIncBand;
	public JLabel lblCanal;
	public JButton btnIncCh;
	public JLabel txtChannel;
	public JLabel txtFreq;
	public JButton btnDecCh;
	public JLabel spaceChannelColor;
	
	/**
	 * Create the panel.
	 */
	public FrequencyItemPanel(int position, ArrayList<DeviceState> deviceStates) {
		this.position = position;
		this.deviceStates = deviceStates;
		deviceId = String.format("%X", position);
		
		setBorder(new LineBorder(Color.GRAY, 2));
		
		txtDeviceLabel = new JLabel("Dispositivo # 1");
		txtDeviceLabel.setBounds(5, 33, 100, 16);
		
		rssiBar = new JProgressBar();
		rssiBar.setBounds(117, 33, 120, 14);
		
		rssiLabel = new JLabel("0000");
		rssiLabel.setBounds(234, 33, 40, 16);
		rssiLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		
		lblRfBand = new JLabel("RF Band");
		lblRfBand.setBounds(5, 68, 73, 16);
		
		btnDecBand = new JButton("<");
		btnDecBand.setBounds(83, 64, 45, 25);
		
		txtBand = new JLabel("R");
		txtBand.setBounds(125, 69, 100, 16);
		txtBand.setHorizontalAlignment(SwingConstants.CENTER);
		
		btnIncBand = new JButton(">");
		btnIncBand.setBounds(230, 64, 45, 25);
		
		lblCanal = new JLabel("Canal #");
		lblCanal.setBounds(5, 105, 73, 16);
		
		btnIncCh = new JButton("<");
		btnIncCh.setBounds(83, 101, 45, 25);
		
		txtChannel = new JLabel("2");
		txtChannel.setBounds(125, 107, 30, 16);
		txtChannel.setHorizontalAlignment(SwingConstants.CENTER);
		
		txtFreq = new JLabel("5695 MHz");
		txtFreq.setHorizontalAlignment(SwingConstants.LEFT);
		txtFreq.setBounds(160, 107, 70, 16);
		
		btnDecCh = new JButton(">");
		btnDecCh.setBounds(230, 100, 45, 25);
		setLayout(null);
		
		spaceChannelColor = new JLabel(" ");
		spaceChannelColor.setBounds(5, 5, 270, 15);
		spaceChannelColor.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
		add(spaceChannelColor);
		add(lblCanal);
		add(lblRfBand);
		add(txtDeviceLabel);
		add(rssiBar);
		add(rssiLabel);
		add(btnIncCh);
		add(txtChannel);
		add(txtFreq);
		add(btnDecBand);
		add(txtBand);
		add(btnDecCh);
		add(btnIncBand);
		
		setSize(283, 137);
		//setSize(250, 110);

		load();
	}
	
	private void load() {
		txtDeviceLabel.setText(String.format(UtilMessage.getMsg("device_number", "Device #%d"), position+1));
		txtChannel.setText("1");
		spaceChannelColor.setOpaque(true);
		spaceChannelColor.setBackground(new Color(Utils.getBackgroundColorItem(position)));
		rssiBar.setMaximum(AppState.RSSI_SPAN);
		String mhzString = UtilMessage.getMsg("mhz_string");
		
        btnDecCh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int newChannel = AppState.getInstance().deviceStates.get(position).channel - 1;
                if (newChannel >= 0) {
                    AppState.getInstance().sendBtCommand("R" + deviceId + "C" + String.format("%X", newChannel));
                }
            }
        });

        btnIncCh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int newChannel = AppState.getInstance().deviceStates.get(position).channel + 1;
                if (newChannel <= 7) {
                    AppState.getInstance().sendBtCommand("R" + deviceId + "C" + String.format("%X", newChannel));
                }
            }
        });

        btnDecBand.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int newBand = AppState.getInstance().deviceStates.get(position).band - 1;
                if (newBand >= 0) {
                    AppState.getInstance().sendBtCommand("R" + deviceId + "B" + String.format("%X", newBand));
                }
            }
        });

        btnIncBand.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int newBand = AppState.getInstance().deviceStates.get(position).band + 1;
                if (newBand <= 6) {
                    AppState.getInstance().sendBtCommand("R" + deviceId + "B" + String.format("%X", newBand));
                }
            }
        });
	}
	
	public void loadMsg() {
		txtDeviceLabel.setText(String.format(UtilMessage.getMsg("device_number", "Device #%d"), position+1));
		lblRfBand.setText(UtilMessage.getMsg("freq_rf_band"));
		lblCanal.setText(UtilMessage.getMsg("freq_channel"));
	}
	
}
