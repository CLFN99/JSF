import calculate.Edge;
import calculate.KochFractal;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BinWithBuffer {

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
          fractal.generateBottomEdge();
            fractal.generateLeftEdge();
            fractal.generateRightEdge();


        }
    }


}
