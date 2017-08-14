import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import java.util.Random;

public class ImagePanel extends JLayeredPane {

    private BufferedImage image = null; 
    private Graphics2D g2d = null;

    private ControlPanel controlPanel;
    private boolean isPaused;

    Cell[] cols;
    int numCells, height, width;  // in pixels
    int animationSpeed;

    public ImagePanel(int width, int height) {
        setBounds(0, 0, width, height);
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2d = (Graphics2D)image.getGraphics();

        g2d.setColor( new Color(0xff000000) );                  // draw outline
        g2d.drawRect((int)(image.getWidth() * 0.1), (int)(image.getHeight() * 0.1), (int)(image.getWidth() * 0.8), (int)(image.getHeight() * 0.8));

        addComponents();

        numCells = 25;
        this.width = (int)(image.getWidth() * 0.8) / numCells; // initially 25 cells
        this.height = (int)(image.getHeight() * 0.8);

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

        cols = new Cell[numCells];
        for(int i = 0; i < cols.length; i++) {
            int val = rand.nextInt(2 * numCells);
            cols[i] = new Cell(val, width, (int)(height * val / (2 * numCells)));
        }
        
    }

    public void drawAll() {
        
        g2d.setColor( new Color(0xffabc123) );                  // draw background
        g2d.fillRect((int)(image.getWidth() * 0.1), (int)(image.getHeight() * 0.1), (int)(image.getWidth() * 0.8), (int)(image.getHeight() * 0.8));
        g2d.setColor( Color.BLACK );                  // draw background
        g2d.drawRect((int)(image.getWidth() * 0.1), (int)(image.getHeight() * 0.1), (int)(image.getWidth() * 0.8), (int)(image.getHeight() * 0.8));


        int x = (int)(image.getWidth() * 0.1);
        int y = (int)(image.getHeight() * 0.1);

        for(int i = 0; i < cols.length; i++) 
            drawCell(Color.BLACK, i, cols[i]);    
        
        repaint();
    }

    public void drawCell(Color color, int index, Cell c) {

        int x = (int)(image.getWidth() * 0.1);
        int y = (int)(image.getHeight() * 0.1);

        x += ((int)(image.getWidth() * 0.8) - numCells * width) / 2;

        g2d.setColor(new Color(0xffabc123));
        g2d.fillRect(x + index * width, y, width, height);
        g2d.setColor(color);
        g2d.fillRect(x + index * width, y + ((int)(image.getHeight() * 0.8) - c.pixelHeight), width, c.pixelHeight);
    }

    public boolean paused() {
        return isPaused == false;
    }

    public int getImageWidth() {
        return image.getWidth();
    }

    protected void paintComponent(Graphics g) {
        g.drawImage(image, 0, 0, null);
    }

    public void setDataSize(int numCells) {
        this.numCells = numCells;
        this.width = (int)(image.getWidth() * 0.8) / numCells; // initially 25 cells
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

                drawCell(Color.BLACK, left, arr[left]);
                drawCell(Color.BLACK, right, arr[right]);
                repaint();

                try{
                    Thread.sleep(animationSpeed);   // small delay so plants smoothly grow
                }
                    catch( Exception e) {}

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

                drawCell(Color.BLACK, k - 1, arr[k - 1]);
                repaint();

                try{
                    Thread.sleep(animationSpeed);   // small delay so plants smoothly grow
                }
                    catch( Exception e) {}
        }

        while(i < leftArr.length )   arr[k++] =  leftArr[i++];
        while(j < rightArr.length)   arr[k++] = rightArr[j++];
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

