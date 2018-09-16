package com.chorusrflaptimer.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

import com.chorusrflaptimer.AppState;
import com.chorusrflaptimer.DataAction;
import com.chorusrflaptimer.IDataListener;
import com.chorusrflaptimer.util.UtilMessage;
import com.chorusrflaptimer.wrapper.SharedPreferences;

/**
 * @author Alexander Rios <br> 15/09/2018
 */
public class SettingPanel extends JPanel {

	private MainWindow mainWindow;

	JCheckBox chkLiPoMonitor;
	JProgressBar batteryProgressBar;
	JLabel lapsValueLbl;
	JLabel mLTValueLBL;
	JLabel chkSkipFirstLapLBL;
	JLabel prepTimeLBL;
	JLabel chkDeviceSoundEnabledLBL;
	JLabel chkSpeakLapTimesLBL;
	JLabel chkSpeakMessagesLBL;
	JLabel chkSpeakEnglishOnlyLBL;
	JLabel languageAppLBL;

	JLabel lapsValue;
	JLabel mLTValue;
	JCheckBox chkSkipFirstLap;
	JLabel prepTimeValueLBL;
	JCheckBox chkDeviceSoundEnabled;
	JCheckBox chkSpeakLapTimes;
	JCheckBox chkSpeakMessages;
	JCheckBox chkSpeakEnglishOnly;

