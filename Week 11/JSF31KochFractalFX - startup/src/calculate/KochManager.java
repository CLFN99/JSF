package calculate;

import jsf31kochfractalfx.JSF31KochFractalFX;
import threading.KochCallable;
import threading.KochType;
import threading.ManagerRunnable;
import timeutil.TimeStamp;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class KochManager {

    private JSF31KochFractalFX application;
    private List<Edge> edges;
    private TimeStamp time;
    private List<String> calcTimes;

    public KochManager(JSF31KochFractalFX application) {
        this.application = application;

        edges = new LinkedList<>();
        time = new TimeStamp();
        calcTimes = new ArrayList<>();
    }

    public synchronized void mergeEdgeList (List<Edge> e) {
        System.out.println("Pre merge: " + edges.size());
        edges.addAll(e);
        System.out.println("Post merge: " + edges.size());
    }

    public void changeLevel(int nxt) {

        edges.clear();

        time.setBegin("Edges are being generated..");
        ManagerRunnable mr = new ManagerRunnable(this, nxt);
        Thread calcThread = new Thread(mr);
        System.out.println(edges.size());
        calcThread.start();
        System.out.println(edges.size());

        time.setEnd("Fractal generation done!");
        application.requestDrawEdges();
    }

    public void drawEdges() {

        application.clearKochPanel();

        time.setBegin("Edges are being drawn..");

        for (Edge e:edges) {
            application.drawEdge(e);
        }
        time.setEnd("Fractal drawing done!");


        calcTimes.add(time.toString()); //Add the string to the calcTimes array

        //System.out.println(calcTimes.get(calcTimes.size() - 1)); //Log the full array

        System.out.println(calcTimes.get(calcTimes.size() - 1)); //Log the full array
        application.setTextCalc(calcTimes.get(calcTimes.size() - 1));
        time.init(); //Empty the internal time array
        calcTimes.clear();
    }

}
