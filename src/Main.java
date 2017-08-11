import javax.swing.*;
import java.awt.*;

public class Main {

    private static final int WIDTH = (int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    private static final int HEIGHT =  (int)(java.awt.Toolkit.getDefaultToolkit().getScreenSize().getHeight());

    public static void main( String[] args ) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        } );
    }

    private static void createAndShowGUI()
    {
        JFrame frame = new JFrame();
        frame.setSize(WIDTH, HEIGHT);
        frame.add(new ImagePanel(WIDTH, HEIGHT), BorderLayout.CENTER);
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setVisible( true );
    }

}