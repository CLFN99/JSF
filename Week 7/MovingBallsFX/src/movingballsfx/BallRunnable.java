package movingballsfx;

import javafx.scene.paint.Color;

/**
 *
 * @author Peter Boots
 */
public class BallRunnable implements Runnable {

    private Ball ball;
    private BallMonitor bm;
    private boolean inCS = false;

    public BallRunnable(Ball ball, BallMonitor bm) {
        this.ball = ball;
        this.bm = bm;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                //Am I entering or leaving? Enter > lock; leaving: notify
                //Reader: RED; Writer: BLUE

                if (ball.getColor() == Color.RED) {
                    if (ball.isEnteringCs()) {
                        System.out.println("Reader entering");
                        bm.enterReader();
                        inCS = true;
                    } else if (ball.isLeavingCs()) {
                        System.out.println("Reader exiting");
                        bm.exitReader();
                        inCS = false;
                    }
                } else if (ball.getColor() == Color.BLUE) {
                    if (ball.isEnteringCs()) {
                        System.out.println("Writer entering");
                        bm.enterWriter();
                        inCS = true;
                    } else if (ball.isLeavingCs()) {
                        System.out.println("Writer exiting");
                        bm.exitWriter();
                        inCS = false;
                    }
                }

                ball.move();
                   
                Thread.sleep(ball.getSpeed());
                
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }

        if (Thread.currentThread().isInterrupted() && inCS) {
            if (ball.getColor() == Color.RED) {
                bm.exitReader();
            } else {
                bm.exitWriter();
            }
        }
    }
}
