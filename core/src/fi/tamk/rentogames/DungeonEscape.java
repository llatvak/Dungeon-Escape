package fi.tamk.rentogames;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Locale;

import fi.tamk.rentogames.Framework.GameAudio;
import fi.tamk.rentogames.Framework.GetSteps;
import fi.tamk.rentogames.Framework.Save;
import fi.tamk.rentogames.Screens.MainMenu;
import fi.tamk.rentogames.Screens.MapScreen;
import fi.tamk.rentogames.Screens.MoveScreenJump;
import fi.tamk.rentogames.Screens.MoveScreenSquat;
import fi.tamk.rentogames.Screens.SplashScreen;

/**
 * Main class to control all game information.
 *
 * <p>
 * Holds methods and variables accessed from almost every class.
 * Creates sprite batch, cameras and viewports for all classes to use.
 * Sets screen sizes in floats and meters.
 * Controls if tutorials and stories are on or off.
 * Controls screen switching and language in game, also loads all sound and music files.
 * </p>
 *
 * @author Lauri Latva-Kyyny
 * @author  Miko Kauhanen
 * @version 1.0
 */
public class DungeonEscape extends Game {
    /**
     * Screen width in game as floats.
     */
    public final float screenWidth = 360f;
    /**
     * Screen height in game as floats.
     */
    public final float screenHeight = 640f;

    /**
     * Screen width in game as meters.
     */
    public final float gameWidth = 360f / 100f;
    /**
     * Screen height in game as meters.
     */
    public final float gameHeight = 640f / 100f;

    // Change this to enable testing features like skipping traps and reseting game saves
    /**
     *
     */
    public static boolean testing = false;
    /**
     * Controls if tutorials are on or off with boolean value.
     */
    public static boolean tutorials = true;
    /**
     * Controls if stories are on or off with boolean value.
     */
    public static boolean story = true;
    /**
     * Controls if jump screen tutorials{@link fi.tamk.rentogames.Interface.MoveTutorials} are on.
     */
    private boolean jumpTutorials = false;
    /**
     * Controls if squat screen tutorials {@link fi.tamk.rentogames.Interface.MoveTutorials} are on.
     */
    private boolean squatTutorials = false;

    /**
     * Sprite batch for all classes to use, so only one gets created to save resources.
     */
    public SpriteBatch batch;

    /**
     * Screens camera with orthographic angle.
     */
    private OrthographicCamera screenCamera;
    /**
     * Game camera with orthographic angle.
     */
    private OrthographicCamera gameCamera;

    /**
     * Map screen {@link MapScreen} used in screen switching.
     */
    private MapScreen mapScreen;

    private boolean mapScreenStatus;
    private boolean moveScreenStatus;

    /**
     * Previous screen in int value.
     */
    private int previousScreen;
    /**
     * Currently active screen in in value.
     */
    private int activeScreen;
    /**
     * Splash screen for switch cases.
     */
    private final static int SPLASHSCREEN = 0;
    /**
     * Main menu screen for switch cases.
     */
    public final static int MAINMENU = 1;
    /**
     *
     */
    public final static int SETTINGSSCREEN = 2;
    /**
     * Map screen for switch cases
     */
    public final static int MAPSCREEN = 3;
    /**
     * Jump screen for switch cases.
     */
    public final static int JUMPSCREEN = 4;
    /**
     * Back button for switch cases.
     */
    public final static int BACK = 5;
    /**
     * Squat screen for switch cases.
     */
    public final static int SQUATSCREEN = 6;

    /**
     * Bundle to control language in game.
     */
    private I18NBundle myBundle;
    /**
     * Game's viewport for all classes to use.
     */
    private Viewport gameViewport;


    /**
     * Current step amount total player has walked in real life.
     */
    private int stepTotal; // renderissä
    private int oldStepTotal;

    public DungeonEscape() {
        // :D
    }

    @Override
    public void create () {
        setStartLanguage();

        createCameras();

        gameViewport = new FitViewport(screenWidth, screenHeight, screenCamera);

        batch = new SpriteBatch();

        //Load sounds
        GameAudio.loadSound("jumpsound.ogg", "jumpsound");
        GameAudio.loadSound("dooropensound.wav", "dooropensound");
        GameAudio.loadSound("walksoundnew.wav", "walksound");
        GameAudio.loadSound("runsound.wav", "runsound");
        GameAudio.loadSound("keysound.wav", "keysound");

        // Load music
        GameAudio.loadMusic("menumusic.ogg", "menumusic");
        GameAudio.loadMusic("mapscreenmusic.ogg", "mapscreenmusic");
        GameAudio.loadMusic("movescreenmusic.mp3", "movescreenmusic");

        Save.saveCurrentSteps(Save.getStepAmount());

        changeScreen(SPLASHSCREEN);
    }

    /**
     * Creates cameras for screen and game, screen uses floats game metrics.
     */
    private void createCameras() {
        screenCamera = new OrthographicCamera();
        screenCamera.setToOrtho(false, screenWidth, screenHeight);

        gameCamera = new OrthographicCamera();
        gameCamera.setToOrtho(false, gameWidth, gameHeight);
    }

    /**
     * Returns game's viewport.
     *
     * @return game's viewport that uses metrics
     */
    public Viewport getGameViewport() {
        return gameViewport;
    }

    /**
     * Returns screen's viewport.
     *
     * @return screen's viewport that uses floats.
     */
    public OrthographicCamera getScreenCamera() {
        return this.screenCamera;
    }

