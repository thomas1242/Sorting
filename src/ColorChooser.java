import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.image.*;

public class ColorChooser extends JPanel {

    public int x, y, curr_x, curr_y, width, height;
    private BufferedImage image = null; 
    private Graphics2D g2d = null;
    private ImagePanel imagePanel;
    private int borderWidth = 7;
    private ControlPanel controlPanel;

    public ColorChooser(ImagePanel imagePanel) {

        setBackground(new Color(50, 50, 50, 140));
        int width = (int)(imagePanel.getWidth() * .175);
        int height = (int)(imagePanel.getHeight() * .15);
        x = curr_x = (int)(imagePanel.getWidth() * (1 - .2));
        y = curr_y = (int)(imagePanel.getHeight() * .20) + height * 3 + 7 * 2;

        this.imagePanel = imagePanel;
        image = new BufferedImage(width - borderWidth, (int)(height / 2.5) - borderWidth, BufferedImage.TYPE_INT_ARGB);
        g2d = (Graphics2D)image.getGraphics();
        setBounds(curr_x, curr_y, width, (int)(height / 2.5));
        setBorder(BorderFactory.createLineBorder(new Color(70, 70, 70, 140), borderWidth));

        drawImage();
        setVisible(true);
        setOpaque(true);

        this.width = image.getWidth() + borderWidth;
        this.height = (int)(height / 2.5);

        repaint();
      
        // JColorChooser tcc = new JColorChooser(banner.getForeground());
        addMouseListener( new MouseAdapter() {
            public void mousePressed( MouseEvent event ) {
                if(event.getButton() == MouseEvent.BUTTON1) {
                    x = (int)event.getPoint().getX();
                    y = (int)event.getPoint().getY();
                    imagePanel.repaint();
                }
            }
        } );

        addMouseMotionListener( new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent event) {
                int x_offset = (int)event.getPoint().getX() - x;
                int y_offset = (int)event.getPoint().getY() - y;
                curr_x += (x_offset);
                curr_y += (y_offset);
                setBounds(curr_x, curr_y, width, (int)(height / 2.5));

                controlPanel.curr_x += (x_offset);
                controlPanel.curr_y += (y_offset);
                controlPanel.setBounds(controlPanel.curr_x, controlPanel.curr_y, controlPanel.width, controlPanel.height);

                imagePanel.repaint();
            }
        } );
    }


    public void drawImage() {
        Color[] colors = getColors( 0xffcccccc,  0x0fFFD700, image.getWidth() );

        for (int i = 0; i < image.getWidth(); i++) {
            g2d.setColor(colors[i]);
            g2d.drawLine(i + borderWidth, borderWidth, i + borderWidth, image.getHeight() + borderWidth);
        }

        // g2d.setColor( new Color(70, 70, 70, 140) );
        // g2d.drawLine(image.getWidth() / 2 + borderWidth, borderWidth, image.getWidth() / 2 + borderWidth, image.getHeight() + borderWidth );

        repaint();
    }

    private double[] getDeltas(int start, int end, int n) {
        double start_R, start_G, start_B,       
                 end_R,   end_G,   end_B,
               delta_R, delta_G, delta_B;
        
        end_R = (end >> 16) & 0xFF;             
        end_G = (end >> 8 ) & 0xFF;
        end_B = (end      ) & 0xFF;
        
        start_R = (start >> 16) & 0xFF;         
        start_G = (start >> 8 ) & 0xFF;
        start_B = (start      ) & 0xFF;
        
        delta_R = (end_R - start_R) / n;        // change per color channel
        delta_G = (end_G - start_G) / n;
        delta_B = (end_B - start_B) / n;
        
        double[] deltas = { delta_R, delta_G, delta_B };    
        return deltas;                                      
    }

    private Color[] getColors(int start, int end, int length) {
        Color[] colors = new Color[length];
        
        int intARGB;                            // integer to hold synthesized color values
        int value = start;                                            
        double value_R = (value >> 16) & 0xFF;
        double value_G = (value >> 8 ) & 0xFF;
        double value_B = (value      ) & 0xFF;
        
        double[] deltas = getDeltas( start, end, colors.length - 1 );  
        colors[0] = new Color(start);
        colors[colors.length - 1] = new Color(end);
        
        // fill with interpolated Colors
        for (int i = 1; i < colors.length - 1; i++) {         
            value_R += deltas[0];
            value_G += deltas[1];
            value_B += deltas[2];
             
            intARGB = (0xFF000000) | ((int)value_R << 16) | ((int)value_G << 8) | (int)value_B;
            colors[i] = new Color(intARGB);
        }

        return colors;
    }

    public void setControlPanel(ControlPanel controlPanel) {
        this.controlPanel = controlPanel;
    }








        protected void paintComponent(Graphics g) {
            g.drawImage(image, 0, 0, null);
        }

}