import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.*;

public class ImagePanel extends JLayeredPane {

    // Image
    private BufferedImage image = null;
    private Graphics2D g2d = null;

    private ControlPanel controlPanel;
    boolean isRunning;

    public ImagePanel(int width, int height) {
        setBounds(0, 0, width, height);
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2d = (Graphics2D)image.getGraphics();
       
        addComponents();
        addListeners();
        drawAll();
    }

    public void addComponents() {
        controlPanel = new ControlPanel(this);
        add(controlPanel, new Integer(3));
    }

    public void drawAll() {
    
        repaint();
    }

    public boolean paused() {
        if( isRunning )
            return false;
        try {
             Thread.sleep(50);
        }
        catch(InterruptedException e) {}
        return isRunning == false;
    }

    public void addListeners() {

        addMouseListener( new MouseListener()
        {
            @Override
            public void mousePressed(MouseEvent event) { }

            @Override
            public void mouseReleased(MouseEvent e) { }

            @Override
            public void mouseClicked(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}
        } );

        addMouseMotionListener( new MouseMotionListener()
        {
            public void mouseDragged(MouseEvent event) {}

            public void mouseMoved(MouseEvent event) {}
        });
    }

    protected void paintComponent(Graphics g) {
        g.drawImage(image, 0, 0, null);
    }
}

