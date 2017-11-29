package opdracht9;

import opdracht9.runnable.ProcessBuilderRunnable;
import timeutil.TimeStamp;

import java.io.*;

@SuppressWarnings("Duplicates")
public class Main9 {
    public static void main(String[] args) throws IOException {
        TimeStamp ts = new TimeStamp();

        System.out.println("Ayy");
        if (args.length < 1) {
            System.err.println("Usage: java OSProcess <command>");
            System.exit(0);
        }

        //Every two args are a single command; empty entries should be handled as an empty string

        for (int i = 0; i < args.length; i+=2) {
            //args[i] = command
            //args[i + 1] = command arg

            String[] pushedArgs = {args[i], args[i + 1]};
            ProcessBuilderRunnable pbr = new ProcessBuilderRunnable(pushedArgs);
            Thread thr = new Thread(pbr);
            thr.start();
        }



    }
}
