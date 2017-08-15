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

        numCells = 100;
        this.width = (int)(image.getWidth() * 0.9) / numCells; // initially 25 cells
        this.height = (int)(image.getHeight() - 2 * (int)(image.getWidth() * 0.05));

        animationSpeed = 50;
        randomizeData();
        drawAll();
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

    public void animateRandomizeData() {
         new Thread( new Runnable() {
                        public void run() { 
                                for(int i = 0; i < cols.length / 2; i++) {
                                    drawCell(Color.RED, i, cols[i], 0);
                                    drawCell(Color.RED, cols.length - 1 - i, cols[cols.length - 1 - i], 1);
                                    drawCell(Color.BLACK, i, cols[i], 0);
                                    drawCell(Color.BLACK, cols.length - 1 - i, cols[cols.length - 1 - i], 0);
                                }
                        }
        } ).start(); 
    }

    public void drawAll() {
        final int width = image.getWidth();
        final int height = this.height;
        SwingUtilities.invokeLater( new Runnable() {
                    public void run() {
                        g2d.setColor( new Color(0xffabc123) );                  // draw background
                        g2d.fillRect((int)(width * 0.05), (int)(width * 0.05) + 1, (int)(width * 0.9), height - 1);

                        for(int i = cols.length - 1; i >= 0; i--) 
                            drawCell(Color.BLACK, i, cols[i]);    

                        g2d.setColor( Color.BLACK );                  // draw background
                        g2d.drawRect((int)(width * 0.05), (int)(width * 0.05), (int)(width * 0.9), height);
                        repaint();
                    }
                    });
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

    public void drawCell(Color color, int index, Cell c, int delay) {
         final int width = this.width;
         final int height = this.height;
         SwingUtilities.invokeLater( new Runnable() {
                    public void run() {
                        int x = (int)(image.getWidth() * 0.05);
                        int y = (int)(image.getWidth() * 0.05);
                        x += ((int)(image.getWidth() * 0.9) - numCells * width) / 2;

                        g2d.setColor(new Color(0xffabc123));
                        g2d.fillRect(x + index * width, y + 1, width, height - 1);
                        g2d.setColor(color);
                        g2d.fillRect(x + index * width, y + (height - c.pixelHeight), width, c.pixelHeight);
                        repaint();
        }
        });
        try{ Thread.sleep(delay);   }
        catch( Exception e)                 {}
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
        drawAll();
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

                drawCell(Color.BLACK, left,   arr[left], animationSpeed);
                drawCell(Color.BLACK, right, arr[right], animationSpeed);
                
                left++;
                right--;
            }
        }

        return left;                    // return partition index
    }

    public void mergeSort() {
        mergeSort(cols, 0, cols.length - 1);
        drawAll();
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
            drawCell(Color.BLACK, k-1, arr[k-1], animationSpeed); 
        }

        while(i < leftArr.length )   arr[k++] =  leftArr[i++];
        while(j < rightArr.length)   arr[k++] = rightArr[j++];
    }

    public void bubbleSort() {

        for(int i = 0; i < cols.length - 1;  i++) {
            for(int j = cols.length - 1; j > i; j--) {
                if(cols[j].val < cols[j - 1].val) {
               
                    drawCell(Color.RED, j, cols[j], animationSpeed);

                    Cell temp = cols[j];   // swap the two elements
                    cols[j] = cols[j - 1];
                    cols[j - 1] = temp;
                    drawCell(Color.BLACK, j, cols[j], 0);
                    drawCell(Color.BLACK, j - 1, cols[j - 1], 0);
                }
            }
        }

        drawAll();
    }

    public void insertSort() {
         for(int i = 0; i < cols.length - 1;  i++) {
            for(int j = i + 1; j > 0; j--) {
                while( j > 0 && cols[j].val < cols[j - 1].val ) {
                    drawCell(Color.RED, j, cols[j], animationSpeed);
                    
                    Cell temp = cols[j];   // swap the two elements
                    cols[j] = cols[j - 1];
                    cols[j - 1] = temp;
                    j--;

                    drawCell(Color.BLACK, j, cols[j], 0);
                    drawCell(Color.BLACK, j + 1, cols[j + 1], 0);
                }
            }
        }

        drawAll();
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
                drawCell(Color.RED, j, cols[j], animationSpeed);
                drawCell(Color.BLACK, j, cols[j], 0);
            }
                    // drawCell(Color.RED, index, cols[index], animationSpeed);

                    Cell temp = cols[i];   // swap the two elements
                    cols[i] = cols[index];
                    cols[index] = temp;

                    drawCell(Color.BLACK, index, cols[index], 0);
                    drawCell(Color.BLACK, i, cols[i], 0);
        }

        drawAll();
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

