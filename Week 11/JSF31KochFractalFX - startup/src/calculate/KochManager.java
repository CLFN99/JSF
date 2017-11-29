package calculate;

import javafx.concurrent.Task;
import javafx.scene.paint.Color;
import jsf31kochfractalfx.JSF31KochFractalFX;
import threading.CalcTask;
import threading.KochCallable;
import threading.KochType;
import threading.ManagerRunnable;
import timeutil.TimeStamp;

import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class KochManager {

    private JSF31KochFractalFX application;
    private List<Edge> edges;
    private TimeStamp time;
    private List<String> calcTimes;
    private final ExecutorService pool = Executors.newFixedThreadPool(3);
    //private CalcTask task;

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

    public synchronized void changeLevel(int nxt) {
        edges.clear();

        time.setBegin("Edges are being generated..");
<<<<<<< HEAD
        CalcTask taskLeft = application.createTask(KochType.LEFT);
        CalcTask taskRight = application.createTask(KochType.RIGHT);
        CalcTask taskBottom = application.createTask(KochType.BOTTOM);
        pool.submit(taskLeft);
        pool.submit(taskRight);
        pool.submit(taskBottom);

        try {
//            for(Edge e : taskLeft.get()){
//                application.drawEdge(e, Color.WHITE);
//            }
            mergeEdgeList(taskLeft.get());
            mergeEdgeList(taskRight.get());
            mergeEdgeList(taskBottom.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        time.setEnd("Fractal generation done!");
        if(taskBottom.isDone() && taskLeft.isDone() && taskRight.isDone()){
            application.requestDrawEdges();
        }

=======
>>>>>>> 2e09ef31dc69d357b144c6603f7d5fbe8d44398f
    }

    public void drawEdges() {

        application.clearKochPanel();

        time.setBegin("Edges are being drawn..");

        for (Edge e:edges) {
        }

        time.setEnd("Fractal drawing done!");


        calcTimes.add(time.toString()); //Add the string to the calcTimes array

        //System.out.println(calcTimes.get(calcTimes.size() - 1)); //Log the full array

        System.out.println(calcTimes.get(calcTimes.size() - 1)); //Log the full array
        application.setTextCalc(calcTimes.get(calcTimes.size() - 1));
        time.init(); //Empty the internal time array
        calcTimes.clear();
    }

    public void terminate() {
        pool.shutdown();
    }
}
