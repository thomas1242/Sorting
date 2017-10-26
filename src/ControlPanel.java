import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.event.*;
import java.util.LinkedList;
import java.util.List;

public class ControlPanel extends JPanel {

    private JButton startSearchButton;
    private List<JButton> algorithmSelectButtons;
    private String selectedAlgorithm = "Quicksort";
    public int x, y, curr_x, curr_y, width, height;
    private ImagePanel imagePanel;
    private ColorChooser colorChooser;
    private ColorDisplay colorDisplay;

    public ControlPanel(ImagePanel imagePanel) {
        this.imagePanel = imagePanel;
        setInitialPosition();
        addComponents();
        addMouseListeners();
        drawBackground();
        setVisible(true);
        setOpaque(true);
    }

    private void setInitialPosition() {
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
        startSearchButton.setForeground(  Color.RED  );
        new Thread( 
            new Runnable() {
                public void run() { 
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
                }
            } ).start(); 
    }

    public void pauseSearch() {
        startSearchButton.setText( "Resume");
        startSearchButton.setForeground(  new Color(0, 175, 0, 255)  );
        // imagePanel.pause();
    }

    public void resumeSearch() {
        startSearchButton.setText( "Pause");
        startSearchButton.setForeground(  Color.RED  );
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
        button.setForeground(  color  );
        button.setFont(new Font("plain", Font.BOLD, size));
    }

