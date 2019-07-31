import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.File;

public class CreatingPDF {

    public static void PDF() {
        PDDocument document = null;

        try {
            document = new PDDocument(); //creates document
            PDPage page = new PDPage(PDRectangle.A2); // sets the size to  A2
            page.setRotation(90); //rotates document so it becomes landscape.
            document.addPage(page); //adds the page to the document


            File f = new File("image.png");
            File ff = new File("image1.png");

            if (f.exists() && ff.exists()) { // if the images exist then:
                //create imagine from the file
                PDImageXObject pdImage = PDImageXObject.createFromFile("image.png", document);
                PDImageXObject pdImage1 = PDImageXObject.createFromFile("image1.png", document);
                //creates stream to add to document
                PDPageContentStream ct = new PDPageContentStream(document, page);
                //where to insert the images
                ct.drawImage(pdImage, 1, 1);
                ct.drawImage(pdImage1, 1, 500);
                //close stream
                ct.close();
                //saves the pdf under a name
                document.save("Pie_Chart.pdf");
                // creates dialog box
                UIUtil.showMessage("Your Pie Chart has been exported please check your folder.");
                document.close();
                //closes file and deletes the images
                f.delete();
                ff.delete();
            } else if (f.exists()) {
                //create imagine from the file
                PDImageXObject pdImage = PDImageXObject.createFromFile("image.png", document);
                //creates stream to add to document
                PDPageContentStream ct = new PDPageContentStream(document, page);
                //where to insert the images
                ct.drawImage(pdImage, 1, 1);
                //close stream
                ct.close();
                //saves the pdf under a name and creates a  dialog box
                document.save("Line_Graph.pdf");
                UIUtil.showMessage("Your Line Graph has been exported please check your folder.");
                document.close();
                //closes document and deletes the image
                f.delete();
            } else {
                UIUtil.showMessage("Please click on a type of chart which you would like to export and click on me again.");
                //creates a dialog box
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
