package calculate;

import jsf31kochfractalfx.JSF31KochFractalFX;

import java.util.Observable;
import java.util.Observer;

public class KochManager implements Observer {

    private JSF31KochFractalFX application;
    private KochFractal koch;

    public KochManager(JSF31KochFractalFX application) {
        this.application = application;
        koch = new KochFractal();
        koch.addObserver(this);
    }

    @Override
    public void update (Observable o, Object arg) {
        application.drawEdge((Edge)arg);
    }


    public void changeLevel(int nxt) {
        koch.setLevel(nxt);
        drawEdges();
    }
    public void drawEdges() {
        application.clearKochPanel();
        koch.generateLeftEdge();
        koch.generateBottomEdge();
        koch.generateRightEdge();
    }

}
