package movingballsfx;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BallMonitor {
    private final Lock lock = new ReentrantLock();

    private int writersActive = 0;
    private int readersActive = 0;
    private int readersWaiting = 0;
    private int writersWaiting = 0;

    private Condition okToRead = lock.newCondition();
    private Condition okToWrite = lock.newCondition();

    public void enterReader() throws InterruptedException {
        lock.lock();
        try {
            while (writersActive != 0) {
                readersWaiting++;
                okToRead.await();
                readersWaiting--;
            }
            readersActive++;
        } finally {
            lock.unlock();
        }
    }

    public void exitReader() {
        lock.lock();
        try {
            readersActive--;
            if (readersActive == 0)
                okToWrite.signal();
        } finally {
            lock.unlock();
        }
    }

    public void enterWriter() throws InterruptedException {
        lock.lock();
        try {
            while (writersActive > 0 || readersActive > 0) {
                writersWaiting++;
                okToWrite.await();
                writersWaiting--;
            }
            writersActive++;
        } finally {
            lock.unlock();
        }
    }

    public void exitWriter() {
        lock.lock();
        try {
            writersActive--;
            if(writersWaiting > 0)
                okToWrite.signal();
            else okToRead.signalAll();
        }  finally {
            lock.unlock();
        }
    }

}
