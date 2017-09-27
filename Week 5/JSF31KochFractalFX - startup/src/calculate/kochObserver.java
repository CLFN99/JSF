package calculate;

import java.util.Observable;
import java.util.Observer;

public class kochObserver implements Observer {
    @Override
    public void update(Observable o, Object arg) {
        KochFractal fractal = (KochFractal)o;
        Edge nieuweEdge = (Edge)arg;
        String melding = "Er is een nieuwe edge aan deze Koch Fractal toegvoegd. Coördinaten: %s, %s, %s, %s";
        System.out.format(melding, nieuweEdge.X1, nieuweEdge.X2, nieuweEdge.Y1, nieuweEdge.Y2 + "\n");
    }
}

