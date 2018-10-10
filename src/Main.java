import javax.swing.*;
import java.awt.*;

public class Main {

    private static final int WIDTH  = (int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() * 0.70);
    private static final int HEIGHT = (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight() * 0.75);

    public static void main(String[] args) {
        SwingUtilities.invokeLater( () -> createAndShowGUI() );
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame();
        frame.setSize(WIDTH, HEIGHT);
        frame.add(new ImagePanel(WIDTH, HEIGHT), BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    
}