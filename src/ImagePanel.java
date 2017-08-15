import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import java.util.Random;

public class ImagePanel extends JLayeredPane {

    private BufferedImage image = null; 
    private Graphics2D g2d = null;

    private ControlPanel controlPanel;
    private Cell[] cols;
    private int numCells, height, width;  // in pixels
    private int animationSpeed;
    private boolean isSorted, isPaused;

    public ImagePanel(int width, int height) {
        setBounds(0, 0, width, height);
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2d = (Graphics2D)image.getGraphics();

        g2d.setColor( new Color(0xff000000) );                  // draw outline
        g2d.drawRect((int)(image.getWidth() * 0.05), (int)(image.getWidth() * 0.05), (int)(image.getWidth() * 0.9), (int)(image.getHeight() * 0.8));

        addComponents();

        numCells = 150;
        this.width = (int)(image.getWidth() * 0.9) / numCells; // initially 25 cells
        this.height = (int)(image.getHeight() - 2 * (int)(image.getWidth() * 0.05));

        animationSpeed = 50;
        randomizeData();
        SwingUtilities.invokeLater( new Runnable() {
                    public void run() {
                        drawAll();
                    }
                    });

        setFocusable(false);
    }

    public void addComponents() {
        controlPanel = new ControlPanel(this);
        add(controlPanel, new Integer(3));
    }

    public void randomizeData() {
        Random rand = new Random();
        isSorted = false;
        cols = new Cell[numCells];
        for(int i = 0; i < cols.length; i++) {
            int val = rand.nextInt(2 * numCells + 1);
            cols[i] = new Cell(val, width, (int)(height * val / (2 * numCells + 1)));
        }
    }

    public void drawAll() {
        g2d.setColor( new Color(0xffabc123) );                  // draw background
        g2d.fillRect((int)(image.getWidth() * 0.05), (int)(image.getWidth() * 0.05) + 1, (int)(image.getWidth() * 0.9), this.height - 1);

        for(int i = cols.length - 1; i >= 0; i--) 
            drawCell(Color.BLACK, i, cols[i]);    

        g2d.setColor( Color.BLACK );                  // draw background
        g2d.drawRect((int)(image.getWidth() * 0.05), (int)(image.getWidth() * 0.05), (int)(image.getWidth() * 0.9), this.height);
        repaint();
    }

    public void drawCell(Color color, int index, Cell c) {
        int x = (int)(image.getWidth() * 0.05);
        int y = (int)(image.getWidth() * 0.05);
        x += ((int)(image.getWidth() * 0.9) - numCells * width) / 2;

        g2d.setColor(new Color(0xffabc123));
        g2d.fillRect(x + index * width, y + 1, width, height - 1);
        g2d.setColor(color);
        g2d.fillRect(x + index * width, y + (this.height - c.pixelHeight), width, c.pixelHeight);
    }

    public void drawFullCell(Color color, int index) {
        int x = (int)(image.getWidth() * 0.05);
        int y = (int)(image.getHeight() * 0.05);
        x += ((int)(image.getWidth() * 0.9) - numCells * width) / 2;

        g2d.setColor(color);
        g2d.fillRect(x + index * width, y + 1, width, height - 1);
    }

    public boolean paused() {
        return isPaused == true;
    }

    public void pause() {
        isPaused = true;
    }

    public void resume() {
        isPaused = false;
    }

    public int getImageWidth() {
        return image.getWidth();
    }

    protected void paintComponent(Graphics g) {
        g.drawImage(image, 0, 0, null);
    }

    public void setDataSize(int numCells) {
        this.numCells = numCells;
        this.width = (int)(image.getWidth() * 0.9) / numCells; // initially 25 cells
    }

    public void setSpeed(int fps) {
        this.animationSpeed = fps;
    }    

    public void quickSort() {
        quickSort(cols, 0, cols.length - 1);
        SwingUtilities.invokeLater( new Runnable() {
                    public void run() {
                        drawAll();
                    }
                    });
    }

    public void quickSort(Cell[] arr, int left, int right) {
            int partition = partition(arr, left, right);

            if(left < partition - 1)
                quickSort(arr, left, partition - 1);

            if(right > partition)
                quickSort(arr, partition, right);
    }

    public int partition(Cell[] arr, int left, int right) {

        int pivot = arr[ (left + right) / 2 ].val;

        while(left <= right) {

            while(arr[left].val < pivot)    // find leftside element greater than pivot
                left++;
            while(arr[right].val > pivot)   // find rightside element less than pivot
                right--;

            if(left <= right) {         
                Cell temp = arr[left];   // swap the two elements
                arr[left] = arr[right];
                arr[right] = temp;

                final int leftIndex = left, rightIndex = right;
                final Cell leftCell = arr[left], rightCell = arr[right];;
                SwingUtilities.invokeLater( new Runnable() {
                public void run() {
                    drawCell(Color.BLACK, leftIndex,   leftCell);
                    drawCell(Color.BLACK, rightIndex, rightCell);
                    repaint();
                }
                } );
                
                try{ Thread.sleep(animationSpeed);   }
                catch( Exception e)                 {}

                left++;
                right--;
            }
        }

        return left;                    // return partition index
    }

    public void mergeSort() {
        mergeSort(cols, 0, cols.length - 1);
        SwingUtilities.invokeLater( new Runnable() {
                    public void run() {
                        drawAll();
                    }
                    });
    }

