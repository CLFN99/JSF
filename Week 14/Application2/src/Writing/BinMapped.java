package Writing;

import calculate.Edge;
import calculate.Edge2;
import calculate.KochFractal;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class BinMapped extends Application implements Observer {
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
        try {
            level = Integer.parseInt(br.readLine());

        } catch (NumberFormatException nfe) {
            System.err.println("Invalid Format!");
        } catch (IOException e) {
            e.printStackTrace();
        }
        long startTime = System.currentTimeMillis();

        //generate edges
        if (level != 0) {
            fractal.setLevel(level);
            fractal.generateBottomEdge();
            fractal.generateLeftEdge();
            fractal.generateRightEdge();

            RandomAccessFile raFile;
            MappedByteBuffer buffer;
            try {
                if (edges.size() == fractal.getNrOfEdges()) {
                    //convert list to byte array
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(bos);
                    oos.writeObject(serializableEdges);
                    byte[] bytes = bos.toByteArray();

                    raFile = new RandomAccessFile(String.valueOf(level) + "rnd.bin", "rw");
                    FileChannel fc = raFile.getChannel();
                    buffer = fc.map(FileChannel.MapMode.READ_WRITE, 0 , bytes.length);

                    buffer.put(bytes);
                    raFile.close();
                    oos.close();

                    long endTime = System.currentTimeMillis();
                    long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
                    System.out.println(duration);
                }
            }
            catch(IOException ioe){
                System.out.println(ioe);
            }
        }
    }
}
