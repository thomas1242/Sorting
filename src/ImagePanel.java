import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class ImagePanel extends JLayeredPane {

    private BufferedImage image = null; 
    private Graphics2D g2d = null;

    private ControlPanel controlPanel;
    private boolean isPaused;

    public ImagePanel(int width, int height) {
        setBounds(0, 0, width, height);
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2d = (Graphics2D)image.getGraphics();

        g2d.setColor( new Color(0xffbbbbbb) );                  // draw background
        g2d.fillRect(0, 0, image.getWidth(), image.getHeight());
       
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
        if(isPaused)
            return false;
        try {
             Thread.sleep(50);
        }
        catch(InterruptedException e) {}
        return isPaused == false;
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

    public int getImageWidth() {
        return image.getWidth();
    }

    protected void paintComponent(Graphics g) {
        g.drawImage(image, 0, 0, null);
    }
}

