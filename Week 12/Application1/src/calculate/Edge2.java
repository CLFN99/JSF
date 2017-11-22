package calculate;

import javafx.scene.paint.Color;

import java.io.Serializable;


public class Edge2 implements Serializable{
    public double X1, Y1, X2, Y2;
    Edge e;

    public Edge2(Edge e) {
        this.X1 = e.X1;
        this.Y1 = e.Y1;
        this.X2 = e.X2;
        this.Y2 = e.Y2;
        serializeColor(e.color);
    }

    public void serializeColor(Color color){
        int red = (int) (color.getRed() * 0xFF);
        int blue = (int) (color.getBlue() * 0xFF) << 020;
        int green = (int) (color.getGreen() * 0xFF) << 010;

    }
}
