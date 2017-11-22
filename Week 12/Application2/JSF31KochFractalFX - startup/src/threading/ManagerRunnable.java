package threading;

import calculate.Edge;
import calculate.KochFractal;
import calculate.KochManager;
import javafx.concurrent.Task;
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
    private int level;
    private JSF31KochFractalFX application;

    public ManagerRunnable(KochManager kochManager, int level, JSF31KochFractalFX application){
        this.kochManager = kochManager;
        this.level = level;
        this.application = application;
    }

    @Override
    public void run() {
        ExecutorService pool = Executors.newFixedThreadPool(3);
//        KochCallable edge1 = new KochCallable(KochType.LEFT, kochManager, level);
//        KochCallable edge2 = new KochCallable(KochType.RIGHT, kochManager, level);
//        KochCallable edge3 = new KochCallable(KochType.BOTTOM, kochManager, level);
//        Future<List> futEdge1 = pool.submit(edge1);
//        Future<List> futEdge2 = pool.submit(edge2);
//        Future<List> futEdge3 = pool.submit(edge3);
        //Task taskLeft = application.createTask(KochType.LEFT);
        Task taskRight = application.createTask(KochType.RIGHT);
        Task taskBottom = application.createTask(KochType.BOTTOM);
        //pool.submit(taskLeft);
        pool.submit(taskRight);
        pool.submit(taskBottom);

        try {
           // kochManager.mergeEdgeList((List<Edge>)taskLeft.get());
            kochManager.mergeEdgeList((List<Edge>)taskRight.get());
            kochManager.mergeEdgeList((List<Edge>)taskBottom.get());
        } catch (InterruptedException e) {
            e.printStackTrace();

        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        pool.shutdown();
    }

}
