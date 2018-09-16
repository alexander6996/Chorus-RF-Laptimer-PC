package com.chorusrflaptimer.gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JPanel;

import com.chorusrflaptimer.AppState;
import com.chorusrflaptimer.DataAction;
import com.chorusrflaptimer.DeviceState;
import com.chorusrflaptimer.IDataListener;
import com.chorusrflaptimer.util.UtilMessage;

/**
 * @author Alexander Rios <br> 15/09/2018
 */
@SuppressWarnings("serial")
public class FrequencyPanel extends JPanel {

	ArrayList<FrequencyItemPanel> panels = new ArrayList<FrequencyItemPanel>();
	
	JPanel panel;
	
	MainWindow mainWindow;
	
	/**
	 * Create the panel.
	 */
	public FrequencyPanel() {
		setLayout(null);
		
		panel = new JPanel();
		panel.setBounds(5, 5, 835, 440);
		add(panel);
		panel.setLayout(new GridLayout(3, 3, 0, 0));

		load();
	}
	
	private void load() {
		AppState.getInstance().addListener(new IDataListener() {
            @SuppressWarnings("incomplete-switch")
			@Override
            public void onDataChange(DataAction dataItemName) {
                switch (dataItemName) {
                    case NDevices:
                    case DeviceBand:
                    case DeviceChannel:
                        updateResults();
                        break;
                    case DeviceRSSI:
                    case Disconnect:
                        updateCurrentRSSI();
                        break;
                }
            }
        });
        useNewAdapter();
	}

	public void loadMsg() {
    	for(int index=0;index < panels.size() ;index++ ) {
    		FrequencyItemPanel devicePanel = panels.get(index);
    		devicePanel.loadMsg();
    	}
	}
	
    public void useNewAdapter() {
    	for(int index=0;index < 8 ;index++ ) {
    		FrequencyItemPanel frequencyPanel = new FrequencyItemPanel( index , AppState.getInstance().deviceStates );
    		panel.add(frequencyPanel);
    		panels.add(frequencyPanel);
    		frequencyPanel.setVisible(false);
    	}
    	updatePanel();
    }
    
    public void updatePanel() {
    	if(AppState.getInstance().deviceStates != null) {
        	for(int index=0;index < AppState.getInstance().deviceStates.size() ;index++ ) {
        		FrequencyItemPanel frequencyPanel = panels.get(index);
        		frequencyPanel.setVisible(true);
        	}
    	}    	
    }

    public void updateResults() {
    	System.out.println("updateResults");
//        ListView listView = (ListView)mRootView.findViewById(R.id.lvChannels);
//        ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
    }

    public void updateCurrentRSSI() {
    	if(AppState.getInstance().deviceStates != null) {
        	for(int index=0;index < AppState.getInstance().deviceStates.size() ;index++ ) {
        		DeviceState deviceState = AppState.getInstance().deviceStates.get(index);
        		FrequencyItemPanel frequencyPanel = panels.get(index);
        		
				int curRssi = AppState.getInstance().getCurrentRssi(index);
				int rssiThreshold = AppState.getInstance().getRssiThreshold(index);
				frequencyPanel.rssiBar.setValue(AppState.convertRssiToProgress(curRssi));
				int colorId = (curRssi > rssiThreshold) ? Color.GRAY.getRGB() : Color.RED.getRGB();
				frequencyPanel.rssiBar.setForeground(new Color(colorId));
				frequencyPanel.rssiLabel.setText(Integer.toString(curRssi));
				
				frequencyPanel.txtChannel.setText(AppState.getInstance().getChannelText(index));
				frequencyPanel.txtBand.setText(AppState.getInstance().getBandText(index));
				String mhzString = UtilMessage.getMsg("mhz_string");
				frequencyPanel.txtFreq.setText(AppState.getInstance().getFrequencyText(index) + " " + mhzString);
        	}
    	}
    	
//        ListView mListView = (ListView)mRootView.findViewById(R.id.lvChannels);
//        int first = mListView.getFirstVisiblePosition();
//        int last = mListView.getLastVisiblePosition();
//        for (int i = first; i <= last; i++) {
//            View convertView = mListView.getChildAt(i - first);
//            if (convertView != null) {
//                ProgressBar bar = (ProgressBar) convertView.findViewById(R.id.rssiBar);
//                TextView txt = (TextView) convertView.findViewById(R.id.txtRssi);
//                int curRssi = AppState.getInstance().getCurrentRssi(i);
//                int rssiThreshold = AppState.getInstance().getRssiThreshold(i);
//                bar.setProgress(AppState.convertRssiToProgress(curRssi));
//                int colorId = (curRssi > rssiThreshold) ? R.color.colorAccent : R.color.colorPrimary;
//                int color = ContextCompat.getColor(mContext, colorId);
//                bar.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
//                txt.setText(Integer.toString(curRssi));
//            }
//        }
    }
    
	public void setMainWindow(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
	}
}
