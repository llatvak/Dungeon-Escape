package fi.tamk.rentogames.Framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public abstract class Save {

    public static Preferences prefs;

    public static void saveMovementPoints(int movementPoints) {
        prefs = Gdx.app.getPreferences("dungeonescapegame");
        prefs.putInteger("movementpoints", movementPoints);
        prefs.flush();
    }

    public static int getMovementPoints() {
        prefs = Gdx.app.getPreferences("dungeonescapegame");
        return prefs.getInteger("movementpoints", 0);
    }

    public static void saveCurrentLevel(int level) {
        prefs = Gdx.app.getPreferences("dungeonescapegame");
        prefs.putInteger("currentlevel", level);
        prefs.flush();
    }

    public static int getCurrentLevel() {
        prefs = Gdx.app.getPreferences("dungeonescapegame");
        return prefs.getInteger("currentlevel", 1);
    }

    public static void saveCurrentProgressbar(int progressbarvalue) {
        prefs = Gdx.app.getPreferences("dungeonescapegame");
        prefs.putInteger("currentprogressbar", progressbarvalue);
        prefs.flush();
    }

    public static int getProgressBarValue() {
        prefs = Gdx.app.getPreferences("dungeonescapegame");
        return prefs.getInteger("currentprogressbar", 0);
    }

    public static void saveLanguage(String languagePrefs) {
        prefs = Gdx.app.getPreferences("dungeonescapegame");
        prefs.putString("language", languagePrefs);
        prefs.flush();
    }

    public static String getLanguagePrefs() {
        prefs = Gdx.app.getPreferences("dungeonescapegame");
        return prefs.getString("language");
    }

    public static void saveCurrentPlayerX(float x) {
        prefs = Gdx.app.getPreferences("dungeonescapegame");
        prefs.putFloat("playerX", x);
        prefs.flush();
    }

    public static float getCurrentPlayerX() {
        prefs = Gdx.app.getPreferences("dungeonescapegame");
        return prefs.getFloat("playerX", 513f);
    }

    public static void saveCurrentPlayerY(float y) {
        prefs = Gdx.app.getPreferences("dungeonescapegame");
        prefs.putFloat("playerY", y);
        prefs.flush();
    }

    public static float getCurrentPlayerY() {
        prefs = Gdx.app.getPreferences("dungeonescapegame");
        return prefs.getFloat("playerY", 65f);
    }

}