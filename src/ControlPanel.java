import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;

public class ControlPanel extends JPanel {

    private int x, y, curr_x, curr_y;
    private JButton quicksortBtn, mergesortBtn, bubblesortBtn, insertsortBtn, sortorderBtn, startSearch;

    public ControlPanel(ImagePanel imagePanel) {
        
        int width = (int)(imagePanel.getWidth() * .175);
        int height = (int)(imagePanel.getHeight() * .5);
        x = curr_x = (int)(imagePanel.getWidth() * (1 - .2));
        y = curr_y = (int)(imagePanel.getHeight() * .25);
        setBounds(x, y, width, height);

        setLayout(new GridLayout(0, 1));
        addComponents(imagePanel);

        setBackground(new Color(50, 50, 50, 200));
        this.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220,  32), 6));
        setVisible(true);
        setOpaque(true);

        addMouseListener( new MouseAdapter() {
                public void mousePressed( MouseEvent event ) {
                    if(event.getButton() == MouseEvent.BUTTON1) {
                        x = (int)event.getPoint().getX();
                        y = (int)event.getPoint().getY();
                    }
                }
            } );

       addMouseMotionListener( new MouseMotionAdapter() {
                public void mouseDragged(MouseEvent event) {
                    Point p = event.getPoint();
                    curr_x += (p.getX() - x);
                    curr_y += (p.getY() - y);
                    setBounds(curr_x, curr_y, width, height);
                }
            } );
    }

    public String getStartSearchText() {
        return startSearch.getText();
    }

    private void clearAll() {
        Color textColor = new Color(0, 0, 0, 250);
        quicksortBtn.setForeground(textColor);
        mergesortBtn.setForeground(textColor);
        bubblesortBtn.setForeground(textColor);
        insertsortBtn.setForeground(textColor);
    }

    public void setSearchText(String s, Color c) {
        startSearch.setText(s);
        startSearch.setForeground(c);
    }

    public void setMazeText(String s, Color c) {
        sortorderBtn.setText(s);
        sortorderBtn.setForeground(c);
    }

    public void runSearch() {
        startSearch.setText( "Pause");
        startSearch.setForeground(  Color.RED  );
    }

    public void pauseSearch() {
        startSearch.setText( "Resume");
        startSearch.setForeground(  new Color(0, 175, 0, 255)  );
    }

    public void resumeSearch() {
        startSearch.setText( "Pause");
        startSearch.setForeground(  Color.RED  );
    }

    public void readySearch() {
        setSearchText("Start Sort",    new Color(0, 0, 0, 250));
    }

    private void doSearch() {

    }

    private void setAscendingSort() {
        sortorderBtn.setText("Ascending sort");
    }

    private void setDecendingSort() {
        sortorderBtn.setText("Descending sort");
    }

    private void selectQuicksort() {
        clearAll();
        quicksortBtn.setForeground(  new Color(0, 175, 0, 255)  );
    }

    private void selectMergesort() {
        clearAll();
        mergesortBtn.setForeground(  new Color(0, 175, 0, 255)  );
    }

    private void selectInsertionsort() {
        clearAll();
        insertsortBtn.setForeground(  new Color(0, 175, 0, 255)  );
    }

    private void selectBubblesort() {
        clearAll();
        bubblesortBtn.setForeground(  new Color(0, 175, 0, 255)  );
    }

    private void addComponents(ImagePanel imagePanel) {
        startSearch = new JButton("Start sort");
        startSearch.setForeground(  new Color(0, 175, 0, 255)  );
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

        sortorderBtn = new JButton("Ascending sort");
        sortorderBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(sortorderBtn.getText().equals("Ascending sort"))
                    setDecendingSort();
                else if (sortorderBtn.getText().equals( "Descending sort"))
                    setAscendingSort();
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

        // Animation speed slider
        JPanel speedSlider = new JPanel();
        speedSlider.setLayout(new GridLayout(0, 1));
        JLabel speedLabel = new JLabel(" Speed");
        speedLabel.setFont(new Font("plain", Font.BOLD, 14));
        speedLabel.setForeground( new Color(0xffbbbbbb) );
        JSlider slider = new JSlider(0, 50, 25);
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

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
        JLabel sizeLabel = new JLabel(" Size of data set: " + 25 + " items");
        sizeLabel.setFont(new Font("plain", Font.BOLD, 14));
        sizeLabel.setForeground( new Color(0xffbbbbbb) );
        JSlider dslider = new JSlider(1, imagePanel.getImageWidth(), 25);
        dslider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                sizeLabel.setText(" Size of data set: " + dslider.getValue() + " items");
            }
        });
        dslider.setMinorTickSpacing(1);
        // slider.setPaintTicks(true);
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
        sortLabel.setForeground( new Color(0xffbbbbbb) );
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
        algo_label.setFont(new Font("plain", Font.BOLD, 14));
        startSearch.setFont(new Font("plain", Font.BOLD, 13));
        mergesortBtn.setFont(new Font("plain", Font.BOLD, 13));
        quicksortBtn.setFont(new Font("plain", Font.BOLD, 13));
        startSearch.setFont(new Font("plain", Font.BOLD, 13));
        bubblesortBtn.setFont(new Font("plain", Font.BOLD, 13));
        insertsortBtn.setFont(new Font("plain", Font.BOLD, 13));
        sortorderBtn.setFont(new Font("plain", Font.BOLD, 13));

        // Test color
        algo_label.setForeground( new Color(0xffbbbbbb) );
        Color textColor = new Color(0, 0, 0, 250);
        startSearch.setForeground(textColor);
        sortorderBtn.setForeground(textColor);
        mergesortBtn.setForeground(textColor);
        quicksortBtn.setForeground(textColor);
        bubblesortBtn.setForeground(textColor);
        insertsortBtn.setForeground(textColor);
        sortorderBtn.setForeground( new Color(0, 175, 0, 255)  );

        mergesortBtn.setOpaque(false);
        quicksortBtn.setOpaque(false);
        startSearch.setOpaque(false);
        bubblesortBtn.setOpaque(false);
        insertsortBtn.setOpaque(false);

        // Add components to JPanel
        add(startSearch);
        add(algo_label);
        add(quicksortBtn);
        add(mergesortBtn);
        add(bubblesortBtn);
        add(insertsortBtn);
        add(speedSlider);
        add(sortSlider);
        add(dataSlider);
        add(sortorderBtn);
    }

}
