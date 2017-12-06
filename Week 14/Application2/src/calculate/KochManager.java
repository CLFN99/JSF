package calculate;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import depreciated.KochFractal;
import jsf31kochfractalfx.JSF31KochFractalFX;
import depreciated.CalcTask;
import depreciated.KochType;
import timeutil.TimeStamp;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.nio.channels.FileChannel.MapMode.READ_ONLY;

public class KochManager {

    private JSF31KochFractalFX application;
    private List<Edge> edges;
    private TimeStamp time;
    private List<String> calcTimes;
    private final ExecutorService pool = Executors.newFixedThreadPool(3);
    //private CalcTask task;

    public KochManager(JSF31KochFractalFX application) {
        this.application = application;

        edges = new LinkedList<>();
        time = new TimeStamp();
        calcTimes = new ArrayList<>();
    }

    public synchronized void mergeEdgeList(List<Edge> e) {
        System.out.println("Pre merge: " + edges.size());
        edges.addAll(e);
        System.out.println("Post merge: " + edges.size());
    }

    public synchronized void changeLevel(int nxt) {
        //Should try to load the file of the given level into the edges list.
        System.out.println("---JSON Serialization---");
        System.out.println("---Unbuffered---");
        edges = deserialize(nxt);
        System.out.println("--Buffered---");
        edges = bufferedDezerialize(nxt);
        System.out.println("---Bin deserialization---");
        System.out.println("---Unbuffered---");
        edges = readEdgeStream(nxt);
        System.out.println("---Buffered---");
        edges = bufferedReadEdgeStream(nxt);
        System.out.println("---RandomAccessStream---");
        edges = bufferedRandomAccessFile(nxt);
        if (edges.size() > 0)
            drawEdges();
        else
            System.out.println("No edges found");
    }

    /**
     * Unbuffered deserialization
     *
     * @param level The level that will be deserialized
     * @return Array of edges found
     */
    private List<Edge> deserialize(int level) {
        Gson gson = new Gson();
        List<Edge> data = new LinkedList<>();
        try {
            File edges = new File("fractals/" + level + ".json"); //The json file that should be read
            Type listType = new TypeToken<LinkedList<Edge>>() {}.getType(); //The type of list that the json array is in
            JsonReader reader = new JsonReader(new FileReader(edges)); //The reader that should be used
            time.setBegin("Reading JSON");
            data = gson.fromJson(reader, listType); //The actual reading of the json file
            time.setEnd("Done reading and deserializing JSON");
            System.out.println(time.toString());
        } catch (IOException iox) {
            iox.printStackTrace();
        }
        return data;
    }

    /**
     * Buffered deserialization
     *
     * @param level The level that will be deserialized
     * @return Array of edges found
     */
    private List<Edge> bufferedDezerialize(int level) {
        Gson gson = new Gson();
        LinkedList<Edge> data = new LinkedList<>();
        try {
            System.out.println("Looking for " + level + ".json");
            BufferedReader bufferedReader = new BufferedReader(new FileReader("fractals/" + level + ".json"));
            String line;
            StringBuilder sb = new StringBuilder();
            time.setBegin("Buffered reading JSON");
            while ((line = bufferedReader.readLine()) != null)
                sb.append(line);
            time.setEndBegin("Done reading JSON, starting deserialization");
            Type listType = new TypeToken<LinkedList<Edge>>() {
            }.getType();
            //System.out.println(sb.toString());
            data = gson.fromJson(sb.toString(), listType);
            time.setEnd("Done deserializing");
            System.out.println(time.toString());
            time.init();
        } catch (IOException iox) {
            iox.printStackTrace();
        }
        return data;
    }

    /**
     * Magic that will read the bin files
     *
     * @return The list of edges inside the bin files
     */
    private LinkedList<Edge> readEdgeStream(int level) {
        LinkedList<Edge> returnvalue = null;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("fractals/" + level + ".bin"))) {
            time.setBegin("Reading bin");
            returnvalue = convertEdgeList((LinkedList<Edge2>) in.readObject());
            time.setEnd("Completed reading and converting bin");
            in.close();
            System.out.println(time.toString());
            time.init();
        } catch (IOException | ClassNotFoundException exception) {
            exception.printStackTrace();
        }
        return returnvalue;
    }

    /**
     * Magic that will read the bin files with a buffer
     *
     * @return The list of edges inside the bin files
     */
    private LinkedList<Edge> bufferedReadEdgeStream(int level) {
        LinkedList<Edge> returnvalue = null;
        try (ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream("fractals/" + level + ".bin")))) {
            time.setBegin("Buffered reading bin");
            returnvalue = convertEdgeList((LinkedList<Edge2>) in.readObject());
            time.setEnd("Completed reading and converting bin");
            in.close();
            System.out.println(time.toString());
            time.init();
        } catch (IOException | ClassNotFoundException exception) {
            exception.printStackTrace();
        }
        return returnvalue;
    }

    /**
     * Converts the custom edge objects (to allow for color serialization) back to Edge objects
     *
     * @return
     */
    private LinkedList<Edge> convertEdgeList(LinkedList<Edge2> edges) {
        LinkedList<Edge> returnvalue = new LinkedList<>();
        for (Edge2 e : edges) {
            returnvalue.add(new Edge(e.X1, e.Y1, e.X2, e.Y2, e.deserializeColor()));
        }
        return returnvalue;
    }

    //https://simplesassim.wordpress.com/2014/06/18/how-to-read-an-object-from-a-memory-mapped-file-with-oracle-jdk/
    private LinkedList<Edge> bufferedRandomAccessFile(int level) {
        long startTime = System.currentTimeMillis();
        try {
            final RandomAccessFile f = new RandomAccessFile("fractals/" + level + "rnd.bin", "r");
            final FileChannel fc = f.getChannel();
            final MappedByteBuffer map = fc.map(READ_ONLY, 0, f.length());

            final byte[] buf = new byte[(int)f.length()];

            map.get(buf);

            final ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(buf));
            time.setBegin("Reading randomaccessfile");
            LinkedList<Edge2> temp = (LinkedList<Edge2>) ois.readObject();
            time.setEnd("Done reading randomaccessfile");
            LinkedList<Edge> edges = convertEdgeList(temp);

            ois.close();
            f.close();
            return edges;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        finally{
            long endTime = System.currentTimeMillis();
            long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
            System.out.println(duration);
        }
        return null;
    }

    public void drawEdges() {

        application.clearKochPanel();

        time.setBegin("Edges are being drawn...");

        for (Edge e : edges) {
            application.drawEdge(e, e.color);
        }

        time.setEnd("Fractal drawing done!");


        calcTimes.add(time.toString()); //Add the string to the calcTimes array

        //System.out.println(calcTimes.get(calcTimes.size() - 1)); //Log the full array

        System.out.println(calcTimes.get(calcTimes.size() - 1)); //Log the full array
        application.setTextCalc(calcTimes.get(calcTimes.size() - 1));
        time.init(); //Empty the internal time array
        calcTimes.clear();
    }
}
