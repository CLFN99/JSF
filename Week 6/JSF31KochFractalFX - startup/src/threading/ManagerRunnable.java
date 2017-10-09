package threading;

import calculate.KochFractal;
import calculate.KochManager;
import jsf31kochfractalfx.JSF31KochFractalFX;

import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ManagerRunnable implements Runnable {

    private KochManager kochManager;
    private JSF31KochFractalFX kochFractalFX;
    private int level;

    public ManagerRunnable(KochManager kochManager, JSF31KochFractalFX kochFractalFX, int level){
        this.kochManager = kochManager;
        this.kochFractalFX = kochFractalFX;
        this.level = level;
    }

    @Override
    public void run() {
        ExecutorService pool = Executors.newFixedThreadPool(3);
        KochCallable edge1 = new KochCallable(KochType.LEFT, kochManager, level);
        KochCallable edge2 = new KochCallable(KochType.RIGHT, kochManager, level);
        KochCallable edge3 = new KochCallable(KochType.BOTTOM, kochManager, level);
        Future<List> futEdge1 = pool.submit(edge1);
        Future<List> futEdge2 = pool.submit(edge2);
        Future<List> futEdge3 = pool.submit(edge3);

        try {
            kochManager.mergeEdgeList(futEdge1.get());
            kochManager.mergeEdgeList(futEdge2.get());
            kochManager.mergeEdgeList(futEdge3.get());
        } catch (InterruptedException e) {
            e.printStackTrace();

        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        pool.shutdown();

        kochFractalFX.requestDrawEdges();
    }

}
