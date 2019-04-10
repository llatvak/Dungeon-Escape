package fi.tamk.rentogames;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Locale;

import fi.tamk.rentogames.Framework.GetSteps;
import fi.tamk.rentogames.Framework.Save;
import fi.tamk.rentogames.Screens.MainMenu;
import fi.tamk.rentogames.Screens.MapScreen;
import fi.tamk.rentogames.Screens.MoveScreenJump;
import fi.tamk.rentogames.Screens.MoveScreenSquat;
import fi.tamk.rentogames.Screens.SettingsScreen;
import fi.tamk.rentogames.Screens.SplashScreen;

public class DungeonEscape extends Game {
    public final float screenWidth = 360f;
    public final float screenHeight = 640f;

    public final float gameWidth = 360f / 100f;
    public final float gameHeight = 640f / 100f;

    // Change this to enable testing features like skipping traps and reseting game saves
    public static boolean testing = true;
    public static boolean tutorials = false;
    public static boolean story = false;

    public SpriteBatch batch;

    private OrthographicCamera screenCamera;
    private OrthographicCamera gameCamera;

    private MapScreen mapScreen;

    private boolean mapScreenStatus;
    private boolean moveScreenStatus;

    private int previousScreen;
    private int activeScreen;
    private final static int SPLASHSCREEN = 0;
    public final static int MAINMENU = 1;
    public final static int SETTINGSSCREEN = 2;
    public final static int MAPSCREEN = 3;
    public final static int JUMPSCREEN = 4;
    public final static int BACK = 5;
    public final static int SQUATSCREEN = 6;

    private I18NBundle myBundle;
    private Viewport gameViewport;


    private int stepTotal; // renderissä
    private int oldStepTotal;

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
        changeScreen(MAPSCREEN);
    }

    private void createCameras() {
        screenCamera = new OrthographicCamera();
        screenCamera.setToOrtho(false, screenWidth, screenHeight);

        gameCamera = new OrthographicCamera();
        gameCamera.setToOrtho(false, gameWidth, gameHeight);
    }

    public Viewport getGameViewport() {
        return gameViewport;
    }

    public OrthographicCamera getScreenCamera() {
        return this.screenCamera;
    }

    public OrthographicCamera getGameCamera() {
        return this.gameCamera;
    }

    public void setLanguage(String s1, String s2, String s3) {
        Locale locale = new Locale(s1, s2);
        myBundle = I18NBundle.createBundle(Gdx.files.internal(s3), locale, "UTF-8");
    }

    public I18NBundle getMyBundle() {
        return myBundle;
    }

    @Override
    public void render () {
        super.render();
        batch.begin();
        batch.end();
    }

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

            case SETTINGSSCREEN:
                activeScreen = SETTINGSSCREEN;
                SettingsScreen settingsScreen = new SettingsScreen(this);
                setScreen(settingsScreen);
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
                //MoveScreenJump moveScreenJump = new MoveScreenJump(this, new MapScreen(this));
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
                //MoveScreenSquat moveScreenSquat = new MoveScreenSquat(this, new MapScreen(this));
                moveScreenStatus = true;
                this.setScreen(moveScreenSquat);
                break;
        }
    }

    public void setPreviousScreen(int screen) {
        this.previousScreen = screen;
    }

    public void setMoveScreenStatus(boolean status) {
        this.moveScreenStatus = status;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public int getActiveScreen() {
        return activeScreen;
    }

    public void setStartLanguage() {
        if(Save.getLanguagePrefs().equals("MyBundle_fi_FI")) {
            setLanguage("fi", "FI", "MyBundle_fi_FI");
        } else if(Save.getLanguagePrefs().equals("MyBundle_en_US")) {
            setLanguage("en", "US", "MyBundle_en_US");
        } else {
            setLanguage("fi", "FI", "MyBundle_fi_FI");
        }
    }

    public int getStepCount() {
        if(stepGetter!= null){
            stepTotal += stepGetter.getNumSteps();
        }
        return stepTotal;
    }

    private GetSteps stepGetter;

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

    @Override
    public void dispose () {
        batch.dispose();
    }
}