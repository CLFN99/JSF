package calculate;

import jsf31kochfractalfx.JSF31KochFractalFX;
import threading.KochCallable;
import threading.KochType;
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
    private int count = 0;

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
        ExecutorService pool = Executors.newFixedThreadPool(3);
        KochCallable edge1 = new KochCallable(KochType.LEFT, this, nxt);
        KochCallable edge2 = new KochCallable(KochType.RIGHT, this, nxt);
        KochCallable edge3 = new KochCallable(KochType.BOTTOM, this, nxt);
        Future<List> futEdge1 = pool.submit(edge1);
        Future<List> futEdge2 = pool.submit(edge2);
        Future<List> futEdge3 = pool.submit(edge3);

        try {
            mergeEdgeList(futEdge1.get());
            mergeEdgeList(futEdge2.get());
            mergeEdgeList(futEdge3.get());
            application.requestDrawEdges();
        } catch (InterruptedException e) {
            e.printStackTrace();

        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        pool.shutdown();
        time.setEnd("Fractal generation done!");

    }
    public void drawEdges() {

        application.clearKochPanel();

        time.setBegin("Edges are being drawn..");

        for (Edge e:edges) {
            application.drawEdge(e);
        }
        time.setEnd("Fractal drawing done!");


        calcTimes.add(time.toString()); //Add the string to the calcTimes array
        System.out.println(calcTimes.get(calcTimes.size() - 1)); //Log the full array
        application.setTextCalc(calcTimes.get(calcTimes.size() - 1));
        time.init(); //Empty the internal time array
        calcTimes.clear();
    }

    public synchronized void setCount(){
        count++;
        if(count >= 3){
            application.requestDrawEdges();
            count = 0;
        }
    }
}
