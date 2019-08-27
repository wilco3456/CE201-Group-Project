# CE201 Group Seven Assignment ReadMe Guide

## Platform Recommendation:
* This program has been run on Windows 10.0.15063 build 15063 and Mac OS X, developed using Java version 8, Intellij version 2016.2.2
Other systems have not been tested, and it is advised to have caution with untested OS.

## Note:
* This work was compiled in Intellij. To compile it for use on the same service, copy and paste all java files into an Intelij src folder, then add to the Java Library all .jar files except ce201-07.jar, that is the executable file.

## To Start:
* Execute ce201-07.jar to begin the program. It will spend some time creating the GUI, before bringing up the Launcher Menu.
** NOTE: To access from the command line, type: java -jar ce201-07.jar, which should run the program. **

## How to use the Program:
* To start of, you will need to download the CSV data from the U.K Grid Watch Website [Grid_Watch](https://www.gridwatch.templar.co.uk/download.php). 

* Then click the "Browse" button, after which a window will pop-up. Browse it until you have found the file you just downloaded, then select that file. Afterwards, that file's classpath will appear inside the textbox left of the browse button.

* At this point, you can choose to use the drop-down box to select how many months will be in a group i.e. 2 for two months per point on 
the X axis, 3 for three months per point on the X axis, ..., N for N months per point on the X axis.

* You can leave this menu alone and display each month individually, but to make any changes, you must select a number before you press
the "Load Data" button.

* Pressing any other button/s at this point will bring up a reminder to "Load" the csv file before the button/s can be used.

* With your month size chosen, you should now press the "Load Data" button, causing the system to access the (csv) file you selected to convert it's data into a java list, then create two specialised lists to store the details to create Line and Pie charts.

* If you didn't select a file, then pushing "Load Data" will bring up a message reminding you to select a file.
** NOTE: If a csv file that contains a different format is used, the the program will malfunction and will need restarting to operate again. **

* When you have finished, a pop-up will appear telling you that the loading of data has been completed.

* To display a line chart, simply press the "Show Line Charts" button to bring up a line graph of all the energy type outputs and demands, with demand on the left and the month/s at the bottom. Moreover, beneath the line chart is a key of the colours for each energy type.

* To close the chart, click on the X in the top right corner of the chart pop-up

* To display a pie chart, simply press the "Show Pie Charts" button to bring up two pie graphs, one for renewable energy and one for non-renewable energy. Beneath each pie chart is a key of the colours for each energy type.

* To see specific amounts and percentages of each pie section, hover the mouse over the desired piece, after a time a notification will
appear displaying the size and the percentage that energy type takes up.

* To close the chart, click on the X in the top right corner of the chart pop-up

* To create a PDF image of any of the charts, you must first select one of the charts to display their contents. If you don't, the "Export PDF" button will be usable until you do. 

* To create a PDF of one of the charts, press either the "Show Line Charts" or "Show Pie Charts" buttons (Then close the chart if it impedes your view of the program), then press the "Export PDF" button. It will produce a message stating the requested PDF has been exported, and to please check your folder for the new PDF files.

* Pressing the button after you have exported a file will bring a reminder asking you to select a chart to make a PDF image of.

* To close the program, press the X in the top right corner of the launcher window, after which it will shutdown.
