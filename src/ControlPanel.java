import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.event.*;
import java.util.List;
import java.util.LinkedList;

public class ControlPanel extends JPanel {

    private JButton startSearchButton;
    private List<JButton> algorithmSelectButtons;
    private String selectedAlgorithm = "Quicksort";
    public int x, y, curr_x, curr_y, width, height;
    private ColorChooser colorChooser;
    private ColorDisplay colorDisplay;
    private ImagePanel imagePanel;

    public ControlPanel(ImagePanel imagePanel) {
        this.imagePanel = imagePanel;
        setPositionAndSize();
        addComponents();
        addMouseListeners();
        drawBackground();
        setVisible(true);
        setOpaque(true);
    }

    private void setPositionAndSize() {
        width = (int)(imagePanel.getWidth() * .175);
        height = (int)(imagePanel.getHeight() * .45);
        x = curr_x = (int)(imagePanel.getWidth() * (1 - .2));
        y = curr_y = (int)(imagePanel.getHeight() * .20);
        setBounds(curr_x, curr_y, width, height);
    }

    private void drawBackground() {
        setBackground(new Color(70, 70, 70, 130));
        setBorder(BorderFactory.createLineBorder(new Color(50, 50, 50, 0), 7));
    }

    private void addMouseListeners() {
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

                colorChooser.curr_x += (x_offset);
                colorChooser.curr_y += (y_offset);
                colorChooser.setBounds(colorChooser.curr_x, colorChooser.curr_y, colorChooser.width, colorChooser.height);

                colorDisplay.curr_x += (x_offset);
                colorDisplay.curr_y += (y_offset);
                colorDisplay.setBounds(colorDisplay.curr_x, colorDisplay.curr_y, colorDisplay.width, colorDisplay.height);
                imagePanel.repaint();
            }
        } );
    }

    private void runSelectedAlgorithm() {
        startSearchButton.setText( "Pause");
        startSearchButton.setForeground( Color.RED );
        new Thread( () -> {
            if(selectedAlgorithm.equals("Quicksort"))
                imagePanel.quickSort();
            else if (selectedAlgorithm.equals("Merge sort"))
                imagePanel.mergeSort();
            else if (selectedAlgorithm.equals("Bubble sort"))
                imagePanel.bubbleSort();
            else if (selectedAlgorithm.equals("Insertion sort"))
                imagePanel.insertSort();
            else if (selectedAlgorithm.equals("Selection sort"))
                imagePanel.selectSort();
            setStartSearchButtonText("Start sort", new Color(0, 0, 0, 250));
        }).start();
    }

    public void pauseSearch() {
        startSearchButton.setText("Resume");
        startSearchButton.setForeground( new Color(0, 175, 0, 255) );
        // imagePanel.pause();
    }

    public void resumeSearch() {
        startSearchButton.setText("Pause");
        startSearchButton.setForeground( Color.RED );
        // imagePanel.resume();
    }

    private void clearAll() {
        Color textColor = new Color(0, 0, 0, 255);
        for(JButton button : algorithmSelectButtons) {
            button.setForeground(textColor);
            button.setFont(new Font("plain", Font.BOLD, 13));
        }
    }

    public void setStartSearchButtonText(String s, Color c) {
        startSearchButton.setText(s);
        startSearchButton.setForeground(c);
    }

    private void setButtonFont(JButton button, Color color, int size) {
        button.setForeground( color );
        button.setFont(new Font("plain", Font.BOLD, size));
    }

    private void addComponents() {
        startSearchButton = new JButton("Start sort");
        startSearchButton.addActionListener(e -> {
            String s = startSearchButton.getText();
            if(s.equals("Start sort"))
                runSelectedAlgorithm();
            else if (s.equals("Pause"))
                pauseSearch();
            else if (s.equals("Resume"))
                resumeSearch();
        });

        JButton randomizeDataButton = new JButton("Randomize Data");
        randomizeDataButton.addActionListener(e -> {
            imagePanel.randomizeData();
            imagePanel.animateRandomizeData();
        });

        algorithmSelectButtons = new LinkedList<>();
        algorithmSelectButtons.add(new JButton("Merge sort"));
        algorithmSelectButtons.add(new JButton("Quicksort"));
        algorithmSelectButtons.add(new JButton("Bubble sort"));
        algorithmSelectButtons.add(new JButton("Insertion sort"));
        algorithmSelectButtons.add(new JButton("Selection sort"));

        Color defaultTextColor = new Color(0, 0, 0, 255);
        for(JButton button : algorithmSelectButtons) {
            button.addActionListener(e -> selectAlgorithm(button));
            button.setForeground(defaultTextColor);
            button.setFont(new Font("plain", Font.BOLD, 13));
            button.setOpaque(false);
        }

        startSearchButton.setForeground(defaultTextColor);
        startSearchButton.setFont(new Font("plain", Font.BOLD, 13));
        startSearchButton.setOpaque(false);
        randomizeDataButton.setForeground(defaultTextColor);
        randomizeDataButton.setFont(new Font("plain", Font.BOLD, 15));
        randomizeDataButton.setOpaque(false);

        JLabel algorithmPanelLabel  = createAlgoPanelLabel();
        JPanel animationSpeedSlider = createAnimationSpeedSlider();
        JPanel dataSetSizeSlider    = createDataSetSizeSlider();

        setLayout(new GridLayout(0, 1));
        add(startSearchButton);
        add(randomizeDataButton);
        add(algorithmPanelLabel);
        for(JButton button : algorithmSelectButtons) add(button);
        add(animationSpeedSlider);
        add(dataSetSizeSlider);
    }

    private void selectAlgorithm(JButton button) {
        this.selectedAlgorithm = button.getText();
        clearAll();
        button.setForeground( new Color(0, 175, 0, 255) );
    }

    private JPanel createAnimationSpeedSlider() {
        JPanel speedSlider = new JPanel();
        
        JLabel speedLabel = new JLabel(" " + 1000/171 + " operations/sec");
        speedLabel.setFont(new Font("plain", Font.BOLD, 14));
        speedLabel.setForeground( new Color(0xffdddddd) );

        JSlider slider = new JSlider(1, 200, 30);
        slider.addChangeListener(e -> {
            speedLabel.setText(" " + (1000 / (1 + slider.getMaximum() - slider.getValue())) + " operations/sec");
            imagePanel.setSpeed( 1 + slider.getMaximum() - slider.getValue() );
        });
        slider.setMinorTickSpacing(1);
        slider.setSnapToTicks(true);

        speedSlider.setLayout(new GridLayout(0, 1));
        speedSlider.add(speedLabel, BorderLayout.CENTER);
        speedSlider.add(slider, BorderLayout.SOUTH);
        speedSlider.setOpaque(false);
        speedSlider.setVisible(true);
        return speedSlider;
    }

    private JLabel createAlgoPanelLabel() {
        JLabel algo_label = new JLabel(" Algorithms");
        algo_label.setFont(new Font("plain", Font.BOLD, 15));
        algo_label.setForeground( new Color(0xffdddddd) );
        return algo_label;
    }

    private JPanel createDataSetSizeSlider() {
        JPanel dataSetSizeSlider = new JPanel( new GridLayout(0, 1) );
        
        int minWidth = 256;
        while(((int)(imagePanel.getImageWidth() * 0.9) / minWidth) % 20 != 0) minWidth--;
        imagePanel.setDataWidth( 12 );
        imagePanel.drawAll();

        JLabel sizeLabel = new JLabel(" " + (int)(imagePanel.getImageWidth() * 0.9) / 75 + " data points");
        sizeLabel.setFont(new Font("plain", Font.BOLD, 14));
        sizeLabel.setForeground( new Color(0xffdddddd) );
        sizeLabel.setText(" " + imagePanel.getNumCells() + " data points");
        
        JSlider slider = new JSlider(1, minWidth, minWidth - 10);
        slider.addChangeListener(e -> {
            imagePanel.setDataWidth( 1 + slider.getMaximum() - slider.getValue() );
            sizeLabel.setText(" " + imagePanel.getNumCells() + " data points");
            imagePanel.drawAll();
        });
        slider.setMinorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setSnapToTicks(true);

        dataSetSizeSlider.add(sizeLabel, BorderLayout.CENTER);
        dataSetSizeSlider.add(slider, BorderLayout.SOUTH);
        dataSetSizeSlider.setOpaque(false);
        dataSetSizeSlider.setVisible(true);
        return dataSetSizeSlider;
    }

    public void setColorChooser(ColorChooser colorChooser, ColorDisplay popUp) {
        this.colorChooser = colorChooser;
        this.colorDisplay = popUp;
    }
}

