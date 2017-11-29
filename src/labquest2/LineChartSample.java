package labquest2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.Timer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;

import net.miginfocom.swing.MigLayout;

public class LineChartSample extends JFrame {

	ArrayList<Double> a = new ArrayList<Double>();
	ArrayList<Double> b = new ArrayList<Double>();
//	double[][] data = { { Math.random(), Math.random(), Math.random() }, { 1, 2, 3 } };
	int i = 0;

	private XYDataset createDataset() {
		a.add((double) i);
		b.add(Math.random());
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

	// XYChart.Series series;// = new XYChart.Series();
	// @Override public void start(Stage stage) {
	public LineChartSample() {
		setSize(320, 240);
		setLocation(0, 0);
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

//		getContentPane().setLayout(new MigLayout("", "[]", "[]"));
		JButton btnStartAnalysis = new JButton("Close");
		btnStartAnalysis.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				setVisible(false);
				dispose();
			}
		});
		getContentPane().add(btnStartAnalysis);//, "cell 0 0");
		setExtendedState(JFrame.MAXIMIZED_BOTH);
//		setUndecorated(true);
		XYDataset ds = createDataset();
		JFreeChart chart = ChartFactory.createXYLineChart("Analysis Chart", "time", "degree C", ds, PlotOrientation.VERTICAL, true,
				true, false);
		ChartPanel cp = new ChartPanel(chart);
		// now make your timer
		int delay = 1000; // milliseconds
		ActionListener timerAction = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				chart.getXYPlot().setDataset(createDataset());
			}
		};
		new Timer(delay, timerAction).start();
		getContentPane().add(cp);
	}
}