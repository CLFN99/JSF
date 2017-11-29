package threading;

import calculate.Edge;
import calculate.KochFractal;
import calculate.KochManager;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.paint.Color;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class  CalcTask extends Task<List<Edge>> implements Observer {

    private KochType type = null;
    private KochFractal fractal;
    private double MAX;
    private List<Edge> edges;

    public CalcTask(KochType type, KochManager manager, int level){
        this.type = type;
        edges = new LinkedList<>();
        fractal = new KochFractal();
        fractal.setLevel(level);
        fractal.addObserver(this);
        //calculates the amnt of edges for this side
        MAX = Math.pow(4, level);
    }

    @Override
    public void update(Observable o, Object arg) {
        //arg = the generated edge. This should be added to an array, which should be pushed back to the manager in a synchronized way
        edges.add((Edge)arg);
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
        for(int i = 0; i <= MAX; i++){
            updateProgress(i, MAX);
            updateMessage("Nr edges: " + i);
        }

        return edges;
    }

    @Override
    protected void cancelled() {
        System.out.println("Canceled " + type);
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
}
