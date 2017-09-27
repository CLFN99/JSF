package calculate;

import jsf31kochfractalfx.JSF31KochFractalFX;
import timeutil.TimeStamp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class KochManager implements Observer {

    private JSF31KochFractalFX application;
    private KochFractal koch;
    private ArrayList<Edge> edges;
    private TimeStamp time;

    public KochManager(JSF31KochFractalFX application) {
        this.application = application;
        koch = new KochFractal();
        koch.addObserver(this);
        edges = new ArrayList<>();
        time = new TimeStamp();
    }

    @Override
    public void update (Observable o, Object arg) {
        edges.add((Edge)arg);
        for (Edge e:edges) {
            application.drawEdge((Edge) arg);
        }
        application.setTextNrEdges(String.valueOf(koch.getNrOfEdges()));
    }


    public void changeLevel(int nxt) {
        koch.setLevel(nxt);
        drawEdges();
    }
    public void drawEdges() {
        time.setBegin("Edges are being drawn..");
        application.clearKochPanel();

        koch.generateLeftEdge();
        koch.generateBottomEdge();
        koch.generateRightEdge();

        time.setEnd("Fractal done!");
        application.setTextCalc(time.toString());

    }

}
