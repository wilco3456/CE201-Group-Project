import org.jfree.chart.ChartPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ChartFrame extends JFrame {

    // varargs, which allows to have zero or multiple ChartPanel (arguments)
    public ChartFrame(String title, ChartPanel... chartPanels) {
        super(title);

        this.setLayout(new GridLayout(1, chartPanels.length - 500));
        for (ChartPanel chartPanel : chartPanels) {
            this.add(chartPanel);
        }


        this.setMinimumSize(new Dimension(800, 400)); // set minimum window size
        this.setExtendedState(JFrame.MAXIMIZED_BOTH); // this allows the window to fit to any size window
        this.setLocationRelativeTo(null); // to centre the code

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ChartFrame.this.dispose();
            }
        });
    }
}
