import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import java.util.*;

public class ImagePanel extends JLayeredPane {

    private BufferedImage image;
    private Graphics2D g2d;
    private Cell[] cells;
    private int height, width, animationSpeed = 30;
    private Color minColor = new Color(0xffcccccc), maxColor = new Color(0x0fffd700), highlightColor = new Color(0xffc80000);

    public ImagePanel(int width, int height) {
        setBounds(0, 0, width, height);
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2d = (Graphics2D)image.getGraphics();
        this.height = image.getHeight();
        setDataWidth(12);
        addComponents();
        drawAll();
    }

    public void quickSort() {
        quickSort(cells, 0, cells.length - 1);
    }

    private void quickSort(Cell[] arr, int left, int right) {
        int partition = partition(arr, left, right);
        if (left < partition - 1)
            quickSort(arr, left, partition - 1);
        if (right > partition)
            quickSort(arr, partition, right);
    }

    private int partition(Cell[] arr, int left, int right) {
        int pivot = arr[ (left + right) / 2 ].val;
        while (left <= right) {
            while (arr[left].val < pivot)
                left++;
            while (arr[right].val > pivot)
                right--;
            if (left <= right) {
                swap(left, right);
                drawCell(highlightColor, left, arr[left], 0);
                drawCell(highlightColor, right, arr[right], animationSpeed);
                drawCell(arr[right].color, right, arr[right], 0);
                drawCell(arr[left].color, left, arr[left], 0);
                left++;
                right--;
            }
        }
        return left;
    }

    public void mergeSort() {
        mergeSort(cells, 0, cells.length - 1);
    }

    private void mergeSort(Cell[] arr, int left, int right) {
        if (left >= right)
            return;

        int middle = (left + right) / 2;

        mergeSort(arr, left, middle);
        mergeSort(arr, middle + 1, right);
        merge(arr, left, right, middle);
    }

    private void merge(Cell[] arr, int left, int right, int middle) {
        Cell[] leftArr  = new Cell[ middle - left + 1 ];
        Cell[] rightArr = new Cell[ right - middle ];

        for (int i = 0; i < leftArr.length;  i++) leftArr[i] = arr[left + i];
        for (int i = 0; i < rightArr.length; i++) rightArr[i] = arr[middle + i + 1];

        int i = 0, j = 0, k = left;
        while (i < leftArr.length && j < rightArr.length) {
            if (leftArr[i].val < rightArr[j].val)  arr[k++] =  leftArr[i++];
            else                                   arr[k++] = rightArr[j++];

            drawCell(highlightColor, k - 1, arr[k - 1], animationSpeed);
            drawCell(arr[k - 1].color, k - 1, arr[k - 1], 0);
        }

        while (i < leftArr.length) {
            arr[k++] = leftArr[i++];
            drawCell(highlightColor, k - 1, arr[k - 1], animationSpeed);
            drawCell(arr[k - 1].color, k - 1, arr[k - 1], 0);
        }
        while (j < rightArr.length) {
            arr[k++] = rightArr[j++];
            drawCell(highlightColor, k - 1, arr[k - 1], animationSpeed);
            drawCell(arr[k - 1].color, k - 1, arr[k - 1], 0);
        }
    }

    public void bubbleSort() {
        for (int i = 0; i < cells.length - 1; i++) {
            for (int j = cells.length - 1; j > i; j--) {
                if (cells[j].val < cells[j - 1].val) {
                    drawCell(highlightColor, j, cells[j], animationSpeed);
                    swap(j, j - 1);
                    drawCell(cells[j].color, j, cells[j], 0);
                    drawCell(cells[j - 1].color, j - 1, cells[j - 1], 0);
                }
            }
        }
    }

    public void insertSort() {
         for (int i = 0; i < cells.length - 1; i++) {
            for (int j = i + 1; j > 0; j--) {
                while (j > 0 && cells[j].val < cells[j - 1].val) {
                    drawCell(highlightColor, j, cells[j], animationSpeed);
                    swap(j, j - 1);
                    j--;
                    drawCell(cells[j].color, j, cells[j], 0);
                    drawCell(cells[j + 1].color, j + 1, cells[j + 1], 0);
                }
            }
        }
    }

