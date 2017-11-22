import calculate.Edge;
import calculate.KochFractal;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TextNoBuffer {
private static List<Edge> edges = new ArrayList<Edge>();

    public static void main(String[] args) {
        KochFractal fractal = new KochFractal();
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
            edges.add(fractal.generateBottomEdge());
            edges.add(fractal.generateLeftEdge());
            edges.add(fractal.generateRightEdge());

            try {
                FileWriter writer = new FileWriter("");
                for(Edge e : edges){
                    writer.write(e.toString());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}
