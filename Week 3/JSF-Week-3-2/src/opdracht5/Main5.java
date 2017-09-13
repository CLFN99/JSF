package opdracht5;

import java.io.*;

public class Main5 {
    public static void main(String[] args) throws IOException {
        System.out.println("Ayy");
        if (args.length != 1) {
            System.err.println("Usage: java OSProcess <command>");
            System.exit(0);
        }

        //===ProcessBuilder way===

        /*
        // args[0] is the command
        ProcessBuilder pb = new ProcessBuilder(args[0]);
        Process proc = pb.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        proc.destroy();
        */

        //===Exec way===

        try {
            Process p = Runtime.getRuntime().exec(args[0]);
            Thread.sleep(1000);
            /*
            p.destroy();
            int exitVal = p.waitFor();
            System.out.println(exitVal);
            */

            // Obtain the input stream
            InputStream is = p.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            // Read what is returned by the command
            String line;
            while ( (line = br.readLine()) != null) {
                System.out.println(line);
            }

            br.close();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
