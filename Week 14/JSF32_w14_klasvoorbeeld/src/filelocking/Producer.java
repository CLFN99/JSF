package filelocking;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.*;
import java.util.Random;

/**
 * Produceert waarden die weg worden geschreven naar een file die fungeert
 * als een 1-plaats buffer
 * 
 * Er wordt exclusive file locking toegepast
 *
 * Layout bestand: 
 * Bytes 0 .. 3 :    4 bytes int with maxvalue  (De maximale waarde die geproduceerd zal worden)
 * Bytes 4 .. 7 :    4 bytes int with status    (1 betekent: nog niet gelezen door consumer)
 * Bytes 8 .. 11:    4 bytes int with value     (De waarde die geproduceerd is)
 *
 * @author marcel
 */
public class Producer {

    private static final String BUFFERFILE = "buffer.bin";
    private static final boolean EXCLUSIVE = false;
    private static final boolean SHARED = true;
    private static final int MAXVAL = 400; // We schrijven 400 waarden weg
    private static final int NBYTES = 12;

    private static final int STATUS_NOT_READ = 1;
    private static final int STATUS_READ = 0;
    
    public static void main(String arsg[]) throws IOException, InterruptedException {

        Random r = new Random();
        FileLock exclusiveLock = null;
        try {
            RandomAccessFile raf = new RandomAccessFile(BUFFERFILE, "rw");
            FileChannel ch = raf.getChannel();
            MappedByteBuffer out = ch.map(FileChannel.MapMode.READ_WRITE, 0, NBYTES);

            int newValue = 0; // de waarde die we naar het bestand gaan schrijven
            while (newValue <= MAXVAL) {
                // Probeer het lock te verkrijgen
                exclusiveLock = ch.lock(0, NBYTES, EXCLUSIVE);

                // layout:
                //      0 .. 3 :    4 bytes int with maxvalue
                //      4 .. 7 :    4 bytes int with status
                //      8 .. 11:    4 bytes int with value

                // Vraag waarde van status op
                out.position(4);
                int status = out.getInt();

                /**
                 * Voorkom dat je voorbij de status gaat met het lezen!
                 * Maxvalue is hier dus hoe maximale hoeveelheid edges; moet even uitgerekend worden.
                 * Er moet eenmalig uit de header gehaald worden om welk level dat het precies gaat; en berekend worden hoe veel edges er in totaal zullen zijn.
                 */

                if (((status < maxValue) || (newValue == 0))) {
                    // Ga naar de byte waar voor het laatst is geschreven; uit te rekenen met het gegeven dat een edge 40 bytes is en met welke edge je was gebleven met schrijven (weet de loop ook nog? even uitproberen)
                    out.position(0);
                    // Schrijf de 5 doubles van één edge weg
                    out.putDouble(1);
                    out.putDouble(2);
                    out.putDouble(3);
                    out.putDouble(4);
                    out.putDouble(5);

                    // Schrijf weg op welke locatie je bent gebleven
                    out.position(4);
                    out.putInt(newValue); //Overschrijft in theorie; even controleren!
                    System.out.println("PRODUCER: " + newValue ); //Log ook even waar je bent gebleven; helpt ;)
                    // De volgende waarde die we uiteindelijk weg willen schrijven
                    newValue++;
                }

                Thread.sleep(r.nextInt(10));
                // release the lock
                exclusiveLock.release();

            }
        } catch (java.io.IOException ioe) {
            System.err.println(ioe);
        } finally {
            if (exclusiveLock != null) {
                exclusiveLock.release();
            }
        }
        System.out.println("PRODUCER: KLAAR" );
    }

}