	/**
	 * Create the panel.
	 */
	public SettingPanel() {
		setLayout(null);

		chkLiPoMonitor = new JCheckBox("Lipo monitor");
		chkLiPoMonitor.setBounds(12, 13, 220, 25);
		add(chkLiPoMonitor);

		chkLiPoMonitor.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				AppState.getInstance().changeEnableLiPoMonitor(e.getStateChange() == ItemEvent.SELECTED ? true : false);
			}
		});

		batteryProgressBar = new JProgressBar();
		batteryProgressBar.setBounds(240, 14, 334, 25);
		batteryProgressBar.setForeground(Color.BLACK);
		batteryProgressBar.setValue(0);
		batteryProgressBar.setStringPainted(true);
		add(batteryProgressBar);

		lapsValueLbl = new JLabel("Numero de vueltas");
		lapsValueLbl.setHorizontalAlignment(SwingConstants.LEFT);
		lapsValueLbl.setBounds(12, 43, 220, 26);
		add(lapsValueLbl);

		JButton btnDecLaps = new JButton("-");
		btnDecLaps.setBounds(237, 44, 37, 25);
		add(btnDecLaps);

		btnDecLaps.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int laps = AppState.getInstance().raceState.lapsToGo;
				AppState.getInstance().changeRaceLaps(laps - 1);
			}
		});

		lapsValue = new JLabel("-");
		lapsValue.setBounds(286, 43, 232, 26);
		lapsValue.setHorizontalAlignment(SwingConstants.CENTER);
		add(lapsValue);

		JButton btnIncLaps = new JButton("+");
		btnIncLaps.setBounds(533, 43, 41, 25);
		add(btnIncLaps);

		btnIncLaps.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int laps = AppState.getInstance().raceState.lapsToGo;
				AppState.getInstance().changeRaceLaps(laps + 1);
			}
		});

		mLTValueLBL = new JLabel("Tiempo minimo vuelta");
		mLTValueLBL.setHorizontalAlignment(SwingConstants.LEFT);
		mLTValueLBL.setBounds(12, 74, 220, 25);
		add(mLTValueLBL);

		JButton btnDecMLT = new JButton("-");
		btnDecMLT.setBounds(237, 74, 37, 25);
		add(btnDecMLT);

		btnDecMLT.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int mlt = AppState.getInstance().raceState.minLapTime;
				if (mlt > 0) {
					mlt--;
				}
				AppState.getInstance().sendBtCommand("R*M" + String.format("%02X", mlt));
			}
		});

		mLTValue = new JLabel("-");
		mLTValue.setBounds(284, 83, 237, 16);
		mLTValue.setHorizontalAlignment(SwingConstants.CENTER);
		add(mLTValue);

		JButton btnIncMLT = new JButton("+");
		btnIncMLT.setBounds(533, 74, 41, 25);
		add(btnIncMLT);

		btnIncMLT.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int mlt = AppState.getInstance().raceState.minLapTime + 1;
				AppState.getInstance().sendBtCommand("R*M" + String.format("%02X", mlt));
			}
		});

		chkSkipFirstLapLBL = new JLabel("Saltar primera vuelta");
		chkSkipFirstLapLBL.setHorizontalAlignment(SwingConstants.LEFT);
		chkSkipFirstLapLBL.setBounds(12, 104, 220, 25);
		add(chkSkipFirstLapLBL);

		chkSkipFirstLap = new JCheckBox("");
		chkSkipFirstLap.setBounds(391, 104, 25, 25);
		add(chkSkipFirstLap);

		chkSkipFirstLap.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				AppState.getInstance().shouldSkipFirstLap = !chkSkipFirstLap.isSelected() ? true : false;
				boolean shouldSkip = AppState.getInstance().shouldSkipFirstLap;
				AppState.getInstance().sendBtCommand("R*1" + (shouldSkip ? "1" : "0"));
			}
		});

		prepTimeLBL = new JLabel("Tiempo para prepara la carrera");
		prepTimeLBL.setBounds(12, 138, 220, 16);
		prepTimeLBL.setHorizontalAlignment(SwingConstants.LEFT);
		add(prepTimeLBL);

		JButton btnDecPrepTime = new JButton("-");
		btnDecPrepTime.setBounds(237, 134, 37, 25);
		add(btnDecPrepTime);

		btnDecPrepTime.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int time = AppState.getInstance().timeToPrepareForRace;
				AppState.getInstance().changeTimeToPrepareForRace(time - 1);
			}
		});

		prepTimeValueLBL = new JLabel("-");
		prepTimeValueLBL.setBounds(286, 134, 237, 16);
		prepTimeValueLBL.setHorizontalAlignment(SwingConstants.CENTER);
		add(prepTimeValueLBL);

		JButton btnIncPrepTime = new JButton("+");
		btnIncPrepTime.setBounds(533, 134, 41, 25);
		add(btnIncPrepTime);

		btnIncPrepTime.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int time = AppState.getInstance().timeToPrepareForRace;
				AppState.getInstance().changeTimeToPrepareForRace(time + 1);
			}
		});

		chkDeviceSoundEnabledLBL = new JLabel("Activar Buzzer");
		chkDeviceSoundEnabledLBL.setBounds(12, 168, 220, 16);
		chkDeviceSoundEnabledLBL.setHorizontalAlignment(SwingConstants.LEFT);
		add(chkDeviceSoundEnabledLBL);

		chkDeviceSoundEnabled = new JCheckBox("");
		chkDeviceSoundEnabled.setBounds(391, 164, 25, 25);
		add(chkDeviceSoundEnabled);

		chkDeviceSoundEnabled.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				AppState.getInstance().isDeviceSoundEnabled = chkDeviceSoundEnabled.isSelected();
				boolean isSoundEnabled = AppState.getInstance().isDeviceSoundEnabled;
				AppState.getInstance().sendBtCommand("R*S" + (isSoundEnabled ? "1" : "0" ));
			}
		});

		chkSpeakLapTimesLBL = new JLabel("Escuchar tiempo de vueltas");
		chkSpeakLapTimesLBL.setHorizontalAlignment(SwingConstants.LEFT);
		chkSpeakLapTimesLBL.setBounds(12, 194, 220, 25);
		add(chkSpeakLapTimesLBL);

		chkSpeakLapTimes = new JCheckBox("");
		chkSpeakLapTimes.setBounds(391, 194, 25, 25);
		add(chkSpeakLapTimes);

		chkSpeakLapTimes.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				AppState.getInstance().changeShouldSpeakLapTimes(chkSpeakLapTimes.isSelected() ? true : false);
			}
		});

		chkSpeakMessagesLBL = new JLabel("Escuchar mensajes");
		chkSpeakMessagesLBL.setHorizontalAlignment(SwingConstants.LEFT);
		chkSpeakMessagesLBL.setBounds(12, 224, 220, 25);
		add(chkSpeakMessagesLBL);

		chkSpeakEnglishOnlyLBL = new JLabel("Speak in english");
		chkSpeakEnglishOnlyLBL.setHorizontalAlignment(SwingConstants.LEFT);
		chkSpeakEnglishOnlyLBL.setBounds(12, 254, 214, 25);
		add(chkSpeakEnglishOnlyLBL);

		chkSpeakEnglishOnly = new JCheckBox("");
		chkSpeakEnglishOnly.setBounds(391, 254, 25, 25);
		add(chkSpeakEnglishOnly);

		chkSpeakEnglishOnly.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				AppState.getInstance().changeShouldSpeakEnglishOnly(chkSpeakEnglishOnly.isSelected() ? true : false);
			}
		});

		languageAppLBL = new JLabel("Language");
		languageAppLBL.setHorizontalAlignment(SwingConstants.LEFT);
		languageAppLBL.setBounds(12, 284, 220, 22);
		add(languageAppLBL);

		JComboBox languageApp = new JComboBox();
		languageApp.setBounds(383, 284, 41, 22);
		languageApp.addItem("en");
		languageApp.addItem("es");
		languageApp.addItem("it");
		languageApp.addItem("de");
		languageApp.addItem("ru");

		add(languageApp);

		languageApp.addActionListener(new ActionListener() {
			@SuppressWarnings("rawtypes")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox) e.getSource();
				String value = (String) cb.getSelectedItem();
				AppState.getInstance().languageApp = value;
				mainWindow.loadMsg();
			}
		});

		chkSpeakMessages = new JCheckBox("");
		chkSpeakMessages.setBounds(391, 224, 25, 25);
		add(chkSpeakMessages);

		chkSpeakMessages.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				AppState.getInstance().changeShouldSpeakMessages(chkSpeakMessages.isSelected() ? true : false);
			}
		});

		load();
	}

	private void load() {
		// ---------------------------
		// Cargar estado
		boolean isEnabled = AppState.getInstance().isLiPoMonitorEnabled;
		chkLiPoMonitor.setSelected(isEnabled);
		batteryProgressBar.setVisible(isEnabled);

		updateText();
		chkSkipFirstLap.setSelected(AppState.getInstance().shouldSkipFirstLap);
		chkDeviceSoundEnabled.setSelected(AppState.getInstance().isDeviceSoundEnabled);
		chkSpeakLapTimes.setSelected(AppState.getInstance().shouldSpeakLapTimes);
		chkSpeakMessages.setSelected(AppState.getInstance().shouldSpeakMessages);
		chkSpeakEnglishOnly.setSelected(AppState.getInstance().shouldSpeakEnglishOnly);
		//updateSkipFirstLapCheckbox();
		//updateSoundCheckbox();
		//updateSpeakLapTimesCheckbox();
		//updateSpeakMessagesCheckbox();
		//updateSpeakEnglishOnlyCheckbox();
		updateLiPoMonitorCheckbox();
		updateBatteryProgressIndicator();
		// ---------------------------
		// Cargar mensajes
		// loadMsg();

		// ---------------------------
		// Listener de pantalla
		AppState.getInstance().addListener(new IDataListener() {
			@Override
			public void onDataChange(DataAction dataItemName) {
				// if (!isAdded()) return;

				switch (dataItemName) {
				case RaceMinLap:
				case RaceLaps:
				case PreparationTime:
				case VoltageAdjustmentConst:
					updateText();
					break;
				case SoundEnable:
					updateSoundCheckbox();
					break;
				case SkipFirstLap:
					updateSkipFirstLapCheckbox();
					break;
				case SpeakLapTimes:
					updateSpeakLapTimesCheckbox();
					break;
				case SpeakMessages:
					updateSpeakMessagesCheckbox();
					break;
				case SpeakEnglishOnly:
					updateSpeakEnglishOnlyCheckbox();
					break;
				case BatteryVoltage:
					updateBatteryProgressIndicator();
					updateBatteryVoltageText();
					break;
				case LiPoMonitorEnable:
					updateLiPoMonitorCheckbox();
					break;
				case ConnectionTester:
					updateRangeTester();
					break;
				}
			}
		});
	}

	public void loadMsg() {
		chkLiPoMonitor.setText(UtilMessage.getMsg("setup_lipo_monitor"));
		lapsValueLbl.setText(UtilMessage.getMsg("setup_nr_laps"));
		mLTValueLBL.setText(UtilMessage.getMsg("setup_min_lap_time"));
		chkSkipFirstLapLBL.setText(UtilMessage.getMsg("setup_skip_first_lap"));
		prepTimeLBL.setText(UtilMessage.getMsg("setup_time_to_prepare"));
		chkDeviceSoundEnabledLBL.setText(UtilMessage.getMsg("setup_enable_device_sounds"));
		chkSpeakLapTimesLBL.setText(UtilMessage.getMsg("setup_speak_lap_times"));
		chkSpeakMessagesLBL.setText(UtilMessage.getMsg("setup_speak_messages"));
		chkSpeakEnglishOnlyLBL.setText(UtilMessage.getMsg("setup_speak_english_only"));
		languageAppLBL.setText(UtilMessage.getMsg("language", "Language"));
	}

	private void updateText() {

		mLTValue.setText(String.format(UtilMessage.getMsg("setup_time", "%d sec"), AppState.getInstance().raceState.minLapTime));

		lapsValue.setText(Integer.toString(AppState.getInstance().raceState.lapsToGo));

		prepTimeValueLBL.setText(String.format(UtilMessage.getMsg("setup_time", "%d sec"), AppState.getInstance().timeToPrepareForRace));
	}

	private void updateSkipFirstLapCheckbox() {
		//chkSkipFirstLap.setSelected(AppState.getInstance().shouldSkipFirstLap);
	}

	private void updateSoundCheckbox() {
		//chkDeviceSoundEnabled.setSelected(AppState.getInstance().isDeviceSoundEnabled);
	}

	private void updateSpeakLapTimesCheckbox() {
		//chkSpeakLapTimes.setSelected(AppState.getInstance().shouldSpeakLapTimes);
	}

	private void updateSpeakMessagesCheckbox() {
		//chkSpeakMessages.setSelected(AppState.getInstance().shouldSpeakMessages);
	}

	private void updateSpeakEnglishOnlyCheckbox() {
		//chkSpeakEnglishOnly.setSelected(AppState.getInstance().shouldSpeakEnglishOnly);
	}

	private void updateLiPoMonitorCheckbox() {
		boolean isEnabled = AppState.getInstance().isLiPoMonitorEnabled;
		batteryProgressBar.setVisible(isEnabled);
	}

	private void updateBatteryProgressIndicator() {
		batteryProgressBar.setString(String.format("%.2f", AppState.getInstance().batteryVoltage) + "V");
		batteryProgressBar.setValue(AppState.getInstance().batteryPercentage);
		// bateryProgressBar.setForeground(AppState.getInstance().batteryPercentage > 10 ? Color.green : Color.red);
	}

	private void updateBatteryVoltageText() {
		batteryProgressBar.setString(String.format("%.2f", AppState.getInstance().batteryVoltage) + "V");
	}

	private void updateRangeTester() {
		// TextView txtRangeTestPercentage = (TextView)
		// rootView.findViewById(R.id.txtRangeTestPercentage);
		// txtRangeTestPercentage.setText(String.format("%.2f",
		// AppState.getInstance().getConnectionTestFailurePercentage()) + "%");
		// TextView txtRangeTestMaxTime = (TextView)
		// rootView.findViewById(R.id.txtRangeMaxTime);
		// txtRangeTestMaxTime.setText(Long.toString(AppState.getInstance().getConnectionTestMaxDelay())
		// + "ms");
		// TextView txtRangeTestAvgTime = (TextView)
		// rootView.findViewById(R.id.txtRangeAvgTime);
		// txtRangeTestAvgTime.setText(Long.toString(AppState.getInstance().getConnectionTestAvgDelay())
		// + "ms");
		// TextView txtRangeTestMinTime = (TextView)
		// rootView.findViewById(R.id.txtRangeMinTime);
		// txtRangeTestMinTime.setText(Long.toString(AppState.getInstance().getConnectionTestMinDelay())
		// + "ms");
		// TextView txtReceiveDelay = (TextView)
		// rootView.findViewById(R.id.txtReceiveDelay);
		// txtReceiveDelay.setText(Long.toString(Utils.getReceiveDelay()) + "ms");
	}

	public void setMainWindow(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
	}
}
