package threading;

import calculate.Edge;
import calculate.KochFractal;
import calculate.KochManager;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Callable;

public class KochCallable implements Callable, Observer {
    private KochType type = null;
    private KochFractal fractal;
    private List<Edge> edges;
    private Boolean done = false;
    private KochManager manager;

    public KochCallable(KochType type, KochManager manager, int level) {
        this.type = type;
        fractal = new KochFractal();
        fractal.addObserver(this);
        edges = new LinkedList<>();
        this.manager = manager;
        fractal.setLevel(level);
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

    @Override
    public Object call() throws Exception {
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
       // manager.setCount();
        return edges;
    }
}
