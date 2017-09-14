package opdracht5;

import timeutil.TimeStamp;

import java.io.*;

@SuppressWarnings("Duplicates")
public class Main5 {
    public static void main(String[] args) throws IOException {
        TimeStamp ts = new TimeStamp();

        System.out.println("Ayy");
        if (args.length != 1) {
            System.err.println("Usage: java OSProcess <command>");
            System.exit(0);
        }

        //===ProcessBuilder way===

        ts.setBegin("ProcessBuilder");
        // args[0] is the command
        ProcessBuilder pb = new ProcessBuilder(args[0]);
        ts.setEnd("pb instance created");
        Process proc = pb.start();
        ts.setEnd("pb process started");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ts.setEnd("pb process lived for 1000ms");
        proc.destroy();
        ts.setEnd("Done ProcessBuilder");

        //===Exec way===

        try {
            ts.setEndBegin("Beginning Runtime.exec()");
            // Execute the command
            Process p = Runtime.getRuntime().exec(args[0]);
            ts.setEnd("process started");
            Thread.sleep(1000);
            ts.setEnd("process lived for 1000ms");


            /*
            int exitVal = p.waitFor();
            System.out.println(exitVal);
            */

            // Obtain the input stream
            InputStream is = p.getInputStream();
            ts.setEnd("InputStream created");
            InputStreamReader isr = new InputStreamReader(is);
            ts.setEnd("InputStreamReader created");
            BufferedReader br = new BufferedReader(isr);
            ts.setEnd("BufferedReader created");

            // Read what is returned by the command
            String line;
            while ( (line = br.readLine()) != null) {
                System.out.println(line);
            }

            ts.setEnd("Spammed string variables");

            br.close();

            ts.setEnd("Closed reader");

            p.destroy();

            ts.setEnd("Destroyed process");

            System.out.print(ts.toString());

        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