    /**
     * Returns game's camera.
     *
     * @return game's camera that uses metrics.
     */
    public OrthographicCamera getGameCamera() {
        return this.gameCamera;
    }

    /**
     * Sets language using string string received, default english.
     *
     * @param countryName receives a short version of the country as string
     * @param countryDialect receives a short version of the country's dialect as string
     * @param bundleName name of the bundle wanted to create as a string
     */
    public void setLanguage(String countryName, String countryDialect, String bundleName) {
        Locale locale = new Locale(countryName, countryDialect);
        myBundle = I18NBundle.createBundle(Gdx.files.internal(bundleName), locale, "UTF-8");
    }

    /**
     * Returns current bundle to switch language.
     *
     * @return bundle used when changing or setting language
     */
    public I18NBundle getMyBundle() {
        return myBundle;
    }

    @Override
    public void render () {
        super.render();
        batch.begin();
        batch.end();
    }

    /**
     * Switch case to control swapping between screens.
     *
     * @param screen value of the screen used in switch case to swap screens
     */
    public void changeScreen(int screen) {

        switch(screen) {
            case SPLASHSCREEN:
                activeScreen = SPLASHSCREEN;
                SplashScreen splashScreen = new SplashScreen(this);
                setScreen(splashScreen);
                break;

            case MAINMENU:
                activeScreen = MAINMENU;
                MainMenu mainMenu = new MainMenu(this);
                setScreen(mainMenu);
                break;
            case MAPSCREEN:
                activeScreen = MAPSCREEN;
                mapScreen = new MapScreen(this);
                mapScreenStatus = true;
                this.setScreen(mapScreen);
                break;

            case JUMPSCREEN:
                activeScreen = JUMPSCREEN;
                MoveScreenJump moveScreenJump = new MoveScreenJump(this, mapScreen.getMapScreen());
                moveScreenStatus = true;
                this.setScreen(moveScreenJump);
                break;

            case BACK:
                if(previousScreen == MAINMENU) {
                    activeScreen = MAINMENU;
                    mainMenu = new MainMenu(this);
                    this.setScreen(mainMenu);

                } else if (previousScreen == MAPSCREEN) {
                    activeScreen = MAPSCREEN;
                    mapScreen = mapScreen.getMapScreen();
                    this.setScreen(mapScreen);

                }
                break;

            case SQUATSCREEN:
                activeScreen = SQUATSCREEN;
                MoveScreenSquat moveScreenSquat = new MoveScreenSquat(this, mapScreen.getMapScreen());
                moveScreenStatus = true;
                this.setScreen(moveScreenSquat);
                break;
        }
    }

    /**
     * Returns the previous screen.
     *
     * @return previous screen as int value for switch case
     */
    public int getPreviousScreen() { return previousScreen; }

    /**
     * Sets the previous screen.
     *
     * @param screen int value for switch case to set the previous screen
     */
    public void setPreviousScreen(int screen) {
        this.previousScreen = screen;
    }

    /**
     * Sets the status of move screen to tell if move screen is on or off.
     *
     * @param status boolean value to set the status of the move screen
     */
    public void setMoveScreenStatus(boolean status) {
        this.moveScreenStatus = status;
    }

    /**
     * Returns the one batch used for all classes to save resources.
     *
     * @return sprite batch for all classes
     */
    public SpriteBatch getBatch() {
        return batch;
    }

    /**
     * Returns currently active screen.
     *
     * @return currently active screen as int value
     */
    public int getActiveScreen() {
        return activeScreen;
    }

    /**
     * Sets language of the game, default english but uses string values to check when to switch.
     */
    public void setStartLanguage() {
        if(Save.getLanguagePrefs().equals("MyBundle_fi_FI")) {
            setLanguage("fi", "FI", "MyBundle_fi_FI");
        } else if(Save.getLanguagePrefs().equals("MyBundle_en_US")) {
            setLanguage("en", "US", "MyBundle_en_US");
        } else {
            setLanguage("en", "US", "MyBundle_en_US");
        }
    }

    /**
     * Returns step count using interface to fetch values from service on android launcher.
     *
     * @return amount of steps received
     */
    public int getStepCount() {
        if(stepGetter!= null){
            stepTotal += stepGetter.getNumSteps();
        }
        return stepTotal;
    }

    /**
     * Are jump tutorials required to be on.
     *
     * @return boolean value to check when to do jump tutorials
     */
    public boolean isJumpTutorials() {
        return jumpTutorials;
    }

    /**
     * Sets jump screen tutorials.
     *
     * @param jumpTutorials boolean value to set jump screen tutorials on or off
     */
    public void setJumpTutorials(boolean jumpTutorials) {
        this.jumpTutorials = jumpTutorials;
    }

    /**
     * Interface variable to receive steps from service in android launcher.
     */
    private GetSteps stepGetter;

    /**
     * Sets the step getter interface received to count steps from service in android launcher.
     *
     * @param sg interface of step counting service
     */
    public void setGetSteps(GetSteps sg){
        stepGetter = sg;
    }

    /**
     * Method to check if squat screen tutorials are on or off.
     *
     * @return boolean value to check if squat screen tutorials are on or off.
     */
    public boolean isSquatTutorials() {
        return squatTutorials;
    }

    /**
     * Sets the state of squat screen tutorials.
     *
     * @param squatTutorials boolean value to set squat tutorials on or off.
     */
    public void setSquatTutorials(boolean squatTutorials) {
        this.squatTutorials = squatTutorials;
    }

    @Override
    public void dispose () {
        batch.dispose();
        GameAudio gameAudio = new GameAudio();
        gameAudio.dispose();
    }
}