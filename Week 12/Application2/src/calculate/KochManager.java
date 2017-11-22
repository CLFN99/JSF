package calculate;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import depreciated.KochFractal;
import jsf31kochfractalfx.JSF31KochFractalFX;
import depreciated.CalcTask;
import depreciated.KochType;
import timeutil.TimeStamp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    public synchronized void mergeEdgeList (List<Edge> e) {
        System.out.println("Pre merge: " + edges.size());
        edges.addAll(e);
        System.out.println("Post merge: " + edges.size());
    }

    public synchronized void changeLevel(int nxt) {
        //Should try to load the file of the given level into the edges list.
        //edges = deserialize(nxt);
        edges = bufferedDezerialize(nxt);
        if (edges.size() > 0)
            drawEdges();
        else
            System.out.println("No edges found");
    }

    /**
     * Unbuffered deserialization
     * @param level The level that will be deserialized
     * @return Array of edges found
     */
    private List<Edge> deserialize(int level) {
        Gson gson = new Gson();
        List<Edge> data = new LinkedList<>();
        try {
            File edges = new File("/fractals/" + level + ".json"); //The json file that should be read
            Type listType = new TypeToken<LinkedList<Edge>>() {}.getType(); //The type of list that the json array is in
            JsonReader reader = new JsonReader(new FileReader(edges)); //The reader that should be used
            data = gson.fromJson(reader, listType); //The actual reading of the json file
        } catch (IOException iox) {
            iox.printStackTrace();
        }
        return data;
    }

    /**
     * Buffered deserialization
     * @param level The level that will be deserialized
     * @return Array of edges found
     */
    private List<Edge> bufferedDezerialize(int level) {
        Gson gson = new Gson();
        List<Edge> data = new LinkedList<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(
                    level + ".json"));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null)
                sb.append(line);

            Type listType = new TypeToken<Collection<Edge>>(){}.getType();
            data = gson.fromJson(sb.toString(), listType);
        } catch (IOException iox) {
            iox.printStackTrace();
        }
        return data;
    }

    public void drawEdges() {

        application.clearKochPanel();

        time.setBegin("Edges are being drawn...");

        for (Edge e:edges) {
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
