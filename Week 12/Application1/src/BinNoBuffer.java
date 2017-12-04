import calculate.Edge;
import calculate.Edge2;
import calculate.KochFractal;
import com.google.gson.Gson;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;

public class BinNoBuffer extends Application implements Observer {
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
        KochFractal fractal = new KochFractal();
        fractal.addObserver(this);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int level = 0;
        System.out.print("Enter level:");
        try{
            level = Integer.parseInt(br.readLine());

        }catch(NumberFormatException nfe){
            System.err.println("Invalid Format!");
        }catch (IOException e) {
            e.printStackTrace();
        }

        long startTime = System.currentTimeMillis();

        //generate edges
        if(level != 0){
            fractal.setLevel(level);
            fractal.generateBottomEdge();
            fractal.generateLeftEdge();
            fractal.generateRightEdge();
            try {
                if (edges.size() == fractal.getNrOfEdges()) {
                    FileOutputStream fos = new FileOutputStream(level + ".bin");
                    ObjectOutputStream out = new ObjectOutputStream(fos);
                    out.writeObject(serializableEdges);
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
}
