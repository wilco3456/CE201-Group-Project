import javax.swing.*;

public class Launcher {

    public static void main(String[] args) throws Exception {                       //Runs software and sets it to visible
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());        //Setup UI

        MainFrame mainFrame = new MainFrame("Energy Chart");                        //New MainFrame made
        mainFrame.setVisible(true);                                                 //Software set to visible
    }

}
