package calculate;

        import javafx.scene.paint.Color;

        import java.io.Serializable;


public class Edge2 implements Serializable{
    private static final long serialVersionUID = -4982361154563378312L;

    public double X1, Y1, X2, Y2;
    public double hue;

    public Edge2(Edge e) {
        this.X1 = e.X1;
        this.Y1 = e.Y1;
        this.X2 = e.X2;
        this.Y2 = e.Y2;
        serializeColor(e.color);
    }

    public void serializeColor(Color color){
        hue = color.getHue();
    }

    public Color deserializeColor() {
        return Color.hsb(hue, 1, 1);
    }
}
