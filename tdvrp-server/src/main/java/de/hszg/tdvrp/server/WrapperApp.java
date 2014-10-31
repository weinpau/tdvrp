package de.hszg.tdvrp.server;

import org.tanukisoftware.wrapper.WrapperListener;
import org.tanukisoftware.wrapper.WrapperManager;

/**
 *
 * @author weinpau
 */
public class WrapperApp implements WrapperListener {

    private final App app = new App();

    @Override
    public Integer start(String[] strings) {
        new java.lang.Thread(() -> {
            app.start();
        }
        ).start();

        return null;
    }

    @Override
    public int stop(int exitCode) {
        app.stop();
        return exitCode;

    }

    @Override
    public void controlEvent(int i) {
    }

    public static void main(String[] args) {
        WrapperManager.start(new WrapperApp(), args);
    }

}
