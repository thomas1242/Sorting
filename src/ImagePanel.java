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

    private Color highlight = new Color(255, 0, 0, 200);

    public ImagePanel(int width, int height) {
        setBounds(0, 0, width, height);
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2d = (Graphics2D)image.getGraphics();
        add(new ControlPanel(this), new Integer(3));

        animationSpeed = 50;
        numCells = (int)(image.getWidth() * 0.9) / 75;
        this.width = 75; // width of column 
        this.height = (int)(image.getHeight() - 2 * (int)(image.getWidth() * 0.05));    // column height

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
                drawCell(highlight, left,   arr[left], animationSpeed);
                drawCell(highlight, right, arr[right], animationSpeed);
                drawCell(arr[right].color, right, arr[right], 0);
                drawCell(arr[left].color, left,   arr[left], 0);
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
            drawCell(new Color(255, 0, 0, 200), k - 1, arr[k - 1], animationSpeed); 
            drawCell(arr[k - 1].color, k - 1, arr[k - 1], 0); 
        }

        drawCell(Color.BLACK, k, arr[k], 1);
        while(i < leftArr.length ) {
            arr[k++] =  leftArr[i++];
            drawCell(new Color(255, 0, 0, 200), k - 1, arr[k - 1], animationSpeed); 
            drawCell(arr[k - 1].color, k - 1, arr[k - 1], 0); 
        }
        while(j < rightArr.length) {  
            arr[k++] = rightArr[j++];
            drawCell(new Color(255, 0, 0, 200), k - 1, arr[k - 1], animationSpeed); 
            drawCell(arr[k - 1].color, k - 1, arr[k - 1], 0); 
        }
    }

    public void bubbleSort() {

        for(int i = 0; i < cols.length - 1;  i++) {
            for(int j = cols.length - 1; j > i; j--) {
                if(cols[j].val < cols[j - 1].val) {
                    drawCell(highlight, j, cols[j], animationSpeed);
                    swap(j, j - 1);
                    drawCell(cols[j].color, j, cols[j], 0);
                    drawCell(cols[j - 1].color, j - 1, cols[j - 1], 0);
                }
            }
        }

    }

    public void insertSort() {

         for(int i = 0; i < cols.length - 1;  i++) {
            for(int j = i + 1; j > 0; j--) {
                while( j > 0 && cols[j].val < cols[j - 1].val ) {
                    drawCell(highlight, j, cols[j], animationSpeed);
                    swap(j, j - 1);
                    j--;
                    drawCell(cols[j].color, j, cols[j], 0);
                    drawCell(cols[j + 1].color, j + 1, cols[j + 1], 0);
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
                drawCell(highlight, j, cols[j], animationSpeed);
                drawCell(cols[j].color, j, cols[j], 0);
            }
            swap(i, index);
            drawCell(cols[index].color, index, cols[index], 0);
            drawCell(cols[i].color, i, cols[i], 0);
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
                    drawCell(cols[i].color, i, cols[i]);    
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
                g2d.setColor(Color.BLACK);
                g2d.drawLine(x + index * width, y + (height - c.pixelHeight), x + index * width + width - 1, y + (height - c.pixelHeight));
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
        g2d.setColor( Color.BLACK );  
        g2d.drawLine(x + index * width, y + (this.height - c.pixelHeight), x + index * width + width - 1, y + (this.height - c.pixelHeight));
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


    public void randomizeData() {
        Random rand = new Random();
        cols = new Cell[numCells];
        for(int i = 0; i < cols.length; i++) {
            int val = rand.nextInt(2 * numCells + 1);
            cols[i] = new Cell(val, width, (int)(height * val / (2 * numCells + 1)));
        }

        Color[] colors = getColors( 0xffcccccc, 0x0fFFD700, numCells );

        Cell[] temp = new Cell[cols.length];
        for(int i = 0; i < cols.length; i++) 
            temp[i] = cols[i];                      // copy random data to temp arr

        for(int i = 0; i < temp.length - 1;  i++) { // sort temp arr 
            for(int j = i + 1; j > 0; j--) {
                while( j > 0 && temp[j].val < temp[j - 1].val ) {
                    Cell t = temp[j];
                    temp[j] = temp[j-1];
                    temp[j-1] = t;
                    j--;
                }
            }
        }

        for (int i = 0; i < temp.length; i++)       // assign iterpolated colors
            temp[i].color = colors[i];
    }

    public void animateRandomizeData() {
        new Thread( new Runnable() {
                        public void run() { 
                            int delay = 1000 / numCells > 0 ? 1000 / numCells : 1;
                            for(int i = 0; i < cols.length / 2; i++) {
                                drawCell(cols[i].color, i, cols[i], 0);
                                drawCell(cols[cols.length - 1 - i].color, cols.length - 1 - i, cols[cols.length - 1 - i], delay);
                            }
                        }
        } ).start(); 
    }

    public int getImageWidth() {
        return image.getWidth();
    }

    public void setDataSize(int pixelWidth) {
        numCells = (int)(image.getWidth() * 0.9) / pixelWidth;
        this.width = pixelWidth; // width of column 
    }

    public void setSpeed(int fps) {
        this.animationSpeed = fps;
    }    

    public int getNumCells() {
        return cols.length;
    }

    private static class Cell {
        int val;
        int pixelWidth, pixelHeight;
        Color color;
        int index;
        public Cell(int val, int pixelWidth, int pixelHeight) {
            this.val = val;
            this.pixelWidth = pixelWidth;
            this.pixelHeight = pixelHeight;
            color = Color.BLACK;
        }
    } 
}