package fi.tamk.rentogames.Framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * @author Lauri Latva-Kyyny
 * @author  Miko Kauhanen
 * @version 1.0
 */
public class Save {

    /**
     *
     */
    private static Preferences prefs;

    /**
     * @param movementPoints
     */
    public static void saveMovementPoints(int movementPoints) {
        prefs = Gdx.app.getPreferences("dungeonescapegame");
        prefs.putInteger("movementpoints", movementPoints);
        prefs.flush();
    }

    /**
     * @return
     */
    public static int getMovementPoints() {
        prefs = Gdx.app.getPreferences("dungeonescapegame");
        return prefs.getInteger("movementpoints", 0);
    }

    /**
     * @param level
     */
    public static void saveCurrentLevel(int level) {
        prefs = Gdx.app.getPreferences("dungeonescapegame");
        prefs.putInteger("currentlevel", level);
        prefs.flush();
    }

    /**
     * @return
     */
    public static int getCurrentLevel() {
        prefs = Gdx.app.getPreferences("dungeonescapegame");
        return prefs.getInteger("currentlevel", 1);
    }

    /**
     * @param progressbarvalue
     */
    public static void saveCurrentProgressbar(int progressbarvalue) {
        prefs = Gdx.app.getPreferences("dungeonescapegame");
        prefs.putInteger("currentprogressbar", progressbarvalue);
        prefs.flush();
    }

    /**
     * @return
     */
    public static int getProgressBarValue() {
        prefs = Gdx.app.getPreferences("dungeonescapegame");
        return prefs.getInteger("currentprogressbar", 0);
    }

    /**
     * @param languagePrefs
     */
    public static void saveLanguage(String languagePrefs) {
        prefs = Gdx.app.getPreferences("dungeonescapegame");
        prefs.putString("language", languagePrefs);
        prefs.flush();
    }

    /**
     * @return
     */
    public static String getLanguagePrefs() {
        prefs = Gdx.app.getPreferences("dungeonescapegame");
        return prefs.getString("language");
    }

    public static void saveCurrentPlayerX(float x) {
        prefs = Gdx.app.getPreferences("dungeonescapegame");
        prefs.putFloat("playerX", x);
        prefs.flush();
    }

    public static void saveCurrentPlayerY(float y) {
        prefs = Gdx.app.getPreferences("dungeonescapegame");
        prefs.putFloat("playerY", y);
        prefs.flush();
    }

    /**
     * @param audioLevel
     */
    public static void saveAudioSettings(float audioLevel) {
        //Number 1 for max volume 0 for mute
        prefs = Gdx.app.getPreferences("dungeonescapegame");
        prefs.putFloat("audioSetting", audioLevel);
        prefs.flush();
    }

    /**
     * @return
     */
    public static float getCurrentAudioSetting() {
        prefs = Gdx.app.getPreferences("dungeonescapegame");
        return prefs.getFloat("audioSetting", 1f);
    }
}