    private void addComponents() {
        setLayout(new GridLayout(0, 1));

        startSearchButton = new JButton("Start sort");
        startSearchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(startSearchButton.getText().equals("Start sort"))
                    runSelectedAlgorithm();
                else if (startSearchButton.getText().equals( "Pause"))
                    pauseSearch();
                else if (startSearchButton.getText().equals( "Resume"))
                    resumeSearch();
            }
        });
        JButton randomizeDataButton = new JButton("Randomize Data");
        randomizeDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                imagePanel.randomizeData();
                imagePanel.animateRandomizeData();
                // imagePanel.drawAll();
            }
        });
        JButton quicksortBtn = new JButton("Quicksort");
        quicksortBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearAll();
                setButtonFont(quicksortBtn, new Color(0, 175, 0, 255), 15);
                selectedAlgorithm = "Quicksort";
            }
        });
        JButton mergesortBtn = new JButton("Merge sort");
        mergesortBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearAll();
                setButtonFont(mergesortBtn, new Color(0, 175, 0, 255), 15);
                selectedAlgorithm = "Merge sort";
            }
        });
        JButton bubblesortBtn = new JButton("Bubble sort");
        bubblesortBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearAll();
                setButtonFont(bubblesortBtn, new Color(0, 175, 0, 255), 15);
                selectedAlgorithm = "Bubble sort";
            }
        });
        JButton insertsortBtn = new JButton("Insertion sort");
        insertsortBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearAll();
                setButtonFont(insertsortBtn, new Color(0, 175, 0, 255), 15);
                selectedAlgorithm = "Insertion sort";
            }
        });
        JButton selectionsortBtn = new JButton("Selection sort");
        selectionsortBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearAll();
                setButtonFont(selectionsortBtn, new Color(0, 175, 0, 255), 15);
                selectedAlgorithm = "Selection sort";
            }
        });

        algorithmSelectButtons = new LinkedList<>();
        algorithmSelectButtons.add(mergesortBtn);
        algorithmSelectButtons.add(quicksortBtn);
        algorithmSelectButtons.add(bubblesortBtn);
        algorithmSelectButtons.add(insertsortBtn);
        algorithmSelectButtons.add(selectionsortBtn);

        Color defaultTextColor = new Color(0, 0, 0, 255);
        for(JButton button : algorithmSelectButtons) {
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

        add(startSearchButton);
        add(randomizeDataButton);
        add(algorithmPanelLabel);
        for(JButton button : algorithmSelectButtons)
            add(button);
        add(animationSpeedSlider);
        add(dataSetSizeSlider);
    }

    private JPanel createAnimationSpeedSlider() {
        JPanel speedSlider = new JPanel();
        speedSlider.setLayout(new GridLayout(0, 1));
        JLabel speedLabel = new JLabel(" " + 1000/30 + " operations/sec");
        speedLabel.setFont(new Font("plain", Font.BOLD, 14));
        speedLabel.setForeground( new Color(0xffdddddd) );
        JSlider slider = new JSlider(1, 200, 30);
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                speedLabel.setText(" " + 1000/slider.getValue() + " operations/sec");
                imagePanel.setSpeed(slider.getValue());
            }
        });;
        slider.setMinorTickSpacing(1);
        slider.setSnapToTicks(true);
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
        JPanel dataSetSizeSlider = new JPanel();
        dataSetSizeSlider.setLayout(new GridLayout(0, 1));
        JLabel sizeLabel = new JLabel(" " + (int)(imagePanel.getImageWidth() * 0.9) / 75 + " data points");
        sizeLabel.setFont(new Font("plain", Font.BOLD, 14));
        sizeLabel.setForeground( new Color(0xffdddddd) );
        int minWidth = 256;
        while(((int)(imagePanel.getImageWidth() * 0.9) / minWidth) % 20 != 0) {
            minWidth--;
        }
        imagePanel.setDataSize( 12 );
        imagePanel.randomizeData();
        imagePanel.drawAll();
        sizeLabel.setText(" " + imagePanel.getNumCells() + " data points");
        JSlider dslider = new JSlider(1, minWidth, minWidth - 10);
        dslider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                imagePanel.setDataSize( 1 + dslider.getMaximum() - dslider.getValue() );
                imagePanel.randomizeData();
                sizeLabel.setText(" " + imagePanel.getNumCells() + " data points");
                imagePanel.drawAll();
            }
        });
        dslider.setMinorTickSpacing(1);
        dslider.setPaintTicks(true);
        dslider.setSnapToTicks(true);
        dataSetSizeSlider.add(sizeLabel, BorderLayout.CENTER);
        dataSetSizeSlider.add(dslider, BorderLayout.SOUTH);
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

    public int x, y, curr_x, curr_y, width, height;
    private BufferedImage image = null;
    private Graphics2D g2d = null;
    private int borderWidth = 7;
    private ControlPanel controlPanel;
    private ColorDisplay colorDisplay;
    boolean isVisible = false;
    int startColor = 0xffcccccc, endColor = 0x0fFFD700;


    public ColorChooser(ImagePanel imagePanel) {

        setBackground(new Color(50, 50, 50, 140));
        int width = (int)(imagePanel.getWidth() * .175);
        int height = (int)(imagePanel.getHeight() * .15);
        x = curr_x = (int)(imagePanel.getWidth() * (1 - .2));
        y = curr_y = (int)(imagePanel.getHeight() * .20) + height * 3 + 7 * 2;

        image = new BufferedImage(width - borderWidth, (int)(height / 2.5) - borderWidth, BufferedImage.TYPE_INT_ARGB);
        g2d = (Graphics2D)image.getGraphics();
        setBounds(curr_x, curr_y, width, (int)(height / 2.5));
        drawImage();

        setBorder(BorderFactory.createLineBorder(new Color(70, 70, 70, 130), borderWidth));
        setBackground(new Color(70, 70, 70, 130));
        setVisible(true);
        setOpaque(true);

        this.width = image.getWidth() + borderWidth;
        this.height = (int)(height / 2.5);
        addMouseListener( new MouseAdapter() {
            public void mousePressed( MouseEvent event ) {
                if(event.getButton() == MouseEvent.BUTTON1) {
                    x = (int)event.getPoint().getX();
                    y = (int)event.getPoint().getY();
                    if(x > borderWidth && y > borderWidth && x < image.getWidth() + borderWidth && y < image.getHeight() + borderWidth) {
                        if(isVisible) {
                            colorDisplay.setVisible(false);
                            isVisible = false;
                        }
                        else {
                            colorDisplay.setVisible(true);
                            isVisible = true;
                        }
                    }
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

                colorDisplay.curr_x += (x_offset);
                colorDisplay.curr_y += (y_offset);
                colorDisplay.setBounds(colorDisplay.curr_x, colorDisplay.curr_y, colorDisplay.width, colorDisplay.height);

                imagePanel.repaint();
            }
        } );
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

    public int x, y, curr_x, curr_y, width, height;
    private ImagePanel imagePanel;
    private ColorChooser colorChooser;
    private BufferedImage image = null;
    private Graphics2D g2d = null;
    private int borderWidth = 7;
    private JSlider r1, g1, b1, r2, g2, b2;

    public ColorDisplay(ImagePanel imagePanel, ColorChooser colorChooser) {

        width = (int)(imagePanel.getWidth() * .4);
        height = (int)(imagePanel.getHeight() * .45);
        x = curr_x = (int)(imagePanel.getWidth() * (1 - .60) - 7 * 2);
        y = curr_y = (int)(imagePanel.getHeight() * .20);

        image = new BufferedImage(width - borderWidth, (int)(height) - borderWidth, BufferedImage.TYPE_INT_ARGB);
        g2d = (Graphics2D)image.getGraphics();

        setBounds(curr_x, curr_y, width, height);
        setLayout(new GridLayout(0, 2));
        setBackground(new Color(70, 70, 70, 130));
        setBorder(BorderFactory.createLineBorder(new Color(70, 70, 70, 130), 7));
        setVisible(false);
        setOpaque(true);

        this.imagePanel = imagePanel;
        this.colorChooser = colorChooser;
        r1 = new JSlider(JSlider.HORIZONTAL,0,255, 0xcc);
        r1.setMajorTickSpacing(85);
        r1.setPaintLabels(true);
        g1 = new JSlider(JSlider.HORIZONTAL,0,255,0xcc);
        g1.setMajorTickSpacing(85);
        g1.setPaintLabels(true);
        b1 = new JSlider(JSlider.HORIZONTAL,0,255,0xcc);
        b1.setMajorTickSpacing(85);
        b1.setPaintLabels(true);

        r2 = new JSlider(JSlider.HORIZONTAL,0,255,0xFF);
        r2.setMajorTickSpacing(85);
        r2.setPaintLabels(true);
        g2 = new JSlider(JSlider.HORIZONTAL,0,255,0xD7);
        g2.setMajorTickSpacing(85);
        g2.setPaintLabels(true);
        b2 = new JSlider(JSlider.HORIZONTAL,0,255,0x00);
        b2.setMajorTickSpacing(85);
        b2.setPaintLabels(true);

        r1.setFont(new Font("plain", Font.BOLD, 13));
        r2.setFont(new Font("plain", Font.BOLD, 13));
        g1.setFont(new Font("plain", Font.BOLD, 13));
        g2.setFont(new Font("plain", Font.BOLD, 13));
        b1.setFont(new Font("plain", Font.BOLD, 13));
        b2.setFont(new Font("plain", Font.BOLD, 13));

        r1.setForeground(Color.RED);
        r2.setForeground(Color.RED);
        g1.setForeground(Color.GREEN);
        g2.setForeground(Color.GREEN);
        b1.setForeground(Color.BLUE);
        b2.setForeground(Color.BLUE);

        Event e = new Event();
        r1.addChangeListener(e);
        g1.addChangeListener(e);
        b1.addChangeListener(e);
        r2.addChangeListener(e);
        g2.addChangeListener(e);
        b2.addChangeListener(e);

        this.add(r1);
        this.add(r2);
        this.add(g1);
        this.add(g2);
        this.add(b1);
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

    public void drawImage() {
        int rValue = r1.getValue();
        int gValue = g1.getValue();
        int bValue = b1.getValue();

        int rValue2 = r2.getValue();
        int gValue2 = g2.getValue();
        int bValue2 = b2.getValue();

        int start = (0xFF000000) | ((int)rValue << 16) | ((int)gValue << 8) | (int)bValue;
        int end = (0xFF000000) | ((int)rValue2 << 16) | ((int)gValue2 << 8) | (int)bValue2;

        Color[] colors = Interpolation.getColors( start, end, image.getWidth() );

        for (int i = 0; i < image.getWidth(); i++) {
            g2d.setColor(colors[i]);
            g2d.drawLine(i + borderWidth, borderWidth, i + borderWidth, image.getHeight() + borderWidth);
        }
        colorChooser.setColors(start, end);
        colorChooser.drawImage();
        imagePanel.setColors(start, end);
        imagePanel.assignColors();
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