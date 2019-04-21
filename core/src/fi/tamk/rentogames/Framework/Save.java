package fi.tamk.rentogames.Framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Class used for saving information in game.
 *
 * This class is used for game info saving and fetching that specific information
 * from saved preferences using key pairs.
 *
 * @author Lauri Latva-Kyyny
 * @author  Miko Kauhanen
 * @version 1.0
 */
public class Save {

    /**
     * Preferences used for saving using key pairs.
     */
    private static Preferences prefs;

    /**
     * Saves received int to saved preferences where int is used to count the amount of movement points.
     *
     * @param movementPoints current amount of movement points needed to be saved
     */
    public static void saveMovementPoints(int movementPoints) {
        prefs = Gdx.app.getPreferences("dungeonescapegame");
        prefs.putInteger("movementpoints", movementPoints);
        prefs.flush();
    }

    /**
     * Returns movement point amount from preferences using key pairs.
     *
     * @return movement point amount from saved preferences
     */
    public static int getMovementPoints() {
        prefs = Gdx.app.getPreferences("dungeonescapegame");
        return prefs.getInteger("movementpoints", 40);
    }

    /**
     * Saves received int to saved preferences where int is used to get current level in game.
     *
     * @param level current level in game
     */
    public static void saveCurrentLevel(int level) {
        prefs = Gdx.app.getPreferences("dungeonescapegame");
        prefs.putInteger("currentlevel", level);
        prefs.flush();
    }

    /**
     * Returns current level from preferences using key pairs.
     *
     * @return int number which is used for getting current level
     */
    public static int getCurrentLevel() {
        prefs = Gdx.app.getPreferences("dungeonescapegame");
        return prefs.getInteger("currentlevel", 1);
    }

    /**
     * Saves received int to saved preferences where int is used to count the amount of steps.
     *
     * @param stepAmount current step amount needed to be saved
     */
    public static void saveCurrentSteps(int stepAmount) {
        prefs = Gdx.app.getPreferences("dungeonescapegame");
        prefs.putInteger("currentstepamount", stepAmount);
        prefs.flush();
    }


    /**
     * Returns step amount from preferences using key pairs.
     *
     * @return step amount from saved preferences
     */
    public static int getStepAmount() {
        prefs = Gdx.app.getPreferences("dungeonescapegame");
        return prefs.getInteger("currentstepamount", 0);
    }

    /**
     * Saves received string used for saving language in game.
     *
     * @param languagePrefs string which contains current language
     */
    public static void saveLanguage(String languagePrefs) {
        prefs = Gdx.app.getPreferences("dungeonescapegame");
        prefs.putString("language", languagePrefs);
        prefs.flush();
    }

    /**
     * Returns current language from preferences using key pairs.
     *
     * @return current language from saved preferences
     */
    public static String getLanguagePrefs() {
        prefs = Gdx.app.getPreferences("dungeonescapegame");
        return prefs.getString("language");
    }


    /**
     * Saves player's x position in map screen.
     *
     * @param x player's current x position
     */
    public static void saveCurrentPlayerX(float x) {
        prefs = Gdx.app.getPreferences("dungeonescapegame");
        prefs.putFloat("playerX", x);
        prefs.flush();
    }

    /**
     * Saves player's y position in map screen.
     *
     * @param y player's current y position
     */
    public static void saveCurrentPlayerY(float y) {
        prefs = Gdx.app.getPreferences("dungeonescapegame");
        prefs.putFloat("playerY", y);
        prefs.flush();
    }

    /**
     * Saves current audio level which is used for controlling audio level.
     *
     * @param audioLevel current audio level from 0 to 1
     */
    public static void saveAudioSettings(float audioLevel) {
        //Number 1 for max volume 0 for mute
        prefs = Gdx.app.getPreferences("dungeonescapegame");
        prefs.putFloat("audioSetting", audioLevel);
        prefs.flush();
    }

    /**
     * Returns current audio level from saved preferences.
     *
     * @return current audio level as float from 0 to 1
     */
    public static float getCurrentAudioSetting() {
        prefs = Gdx.app.getPreferences("dungeonescapegame");
        return prefs.getFloat("audioSetting", 1f);
    }
}