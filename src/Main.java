import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main( String[] args ) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        } );
    }

    private static void createAndShowGUI() {
        int WIDTH =   (int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth();   // full screen
        int HEIGHT =  (int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().getHeight();

        JFrame frame = new JFrame();
        frame.setSize(WIDTH, HEIGHT);
        frame.add(new ImagePanel(WIDTH, HEIGHT), BorderLayout.CENTER);
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setVisible( true );
    }

}