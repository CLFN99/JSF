package calculate;

import jsf31kochfractalfx.JSF31KochFractalFX;
import threading.KochRunnable;
import threading.KochType;
import timeutil.TimeStamp;

import java.util.*;

public class KochManager implements Observer {

    private JSF31KochFractalFX application;
    private final KochFractal koch;
    private List<Edge> edges;
    private TimeStamp time;
    private ArrayList<String> calcTimes;
    private int count = 0;

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

    public synchronized void addEdge(Edge e) {
        edges.add(e);
    }

    public synchronized void mergeEdgeList (List<Edge> e) {
        System.out.println("Pre merge: " + edges.size());
        edges.addAll(e);
        System.out.println("Post merge: " + edges.size());
    }

    public void changeLevel(int nxt) {
        koch.setLevel(nxt);

        edges.clear();

        time.setBegin("Edges are being generated..");
        Thread edge1 = new Thread(new KochRunnable(KochType.LEFT, this)) {

        };
        Thread edge2 = new Thread(new KochRunnable(KochType.RIGHT, this)) {

        };
        Thread edge3 = new Thread(new KochRunnable(KochType.BOTTOM, this)) {

        };

        edge1.start();
        edge2.start();
        edge3.start();

        time.setEnd("Fractal generation done!");
        if(count == 3){
            application.requestDrawEdges();
            count = 0;
        }
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
        //System.out.println(calcTimes.get(calcTimes.size() - 1)); //Log the full array
    }

    public void setCount(){count ++;}
}
