import calculate.Edge;
import calculate.Edge2;
import calculate.KochFractal;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;

public class BinWithBuffer extends Application implements Observer {
    private List<Edge> edges = new LinkedList<>();
    private List<Edge2> serializableEdges = new LinkedList<>();

    public static void main(String[] args) {
        launch(args);

    }

    @Override
    public void update(Observable o, Object arg) {
        edges.add((Edge) arg);
        serializableEdges.add(new Edge2((Edge)arg));
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int level = -1;
        System.out.print("Enter level:");
        try{
            level = Integer.parseInt(br.readLine());
        }catch(NumberFormatException nfe){
            System.err.println("Invalid Format!");
        }catch (IOException e) {
            e.printStackTrace();
        }

        //generate edges
        if(level > 0){
            generateEdges(level);
        }
        if (level == 0) {
            System.out.println("Generating 10 levels");
            for (int i = 1; i < 11; i++){
                System.out.println("Generating level " + i);
                generateEdges(i);
            }
        }
    }

    private synchronized void generateEdges(int level) {
        KochFractal fractal = new KochFractal();
        fractal.addObserver(this);

        fractal.setLevel(level);
        fractal.generateBottomEdge();
        fractal.generateLeftEdge();
        fractal.generateRightEdge();
        long startTime = System.currentTimeMillis();

        try {
            if (edges.size() == fractal.getNrOfEdges()) {
                FileOutputStream fos = new FileOutputStream(level + ".bin");
                BufferedOutputStream buffer = new BufferedOutputStream(fos);
                ObjectOutputStream out = new ObjectOutputStream(buffer);
                out.writeObject(serializableEdges);
                out.close();
                long endTime = System.currentTimeMillis();
                long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
                System.out.println(duration);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
