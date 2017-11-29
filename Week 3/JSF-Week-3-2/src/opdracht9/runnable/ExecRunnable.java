package opdracht9.runnable;

import timeutil.TimeStamp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@SuppressWarnings("Duplicates")
public class ExecRunnable implements Runnable {

    private TimeStamp ts;
    private String[] args;

    public ExecRunnable(String[] args)  {
        ts = new TimeStamp();

        this.args = args;
    }


    @Override
    public void run() {


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
        } catch (IOException io) {
            io.printStackTrace();
        }
    }
}
