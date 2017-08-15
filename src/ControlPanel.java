import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class ControlPanel extends JPanel {

    private int x, y, curr_x, curr_y;
    private JButton quicksortBtn, mergesortBtn, bubblesortBtn, insertsortBtn, selectionsortBtn, sortorderBtn, startSearch;
    private boolean quickSort, mergeSort, bubbleSort, insertSort, selectSort;
    private ImagePanel imagePanel;

    public ControlPanel(ImagePanel imagePanel) {
        
        int width = (int)(imagePanel.getWidth() * .175);
        int height = (int)(imagePanel.getHeight() * .5);
        x = curr_x = (int)(imagePanel.getWidth() * (1 - .2));
        y = curr_y = (int)(imagePanel.getHeight() * .25);
        this.imagePanel = imagePanel;
        setBounds(curr_x, curr_y, width, height);

        setLayout(new GridLayout(0, 1));
        addComponents(imagePanel);

        setBackground(new Color(50, 50, 50, 150));
        // this.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220,  32), 6));
        setVisible(true);
        setOpaque(true);

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
                    Point p = event.getPoint();
                    curr_x += (p.getX() - x);
                    curr_y += (p.getY() - y);
                    setBounds(curr_x, curr_y, width, height);
                    imagePanel.repaint();
                }
            } );
    }

    public String getStartSearchText() {
        return startSearch.getText();
    }

    private void clearAll() {
        Color textColor = new Color(0, 0, 0, 255);
        quicksortBtn.setForeground(textColor);
        quicksortBtn.setFont(new Font("plain", Font.BOLD, 13));
        mergesortBtn.setForeground(textColor);
        mergesortBtn.setFont(new Font("plain", Font.BOLD, 13));
        bubblesortBtn.setForeground(textColor);
        bubblesortBtn.setFont(new Font("plain", Font.BOLD, 13));
        insertsortBtn.setForeground(textColor);
        insertsortBtn.setFont(new Font("plain", Font.BOLD, 13));
        selectionsortBtn.setForeground(textColor);
        selectionsortBtn.setFont(new Font("plain", Font.BOLD, 13));
        quickSort = false;
        mergeSort = false;
        bubbleSort = false;
        insertSort = false;
        selectSort = false;
    }

    public void setSearchText(String s, Color c) {
        startSearch.setText(s);
        startSearch.setForeground(c);
    }

    public void setOrderText(String s, Color c) {
        sortorderBtn.setText(s);
        sortorderBtn.setForeground(c);
    }

    public void runSearch() {
        startSearch.setText( "Pause");
        startSearch.setForeground(  Color.RED  );
        new Thread( new Runnable() {
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
                            readySearch();
                            imagePanel.sortStatus(true);
                        }
        } ).start(); 
    }

    public void pauseSearch() {
        startSearch.setText( "Resume");
        startSearch.setForeground(  new Color(0, 175, 0, 255)  );
        imagePanel.pause();
    }

    public void resumeSearch() {
        startSearch.setText( "Pause");
        startSearch.setForeground(  Color.RED  );
        imagePanel.resume();
    }

    public void readySearch() {
        setSearchText("Start sort",    new Color(0, 0, 0, 250));
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
        JLabel speedLabel = new JLabel(" " + 1000/20 + " operations/sec");
        speedLabel.setFont(new Font("plain", Font.BOLD, 14));
        speedLabel.setForeground( new Color(0xffdddddd) );
        JSlider slider = new JSlider(1, 200, 20);
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
        JLabel sizeLabel = new JLabel(" " + 100 + " data points");
        sizeLabel.setFont(new Font("plain", Font.BOLD, 14));
        sizeLabel.setForeground( new Color(0xffdddddd) );
        int maxCells = imagePanel.getImageWidth();
        while(((int)(imagePanel.getImageWidth() * 0.9)) / maxCells < 1) 
            maxCells -= 5;
        while(maxCells % 10 != 0)
            maxCells--;
        JSlider dslider = new JSlider(5, maxCells, 100);
        dslider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                sizeLabel.setText(" " + dslider.getValue() + " data points");
                imagePanel.setDataSize( dslider.getValue() );
                imagePanel.randomizeData();
                imagePanel.drawAll();
            }
        });
        dslider.setMinorTickSpacing(1);
        // dslider.setPaintTicks(true);
        dslider.setSnapToTicks(true);
        dataSlider.add(sizeLabel, BorderLayout.CENTER);
        dataSlider.add(dslider, BorderLayout.SOUTH);
        dataSlider.setOpaque(false);
        dataSlider.setVisible(true);

        // % sorted slider
        JPanel sortSlider = new JPanel();
        sortSlider.setLayout(new GridLayout(0, 1));
        JLabel sortLabel = new JLabel( " " + String.valueOf( slider.getValue()) + "% sorted ");
        sortLabel.setFont(new Font("plain", Font.BOLD, 14));
        sortLabel.setForeground( new Color(0xffdddddd) );
        JSlider sslider = new JSlider(0, 100, 50);
        sslider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (getStartSearchText().equals( "Pause")) // pause if running
                    pauseSearch();

                int size = slider.getValue();
                sortLabel.setText( " " + String.valueOf( sslider.getValue()) + "% sorted");
            }
        });;
        sslider.setMinorTickSpacing(1);
        // slider.setPaintTicks(true);
        sslider.setSnapToTicks(true);
        sortSlider.add(sortLabel, BorderLayout.CENTER);
        sortSlider.add(sslider, BorderLayout.SOUTH);
        sortSlider.setOpaque(false);
        sortSlider.setVisible(true);

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
        algo_label.setForeground( new Color(0xffdddddd) );
        Color textColor = new Color(0, 0, 0, 255);
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
        // add(sortSlider);
        add(dataSlider);
        selectMergesort();
    }

}
