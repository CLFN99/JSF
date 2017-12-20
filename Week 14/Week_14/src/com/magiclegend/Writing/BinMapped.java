package com.magiclegend.Writing;


import com.magiclegend.calculate.Edge;
import com.magiclegend.calculate.Edge2;
import com.magiclegend.calculate.KochFractal;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class BinMapped extends Application implements Observer {
    private List<Edge> edges = new LinkedList<>();
    private List<Edge2> serializableEdges = new LinkedList<>();
    private FileLock exclusiveLock = null;
    private int status = 0;

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
            FileLock headLock;
            FileLock edgeLock;
            try {
                if (edges.size() == fractal.getNrOfEdges()) {
                    //convert list to byte array
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(bos);
                    oos.writeObject(serializableEdges);
                    byte[] bytes = bos.toByteArray();

                    raFile = new RandomAccessFile("fractals/" + String.valueOf(level) + ".bin", "rw");
                    FileChannel fc = raFile.getChannel();
                    buffer = fc.map(FileChannel.MapMode.READ_WRITE, 0 , bytes.length);

                    //File structure:
                    //      0 .. 3 :        4 bytes int with level - Reader can find maxvalue with the formula (4^level * 3)
                    //      4 .. 7 :        4 bytes int with status - Current writing location; amount of edges that have been written
                    //      8 .. * :        4 bytes int with value - The edges themselves
                    //Edge structure:
                    //      0 .. 7 :        8 bytes long with X1
                    //      8 .. 15 :       8 bytes long with Y1
                    //      16 .. 23 :      8 bytes long with X2
                    //      24 .. 31 :      8 bytes long with Y2
                    //      32 .. 39 :      8 bytes long with hue
                    //      --- 40 bytes per edge ---

                    //      0 .. 7 :        Header
                    //      8 .. 39 :       Edge 1
                    //      40 .. 79 :      Edge 2
                    //      80 .. 119 :     Edge 3
                    //      120 .. 159 :    Edge 4
                    //      160 .. 199 :    Edge 5


                    status = 0;
                    //buffer.putInt(0, 2);
                    //buffer.putInt(4, status);
                    edgeLock = fc.lock(8, 10, false);
                    buffer.putDouble(serializableEdges.get(0).X1);
                    buffer.putDouble(serializableEdges.get(0).Y1);
                    buffer.putDouble(serializableEdges.get(0).X2);
                    buffer.putDouble(serializableEdges.get(0).Y2);
                    buffer.putDouble(serializableEdges.get(0).hue);

                    headLock = fc.lock(4,4, false);
                    buffer.putInt(0, level);
                    buffer.putInt(4, status);
                    edgeLock.release();
                    headLock.release();

//                    for(Edge2 e : serializableEdges){
//                        edgeLock = fc.lock(8, 10, false);
//                        buffer.putDouble(e.X1);
//                        buffer.putDouble(e.Y1);
//                        buffer.putDouble(e.X2);
//                        buffer.putDouble(e.Y2);
//                        buffer.putDouble(e.hue);
//
//                        headLock = fc.lock(4,4, false);
//                        buffer.putInt(0, level);
//                        buffer.putInt(4, status);
//                        edgeLock.release();
//                        headLock.release();
//
//                        status++;
//                    }
                    bos.close();
                    raFile.close();
                    oos.close();

                    System.exit(0);
                }
            }
            catch(IOException ioe){
                System.out.println(ioe);
            }
        }
    }

    private void updateStatus(){

    }
}
