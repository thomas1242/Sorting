import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.event.*;

public class ControlPanel extends JPanel {

    private JButton quicksortBtn, mergesortBtn, bubblesortBtn, insertsortBtn, selectionsortBtn, sortorderBtn, startSearch;
    private boolean quickSort, mergeSort, bubbleSort, insertSort, selectSort; 
    public int x, y, curr_x, curr_y, width, height;
    private ImagePanel imagePanel;
    private ColorChooser colorChooser;
    private ColorDisplay colorDisplay;

    public ControlPanel(ImagePanel imagePanel) {
        
        width = (int)(imagePanel.getWidth() * .175);
        height = (int)(imagePanel.getHeight() * .45);
        x = curr_x = (int)(imagePanel.getWidth() * (1 - .2));
        y = curr_y = (int)(imagePanel.getHeight() * .20);

        setBounds(curr_x, curr_y, width, height);
        setLayout(new GridLayout(0, 1));
        setBackground(new Color(70, 70, 70, 130));
        setBorder(BorderFactory.createLineBorder(new Color(50, 50, 50, 0), 7));
        setVisible(true);
        setOpaque(true);

        this.imagePanel = imagePanel;
        addComponents(imagePanel);

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

    private void runSearch() {
        startSearch.setText( "Pause");
        startSearch.setForeground(  Color.RED  );
        new Thread( 
            new Runnable() {
                public void run() { 
                    if(quickSort)
                        imagePanel.quickSort();
                    else if (mergeSort)
                        imagePanel.mergeSort();
                    else if (bubbleSort)
                        imagePanel.bubbleSort();
                    else if (insertSort)
                        imagePanel.insertSort();
                    else if (selectSort)
                        imagePanel.selectSort();
                    setSearchText("Start sort", new Color(0, 0, 0, 250));
                }
            } ).start(); 
    }

    public void pauseSearch() {
        startSearch.setText( "Resume");
        startSearch.setForeground(  new Color(0, 175, 0, 255)  );
        // imagePanel.pause();
    }

    public void resumeSearch() {
        startSearch.setText( "Pause");
        startSearch.setForeground(  Color.RED  );
        // imagePanel.resume();
    }

    private void selectQuicksort() {
        clearAll();
        setButtonFont(quicksortBtn, new Color(0, 175, 0, 255), 15);
        quickSort = true;
    }
    private void selectMergesort() {
        clearAll();
        setButtonFont(mergesortBtn, new Color(0, 175, 0, 255), 15);
        mergeSort = true;
    }
    private void selectInsertionsort() {
        clearAll();
        setButtonFont(insertsortBtn, new Color(0, 175, 0, 255), 15);
        insertSort = true;
    }
    private void selectBubblesort() {
        clearAll();
        setButtonFont(bubblesortBtn, new Color(0, 175, 0, 255), 15);
        bubbleSort = true;
    }
    private void selectSelectionsort() {
        clearAll();
        setButtonFont(selectionsortBtn, new Color(0, 175, 0, 255), 15);
        selectSort = true;
    }
    private void setButtonFont(JButton button, Color color, int size) {
        button.setForeground(  color  );
        button.setFont(new Font("plain", Font.BOLD, size));
    }

    private void clearAll() {
        Color textColor = new Color(0, 0, 0, 255);
        quickSort = false;
        quicksortBtn.setForeground(textColor);
        quicksortBtn.setFont(new Font("plain", Font.BOLD, 13));
        mergeSort = false;
        mergesortBtn.setForeground(textColor);
        mergesortBtn.setFont(new Font("plain", Font.BOLD, 13));
        bubbleSort = false;
        bubblesortBtn.setForeground(textColor);
        bubblesortBtn.setFont(new Font("plain", Font.BOLD, 13));
        insertSort = false;
        insertsortBtn.setForeground(textColor);
        insertsortBtn.setFont(new Font("plain", Font.BOLD, 13));
        selectSort = false;
        selectionsortBtn.setForeground(textColor);
        selectionsortBtn.setFont(new Font("plain", Font.BOLD, 13));
    }

    public void setSearchText(String s, Color c) {
        startSearch.setText(s);
        startSearch.setForeground(c);
    }

    private void addComponents(ImagePanel imagePanel) {
        startSearch = new JButton("Start sort");
        startSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(startSearch.getText().equals("Start sort"))
                    runSearch();
                else if (startSearch.getText().equals( "Pause"))
                    pauseSearch();
                else if (startSearch.getText().equals( "Resume"))
                    resumeSearch();
            }
        });
        sortorderBtn = new JButton("Randomize Data");
        sortorderBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                imagePanel.randomizeData();
                imagePanel.animateRandomizeData();
                // imagePanel.drawAll();
            }
        });
        quicksortBtn = new JButton("Quicksort");
        quicksortBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectQuicksort();
            }
        });
        mergesortBtn = new JButton("Merge Sort");
        mergesortBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectMergesort();
            }
        });
        bubblesortBtn = new JButton("Bubble sort");
        bubblesortBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectBubblesort();
            }
        });
        insertsortBtn = new JButton("Insertion sort");
        insertsortBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectInsertionsort();
            }
        });
        selectionsortBtn = new JButton("Selection sort");
        selectionsortBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectSelectionsort();
            }
        });

        // Animation speed slider
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
        // slider.setPaintTicks(true);
        slider.setSnapToTicks(true);
        speedSlider.add(speedLabel, BorderLayout.CENTER);
        speedSlider.add(slider, BorderLayout.SOUTH);
        speedSlider.setOpaque(false);
        speedSlider.setVisible(true);

        // Data set size slider
        JPanel dataSlider = new JPanel();
        dataSlider.setLayout(new GridLayout(0, 1));
        JLabel sizeLabel = new JLabel(" " + (int)(imagePanel.getImageWidth() * 0.9) / 75 + " data points");
        sizeLabel.setFont(new Font("plain", Font.BOLD, 14));
        sizeLabel.setForeground( new Color(0xffdddddd) );
        int minWidth = 256;
        while(((int)(imagePanel.getImageWidth() * 0.9) / minWidth) % 10 != 0) {
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
        dataSlider.add(sizeLabel, BorderLayout.CENTER);
        dataSlider.add(dslider, BorderLayout.SOUTH);
        dataSlider.setOpaque(false);
        dataSlider.setVisible(true);

        // Font size
        JLabel algo_label = new JLabel(" Algorithms");
        algo_label.setFont(new Font("plain", Font.BOLD, 15));
        startSearch.setFont(new Font("plain", Font.BOLD, 13));
        mergesortBtn.setFont(new Font("plain", Font.BOLD, 13));
        quicksortBtn.setFont(new Font("plain", Font.BOLD, 13));
        bubblesortBtn.setFont(new Font("plain", Font.BOLD, 13));
        insertsortBtn.setFont(new Font("plain", Font.BOLD, 13));
        selectionsortBtn.setFont(new Font("plain", Font.BOLD, 13));
        startSearch.setFont(new Font("plain", Font.BOLD, 15));
        sortorderBtn.setFont(new Font("plain", Font.BOLD, 15));

        // Text color
        Color textColor = new Color(0, 0, 0, 255);
        algo_label.setForeground( new Color(0xffdddddd) );
        startSearch.setForeground(textColor);
        sortorderBtn.setForeground(textColor);
        mergesortBtn.setForeground(textColor);
        quicksortBtn.setForeground(textColor);
        bubblesortBtn.setForeground(textColor);
        insertsortBtn.setForeground(textColor);
        selectionsortBtn.setForeground(textColor);
        sortorderBtn.setForeground( new Color(0, 175, 0, 255)  );
        mergesortBtn.setOpaque(false);
        quicksortBtn.setOpaque(false);
        startSearch.setOpaque(false);
        bubblesortBtn.setOpaque(false);
        insertsortBtn.setOpaque(false);
        selectionsortBtn.setOpaque(false);

        // Add components to JPanel
        add(startSearch);
        add(sortorderBtn);
        add(algo_label);
        add(quicksortBtn);
        add(mergesortBtn);
        add(bubblesortBtn);
        add(insertsortBtn);
        add(selectionsortBtn);
        add(speedSlider);
        add(dataSlider);
        selectMergesort();
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
        setBorder(BorderFactory.createLineBorder(new Color(70, 70, 70, 130), borderWidth));
        setBackground(new Color(70, 70, 70, 130));

        drawImage();
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

        // g2d.setColor( new Color(70, 70, 70, 140) );
        // g2d.drawLine(image.getWidth() / 2 + borderWidth, borderWidth, image.getWidth() / 2 + borderWidth, image.getHeight() + borderWidth );

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

    protected void paintComponent(Graphics g) {
        g.drawImage(image, 0, 0, null);
    }
}