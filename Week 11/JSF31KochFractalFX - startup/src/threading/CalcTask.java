package threading;

import calculate.Edge;
import calculate.KochFractal;
import calculate.KochManager;
import javafx.concurrent.Task;

import java.util.LinkedList;
import java.util.List;

public class  CalcTask extends Task<Edge> {

    private KochType type = null;
    private KochFractal fractal;
    private Edge edge;
    private Boolean done = false;

    public CalcTask(KochType type, KochManager manager, int level){
        this.type = type;
        fractal = new KochFractal();
        fractal.setLevel(level);
    }

    @Override
    protected Edge call() throws Exception {

        return null;
    }

    @Override
    protected void cancelled() {
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
