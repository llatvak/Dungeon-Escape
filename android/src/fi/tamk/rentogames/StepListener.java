package fi.tamk.rentogames;

/**
 * Interface to listen to step alerts.
 *
 * @author Anu S Pillai
 * @version 1.0
 * @see <a href="http://www.gadgetsaint.com/android/create-pedometer-step-counter-android/">http://www.gadgetsaint.com/android/create-pedometer-step-counter-android/</a>
 */
public interface StepListener {

    void step(long timeNs);

}
