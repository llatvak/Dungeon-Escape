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
 * @author Lauri Latva-Kyyny
 * @author  Miko Kauhanen
 * @version 1.0
 */
public class DungeonEscape extends Game {
    /**
     *
     */
    public final float screenWidth = 360f;
    /**
     *
     */
    public final float screenHeight = 640f;

    /**
     *
     */
    public final float gameWidth = 360f / 100f;
    /**
     *
     */
    public final float gameHeight = 640f / 100f;

    // Change this to enable testing features like skipping traps and reseting game saves
    /**
     *
     */
    public static boolean testing = true;
    /**
     *
     */
    public static boolean tutorials = true;
    /**
     *
     */
    public static boolean story = true;
    /**
     *
     */
    private boolean jumpTutorials = false;
    /**
     *
     */
    private boolean squatTutorials = false;

    /**
     *
     */
    public SpriteBatch batch;

    /**
     *
     */
    private OrthographicCamera screenCamera;
    /**
     *
     */
    private OrthographicCamera gameCamera;

    /**
     *
     */
    private MapScreen mapScreen;

    private boolean mapScreenStatus;
    private boolean moveScreenStatus;

    /**
     *
     */
    private int previousScreen;
    /**
     *
     */
    private int activeScreen;
    /**
     *
     */
    private final static int SPLASHSCREEN = 0;
    /**
     *
     */
    public final static int MAINMENU = 1;
    /**
     *
     */
    public final static int SETTINGSSCREEN = 2;
    /**
     *
     */
    public final static int MAPSCREEN = 3;
    /**
     *
     */
    public final static int JUMPSCREEN = 4;
    /**
     *
     */
    public final static int BACK = 5;
    /**
     *
     */
    public final static int SQUATSCREEN = 6;

    /**
     *
     */
    private I18NBundle myBundle;
    /**
     *
     */
    private Viewport gameViewport;


    /**
     *
     */
    private int stepTotal; // renderissä
    private int oldStepTotal;

    /**
     *
     */
    public DungeonEscape() {
        // :D
    }

    @Override
    public void create () {
        System.out.println(Gdx.graphics.getWidth() +" x "+ Gdx.graphics.getHeight());

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

        changeScreen(MAINMENU);
    }

    /**
     *
     */
    private void createCameras() {
        screenCamera = new OrthographicCamera();
        screenCamera.setToOrtho(false, screenWidth, screenHeight);

        gameCamera = new OrthographicCamera();
        gameCamera.setToOrtho(false, gameWidth, gameHeight);
    }

    /**
     * @return
     */
    public Viewport getGameViewport() {
        return gameViewport;
    }

    /**
     * @return
     */
    public OrthographicCamera getScreenCamera() {
        return this.screenCamera;
    }

    /**
     * @return
     */
    public OrthographicCamera getGameCamera() {
        return this.gameCamera;
    }

    /**
     * @param s1
     * @param s2
     * @param s3
     */
    public void setLanguage(String s1, String s2, String s3) {
        Locale locale = new Locale(s1, s2);
        myBundle = I18NBundle.createBundle(Gdx.files.internal(s3), locale, "UTF-8");
    }

    /**
     * @return
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
     * @param screen
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
     * @return
     */
    public int getPreviousScreen() { return previousScreen; }

    /**
     * @param screen
     */
    public void setPreviousScreen(int screen) {
        this.previousScreen = screen;
    }

    /**
     * @param status
     */
    public void setMoveScreenStatus(boolean status) {
        this.moveScreenStatus = status;
    }

    /**
     * @return
     */
    public SpriteBatch getBatch() {
        return batch;
    }

    /**
     * @return
     */
    public int getActiveScreen() {
        return activeScreen;
    }

    /**
     *
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
     * @return
     */
    public int getStepCount() {
        if(stepGetter!= null){
            stepTotal += stepGetter.getNumSteps();
        }
        return stepTotal;
    }

    /**
     * @return
     */
    public boolean isJumpTutorials() {
        return jumpTutorials;
    }

    /**
     * @param jumpTutorials
     */
    public void setJumpTutorials(boolean jumpTutorials) {
        this.jumpTutorials = jumpTutorials;
    }

    /**
     *
     */
    private GetSteps stepGetter;

    /**
     * @param sg
     */
    public void setGetSteps(GetSteps sg){
        stepGetter = sg;
    }

    // Not used
//    public void addSteps() {
//        // Is map screen open
//        if(mapScreenStatus) {
//            // Is movement screen open
//            if(!moveScreenStatus) {
//                mapScreen.addStep();
//            }
//        }
//    }

    /**
     * @return
     */
    public boolean isSquatTutorials() {
        return squatTutorials;
    }

    /**
     * @param squatTutorials
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