class ColorChooser extends JPanel {

    private BufferedImage image;
    private Graphics2D g2d;
    private ControlPanel controlPanel;
    private ColorDisplay colorDisplay;
    private int startColor = 0xffcccccc, endColor = 0x0fFFD700;
    public int x, y, curr_x, curr_y, width, height;
    private int borderWidth = 7;
    boolean isVisible = false;

    public ColorChooser(ImagePanel imagePanel) {

        int width = (int)(imagePanel.getWidth() * .175);
        int height = (int)(imagePanel.getHeight() * .15);
        x = curr_x = (int)(imagePanel.getWidth() * (1 - .2));
        y = curr_y = (int)(imagePanel.getHeight() * .20) + height * 3 + borderWidth * 2;
        setBounds(curr_x, curr_y, width, (int)(height / 2.5));

        image = new BufferedImage(width - borderWidth, (int)(height / 2.5) - borderWidth, BufferedImage.TYPE_INT_ARGB);
        g2d = (Graphics2D)image.getGraphics();
        drawImage();

        addMouseListener( new MouseAdapter() {
            public void mousePressed( MouseEvent event ) {
                if(event.getButton() == MouseEvent.BUTTON1) {
                    x = (int)event.getPoint().getX();
                    y = (int)event.getPoint().getY();
                    if(x > borderWidth && y > borderWidth && x < image.getWidth() + borderWidth && y < image.getHeight() + borderWidth) {
                        isVisible ^= true; 
                        colorDisplay.setVisible(isVisible);
                    }
                    imagePanel.repaint();
                }
            }
        });

        this.width = image.getWidth() + borderWidth;
        this.height = (int)(height / 2.5);
        setBorder(BorderFactory.createLineBorder(new Color(70, 70, 70, 130), borderWidth));
        setBackground(new Color(70, 70, 70, 130));
        setVisible(true);
        setOpaque(true);
    }

