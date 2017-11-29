package labquest2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.Timer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;

import arduino.Arduino;
import net.miginfocom.swing.MigLayout;

public class LineChartSample extends JFrame {

	ArrayList<Double> a = new ArrayList<Double>();
	ArrayList<Double> b = new ArrayList<Double>();
	// double[][] data = { { Math.random(), Math.random(), Math.random() }, { 1,
	// 2, 3 } };
	int i = 0;

	double temperature=0.0f;
	private XYDataset createDataset() {
		a.add((double) i);
		if(LabQuest2.arduino!=null) {
			try {
				temperature=Double.parseDouble(LabQuest2.arduino.serialRead(0));				
			}catch(Exception ex) {
				
			}
		}
		b.add(temperature);
		DefaultXYDataset ds = new DefaultXYDataset();
		double[][] data = { extractArray(a), extractArray(b) };
		i++;
		ds.addSeries("Temperature", data);

		return ds;
	}

	private double[] extractArray(ArrayList a) {
		double[] xy = new double[a.size()];
		int index = 0;
		for (Iterator i = a.iterator(); i.hasNext();) {
			xy[index] = (double) i.next();
			index++;
		}
		return xy;
	}

	// HTTP POST request
	private final String USER_AGENT = "Mozilla/5.0";

	private void sendPost() {

//		System.setProperty("https.proxyHost", "https://proxy.ctu.edu.vn");
//		System.setProperty("https.proxyPort", "3128");
//		System.setProperty("http.proxyHost", "https://proxy.ctu.edu.vn");
//		System.setProperty("http.proxyPort", "3128");
		String url = "http://smartmekong.vn/testquantrac2/webservice/public/solieucambien";
		URL obj;
		try {
			obj = new URL(url);
//			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("172.18.72.17", 3128));
//			HttpURLConnection con = (HttpURLConnection) obj.openConnection(proxy);
			

			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			
			// add reuqest header
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			String urlParameters = "StationID=TQT1&TMP="+temperature+"&token=Nguyen@Trung@Truc";

			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'POST' request to URL : " + url);
			System.out.println("Post parameters : " + urlParameters);
			System.out.println("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// print result
			System.out.println(response.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//			System.exit(1);
		}

	}

	// XYChart.Series series;// = new XYChart.Series();
	// @Override public void start(Stage stage) {
	public LineChartSample() {
		setSize(320, 240);
		setLocation(0, 0);
		// setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		// getContentPane().setLayout(new MigLayout("", "[]", "[]"));
		JButton btnStartAnalysis = new JButton("Close");
		btnStartAnalysis.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				setVisible(false);
				dispose();
			}
		});
		getContentPane().add(btnStartAnalysis);// , "cell 0 0");
		 setExtendedState(JFrame.MAXIMIZED_BOTH);
		// setUndecorated(true);
		XYDataset ds = createDataset();
		JFreeChart chart = ChartFactory.createXYLineChart("Analysis Chart", "time", "degree C", ds,
				PlotOrientation.VERTICAL, true, true, false);
		ChartPanel cp = new ChartPanel(chart);
		// now make your timer
		int delay = 1000; // milliseconds
		ActionListener timerAction = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				chart.getXYPlot().setDataset(createDataset());
			}
		};
		new Timer(delay, timerAction).start();

		ActionListener posttimerAction = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				sendPost();
			}
		};
		new Timer(50000 , posttimerAction).start();
		getContentPane().add(cp);
	}
}