    public void mergeSort(Cell[] arr, int left, int right) {
        if(left >= right)
            return;

        int middle = (left + right) / 2;

        mergeSort(arr, left, middle);            // sort left half including middle element
        mergeSort(arr, middle + 1, right);       // sort right half
        merge(arr, left, right, middle);         // merge two sorted halfs together
    }

    public void merge(Cell[] arr, int left, int right, int middle) {
        Cell[] leftArr  = new Cell[ middle - left + 1 ];
        Cell[] rightArr = new Cell[ right - middle ];

        for(int i = 0; i < leftArr.length;  i++) leftArr[i] = arr[left + i];
        for(int i = 0; i < rightArr.length; i++) rightArr[i] = arr[middle + i + 1];

        int i = 0, j = 0, k = left;
        while(i < leftArr.length && j < rightArr.length) {
            if(leftArr[i].val < rightArr[j].val)  arr[k++] =  leftArr[i++];
            else                                  arr[k++] = rightArr[j++];

            final int xx = k-1;
            final Cell yy = arr[k-1];
            SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                drawCell(Color.BLACK, xx, yy);
                repaint();
            }
            } );
            try{ Thread.sleep(animationSpeed);   }
            catch( Exception e)                 {}
        }

        while(i < leftArr.length )   arr[k++] =  leftArr[i++];
        while(j < rightArr.length)   arr[k++] = rightArr[j++];
    }

    public void bubbleSort() {

        for(int i = 0; i < cols.length - 1;  i++) {
            for(int j = cols.length - 1; j > i; j--) {
                if(cols[j].val < cols[j - 1].val) {
                    final int x11 = j;
                    final Cell y11 = cols[j];
                    SwingUtilities.invokeLater( new Runnable() {
                    public void run() {
                        drawCell(Color.RED, x11, y11);
                        repaint();
                    }
                    } );
                    try{ Thread.sleep(animationSpeed);   }
                    catch( Exception e)                 {}
                    Cell temp = cols[j];   // swap the two elements
                    cols[j] = cols[j - 1];
                    cols[j - 1] = temp;
                    final int x1 = j, x2 = j - 1;
                    final Cell y1 = cols[j], y2 = cols[j - 1];
                    SwingUtilities.invokeLater( new Runnable() {
                    public void run() {
                        drawCell(Color.BLACK, x1, y1);
                        drawCell(Color.BLACK, x2, y2);
                        repaint();
                    }
                    } );
                }
            }
        }

        SwingUtilities.invokeLater( new Runnable() {
                    public void run() {
                        drawAll();
                    }
                    });
    }

    public void insertSort() {

         for(int i = 0; i < cols.length - 1;  i++) {
            for(int j = i + 1; j > 0; j--) {
                while( j > 0 && cols[j].val < cols[j - 1].val ) {
                    final int x11 = j;
                    final Cell y11 = cols[j];
                    SwingUtilities.invokeLater( new Runnable() {
                    public void run() {
                        drawCell(Color.RED, x11, y11);
                        repaint();
                    }
                    } );
                    try{ Thread.sleep(animationSpeed);   }
                    catch( Exception e)                 {}
                    Cell temp = cols[j];   // swap the two elements
                    cols[j] = cols[j - 1];
                    cols[j - 1] = temp;
                    final int x1 = j, x2 = j - 1;
                    final Cell y1 = cols[j], y2 = cols[j - 1];
                    j--;
                    SwingUtilities.invokeLater( new Runnable() {
                    public void run() {
                        drawCell(Color.BLACK, x1, y1);
                        drawCell(Color.BLACK, x2, y2);
                        repaint();
                    }
                    } );
                }
            }
        }

        SwingUtilities.invokeLater( new Runnable() {
                    public void run() {
                        drawAll();
                    }
                    });
    }

    public void selectSort() {

        int index = 0;
        for(int i = 0; i < cols.length;  i++) {
            int j, min = Integer.MAX_VALUE;
            for(j = i; j < cols.length;  j++) { // find min
                if(cols[j].val < min) {
                    index = j;
                    min = cols[j].val;
                }
            }

                    final int x11 = index;
                    final Cell y11 = cols[index];
                    SwingUtilities.invokeLater( new Runnable() {
                    public void run() {
                        drawCell(Color.RED, x11, y11);
                        repaint();
                    }
                    } );
                    try{ Thread.sleep(animationSpeed * 2);   }
                    catch( Exception e)                 {}

                    Cell temp = cols[i];   // swap the two elements
                    cols[i] = cols[index];
                    cols[index] = temp;


                    final int x1 = index, x2 = i;
                    final Cell y1 = cols[index], y2 = cols[i];
                    SwingUtilities.invokeLater( new Runnable() {
                    public void run() {
                        drawCell(Color.BLACK, x1, y1);
                        drawCell(Color.BLACK, x2, y2);
                        repaint();
                    }
                    } );
        }

                SwingUtilities.invokeLater( new Runnable() {
                    public void run() {
                        drawAll();
                    }
                    } );
    }

    public boolean isSorted() {
        return this.isSorted;
    }

    public void sortStatus(boolean isSorted) {
        this.isSorted = isSorted;
    }

    private static class Cell {
        int val;
        int pixelWidth, pixelHeight;
        public Cell(int val, int pixelWidth, int pixelHeight) {
            this.val = val;
            this.pixelWidth = pixelWidth;
            this.pixelHeight = pixelHeight;
        }
    } 
}

