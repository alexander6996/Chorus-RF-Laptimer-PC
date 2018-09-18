package com.chorusrflaptimer.gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JPanel;

import com.chorusrflaptimer.AppState;
import com.chorusrflaptimer.DataAction;
import com.chorusrflaptimer.DeviceState;
import com.chorusrflaptimer.IDataListener;
import com.chorusrflaptimer.Utils;
import com.chorusrflaptimer.util.UtilMessage;

/**
 * @author Alexander Rios <br> 15/09/2018
 */
@SuppressWarnings("serial")
public class PilotsPanel extends JPanel {
	
	ArrayList<PilotsItemPanel> panels = new ArrayList<PilotsItemPanel>();

	JPanel panel;
	
	MainWindow mainWindow;
	
	/**
	 * Create the panel.
	 */
	public PilotsPanel() {
		
		panel = new JPanel();
		panel.setBounds(5, 5, 820, 400);
		panel.setLayout(new GridLayout(3, 3, 0, 0));
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 805, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(334, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 457, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(148, Short.MAX_VALUE))
		);
		setLayout(groupLayout);
		
		load();
	}
	
	private void load() {
        AppState.getInstance().addListener(new IDataListener() {
            @Override
            public void onDataChange(DataAction dataItemName) {
                switch (dataItemName) {
                    case NDevices:
                    case DeviceChannel:
                    case DeviceBand:
                    case DeviceThreshold:
                    case PilotEnabledDisabled:
                    case ThresholdSetupState:
                        updateResults();
                        break;
                    case DeviceRSSI:
                    case Disconnect:
                        updateCurrentRSSI();
                        break;
                    case SPECIAL_DevicePilot_EditUpdate:
                        updatePilotNames();
                }
            }
        });
        useNewAdapter();
	}
	
	public void loadMsg() {
    	for(int index=0;index < panels.size() ;index++ ) {
    		PilotsItemPanel devicePanel = panels.get(index);
    		devicePanel.loadMsg();
    	}
	}
	
    public void useNewAdapter() {
    	for(int index=0;index < 8 ;index++ ) {
    		PilotsItemPanel pilotsPanel = new PilotsItemPanel( index , AppState.getInstance().deviceStates );
    		panel.add(pilotsPanel);
    		panels.add(pilotsPanel);
    		pilotsPanel.setVisible(false);
    	}
    	
    	updatePanel();
    }
    
    public void updatePanel() {
    	if(AppState.getInstance().deviceStates != null) {
        	for(int index=0;index < AppState.getInstance().deviceStates.size() ;index++ ) {
        		PilotsItemPanel pilotsPanel = panels.get(index);
        		pilotsPanel.loadData();
        		pilotsPanel.setVisible(true);
        		
        		pilotsPanel.txtThresh.setText(Integer.toString(AppState.getInstance().deviceStates.get(index).threshold));	
        	}
    	}  	
    }
    
    public void updateResults() {
//        ListView listView = (ListView)mRootView.findViewById(R.id.lvPilots);
//        ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
    }

    public void updatePilotNames() {
        int count = AppState.getInstance().deviceStates.size();
        for (int i = 0; i < count; i++) {
        	PilotsItemPanel pilotsPanel = panels.get(i);
            if (pilotsPanel != null) {
            	pilotsPanel.edPilotName.setText(AppState.getInstance().deviceStates.get(i).pilotName);
            }
        }
    }

    public void updateCurrentRSSI() {
    	if(AppState.getInstance().deviceStates != null) {
        	for(int index=0;index < AppState.getInstance().deviceStates.size() ;index++ ) {
        		
        		PilotsItemPanel pilotsPanel = panels.get(index);
        		
        		DeviceState deviceState = AppState.getInstance().deviceStates.get(index);
	            String ch = AppState.getInstance().getChannelText(index);
	            String band = AppState.getInstance().getBandText(index);
	            Boolean isEnabled = AppState.getInstance().getIsPilotEnabled(index);
	            int thresholdSetupState = AppState.getInstance().getThresholdSetupState(index);
	            
	            String mhzString = UtilMessage.getMsg("mhz_string");
	            String freq = AppState.getInstance().getFrequencyText(index) + " " + mhzString;
        		
	            int curRssi = AppState.getInstance().getCurrentRssi(index);
	            int rssiThreshold = AppState.getInstance().getRssiThreshold(index);
        		
	            pilotsPanel.txtChannelLabel.setText(String.format(UtilMessage.getMsg("channel_descriptor"), band, ch, freq));
	            //pilotsPanel.txtChannelLabel.setSelected(isEnabled);
	            
	            pilotsPanel.rssiLabel.setText(Integer.toString(curRssi));
	            pilotsPanel.rssiBar.setValue(AppState.convertRssiToProgress(curRssi));
	            
            	//pilotsPanel.txtThresh.setText(Integer.toString(deviceState.threshold));	
	            
	            pilotsPanel.spacePilotColor.setOpaque(true);
	            pilotsPanel.spacePilotColor.setBackground(new Color(Utils.getBackgroundColorItem(index)));
	            
	            pilotsPanel.rssiLabel.setForeground((curRssi > rssiThreshold) ? Color.RED : Color.BLACK );
	            pilotsPanel.rssiLabel.setBackground((curRssi > rssiThreshold) ? Color.RED : Color.BLACK );
	            
        	}
    	}
    }
    
	public void setMainWindow(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
	}
}
