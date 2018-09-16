package com.chorusrflaptimer.gui;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.chorusrflaptimer.util.UtilMessage;

/**
 * @author Alexander Rios <br> 15/09/2018
 */
@SuppressWarnings("serial")
public class MainWindow extends JFrame {

	private JPanel contentPane;
	private SettingPanel settingPanel;
	private FrequencyPanel frequencyPanel;
	private PilotsPanel pilotsPanel;
	private RacePanel racePanel;
	private JPanel panel;

	private JTabbedPane tabbedPane;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	
	private ConexionWindow conexionWindow;
	
	public JButton connectionButton;
	
	/**
	 * Create the frame.
	 */
	public MainWindow() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(MainWindow.class.getResource("/com/chorusrflaptimer/img/ic_appbar.png")));
		setTitle("Chorus Rf Laptimer V1.0 - BETA");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 850, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		setLocationRelativeTo(null);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		
		settingPanel = new SettingPanel();
		frequencyPanel = new FrequencyPanel();
		pilotsPanel = new PilotsPanel();
		racePanel = new RacePanel();
		
		tabbedPane.addTab("Setting", null, settingPanel, null);
		tabbedPane.addTab("Frequency", null, frequencyPanel, null);
		tabbedPane.addTab("Pilots", null, pilotsPanel, null);
		tabbedPane.addTab("Race", null, racePanel, null);
		
		panel = new JPanel();
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(panel, GroupLayout.DEFAULT_SIZE, 614, Short.MAX_VALUE)
				.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 614, Short.MAX_VALUE)
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 54, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 371, Short.MAX_VALUE))
		);
		
		lblNewLabel = new JLabel("");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setIcon(new ImageIcon(MainWindow.class.getResource("/com/chorusrflaptimer/img/ic_appbar.png")));
		
		lblNewLabel_1 = new JLabel("Chorus RF Laptimer");
		lblNewLabel_1.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 24));
		
		connectionButton = new JButton("");
		connectionButton.setContentAreaFilled(false);
		connectionButton.setOpaque(true);
		connectionButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(conexionWindow == null) {

				}
				conexionWindow.setVisible(true);
				conexionWindow.load();
			}
		});
		connectionButton.setIcon(new ImageIcon(MainWindow.class.getResource("/com/chorusrflaptimer/img/ic_connection.png")));
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 439, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 265, Short.MAX_VALUE)
					.addComponent(connectionButton, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
					.addComponent(lblNewLabel, GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE)
					.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(11)
					.addComponent(connectionButton, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
					.addGap(11))
		);
		panel.setLayout(gl_panel);
		contentPane.setLayout(gl_contentPane);
		
		load();
	}
	
	private void load() {
		conexionWindow = new ConexionWindow();
		conexionWindow.setVisible(false);
		conexionWindow.setMainWindow(this);
		
		frequencyPanel.setMainWindow(this);
		pilotsPanel.setMainWindow(this);
		settingPanel.setMainWindow(this);
		
		loadMsg();
	}

	public void loadMsg() {
		settingPanel.loadMsg();
		frequencyPanel.loadMsg();
		pilotsPanel.loadMsg();
		racePanel.loadMsg();
		
		tabbedPane.setTitleAt(0, UtilMessage.getMsg("tab_setup"));
		tabbedPane.setTitleAt(1, UtilMessage.getMsg("tab_frequency"));
		tabbedPane.setTitleAt(2, UtilMessage.getMsg("tab_pilots"));
		tabbedPane.setTitleAt(3, UtilMessage.getMsg("tab_race"));
	}
	
    public void updatePanels() {
    	frequencyPanel.updatePanel();
    	pilotsPanel.updatePanel();
    }
}
