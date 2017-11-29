package calculate;

import jsf31kochfractalfx.JSF31KochFractalFX;
import threading.CalcTask;
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
        //Check level difference/tasks running and stop?
        edges.clear();

        time.setBegin("Edges are being generated..");
        pool.submit(createTask(KochType.LEFT));
        pool.submit(createTask(KochType.RIGHT));
        pool.submit(createTask(KochType.BOTTOM));

        time.setEnd("Fractal generation done!");
        application.requestDrawEdges();
    }

    public void drawEdges() {

        application.clearKochPanel();

        time.setBegin("Edges are being drawn...");

        for (Edge e:edges) {
            application.drawEdge(e, e.color);
        }

        time.setEnd("Fractal drawing done!");


        calcTimes.add(time.toString()); //Add the string to the calcTimes array

        //System.out.println(calcTimes.get(calcTimes.size() - 1)); //Log the full array

        System.out.println(calcTimes.get(calcTimes.size() - 1)); //Log the full array
        application.setTextCalc(calcTimes.get(calcTimes.size() - 1));
        time.init(); //Empty the internal time array
        calcTimes.clear();
    }

    /**
     * Creates a new task to be calculated.
     * @param type The side that has to be calculated.
     * @return The task object.
     */
    private CalcTask createTask(KochType type) {
//        if (task != null) {
//            task.cancel();
//            application.unbindProperties(task);
//        }

        KochFractal fractal = new KochFractal();
        CalcTask task = new CalcTask(type, fractal, application.getCurrentLevel());

        //Add listener that will fetch the result when the task is completed
        task.setOnSucceeded(e -> {
            System.out.println("Retrieved result from " + task.getType());
            //System.out.println(task.getValue());
            mergeEdgeList(task.getValue());
            application.unbindProperties(task);
            drawEdges();
        });

        //fractal.addObserver(this); //Depreciated? Replaced by the setOnSucceed event listener?
        fractal.addObserver(application); //Adding listener to allow for the white lines
        fractal.addObserver(task); //Adding task listener to allow the task to gather a list of generated edges

        application.bindProperties(task);

        return task;
    }

    public void terminate() {
        pool.shutdown();
    }
}
