package threading;

import calculate.Edge;
import calculate.KochFractal;
import calculate.KochManager;
import javafx.concurrent.Task;
import java.util.List;

public class  CalcTask extends Task<List<Edge>> {

    private KochType type = null;
    private KochFractal fractal;
    private double MAX;

    public CalcTask(KochType type, KochManager manager, int level){
        this.type = type;
        fractal = new KochFractal();
        fractal.setLevel(level);

        //calculates the amnt of edges for this side
        MAX = Math.pow(4, level);
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
        return null;
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