    public void selectSort() {
        int index = 0;
        for (int i = 0; i < cells.length; i++) {
            int min = Integer.MAX_VALUE;
            for (int j = i; j < cells.length; j++) {
                if (cells[j].val < min) {
                    drawCell(cells[index].color, index, cells[index], 0);
                    index = j;
                    min = cells[j].val;
                }
                drawCell(highlightColor, j, cells[j], animationSpeed);
                drawCell(highlightColor, index, cells[index], 0);
                drawCell(cells[j].color, j, cells[j], 0);
            }
            swap(i, index);
            drawCell(cells[index].color, index, cells[index], 0);
            drawCell(cells[i].color, i, cells[i], 0);
        }
    }

    private void swap(int i, int j) {
        Cell temp = cells[i];
        cells[i] = cells[j];
        cells[j] = temp;
    }

    public void drawAll() {
        final int width = image.getWidth(), height = this.height;
        SwingUtilities.invokeLater( () -> {
            g2d.setColor(new Color(0xffabc123));         // draw background
            g2d.fillRect((int)(width * 0), (int)(width * 0) + 1, (int)(width * 1.0), height - 1);
            for (int i = cells.length - 1; i >= 0; i--)  // draw cells
                drawCell(cells[i].color, i, cells[i]);
            g2d.setColor(Color.BLACK);                   // draw border
            g2d.drawRect((int)(width * 0), (int)(width * 0), (int)(width * 1.0), height);
            repaint();
        });
    }

    public void drawCell(Color color, int index, Cell cell) {
        int x = 0;
        int y = 0;
        x += ((int)(image.getWidth() * 1.0) - cells.length * width) / 2;
        
        g2d.setColor(new Color(0xffabc123));
        g2d.fillRect(x + index * width, y + 1, width, height - 1);
        g2d.setColor(color);
        g2d.fillRect(x + index * width, y + height - cell.pixelHeight, width, cell.pixelHeight);
        g2d.setColor(Color.BLACK);
        g2d.drawLine(x + index * width, y + height - cell.pixelHeight, x + index * width + width - 1, y + height - cell.pixelHeight);
    }

    public void drawCell(Color color, int index, Cell cell, int delay) {
        final int width = this.width, height = this.height;
        SwingUtilities.invokeLater( () -> {
             drawCell(color, index, cell);
             repaint();
        });
        try{ Thread.sleep(delay); }
        catch(Exception e) {}
    }

    private void addComponents() {
        ControlPanel controlPanel = new ControlPanel(this);
        ColorChooser colorChooser = new ColorChooser(this);
        ColorDisplay popUp = new ColorDisplay(this, colorChooser);
        controlPanel.setColorChooser(colorChooser, popUp);
        colorChooser.setControlPanel(controlPanel, popUp);
        add(controlPanel, new Integer(3));
        add(colorChooser, new Integer(4));
        add(popUp, new Integer(4));
    }

    public void randomizeData() {
        Random rand = new Random();
        cells = new Cell[cells.length];
        for (int i = 0; i < cells.length; i++) {
            int randVal = rand.nextInt(2 * cells.length + 1);
            cells[i] = new Cell(randVal, (int)((height) * randVal / (2 * cells.length + 1)));
        }
        assignColors();
    }

    public void assignColors() {
        Cell[] temp = Arrays.copyOf(cells, cells.length);
        Arrays.sort(temp, (Cell a, Cell b) -> a.val - b.val);

        Color[] colors = Interpolation.getColors(minColor, maxColor, cells.length);
        for (int i = 0; i < temp.length; i++) 
            temp[i].color = colors[i];
    }

    public void animateRandomizeData() {
        new Thread( () -> {
            int delay = 1000 / cells.length > 0 ? 1000 / cells.length : 1;
            for (int i = 0; i < cells.length / 2; i++) {
                drawCell(cells[i].color, i, cells[i], 0);
                drawCell(cells[cells.length - 1 - i].color, cells.length - 1 - i, cells[cells.length - 1 - i], delay);
            }
        }).start();
    }

    public int getImageWidth() {
        return image.getWidth();
    }

    public void setDataWidth(int pixelWidth) {
        int numCells = (int)(image.getWidth() * 1.0) / pixelWidth;
        cells = new Cell[numCells];
        this.width = pixelWidth;  // column width
        randomizeData();
    }

    public void setSpeed(int fps) {
        this.animationSpeed = fps;
    }

    public int getNumCells() {
        return cells.length;
    }

    public void setColors(int startRGB, int endRGB) {
        minColor = new Color(startRGB);
        maxColor = new Color(endRGB);
        assignColors();
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.drawImage(image, 0, 0, null);
    }

    private static class Cell {
        int val;
        int pixelHeight;
        Color color = Color.BLACK;
        public Cell(int val, int pixelHeight) {
            this.val = val;
            this.pixelHeight = pixelHeight;
        }
    }
}