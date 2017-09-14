package opdracht9.runnable;

import timeutil.TimeStamp;

import java.io.IOException;

public class ProcessBuilderRunnable implements Runnable {

    private TimeStamp ts;
    private String[] args;

    public ProcessBuilderRunnable(String[] args)  {
        ts = new TimeStamp();

        this.args = args;
    }

    @Override
    public void run() {
        //===ProcessBuilder way===

        try {
            ts.setBegin("ProcessBuilder");
            // args[0] is the command
            ProcessBuilder pb = new ProcessBuilder(args);
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

            System.out.print(ts.toString());
            System.out.println("Process " + args[0] + " with arg " + args[1] + " is terminated");
        } catch(IOException io) {
            io.printStackTrace();
        }
    }
}
