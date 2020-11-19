package com.FLNStudios.TestSnipShip;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.*;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;

import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.global.opencv_photo;
import org.bytedeco.opencv.opencv_core.*;
import static org.bytedeco.opencv.global.opencv_imgproc.*;
import static org.bytedeco.opencv.global.opencv_imgcodecs.*;

import com.inet.jortho.SpellChecker;
import com.inet.jortho.FileUserDictionary;

public class SnipTool extends Thread {

    private int waitSeconds = 3;

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void run() {
        try {
            SpellChecker.setUserDictionaryProvider(new FileUserDictionary());
            URL u = new URL("file", null, "TestSnipShip/src/resources/dictionaries.cnf");
            SpellChecker.registerDictionaries(u, "en");
            Thread.sleep(1000 * waitSeconds);
            BufferedImage bf;
            Robot r = new Robot();
            Rectangle dimensRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            bf = r.createScreenCapture(dimensRect);
            JFrame jf = new JFrame("Newest Capture");
            jf.setSize(dimensRect.width, dimensRect.height);
            InteractablePanel jp = new InteractablePanel(bf);
            jf.add(jp);
            jf.setVisible(true);
            while (!jp.isComplete()) {
                jp.repaint();
            }
            jf.setVisible(false);
            jf.dispose();
            Rectangle snipArea = jp.getSubRectangle();
            bf = bf.getSubimage(snipArea.x, snipArea.y, snipArea.width, snipArea.height);
            File tempFile = new File("temp.png");
            ImageIO.write(bf, "png", tempFile);
            Mat img = imread("temp.png", IMREAD_GRAYSCALE);
            opencv_imgproc.threshold(img, img, 0, 255, THRESH_OTSU);
            opencv_photo.fastNlMeansDenoising(img, img);
            if (imwrite("temp.png", img)) {
                bf = ImageIO.read(tempFile);
            }
            tempFile.delete();
            Tesseract tesseract = new Tesseract();
            tesseract.setDatapath(new URL("file", null, "TestSnipShip/src/tessdata").getFile());
            String result = tesseract.doOCR(bf);
            final JFrame resultFrame = new JFrame("Result");
            JPanel contentPane = new JPanel();
            final JTextArea textArea = new JTextArea(15, 25);
            SpellChecker.register(textArea);
            textArea.setText(result);
            textArea.setEditable(true);
            SpellChecker.register(textArea);
            contentPane.add(textArea);
            resultFrame.add(contentPane);
            resultFrame.setBounds(0, 0, 400, 325);
            JButton copyButton = new JButton("Copy to Clipboard");
            copyButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    StringSelection ss = new StringSelection(textArea.getText());
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    clipboard.setContents(ss, null);
                    resultFrame.dispose();
                }
            });
            contentPane.add(copyButton);
            resultFrame.setVisible(true);
        } catch (AWTException | TesseractException | InterruptedException | IOException ae) {
        }
    }

    public void setSeconds(int s) {
        waitSeconds = s;
    }
}
