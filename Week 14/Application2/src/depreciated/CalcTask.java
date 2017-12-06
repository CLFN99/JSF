package depreciated;

import calculate.Edge;
import calculate.KochFractal;
import javafx.concurrent.Task;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class CalcTask extends Task<List<Edge>> implements Observer {

    private KochType type = null;
    private KochFractal fractal; //Moved to kochfractalFX, subscribe there fractal.addObserver(this);
    private double MAX;
    List<Edge> edges;

    public CalcTask(KochType type, KochFractal fractal, int level){
        this.type = type;
        //fractal = new KochFractal();
        fractal.setLevel(level);
        this.fractal = fractal;
        this.edges = new LinkedList<>();

        //calculates the amount of edges for this side. Inaccurate?
        MAX = Math.pow(4, level);
    }

    public KochType getType() {
        return this.type;
    }

    @Override
    protected List<Edge> call() throws Exception {
        switch (type) {
            case LEFT:
                fractal.generateLeftEdge();
                break;
            case RIGHT:
                fractal.generateRightEdge();
                break;
            case BOTTOM:
                fractal.generateBottomEdge();
                break;
            default:
                break;
        }
        return edges;
    }

    @Override
    protected void cancelled() {
        fractal.cancel();
        super.cancelled();
    }

    /***
     * Called if execution state is Worker.State FAILED
     * (see interface Worker<V>)
     */
    @Override
    protected void failed() {
        super.failed();
    }

    /**
     * *
     * Called if execution state is Worker.State RUNNING
     */
    @Override
    protected void running() {
        super.running();
    }

    /**
     * *
     * Called if execution state is Worker.State SCHEDULED
     */
    @Override
    protected void scheduled() {
        super.scheduled();
    }

    /**
     * *
     * Called if execution state is Worker.State SUCCEEDED
     */
    @Override
    protected void succeeded() {
        super.succeeded();
    }

    /***
     * Called if FutureTask behaviour is done
     */
    @Override
    protected void done() {
        super.done();
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
        edges.add((Edge) arg);
        updateProgress(edges.size(), MAX);
        updateMessage("Nr edges: " + edges.size());
    }
}
