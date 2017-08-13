import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;

public class ControlPanel extends JPanel {

    private ImagePanel imagePanel;
    private JButton startSearch, createMaze;
    private JButton selectBFS, selectDFS, selectDijkstra, selectA_star;
    private Color textColor = new Color(0, 0, 0, 250);
    private int x, y, curr_x, curr_y, width, height; 

    public ControlPanel(ImagePanel imagePanel) {
        
        setLayout(new GridLayout(0, 1));
        this.imagePanel = imagePanel;
        width = (int)(imagePanel.getWidth() * .175);
        height = (int)(imagePanel.getHeight() * .5);
        x = curr_x = (int)(imagePanel.getWidth() * (1 - .2));
        y = curr_y = (int)(imagePanel.getHeight() * .25);
        setBounds(x, y, width, height);

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

        createMaze = new JButton("Ascending sort");
        createMaze.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(createMaze.getText().equals("Ascending sort"))
                    setDecendingSort();
                else if (createMaze.getText().equals( "Descending sort"))
                    setAscendingSort();
            }
        });

        selectBFS = new JButton("Quicksort");
        selectBFS.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectBFS();
            }
        });

        selectDFS = new JButton("Merge Sort");
        selectDFS.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectDFS();
            }
        });

        selectDijkstra = new JButton("Bubble sort");
        selectDijkstra.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectDijkstra();
            }
        });

        selectA_star = new JButton("Insertion sort");
        selectA_star.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectAstar();
            }
        });

        // animation speed slider
        JPanel speedSlider = new JPanel();
        speedSlider.setLayout(new GridLayout(0, 1));

        JLabel label = new JLabel(" Speed");
        label.setFont(new Font("plain", Font.BOLD, 14));
        label.setForeground( new Color(0xffbbbbbb) );

        JSlider slider = new JSlider(0, 50, 25);
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                
            }
        });;
        slider.setMinorTickSpacing(1);
        // slider.setPaintTicks(true);
        slider.setSnapToTicks(true);
        speedSlider.add(label, BorderLayout.CENTER);
        speedSlider.add(slider, BorderLayout.SOUTH);
        speedSlider.setOpaque(false);
        speedSlider.setVisible(true);

        // animation speed slider
        JPanel dataSlider = new JPanel();
        dataSlider.setLayout(new GridLayout(0, 1));

        JLabel label3 = new JLabel(" Data size");
        label3.setFont(new Font("plain", Font.BOLD, 14));
        label3.setForeground( new Color(0xffbbbbbb) );

        JSlider dslider = new JSlider(0, 50, 25);
        dslider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                
            }
        });;
        dslider.setMinorTickSpacing(1);
        // slider.setPaintTicks(true);
        dslider.setSnapToTicks(true);
        dataSlider.add(label3, BorderLayout.CENTER);
        dataSlider.add(dslider, BorderLayout.SOUTH);
        dataSlider.setOpaque(false);
        dataSlider.setVisible(true);

        JLabel algo_label = new JLabel(" Algorithms");

        algo_label.setFont(new Font("plain", Font.BOLD, 14));
        startSearch.setFont(new Font("plain", Font.BOLD, 13));
        selectDFS.setFont(new Font("plain", Font.BOLD, 13));
        selectBFS.setFont(new Font("plain", Font.BOLD, 13));
        startSearch.setFont(new Font("plain", Font.BOLD, 13));
        selectDijkstra.setFont(new Font("plain", Font.BOLD, 13));
        selectA_star.setFont(new Font("plain", Font.BOLD, 13));
        createMaze.setFont(new Font("plain", Font.BOLD, 13));

        algo_label.setForeground( new Color(0xffbbbbbb) );
        startSearch.setForeground(textColor);
        createMaze.setForeground(textColor);
        selectDFS.setForeground(textColor);
        selectBFS.setForeground(textColor);
        selectDijkstra.setForeground(textColor);
        selectA_star.setForeground(textColor);
        createMaze.setForeground( new Color(0, 175, 0, 255)  );

        selectDFS.setOpaque(false);
        selectBFS.setOpaque(false);
        startSearch.setOpaque(false);
        selectDijkstra.setOpaque(false);
        selectA_star.setOpaque(false);

        // add components
        add(startSearch);
        add(algo_label);
        add(selectBFS);
        add(selectDFS);
        add(selectDijkstra);
        add(selectA_star);
        add(speedSlider);
        add(new SizeSlider(imagePanel, this));
        add(dataSlider);
        add(createMaze);

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
        selectBFS.setForeground(textColor);
        selectDFS.setForeground(textColor);
        selectDijkstra.setForeground(textColor);
        selectA_star.setForeground(textColor);
    }

    public void setSearchText(String s, Color c) {
        startSearch.setText(s);
        startSearch.setForeground(c);
    }

    public void setMazeText(String s, Color c) {
        createMaze.setText(s);
        createMaze.setForeground(c);
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
        setSearchText("Start Sort", textColor);
    }

    private void doSearch() {

    }

    private void setAscendingSort() {
        createMaze.setText("Ascending sort");
    }

    private void setDecendingSort() {
        createMaze.setText("Descending sort");
    }

    private void selectBFS() {
        clearAll();
        selectBFS.setForeground(  new Color(0, 175, 0, 255)  );
    }

    private void selectDFS() {
        clearAll();
        selectDFS.setForeground(  new Color(0, 175, 0, 255)  );
    }

    private void selectAstar() {
        clearAll();
        selectA_star.setForeground(  new Color(0, 175, 0, 255)  );
    }

    private void selectDijkstra() {
        clearAll();
        selectDijkstra.setForeground(  new Color(0, 175, 0, 255)  );
    }

}

class SizeSlider extends JPanel {

    private JSlider slider;
    private JLabel label;

    public SizeSlider(ImagePanel imagePanel, ControlPanel controls) {
        setLayout(new GridLayout(0, 1));

        label = new JLabel();
        label.setFont(new Font("plain", Font.BOLD, 14));
        label.setForeground( new Color(0xffbbbbbb) );

        slider = new JSlider(0, 100, 2);
        label.setText( " " + String.valueOf( slider.getValue()) + "% sorted ");

        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

                if (controls.getStartSearchText().equals( "Pause")) // pause if running
                    controls.pauseSearch();

                int size = slider.getValue();
                label.setText( " " + String.valueOf( slider.getValue()) + "% sorted");
            }
        });;

        slider.setMinorTickSpacing(3);
        // slider.setPaintTicks(true);
        slider.setSnapToTicks(true);

        add(label, BorderLayout.CENTER);
        add(slider, BorderLayout.SOUTH);

        setOpaque(false );
        setVisible(true);
    }
}