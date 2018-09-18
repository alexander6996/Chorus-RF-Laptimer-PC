package com.chorusrflaptimer.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.chorusrflaptimer.AppState;
import com.chorusrflaptimer.DataAction;
import com.chorusrflaptimer.DeviceState;
import com.chorusrflaptimer.IDataListener;
import com.chorusrflaptimer.LapResult;
import com.chorusrflaptimer.Utils;
import com.chorusrflaptimer.util.UtilMessage;
import com.chorusrflaptimer.wrapper.Handler;
import com.chorusrflaptimer.wrapper.Message;

/**
 * @author Alexander Rios <br> 15/09/2018
 */
@SuppressWarnings("serial")
public class RacePanel extends JPanel {
	
	private JTable generalTbl;
	
	private boolean mIsStartingRace = false;
	
	JButton btnRace;
	
	JButton btnCalibrate;
	
	JButton btnStop;
	
	JButton btnExpCsv;
	
	JLabel lblMensaje;
	
	private Handler mRaceStartingHandler;
	
	private static final String MISSING_RESULTS_MARKER = "-";
	
	SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss.SSSS");
	private JTable detalleTbl;

	/**
	 * Create the panel.
	 */
	public RacePanel() {
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		
		JPanel botonesPanel = new JPanel();
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(botonesPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(tabbedPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 672, Short.MAX_VALUE))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(botonesPanel, GroupLayout.PREFERRED_SIZE, 59, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 415, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		btnRace = new JButton("Start");
		btnRace.setBounds(0, 30, 150, 25);
		
		btnCalibrate = new JButton("Calibrar");
		btnCalibrate.setBounds(309, 30, 150, 25);
		
		btnExpCsv = new JButton("-> CSV");
		btnExpCsv.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				triggerCSVReportGeneration();
			}
		});
		btnExpCsv.setBounds(463, 30, 150, 25);
		
		btnStop = new JButton("Stop");
		btnStop.setBounds(154, 30, 150, 25);
		botonesPanel.setLayout(null);
		botonesPanel.add(btnRace);
		botonesPanel.add(btnStop);
		botonesPanel.add(btnCalibrate);
		botonesPanel.add(btnExpCsv);
		
		lblMensaje = new JLabel("");
		lblMensaje.setForeground(Color.RED);
		lblMensaje.setBounds(0, 1, 795, 16);
		botonesPanel.add(lblMensaje);
		
		JPanel generalPanel = new JPanel();
		tabbedPane.addTab("General", null, generalPanel, null);
		generalPanel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		generalPanel.add(scrollPane, BorderLayout.CENTER);
		
		generalTbl = new JTable();
		generalTbl.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
					"Color", "Piloto", "Cant. Vueltas" ,"Posicion", "Pocicion Mejor vuelta","Tiempo Carrera", "Ultima Vuelta", "Mejor vuelta" 
			}
		));
		scrollPane.setViewportView(generalTbl);
		
		JPanel detallePanel = new JPanel();
		tabbedPane.addTab("Detalle", null, detallePanel, null);
		detallePanel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		detallePanel.add(scrollPane_1);
		
		detalleTbl = new JTable();
		detalleTbl.setModel(new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
						"Color", "Piloto", "Time" ,"Full Time" 
				}
			));
		scrollPane_1.setViewportView(detalleTbl);
		setLayout(groupLayout);
		
		load();
	}
	
	private void load() {
		
        mRaceStartingHandler = new Handler() {
            public void handleMessage(Message msg) {
                int counter = msg.what;
                if (counter == 0) {
                    //last beep
                    AppState.getInstance().playTone(AppState.TONE_GO, AppState.DURATION_GO);
                    AppState.getInstance().sendBtCommand("R*R1");
                } else {
                    this.sendEmptyMessageDelayed(counter - 1, 1000);
                    //first 3 beeps
                    if (counter < AppState.START_BEEPS_COUNT) {
                        AppState.getInstance().sendBtCommand("T"+ String.format("%01X", counter));
                        AppState.getInstance().playTone(AppState.TONE_PREPARE, AppState.DURATION_PREPARE);
                    }
                }
            }
        };
		
	       AppState.getInstance().addListener(new IDataListener() {
	            @SuppressWarnings("incomplete-switch")
				@Override
	            public void onDataChange(DataAction dataItemName) {

	                switch (dataItemName) {
	                    case RaceState:
	                    case NDevices:
	                        if (AppState.getInstance().raceState.isStarted) {
	                            mIsStartingRace = false;
	                            resetRaceResults();
	                        }
	                        updateButtons();
	                        break;
	                    case RaceIsFinished:
	                        
	                        break;
	                    case DeviceThreshold:
	                    case Disconnect:
	                    case DeviceCalibrationStatus:
	                        updateButtons();
	                        break;
	                    case PilotEnabledDisabled:
	                        updateButtons();
	                        updateResults();
	                        break;
	                    case LapResult:
	                    case RaceLaps:
	                    case SkipFirstLap:
//	                    case DeviceChannel:
//	                    case DeviceBand:
	                        updateResults();
	                        break;
	                }
	            }
	        });
	       
	        btnCalibrate.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	            	
	            	JFrame frame = null;
	            	JDialog dlgProgress = new JDialog(frame, "Please wait...", true);
	            	dlgProgress.setLocationRelativeTo(null);
	            	JLabel lblStatus = new JLabel(UtilMessage.getMsg("calibrate_timers"));
	            	JProgressBar pbProgress = new JProgressBar(0, AppState.CALIBRATION_TIME_MS);
	            	//pbProgress.setIndeterminate(true); //we'll use an indeterminate progress bar
	            	dlgProgress.getContentPane().add(BorderLayout.NORTH, lblStatus);
	            	dlgProgress.getContentPane().add(BorderLayout.CENTER, pbProgress);
	            	dlgProgress.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE); // prevent the user from closing the dialog
	            	dlgProgress.setSize(300, 90);
	            	
	                AppState.getInstance().clearOldCalibrationTimes();
	                final long msStart = System.currentTimeMillis();
	            	
	                final Handler h = new Handler() {
	                    public void handleMessage(Message msg) {
	                        switch(msg.what) {
	                            case 0:
	                                if (pbProgress.getValue() < pbProgress.getMaximum()) {
	                                	pbProgress.setValue(AppState.CALIBRATION_TIME_MS/100);
	                                    this.sendEmptyMessageDelayed(0, AppState.CALIBRATION_TIME_MS/100);
	                                }
	                                break;
	                            case 1:
	                                long msEnd = System.currentTimeMillis();
	                                AppState.getInstance().sendBtCommand("R*t");
	                                AppState.getInstance().setCalibrationActualTime((int)(msEnd - msStart));
	                                dlgProgress.setVisible(false);
	                                break;
	                        }
	                    }
	                };

	                AppState.getInstance().sendBtCommand("R*t");
	                h.sendEmptyMessage(0);
	                h.sendEmptyMessageDelayed(1, AppState.CALIBRATION_TIME_MS);
	            	
	            	dlgProgress.setVisible(true);
	            }
	        });

	        btnRace.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                boolean isStarted = AppState.getInstance().raceState.isStarted;
	                if (!isStarted && !mIsStartingRace) {
	                    btnRace.setEnabled(false);
	                    btnStop.setEnabled(true);

	                    //stop rssi monitoring first, then start race
	                    AppState.getInstance().sendBtCommand("R*I0000");

	                    btnRace.setText(UtilMessage.getMsg("starting_race"));
	                    AppState.getInstance().sendBtCommand("TP");
	                    mIsStartingRace = true;
	                    int timeBeforeRace = AppState.getInstance().timeToPrepareForRace;
	                    if (timeBeforeRace >= AppState.MIN_TIME_BEFORE_RACE_TO_SPEAK) {
	                        //AppState.getInstance().textSpeaker.speak(R.string.race_announcement_starting, timeBeforeRace - 2);
	                    	//System.out.println(timeBeforeRace - 2);
	                    }

	                   mRaceStartingHandler.sendEmptyMessage(timeBeforeRace);
	                }
	            }
	        });

	        // use long press to stop race to make sure it doesn't stop occasionally
	        btnStop.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                boolean isStarted = AppState.getInstance().raceState.isStarted;
                    btnRace.setEnabled(true);
                    btnStop.setEnabled(false);

	                if (isStarted) {
	                    //stop race and start RSSI monitoring
	                    AppState.getInstance().sendBtCommand("R*R0");
	                    AppState.getInstance().sendBtCommand("R*I0064");
	                }  else if (mIsStartingRace) {
	                    //TODO: move mIsStartingRace flag into appState, use updateButtons to update button captions
	                    mIsStartingRace = false;
	                    AppState.getInstance().sendBtCommand("R*R0"); // send end race (workaround for the led gate to switch to no-race mode)
	                    mRaceStartingHandler.removeCallbacksAndMessages(null);
	                    updateButtons();
	                }
	            }
	        });
	        
	        generalTbl.getColumnModel().getColumn(0).setCellRenderer(new StatusColumnCellRenderer());
	        generalTbl.getColumnModel().getColumn(0).setPreferredWidth(20);

	        detalleTbl.getColumnModel().getColumn(0).setCellRenderer(new StatusColumnCellRenderer());
	        detalleTbl.getColumnModel().getColumn(0).setPreferredWidth(20);

	}
	
	public class StatusColumnCellRenderer extends DefaultTableCellRenderer {
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
			Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
            cell.setForeground(new Color(Integer.parseInt((String) value)));
			
			// Cells are by default rendered as a JLabel.
			JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
			l.setBackground(new Color(Integer.parseInt((String) value)));
			value = "";
			return l;
		}
	}
	
	public void loadMsg() {
		btnStop.setText(UtilMessage.getMsg("stop_race"));
		btnRace.setText(UtilMessage.getMsg("start_race"));
		btnCalibrate.setText(UtilMessage.getMsg("setup_calibrate_timers"));
		//btnExpCsv.setText();
		
		//Changes Collumn header text
		generalTbl.getColumnModel().getColumn(0).setHeaderValue("-->");
		generalTbl.getColumnModel().getColumn(1).setHeaderValue(UtilMessage.getMsg("setup_pilot_name"));
		generalTbl.getColumnModel().getColumn(2).setHeaderValue(UtilMessage.getMsg("setup_nr_laps"));
		generalTbl.getColumnModel().getColumn(3).setHeaderValue(UtilMessage.getMsg("race_position"));
		generalTbl.getColumnModel().getColumn(4).setHeaderValue(UtilMessage.getMsg("best_lap_position"));
		generalTbl.getColumnModel().getColumn(5).setHeaderValue(UtilMessage.getMsg("race_time"));
		generalTbl.getColumnModel().getColumn(6).setHeaderValue(UtilMessage.getMsg("last_lap"));
		generalTbl.getColumnModel().getColumn(7).setHeaderValue(UtilMessage.getMsg("best_lap"));
		
		// Forces the header to resize and repaint itself
		generalTbl.getTableHeader().resizeAndRepaint();
		
		lblMensaje.setText("");
		
		detalleTbl.getColumnModel().getColumn(0).setHeaderValue("-->");
		detalleTbl.getColumnModel().getColumn(1).setHeaderValue(UtilMessage.getMsg("setup_pilot_name"));
		detalleTbl.getColumnModel().getColumn(2).setHeaderValue(UtilMessage.getMsg("race_time"));
		detalleTbl.getColumnModel().getColumn(3).setHeaderValue(UtilMessage.getMsg("race_time"));

	}
	
    public void useNewAdapter() {
    	removeAllRowTable(generalTbl);
    	removeAllRowTable(detalleTbl);
    }

    public void updateResults() {
    	AppState app = AppState.getInstance();
    	
    	ArrayList<String[]> dataTableGeneral = new ArrayList<String[]>();
    	ArrayList<String[]> dataTableDetail = new ArrayList<String[]>();
    	for(int deviceId =0; deviceId < app.raceResults.size(); deviceId++) {
            DeviceState currentState = app.deviceStates.get(deviceId);

            String color = ""+Utils.getBackgroundColorItem(deviceId);
            
            String pilotName = currentState.pilotName;
            
            int laps = AppState.getInstance().getLapsCount(deviceId);
            
            int positionByTime = AppState.getInstance().getPilotPositionByTotalTime(deviceId);
            String position = positionByTime != -1 ? Integer.toString(positionByTime) : MISSING_RESULTS_MARKER;
            
            int positionByBestLap = AppState.getInstance().getPilotPositionByBestLap(deviceId);
            String textPositionByBestLap = positionByBestLap != -1 ? Integer.toString(positionByBestLap) : MISSING_RESULTS_MARKER;

            int raceTime = AppState.getInstance().getTotalRaceTime(deviceId);
            String textRaceTime = Utils.convertMsToDisplayTime(raceTime);

            LapResult last = AppState.getInstance().getLastLap(deviceId);
            String textLastLap = last != null ? last.getDisplayTime() : MISSING_RESULTS_MARKER;

            String textBestLap = "";
            int bestLapId = AppState.getInstance().getBestLapId(deviceId);
            if (bestLapId != -1) {
                LapResult best = AppState.getInstance().raceResults.get(deviceId).get(bestLapId);
                textBestLap = best.getDisplayTime();
            } else {
                textBestLap ="-";
            }

            dataTableGeneral.add(new String[] {color, pilotName, ""+laps, position, textPositionByBestLap, textRaceTime, textLastLap, textBestLap });
            
            //---------------------------
            //Detail
            ArrayList<LapResult> lapDetailList = app.raceResults.get(deviceId);
            for(int indexLapResult = 0;indexLapResult<lapDetailList.size();indexLapResult++) {
            	LapResult lapResult = lapDetailList.get(indexLapResult);
            	
            	dataTableDetail.add(new String[] { color, pilotName, lapResult.getDisplayTime(), format.format(lapResult.getRecordDate()) });
            }
    	}
    	
    	removeAllRowTable(generalTbl);
    	insertRowTable(dataTableGeneral, generalTbl);

		Collections.sort(dataTableDetail, new Comparator<String[]>() {
			public int compare(String[] s1, String[] s2) {
				return s1[3].compareTo(s2[3]);
			}
		});
    	
    	removeAllRowTable(detalleTbl);
    	insertRowTable(dataTableDetail, detalleTbl);

    }

    public void resetRaceResults() {
    	AppState.getInstance().resetRaceResults();
        useNewAdapter();
    }

    public void updateButtons() {
    	
    	lblMensaje.setText("");

        boolean areAllCalibrated = AppState.getInstance().areAllEnabledDevicesCalibrated();

        //btnCalibrate.setEnabled(!areAllCalibrated);
        btnCalibrate.setEnabled(false);

        if (AppState.getInstance().raceState.isStarted) {
            btnRace.setEnabled(false);
            btnStop.setEnabled(true);
        } else {
            boolean areAllThrSet = AppState.getInstance().areAllThresholdsSet();
            int numberOfPilots = AppState.getInstance().getEnabledPilotsCount();
            if (!areAllCalibrated) {
                btnRace.setEnabled(false);
            } else if (areAllThrSet) {
                if (numberOfPilots == 0) {
                    btnRace.setEnabled(false);
                    btnStop.setEnabled(false);
                    lblMensaje.setText(UtilMessage.getMsg("start_race_validation_pilots"));
                } else {
                    btnRace.setEnabled(true);
                    btnRace.setText(String.format(UtilMessage.getMsg("pilots_in_race.one"), numberOfPilots, numberOfPilots));
                }
            } else {
                //btnRace.setEnabled(false);
                //btnStop.setEnabled(false);
                lblMensaje.setText(UtilMessage.getMsg("start_race_validation_thresholds"));
            }
        }

        if (!AppState.getInstance().isConnected) {
            btnRace.setEnabled(false);
            btnStop.setEnabled(false);
            btnCalibrate.setEnabled(false);
        }
    }

    private void triggerCSVReportGeneration(){
    	
    	JFileChooser fileChooser = new JFileChooser();
    	fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
    	int result = fileChooser.showSaveDialog(this);
    	if (result == JFileChooser.APPROVE_OPTION) {
    	    File selectedFile = fileChooser.getSelectedFile();
    	    
    	    generateCSVReport(selectedFile.getAbsolutePath());
    	}
    }

    /**
     * This function will generate the Report string which is to be written in the csv file
     * @return
     */
    @SuppressWarnings("unused")
	private String generateCSVReportString(){
        AppState app = AppState.getInstance();
        ArrayList<ArrayList<LapResult>> raceResults = app.raceResults;

        StringBuilder sb = new StringBuilder();

        sb.append("LAP,PILOT,CHANNEL,FREQUENCY,TIME,RECORDED AT\n");

        int numLaps = app.raceState.lapsToGo;

        //startOfLapCount depends if it should skip First Lap.
        int startOfLapCount = 0;
        boolean shouldSkipFirstLap = app.shouldSkipFirstLap;
        if(shouldSkipFirstLap){
            startOfLapCount = 1;
        }
        //iterate per pilot
        for(int i = 0; i < app.deviceStates.size(); i++){
            ArrayList<LapResult> pilotResults = raceResults.get(i);
            int pilotLaps = pilotResults.size();
            String pilot = app.deviceStates.get(i).pilotName;
            String channel = app.getChannelText(i);
            String band = app.getBandText(i);
            String freq = app.getFrequencyText(i);


            //iterate per lap of each pilot. till allowed number of laps.
            for(int j = startOfLapCount; j < pilotLaps; j++){
                //if shouldSkipFirstLap, lapCount will start from 1
                int lapNumber = shouldSkipFirstLap ? j : j + 1;
                LapResult lapResult = pilotResults.get(j);
                String recordTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SZ").format(lapResult.getRecordDate());
                sb.append(lapNumber + "," + pilot + "," + band + channel + "," + freq+ "," + Utils.convertMsToReportTime(lapResult.getMs()) + "," + recordTime + "\n");
            }
        }
        return sb.toString();
    }

	/**
	 * This function will generate the csv file report
	 */
	private void generateCSVReport(String fileName) {

		// generate CSVReport String - to be written in csv file
		String report = generateCSVReportString();
		Date today = new Date();

		String path = Utils.getReportPath();

		// Create the folder.
		File folder = new File(path);
		if (!folder.exists()) {
			folder.mkdirs();
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		// dateSuffix Format will be like this: 20170214_104304
		String dateSuffix = sdf.format(today);

		// Create the file.
		// File name will look like this: race_20170214_104304
		File file = new File(fileName + "_race_" + dateSuffix + ".csv");
		try {
			file.createNewFile();
			FileOutputStream fOut = new FileOutputStream(file);
			OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
			myOutWriter.append(report);
			myOutWriter.close();
			fOut.flush();
			fOut.close();
			// set fileName for toast in RaceResultFragment
			fileName = file.getPath();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void insertRowTable(ArrayList<String[]> dataTable, JTable table) {
		if(dataTable.size() > 0) {
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			for(int index =0;index < dataTable.size();index++) {
				model.addRow(dataTable.get(index));	
			}
		}
	}

	private void removeAllRowTable(JTable table) {
		if(((DefaultTableModel)table.getModel()).getRowCount() > 0) {
			do {
				((DefaultTableModel)table.getModel()).removeRow(0);	
			}while(((DefaultTableModel)table.getModel()).getRowCount() > 0);
		}
	}
}
