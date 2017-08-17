import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.image.*;


public class ColorPopUp extends JPanel {

    public int x, y, curr_x, curr_y, width, height;
    private ImagePanel imagePanel;
    private ColorChooser colorChooser;

    private BufferedImage image = null; 
    private Graphics2D g2d = null;
    private int borderWidth = 7;

    private JSlider r,g,b;
    private int rValue,gValue,bValue;

    private JSlider r2,g2,b2;
    private int rValue2,gValue2,bValue2;

    public ColorPopUp(ImagePanel imagePanel, ColorChooser colorChooser) {
        
        width = (int)(imagePanel.getWidth() * .6);
        height = (int)(imagePanel.getHeight() * .45);
        x = curr_x = (int)(imagePanel.getWidth() * (1 - .80));
        y = curr_y = (int)(imagePanel.getHeight() * .20);

        image = new BufferedImage(width - borderWidth, (int)(height) - borderWidth, BufferedImage.TYPE_INT_ARGB);
        g2d = (Graphics2D)image.getGraphics();

        setBounds(curr_x, curr_y, width, height);
        setLayout(new GridLayout(0, 2));
        setBackground(new Color(50, 50, 50, 140));
        setBorder(BorderFactory.createLineBorder(new Color(50, 50, 50, 70), 7));
       
        setVisible(true);
        setOpaque(true);

        this.imagePanel = imagePanel;
        this.colorChooser = colorChooser;       
    
        r = new JSlider(JSlider.HORIZONTAL,0,255, 0xcc);
        r.setMajorTickSpacing(85);
        r.setPaintLabels(true);   

        g = new JSlider(JSlider.HORIZONTAL,0,255,0xcc);
        g.setMajorTickSpacing(85);
        g.setPaintLabels(true);
        
        b = new JSlider(JSlider.HORIZONTAL,0,255,0xcc);
        b.setMajorTickSpacing(85);
        b.setPaintLabels(true);
        
        eventOne e = new eventOne();
        r.addChangeListener(e);
        g.addChangeListener(e);
        b.addChangeListener(e);

        r2 = new JSlider(JSlider.HORIZONTAL,0,255,0xFF);
        r2.setMajorTickSpacing(85);
        r2.setPaintLabels(true);
        
        g2 = new JSlider(JSlider.HORIZONTAL,0,255,0xD7);
        g2.setMajorTickSpacing(85);
        g2.setPaintLabels(true);
        
        b2 = new JSlider(JSlider.HORIZONTAL,0,255,0x00);
        b2.setMajorTickSpacing(85);
        b2.setPaintLabels(true);

        r.setFont(new Font("plain", Font.BOLD, 13));
        r2.setFont(new Font("plain", Font.BOLD, 13));
        g.setFont(new Font("plain", Font.BOLD, 13));
        g2.setFont(new Font("plain", Font.BOLD, 13));
        b.setFont(new Font("plain", Font.BOLD, 13));
        b2.setFont(new Font("plain", Font.BOLD, 13));

        r.setForeground(Color.RED);
        r2.setForeground(Color.RED);
        g.setForeground(Color.GREEN);
        g2.setForeground(Color.GREEN);
        b.setForeground(Color.BLUE);
        b2.setForeground(Color.BLUE);
        
        eventTwo e2 = new eventTwo();
        r2.addChangeListener(e2);
        g2.addChangeListener(e2);
        b2.addChangeListener(e2);
        
        this.add(r);
        this.add(r2);
        this.add(g);
        this.add(g2);
        this.add(b);
        this.add(b2);

        drawImage();

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
                setBounds(curr_x, curr_y, width, height);
                imagePanel.repaint();
            }
        } );
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

    public void drawImage() {
        rValue = r.getValue();
        gValue = g.getValue();
        bValue = b.getValue();

        rValue2 = r2.getValue();
        gValue2 = g2.getValue();
        bValue2 = b2.getValue();

        int start = (0xFF000000) | ((int)rValue << 16) | ((int)gValue << 8) | (int)bValue;
        int end = (0xFF000000) | ((int)rValue2 << 16) | ((int)gValue2 << 8) | (int)bValue2;

        Color[] colors = getColors( start, end, image.getWidth() );

        for (int i = 0; i < image.getWidth(); i++) {
            g2d.setColor(colors[i]);
            g2d.drawLine(i + borderWidth, borderWidth, i + borderWidth, image.getHeight() + borderWidth);
        }
        colorChooser.setColors(start, end);
        colorChooser.drawImage();
        imagePanel.setColors(start, end);
        repaint();
    }

    class eventOne implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            drawImage();
        }
    }

    class eventTwo implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            drawImage();
        }
    }

    protected void paintComponent(Graphics g) {
            g.drawImage(image, 0, 0, null);
        }
}