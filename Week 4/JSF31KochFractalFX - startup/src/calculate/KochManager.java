package calculate;

import jsf31kochfractalfx.JSF31KochFractalFX;
import timeutil.TimeStamp;

import java.util.*;

public class KochManager implements Observer {

    private JSF31KochFractalFX application;
    private KochFractal koch;
    private List<Edge> edges;
    private TimeStamp time;
    private ArrayList<String> calcTimes;

    public KochManager(JSF31KochFractalFX application) {
        this.application = application;
        koch = new KochFractal();
        koch.addObserver(this);
        edges = new LinkedList<>();
        time = new TimeStamp();
        calcTimes = new ArrayList<>();
    }

    @Override
    public void update(Observable o, Object arg) {
        edges.add((Edge)arg);
        application.setTextNrEdges(String.valueOf(koch.getNrOfEdges()));
    }


    public void changeLevel(int nxt) {
        koch.setLevel(nxt);

        edges.clear();

        time.setBegin("Edges are being generated..");

        koch.generateLeftEdge();
        koch.generateBottomEdge();
        koch.generateRightEdge();

        time.setEnd("Fractal generation done!");

        drawEdges();
    }
    public void drawEdges() {

        application.clearKochPanel();

        time.setBegin("Edges are being drawn..");
        for (Edge e:edges) {
            application.drawEdge(e);
        }
        time.setEnd("Fractal drawing done!");

        //application.setTextCalc(time.toString());
        calcTimes.add(time.toString()); //Add the string to the calcTimes array
        System.out.println(calcTimes.get(calcTimes.size() - 1)); //Log the full array
    }

}
