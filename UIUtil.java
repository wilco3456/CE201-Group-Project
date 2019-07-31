import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;

public class UIUtil {
    // varargs, which allows to have zero or multiple JComponent (arguments)
    public static JComponent addComponents(JComponent parent, JComponent... children) {
        for (Component child : children) {
            parent.add(child);
        }

        return parent;
    }

    public static File selectFile(String extension) {
        JFileChooser fc = new JFileChooser(); // select a open function
        fc.setCurrentDirectory(FileSystemView.getFileSystemView().getHomeDirectory());// to set current directory to desktop when window prompt
        fc.setMultiSelectionEnabled(false);// to disable the multiple selection, only one file is allows
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);// select files only

        // set file filter to narrow down correctly file type
        fc.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith("." + extension);
            }

            @Override
            public String getDescription() {
                return "*." + extension;
            }
        });

        int option = fc.showOpenDialog(null); // prompt a window
        // to get the file which has been selected and input
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();

            return file;
        }

        return null;
    }

    public static void showMessage(String msg) {
        JOptionPane.showMessageDialog(null, msg, "Notice", JOptionPane.INFORMATION_MESSAGE);
    }
}
