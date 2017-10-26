import java.awt.*;

public class Interpolation {
    private Interpolation() {}

    public static Color[] getColors(int start, int end, int length) {
        Color[] colors = new Color[length];

        double value_R = (start >> 16) & 0xFF;
        double value_G = (start >> 8 ) & 0xFF;
        double value_B = (start      ) & 0xFF;

        double[] deltas = getDeltas( start, end, colors.length - 1 );
        colors[0] = new Color(start);
        colors[colors.length - 1] = new Color(end);

        for (int i = 1; i < colors.length - 1; i++) {   // fill 1D array with interpolated Colors
            value_R += deltas[0];
            value_G += deltas[1];
            value_B += deltas[2];                    
            int intARGB = (0xFF000000) | ((int)value_R << 16) | ((int)value_G << 8) | (int)value_B;
            colors[i] = new Color(intARGB);
        }

        return colors;
    }

    private static double[] getDeltas(int start, int end, int n) {
        double start_R, start_G, start_B,
                 end_R,   end_G,   end_B,
               delta_R, delta_G, delta_B;

        end_R = (end >> 16) & 0xFF;
        end_G = (end >> 8 ) & 0xFF;
        end_B = (end      ) & 0xFF;

        start_R = (start >> 16) & 0xFF;
        start_G = (start >> 8 ) & 0xFF;
        start_B = (start      ) & 0xFF;

        delta_R = (end_R - start_R) / n;        // color channel change per step
        delta_G = (end_G - start_G) / n;
        delta_B = (end_B - start_B) / n;

        double[] deltas = { delta_R, delta_G, delta_B };
        return deltas;
    }
}