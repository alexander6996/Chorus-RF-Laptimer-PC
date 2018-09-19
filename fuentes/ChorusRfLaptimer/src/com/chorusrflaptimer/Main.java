package com.chorusrflaptimer;

import java.awt.EventQueue;

import javax.swing.UIManager;

import com.chorusrflaptimer.gui.MainWindow;
import com.chorusrflaptimer.wrapper.SharedPreferences;
import com.chorusrflaptimer.wrapper.TextSpeaker;

public class Main {

	public static void main(String[] args) {
		new Main().run();
	}
	
	public void run() {
		AppState.getInstance().textSpeaker = new TextSpeaker();
		
		//load sounds
		//UtilSound.getInstance();
			
		//load properties config
		AppState.getInstance().preferences = SharedPreferences.getInstance();
		AppState.getInstance().preferences.load();
		//Config app and device.
		AppPreferences.applyAll();
		
		DeviceState ds = new DeviceState();
       ds.threshold = 0;
       ds.channel = 1%8;
       ds.pilotName = "Pilot " + Integer.toString(1+1);
       AppState.getInstance().deviceStates.add(ds);
       
       ds = new DeviceState();
       ds.threshold = 0;
       ds.channel = 2%8;
       ds.pilotName = "Pilot " + Integer.toString(2+1);
       AppState.getInstance().deviceStates.add(ds);

       ds = new DeviceState();
       ds.threshold = 0;
       ds.channel = 3%8;
       ds.pilotName = "Pilot " + Integer.toString(3+1);
       AppState.getInstance().deviceStates.add(ds);
       
       ds = new DeviceState();
       ds.threshold = 0;
       ds.channel = 4%8;
       ds.pilotName = "Pilot " + Integer.toString(4+1);
       AppState.getInstance().deviceStates.add(ds);
       
       ds = new DeviceState();
       ds.threshold = 0;
       ds.channel = 5%8;
       ds.pilotName = "Pilot " + Integer.toString(5+1);
       AppState.getInstance().deviceStates.add(ds);
       
       ds = new DeviceState();
       ds.threshold = 0;
       ds.channel = 65%8;
       ds.pilotName = "Pilot " + Integer.toString(6+1);
       AppState.getInstance().deviceStates.add(ds);
       
       ds = new DeviceState();
       ds.threshold = 0;
       ds.channel = 7%8;
       ds.pilotName = "Pilot " + Integer.toString(7+1);
       AppState.getInstance().deviceStates.add(ds);
       
       ds = new DeviceState();
       ds.threshold = 0;
       ds.channel = 8%8;
       ds.pilotName = "Pilot " + Integer.toString(8+1);
       AppState.getInstance().deviceStates.add(ds);
       
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					MainWindow frame = new MainWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});		
	}
}
