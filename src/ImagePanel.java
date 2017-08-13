import java.awt.*;
import javax.swing.*;
import java.awt.image.*;

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

        g2d.setColor( new Color(0xffabc123) );                  // draw background
        g2d.fillRect((int)(image.getWidth() * 0.1), (int)(image.getHeight() * 0.1), (int)(image.getWidth() * 0.8), (int)(image.getHeight() * 0.8));
        g2d.setColor( new Color(0xff000000) );                  // draw background
        g2d.drawRect((int)(image.getWidth() * 0.1), (int)(image.getHeight() * 0.1), (int)(image.getWidth() * 0.8), (int)(image.getHeight() * 0.8));

        addComponents();
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
        return isPaused == false;
    }

    public int getImageWidth() {
        return image.getWidth();
    }

    protected void paintComponent(Graphics g) {
        g.drawImage(image, 0, 0, null);
    }
}

