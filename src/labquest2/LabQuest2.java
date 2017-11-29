package labquest2;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

import arduino.Arduino;
import arduino.PortDropdownMenu;
import net.miginfocom.swing.MigLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class LabQuest2 {

	private JFrame frame;

	static JButton btnRefresh;
	static Arduino arduino;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LabQuest2 window = new LabQuest2();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LabQuest2() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setSize(screenSize);

		frame.setSize(320, 240);
		frame.setBounds(100, 100, 320, 240);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new MigLayout("", "[]", "[]"));
//
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
//		frame.setUndecorated(true);
		JButton btnStartAnalysis = new JButton("Start Analysis");
		btnStartAnalysis.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new LineChartSample();
			}
		});
		frame.getContentPane().add(btnStartAnalysis, "cell 0 0");

		populateMenu();
	}
	

	public void populateMenu(){ //gets the list of available ports and fills the dropdown menu
		final PortDropdownMenu portList = new PortDropdownMenu();
		portList.refreshMenu();
		final JButton connectButton = new JButton("Connect");
		
		
		ImageIcon refresh = new ImageIcon("libs/refresh.png");
		btnRefresh = new JButton(refresh);
		
		JPanel topPanel = new JPanel();
		
		btnRefresh.addActionListener(new ActionListener(){
			
			@Override
			public void actionPerformed(ActionEvent e) {
				portList.refreshMenu();
			
		}
		});
		topPanel.add(portList);
		topPanel.add(btnRefresh);
		topPanel.add(connectButton);
		// populate the drop-down box
		
		connectButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(connectButton.getText().equals("Connect")){
				 arduino = new Arduino(portList.getSelectedItem().toString(),4800);
				 if(arduino.openConnection()){
					 connectButton.setText("Disconnect");
					 portList.setEnabled(false);
					 btnRefresh.setEnabled(false);
//					 frame.pack();
				 }
				}
				else {
					arduino.closeConnection();
					connectButton.setText("Connect");;
					portList.setEnabled(true);
					btnRefresh.setEnabled(true);
				}
			}
			
		});
		//topPanel.setBackground(Color.BLUE);
		frame.add(topPanel, BorderLayout.NORTH);
	}
	

}
