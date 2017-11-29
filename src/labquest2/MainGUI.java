package labquest2;

import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;

import javafx.scene.chart.XYChart;

public class MainGUI implements Runnable {

	// XYChart.Series series;//
	XYDataset ds;
	double[][] data;
	JFreeChart chart;

	public MainGUI(JFreeChart c,XYDataset s) {
		ds = s;
		chart=c;
	}

	int i = 0;

	@Override
	public void run() {

		while (true) {
			DefaultXYDataset ds = new DefaultXYDataset();

			double[][] data = { { Math.random(), Math.random(), Math.random() }, { 1, 2, 3 } };
			ds.addSeries("series1", data);

			// series.getData().add(new XYChart.Data(i, Math.random()*100));
			i++;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// Thread.sleep(100);

	}

}
