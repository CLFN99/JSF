import calculate.Edge;
import calculate.KochFractal;
import com.google.gson.Gson;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class BinNoBuffer extends Application implements Observer {
    private static List<Edge> edges = new ArrayList<Edge>();

    public static void main(String[] args) {


    }


    @Override
    public void update(Observable o, Object arg) {

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        KochFractal fractal = new KochFractal();
        fractal.addObserver(this)
        ;
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

        //generate edges
        if(level != 0){
            fractal.setLevel(level);
            fractal.generateBottomEdge();
            fractal.generateLeftEdge();
            fractal.generateRightEdge();
            try {
                FileOutputStream fos = new FileOutputStream(level + ".bin");
                ObjectOutputStream out = new ObjectOutputStream(fos);

                //out.write(edges);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }


    }
}