    public void drawImage() {
        Color[] colors = Interpolation.getColors(startColor, endColor, image.getWidth() );
        for (int i = 0; i < image.getWidth(); i++) {
            g2d.setColor(colors[i]);
            g2d.drawLine(i + borderWidth, borderWidth, i + borderWidth, image.getHeight() + borderWidth);
        }
        repaint();
    }

    public void setColors(int start, int end) {
        this.startColor = start;
        this.endColor = end;
    }

    public void setControlPanel(ControlPanel controlPanel, ColorDisplay popUp) {
        this.controlPanel = controlPanel;
        this.colorDisplay = popUp;
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.drawImage(image, 0, 0, null);
    }
}

class ColorDisplay extends JPanel {

    private ImagePanel imagePanel;
    private ColorChooser colorChooser;
    private BufferedImage image;
    private Graphics2D g2d;
    private JSlider[] sliders;
    public int x, y, curr_x, curr_y, width, height;
    private int borderWidth = 7;

    public ColorDisplay(ImagePanel imagePanel, ColorChooser colorChooser) {
        this.imagePanel = imagePanel;
        this.colorChooser = colorChooser;
        setPosition();

        image = new BufferedImage(width - borderWidth, (int)(height) - borderWidth, BufferedImage.TYPE_INT_ARGB);
        g2d = (Graphics2D)image.getGraphics();

        addComponents();
        addMouseListeners();
        drawBackground();
        drawImage();
        setOpaque(true);
        setVisible(false);
    }

    private void setPosition() {
        width = (int)(imagePanel.getWidth() * .4);
        height = (int)(imagePanel.getHeight() * .45);
        x = curr_x = (int)(imagePanel.getWidth() * (1 - .60) - borderWidth * 2);
        y = curr_y = (int)(imagePanel.getHeight() * .20);
        setBounds(curr_x, curr_y, width, height);
    }

    private void drawBackground() {
        setBackground( new Color(70, 70, 70, 130) );
        setBorder(BorderFactory.createLineBorder(new Color(70, 70, 70, 130), borderWidth));
    }

    private void addComponents() {
        setLayout(new GridLayout(0, 2));

        sliders = new JSlider[6];
        int[] vals = new int[]{0xCC, 0xFF, 0xCC, 0xD7, 0xCC, 0x00};
        Color[] textColors = new Color[]{Color.RED, Color.RED, Color.GREEN, Color.GREEN, Color.BLUE, Color.BLUE};

        for (int i = 0; i < sliders.length; i++) {
            sliders[i] = new JSlider(JSlider.HORIZONTAL, 0, 255, vals[i]);
            sliders[i].setForeground(textColors[i]);
            sliders[i].addChangeListener(new Event());
            sliders[i].setMajorTickSpacing(85);
            sliders[i].setPaintLabels(true);
            sliders[i].setFont(new Font("plain", Font.BOLD, 13));
            add(sliders[i]);
        }
    }

    private void addMouseListeners() {
         addMouseListener( new MouseAdapter() {
            public void mousePressed( MouseEvent event ) {
                x = (int)event.getPoint().getX();
                y = (int)event.getPoint().getY();
                imagePanel.repaint();
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

    private void drawImage() {
        int start = 0xFF000000 | sliders[0].getValue() << 16 | sliders[2].getValue() << 8 | sliders[4].getValue();
        int end   = 0xFF000000 | sliders[1].getValue() << 16 | sliders[3].getValue() << 8 | sliders[5].getValue();

        Color[] colors = Interpolation.getColors( start, end, image.getWidth() );
        for (int i = 0; i < image.getWidth(); i++) {
            g2d.setColor(colors[i]);
            g2d.drawLine(i + borderWidth, borderWidth, i + borderWidth, image.getHeight() + borderWidth);
        }

        colorChooser.setColors(start, end);
        colorChooser.drawImage();
        imagePanel.setColors(start, end);
        imagePanel.drawAll();
        repaint();
    }

    class Event implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            drawImage();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.drawImage(image, 0, 0, null);
    }
}