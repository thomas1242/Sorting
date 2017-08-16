import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import java.util.Random;

public class ImagePanel extends JLayeredPane {

    private BufferedImage image = null; 
    private Graphics2D g2d = null;

    private Cell[] cols;
    private int numCells;
    private int height, width;  // in pixels
    private int animationSpeed;

    public ImagePanel(int width, int height) {
        setBounds(0, 0, width, height);
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2d = (Graphics2D)image.getGraphics();
        add(new ControlPanel(this), new Integer(3));

        animationSpeed = 50;
        numCells = 100;
        this.width = (int)(image.getWidth() * 0.9) / numCells; 
        this.height = (int)(image.getHeight() - 2 * (int)(image.getWidth() * 0.05));

        randomizeData();
        drawAll();
    }

    protected void paintComponent(Graphics g) {
        g.drawImage(image, 0, 0, null);
    }

    public void quickSort() {
        quickSort(cols, 0, cols.length - 1);
    }

    private void quickSort(Cell[] arr, int left, int right) {
        int partition = partition(arr, left, right);
        if(left < partition - 1)
            quickSort(arr, left, partition - 1);
        if(right > partition)
            quickSort(arr, partition, right);
    }

    private int partition(Cell[] arr, int left, int right) {
        int pivot = arr[ (left + right) / 2 ].val;
        while(left <= right) {
            while(arr[left].val < pivot)    // find leftside element greater than pivot
                left++;
            while(arr[right].val > pivot)   // find rightside element less than pivot
                right--;
            if(left <= right) {         
                swap(left, right);
                drawCell(Color.RED, left,   arr[left], animationSpeed);
                drawCell(Color.RED, right, arr[right], animationSpeed);
                drawCell(Color.BLACK, right, arr[right], 0);
                drawCell(Color.BLACK, left,   arr[left], 0);
                left++;
                right--;
            }
        }
        return left;                    // return partition index
    }

    public void mergeSort() {
        mergeSort(cols, 0, cols.length - 1);
    }

    private void mergeSort(Cell[] arr, int left, int right) {
        if(left >= right)
            return;

        int middle = (left + right) / 2;

        mergeSort(arr, left, middle);            // sort left half including middle element
        mergeSort(arr, middle + 1, right);       // sort right half
        merge(arr, left, right, middle);         // merge two sorted halfs together
    }

    private void merge(Cell[] arr, int left, int right, int middle) {
        Cell[] leftArr  = new Cell[ middle - left + 1 ];
        Cell[] rightArr = new Cell[ right - middle ];

        for(int i = 0; i < leftArr.length;  i++) leftArr[i] = arr[left + i];
        for(int i = 0; i < rightArr.length; i++) rightArr[i] = arr[middle + i + 1];

        int i = 0, j = 0, k = left;
        while(i < leftArr.length && j < rightArr.length) {
            if(leftArr[i].val < rightArr[j].val)  arr[k++] =  leftArr[i++];
            else                                  arr[k++] = rightArr[j++];
            drawCell(Color.RED, k - 1, arr[k - 1], animationSpeed); 
            drawCell(Color.BLACK, k - 1, arr[k - 1], 0); 
        }

        drawCell(Color.BLACK, k, arr[k], 1);
        while(i < leftArr.length ) {
            arr[k++] =  leftArr[i++];
            drawCell(Color.RED, k - 1, arr[k - 1], animationSpeed); 
            drawCell(Color.BLACK, k - 1, arr[k - 1], 0); 
        }
        while(j < rightArr.length) {  
            arr[k++] = rightArr[j++];
            drawCell(Color.RED, k - 1, arr[k - 1], animationSpeed); 
            drawCell(Color.BLACK, k - 1, arr[k - 1], 0); 
        }
    }

    public void bubbleSort() {

        for(int i = 0; i < cols.length - 1;  i++) {
            for(int j = cols.length - 1; j > i; j--) {
                if(cols[j].val < cols[j - 1].val) {
                    drawCell(Color.RED, j, cols[j], animationSpeed);
                    swap(j, j - 1);
                    drawCell(Color.BLACK, j, cols[j], 0);
                    drawCell(Color.BLACK, j - 1, cols[j - 1], 0);
                }
            }
        }

    }

    public void insertSort() {

         for(int i = 0; i < cols.length - 1;  i++) {
            for(int j = i + 1; j > 0; j--) {
                while( j > 0 && cols[j].val < cols[j - 1].val ) {
                    drawCell(Color.RED, j, cols[j], animationSpeed);
                    swap(j, j - 1);
                    j--;
                    drawCell(Color.BLACK, j, cols[j], 0);
                    drawCell(Color.BLACK, j + 1, cols[j + 1], 0);
                }
            }
        }

    }

    public void selectSort() {

        int index = 0;
        for(int i = 0; i < cols.length;  i++) {
            int min = Integer.MAX_VALUE;
            for(int j = i; j < cols.length;  j++) { // find min
                if(cols[j].val < min) {
                    index = j;
                    min = cols[j].val;
                }
                drawCell(Color.RED, j, cols[j], animationSpeed);
                drawCell(Color.BLACK, j, cols[j], 0);
            }
            swap(i, index);
            drawCell(Color.BLACK, index, cols[index], 0);
            drawCell(Color.BLACK, i, cols[i], 0);
        }

    }

    private void swap(int index1, int index2) {
        Cell temp = cols[index1];           // swap the two elements
        cols[index1] = cols[index2];
        cols[index2] = temp;
    }

    private void drawBackground(Color color) {
        g2d.setColor( color );        // draw background
        g2d.fillRect((int)(width * 0.05), (int)(width * 0.05) + 1, (int)(width * 0.9), height - 1);
    }

    private void drawBorder(Color color) {
        g2d.setColor( Color.BLACK );                  // draw border
        g2d.drawRect((int)(width * 0.05), (int)(width * 0.05), (int)(width * 0.9), height);
    }

    public void drawAll() {
        final int width = image.getWidth(), height = this.height;
        SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                g2d.setColor( new Color(0xffabc123) );        // draw background
                g2d.fillRect((int)(width * 0.05), (int)(width * 0.05) + 1, (int)(width * 0.9), height - 1);
                for(int i = cols.length - 1; i >= 0; i--)     
                    drawCell(Color.BLACK, i, cols[i]);    
                g2d.setColor( Color.BLACK );                  // draw border
                g2d.drawRect((int)(width * 0.05), (int)(width * 0.05), (int)(width * 0.9), height);
                repaint();
            }
        });
    }

    public void drawCell(Color color, int index, Cell c, int delay) {  
        final int width = this.width, height = this.height;
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
        catch( Exception e)        {}
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

    public void randomizeData() {
        Random rand = new Random();
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
                                drawCell(Color.BLACK, i, cols[i], 0);
                                drawCell(Color.BLACK, cols.length - 1 - i, cols[cols.length - 1 - i], 1);
                            }
                        }
        } ).start(); 
    }

    public int getImageWidth() {
        return image.getWidth();
    }

    public void setDataSize(int numCells) {
        this.numCells = numCells;
        this.width = (int)(image.getWidth() * 0.9) / numCells; 
    }

    public void setSpeed(int fps) {
        this.animationSpeed = fps;
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