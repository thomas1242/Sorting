import java.awt.*;

public class Interpolation {

    private Interpolation() {}

    public static Color[] getColors(Color start, Color end, int length) {
        return getColors(start.getRGB(), end.getRGB(), length);
    }

    public static Color[] getColors(int start, int end, int length) {
        Color[] colors = new Color[length];

        colors[0] = new Color(start);
        colors[colors.length - 1] = new Color(end);
        double[] deltas = getDeltas( start, end, colors.length - 1 );

        double value_R = (start >> 16) & 0xFF;
        double value_G = (start >> 8 ) & 0xFF;
        double value_B = (start      ) & 0xFF;

        for (int i = 1; i < colors.length - 1; i++) {   // fill 1D array with interpolated colors
            value_R += deltas[0];
            value_G += deltas[1];
            value_B += deltas[2];
            int intARGB = (0xFF000000) | ((int)value_R << 16) | ((int)value_G << 8) | (int)value_B;
            colors[i] = new Color(intARGB);
        }

        return colors;
    }

    private static double[] getDeltas(int start, int end, int n) {
        double delta_R, delta_G, delta_B;

        delta_R = (((end >> 16) & 0xFF) - ((start >> 16) & 0xFF)) / 1.0 / n;      
        delta_G = (((end >> 8 ) & 0xFF) - ((start >> 8 ) & 0xFF)) / 1.0 / n;
        delta_B = (((end >> 0 ) & 0xFF) - ((start      ) & 0xFF)) / 1.0 / n;

        return new double[]{ delta_R, delta_G, delta_B };
    }

}