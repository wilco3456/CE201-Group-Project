import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public class MainFrame extends JFrame {

    private static final long serialVersionUID = 20180226L;                                 //Provides ID

    private static final String TITLE_PIE_FRAME = "Pie Charts";                             //Title of PieChart
    private static final String TITLE_PIE_RENEWABLE = "Renewable Energy Percents";          //Title for renewable Pie Chart in graph
    private static final String TITLE_PIE_NON_RENEWABLE = "Non-renewable Energy Percents";  //Title for non-renewable Pie Chart in graph

    private static final String TITLE_LINE_CHART_FRAME = "Line Charts";                     //Title of LineChart
    private static final String TITLE_LINE_CHART = "Monthly Energy Demand";                 //Title for Line Chart in graph

    private Integer mgComboLimit = 1;                                                       //Stores the month compiler size

    private JLabel lblFile;                                                                 //JLabel to describe the searchbar
    private JTextField tfFile;                                                              //Stores the pathway to the csv file
    private JButton btnBrowse;                                                              //Button to trigger the search window

    private JButton btnLoadData;                                                            //Button to process the csv pathway
    private JButton btnDrawLineChart;                                                       //Button to display the LineChart
    private JButton btnDrawPieChart;                                                        //Button to display the PieChart
    private JButton btnPDF;                                                                 //Button to print a PDF of selected graphs
    private Integer[] mgList = {1, 2, 3, 4, 6, 12};                                         //List to provide month compiler size
    private JComboBox mgCombo = new JComboBox(mgList);                                      //Combo list to present
    private JLabel mgLabel = new JLabel("<html>Please select month compiler size:<br> 2 = Jan, Feb; 3 = Jan, Feb, Mar; ETC.</html>");   //Button to process the csv pathway

    private List<EnergyRow> energyRows;                                                     //Stores all records to use in the graphs

    private List<EnergyRow> monthlyEnergyRows;                                              //Combines all records into a monthly(or larger) list
    private EnumMap<EnergyType, Double> renewableMap;                                       //Stores values for each renewable energy type in the Pie Chart
    private EnumMap<EnergyType, Double> nonRenewableMap;                                    //Stores values for each non-renewable energy type in the Pie Chart

    public MainFrame(String title) {                                                        //Runs the software and turns on the UI and Listeners
        super(title);                                                                       //Sets title to variable "title"

        initUI();                                                                           //Activates the UI
        initListeners();                                                                    //Activates Listeners
    }

    private void initUI() {                                                                 //Runs the UI
        lblFile = new JLabel("Data Path:");                                                 //Set descriptor text
        tfFile = new JTextField(40);                                                        //Set searchbar size
        tfFile.setEditable(false);                                                          //Remove searchbar edibility
        btnBrowse = new JButton("Browse");                                                  //Set text for btnBrowse

        //Next four lines set text for the remaining four buttons
        btnLoadData = new JButton("Load Data");
        btnDrawLineChart = new JButton("Show Line Charts");
        btnDrawPieChart = new JButton("Show Pie Charts");
        btnPDF = new JButton("Export PDF");

        JPanel pnlFile = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 10));                //Creates JPanel on the LEFT
        UIUtil.addComponents(pnlFile, lblFile, tfFile, btnBrowse);                          //Adds search bar functions to pnlFile

        JPanel pnlControl = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));          //Creates JPanel on the CENTRE
        UIUtil.addComponents(pnlControl, mgLabel, mgCombo, btnLoadData, btnDrawLineChart, btnDrawPieChart, btnPDF); //Adds remaining functions to pnlControl

        //Remaining lines set the layout, add the panels, set its editability and its default close operation.
        this.setLayout(new GridLayout(2, 1));
        this.add(pnlFile);
        this.add(pnlControl);
        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void initListeners() {                                                          //Sets the listeners attached to each button
        btnBrowse.addActionListener(e -> {                                                  //Search for csv file pathways with a pop-up window
            File cvsFile = UIUtil.selectFile("csv");
            if (cvsFile != null) {                                                          //If prevents non-csv files being displayed
                tfFile.setText(cvsFile.getAbsolutePath());
            }
        });

        mgCombo.addActionListener(e ->                                                      //Sets mgComboLimit to the seleced item from mgCombo
        {
            mgComboLimit = (Integer) mgCombo.getSelectedItem();
        });

        btnLoadData.addActionListener(e -> {                                                //Loads the csv file if one is selected
            String csvPath = tfFile.getText();                                              //The pathway to the file is retrieved
            if (csvPath.isEmpty()) {                                                        //If statement displays error message if no pathway is selected
                UIUtil.showMessage("Please select a CSV file first.");
                return;
            }

            loadAndPrepareData(csvPath);                                                    //Processes the selected file
        });

        btnDrawLineChart.addActionListener(e -> {                                           //Draws and shows the Line Chart
            if (energyRows == null) {                                                       //If statement displays error message if energyRows is empty
                UIUtil.showMessage("Please load data first.");
                return;
            }

            drawLineChart();                                                                //Draws the Line Chart
        });

        btnDrawPieChart.addActionListener(e -> {                                            //Draws and shows the Pie Chart
            if (energyRows == null) {                                                       //If statement displays error message if energyRows is empty
                UIUtil.showMessage("Please load data first.");
                return;
            }

            drawPieChart();                                                                 //Draws the Pie Chart
        });
        btnPDF.addActionListener(e -> {                                                     //Exports a PDF
            if (energyRows == null) {                                                       //If statement displays error message if energyRows is empty
                UIUtil.showMessage("Please load data first.");
                return;
            }

            CreatingPDF.PDF();                                                              //Creates relevant PDF's
        });
    }

    private void loadAndPrepareData(String csvPath) {                                       //Creates and prepares the graphs for display
        //All buttons are set to false temporarily
        btnLoadData.setEnabled(false);
        btnDrawLineChart.setEnabled(false);
        btnDrawPieChart.setEnabled(false);
        btnPDF.setEnabled(false);

        Runnable task = () -> {                                                             //Creates a thread to run processing
            try {                                                                           //Try extracts the records from the CSV and uses it to prepare the Line and Pie Charts
                energyRows = EnergyReader.readEnergyRows(csvPath);
                preparePieChartData();
                prepareLineChartData();

                SwingUtilities.invokeLater(() -> {                                          //Re-enables all but the PDf button for use once a period of time has passed.
                    btnLoadData.setEnabled(true);
                    btnDrawLineChart.setEnabled(true);
                    btnDrawPieChart.setEnabled(true);
                    btnPDF.setEnabled(false);
                });

                UIUtil.showMessage("Loading data completed.");                              //Display pop-up informing user of successful processing
            } catch (IOException ex) {                                                      // Catch displays pop-up informing user of failure to process.
                UIUtil.showMessage("Error when loading data: " + ex.getMessage());
            }
        };

        new Thread(task).start();                                                           //The new thread is then run.
    }

    /**
     * Sums different energy monthly.
     */
    private void prepareLineChartData() {                                                   //Prepares records for the line graph
        monthlyEnergyRows = new ArrayList<>(energyRows.size() / 30);                        //Sets size of the month ordered list
        int size = energyRows.size();                                                       //Records amount of records
        Integer mgCounter = 1;                                                              //Stores counter for Month Compiler
        Integer meanCounter = 0;                                                            //Stores records input per month
        EnergyRow erStorage = energyRows.get(0);                                            //Sets backup to store monthSum when mgCounter > 1
        boolean mgCheck = false;                                                            //Boolean to add erStorage to monthlyEnergyRows

        for (int i = 0; i < size; ) {                                                       //For loop goes through entire list, selecting a month and making it the new monthlyEnergyRow
            EnergyRow current = energyRows.get(i);
            EnergyRow monthlyEnergyRow = current;

            while (true) {                                                                  //While loop breaks only when the current monthlyEnergyRow month matches the selected record's month
                EnergyRow next = energyRows.get(i);
                if (mgCheck) {                                                              //If statement adds erStorage to monthlyEnergyRow
                    monthlyEnergyRow = monthlyEnergyRow.add(erStorage);
                    mgCheck = false;
                }

                if (current.isSameMonth(next)) { // current's month is the same as next     //If statement adds record values to monthlyEnergyRow
                    monthlyEnergyRow = monthlyEnergyRow.add(next);
                    meanCounter++;
                } else if (mgCounter <= mgComboLimit ^ mgComboLimit.compareTo(1) == 0) {    //Else If statement sets erStorage to current value of monthlyEnergyRow, mgCounter increments and check to add erStorage-
                    mgCounter++;                                                            //- to the next monthlyEnergyRow is true. Then, break the while loop
                    erStorage = monthlyEnergyRow;
                    mgCheck = true;
                    break;
                } else { // not same month, current month ends                              //Else statement adds the current monthlyEnergyRow once its values are divided by mgCounter. -
                    //mgCounter and meanCounter are reset, then the while loop is broken
                    monthlyEnergyRows.add(monthlyEnergyRow.getMean(monthlyEnergyRow, meanCounter));
                    mgCounter = 1;
                    meanCounter = 0;

                    break;
                }

                if (++i == size) {                                                          //If statement causes break if the final record is reached
                    break;
                }
            }
        }


    }

    /**
     * Sums different energy by type.
     */
    private void preparePieChartData() {                                                    //Prepares records for pie graph
        //Custom maps to store the non/renewable values for each energy type
        renewableMap = new EnumMap<>(EnergyType.class);
        nonRenewableMap = new EnumMap<>(EnergyType.class);

        for (EnergyRow energyRow : energyRows) {                                            //For loop iterates for every record in energyRows
            for (EnergyType type : EnergyType.values()) {                                   //For loop iterates through each value in the selected record
                if (type.isRenewable()) {                                                   //If statement adds renewable energy values to renewableMap (Under the value's energy type)

                    Double current = renewableMap.get(type);

                    if (current == null) {                                                  //If the value is empty, set current to 0.0
                        current = 0.0;
                    }

                    renewableMap.put(type, current + energyRow.getEnergy(type));
                } else { // energy is non-renewable                                         //Else statement adds non-renewable energy values to nonrenewableMap (Under the value's energy type)
                    Double current = nonRenewableMap.get(type);

                    if (current == null) {                                                  //If the value is empty, set current to 0.0
                        current = 0.0;
                    }

                    nonRenewableMap.put(type, current + energyRow.getEnergy(type));
                }
            }
        }
    }

    private void drawLineChart() {                                                          //Creates pop-up displaying the Line Chart
        CategoryDataset lineDataset = createLineDataset();                                  //lineDataSet contains records sorted by createLineDataset
        JFreeChart lineChart = ChartFactory.createLineChart(TITLE_LINE_CHART, "Time", "Demand", //A chart is created using contents of lineDataSet
                lineDataset, PlotOrientation.VERTICAL, true, true, false);
        //JFreeChart lineChart = ChartFactory.createLineChart(
        //        TITLE_LINE_CHART, "Time", "Demand", lineDataset);
        ChartPanel chartPanel = new ChartPanel(lineChart);                                  //A new chartPanel is made using lineChart

        ChartFrame chartFrame = new ChartFrame(TITLE_LINE_CHART_FRAME, chartPanel);         //A new ChartFrame is made using chartPanel
        chartFrame.setVisible(true);                                                        //The ChartFrame is then set to be visible

        //Files are prepared to store a PDF of the current line graph
        File p = new File("image.png");
        File pff = new File("image1.png");

        if (p.exists() && pff.exists()) {                                                   //If statement checks if p and pff images already exist, deletes both if they do
            p.delete();
            pff.delete();
        } else if (p.exists()) {                                                            //Else If deletes p if it already exists
            p.delete();
        }

        try {                                                                               //Try saves the line chart into a PNG file
            ChartUtilities.saveChartAsPNG(new File("image.png"), lineChart, 400, 300);
        } catch (IOException e) {                                                           //Catch prints a StackTrace of where the program went wrong
            e.printStackTrace();
        }
        btnPDF.setEnabled(true);                                                            //PDF export function is re-enabled
    }

    private CategoryDataset createLineDataset() {                                           //Re-creates the record into a format processable by the drawLineChart function
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();                      //Creates a default dataset
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM");                 //Creates new date format

        for (EnergyRow row : monthlyEnergyRows) {                                           //For loop formats the date and adds the demand for every record in monthlyEnergyRows
            String month = formatter.format(row.getTimestamp());

            dataset.addValue(row.getDemand(), "demand", month);
            for (EnergyType type : EnergyType.values()) {                                   //For loop goes through each EnergyType in a record and adds the value of each type to the new record
                double monthlyEnergy = row.getEnergy(type);
                dataset.addValue(monthlyEnergy, type.getName(), month);
            }
        }

        return dataset;                                                                     //returns the new record
    }

    private void drawPieChart() {                                                           //Creates pop-up displaying the Line Chart
        PieDataset renewableDataset = createPieDataset(true);                               //Format the records into renewable records using createPieDataset
        JFreeChart renewablePieChart = createPieChart(renewableDataset, TITLE_PIE_RENEWABLE);   //Create the PieChart using it's renewable title and the renewableDataset
        ChartPanel renewableChartPanel = new ChartPanel(renewablePieChart);                 //Create a new panel to contain the renewable graph renewablePieChart

        PieDataset nonRenewableDataset = createPieDataset(false);                           //Format the records into non-renewable records using createPieDataset
        JFreeChart nonRenewablePieChart = createPieChart(nonRenewableDataset, TITLE_PIE_NON_RENEWABLE); //Create the PieChart using it's non-renewable title and the nonRenewableDataset
        ChartPanel nonRenewableChartPanel = new ChartPanel(nonRenewablePieChart);           //Create a new panel to contain the non-renewable graph nonRenewablePieChart

        ChartFrame pieChartFrame = new ChartFrame(                                          //Create a frame using both of the pie chart panels and the pie chart frame title
                TITLE_PIE_FRAME, renewableChartPanel, nonRenewableChartPanel);
        pieChartFrame.setVisible(true);                                                     //Sets the frame to be visible

        //Files are prepared to store a PDF of the current pie graph
        File i = new File("image.png");
        File iff = new File("image1.png");

        if (i.exists() && iff.exists()) {                                                   //If statement checks if i and iff images already exist, deletes both if they do
            i.delete();
            iff.delete();
        } else if (i.exists()) {                                                            //Else If deletes i if it already exists
            i.delete();
        }

        try {                                                                               //Try saves copies of each pie graph as a PNG file
            ChartUtilities.saveChartAsPNG(new File("image.png"), renewablePieChart, 400, 300);
            ChartUtilities.saveChartAsPNG(new File("image1.png"), nonRenewablePieChart, 400, 300);
        } catch (IOException e) {                                                           //Catch prints a StackTrace of where the program went wrong
            e.printStackTrace();
        }
    }

    private JFreeChart createPieChart(PieDataset dataset, String chartTitle) {              //Creates pie charts using a dataset and the chart's title
        JFreeChart pieChart = ChartFactory.createPieChart(                                  //pieChart contains a pieChart made using the chart's title and dataset
                chartTitle, dataset, true, true, false);
        PiePlot plot = (PiePlot) pieChart.getPlot();                                        //Points on the pie chart are plotted out
        plot.setLabelGenerator(null);                                                       //Label generator doesn't exist
        btnPDF.setEnabled(true);                                                            //PDF export function is re-enabled

        return pieChart;                                                                    //the pie chart is returned
    }

    private PieDataset createPieDataset(boolean renewable) {                                //Re-creates the record into a format processable by the drawPieChart function
        DefaultPieDataset dataset = new DefaultPieDataset();                                //A default dataset is created

        double total;                                                                       //Total stores the value of any energyType

        for (EnergyType type : EnergyType.values()) {                                       //For loop iterates through each energyType in a record
            if (renewable && type.isRenewable()) {                                          //If statement collects the data of renewable energy and adds it to the dataset under renewables
                total = renewableMap.get(type);
                dataset.setValue(type.getName(), total);

            } else if (!renewable && !type.isRenewable()) {                                 //If statement collects the data of non-renewable energy and adds it to the dataset under non-renewables
                total = nonRenewableMap.get(type);
                dataset.setValue(type.getName(), total);
            }
        }

        return dataset;                                                                     //Return dataset
    }

}