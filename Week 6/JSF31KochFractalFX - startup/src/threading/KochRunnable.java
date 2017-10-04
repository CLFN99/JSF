package threading;

import calculate.Edge;
import calculate.KochFractal;
import calculate.KochManager;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class KochRunnable implements Runnable, Observer {
    private KochType type = null;
    private KochFractal fractal;
    private List<Edge> edges;
    private Boolean done = false;
    private KochManager manager;

    public KochRunnable(KochType type, KochManager manager, int level) {
        this.type = type;
        fractal = new KochFractal();
        fractal.addObserver(this);
        edges = new LinkedList<>();
        this.manager = manager;
        fractal.setLevel(level);
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public  void run() {
        switch (type) {
            case LEFT:
                done = fractal.generateLeftEdge();
                break;
            case RIGHT:
                done = fractal.generateRightEdge();
                break;
            case BOTTOM:
                done = fractal.generateBottomEdge();
                break;
            default:
                System.out.println("ERR: Runnable did not get a type!");
                break;
        }
        System.out.println("Done generating, pushing list back to manager");
        manager.mergeEdgeList(this.edges);
        manager.setCount();
    }



    /**
     * This method is called whenever the observed object is changed. An
     * application calls an <tt>Observable</tt> object's
     * <code>notifyObservers</code> method to have all the object's
     * observers notified of the change.
     *
     * @param o   the observable object.
     * @param arg an argument passed to the <code>notifyObservers</code>
     */

    @Override
    public void update(Observable o, Object arg) {
        //arg = the generated edge. This should be added to an array, which should be pushed back to the manager in a synchronized way
        edges.add((Edge)arg);
    }
}
