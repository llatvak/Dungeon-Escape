package fi.tamk.rentogames;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Locale;

public class DungeonEscape extends Game {
    final float screenWidth = 360f;
    final float screenHeight = 640f;

    final float gameWidth = 360f / 100f;
    final float gameHeight = 640f / 100f;

	SpriteBatch batch;

	private OrthographicCamera screenCamera;
    private OrthographicCamera gameCamera;

    private MapScreen mapScreen;

    private boolean mapScreenStatus;
    private boolean moveScreenStatus;

    private int previousScreen;

    private final static int SPLASHSCREEN = 0;
    final static int MAINMENU = 1;
    final static int SETTINGSSCREEN = 2;
    final static int MAPSCREEN = 3;
    final static int JUMPSCREEN = 4;
    final static int BACK = 5;
    final static int SQUATSCREEN = 6;

    private I18NBundle myBundle;
    private Viewport gameViewport;
    private Viewport fontViewport;

    public DungeonEscape() {
       // this.pedometerStatus = pedometerStatus;
       // pedometerStatus.setStatus(1);
    }

    @Override
    public void create () {
        System.out.println(Gdx.graphics.getWidth() +" x "+ Gdx.graphics.getHeight());

        setLanguage("fi", "FI", "MyBundle_fi_FI");
        createCameras();

        gameViewport = new StretchViewport(screenWidth, screenHeight, screenCamera);
        fontViewport = new FitViewport(screenWidth, screenHeight, screenCamera);

        batch = new SpriteBatch();
        changeScreen(SPLASHSCREEN);
    }

    private void createCameras() {
        screenCamera = new OrthographicCamera();
        screenCamera.setToOrtho(false, screenWidth, screenHeight);

        gameCamera = new OrthographicCamera();
        gameCamera.setToOrtho(false, gameWidth, gameHeight);
    }

    Viewport getGameViewport() {
        return gameViewport;
    }

    Viewport getFontViewport() {
        return fontViewport;
    }

    OrthographicCamera getScreenCamera() {
        return this.screenCamera;
    }

    OrthographicCamera getGameCamera() {
        return this.gameCamera;
    }

    void setLanguage(String s1, String s2, String s3) {
        Locale locale = new Locale(s1, s2);
        myBundle = I18NBundle.createBundle(Gdx.files.internal(s3), locale, "UTF-8");
    }

    I18NBundle getMyBundle() {
        return myBundle;
    }

    @Override
    public void render () {
        super.render();
        batch.begin();
        batch.end();
    }

    void changeScreen(int screen) {

        switch(screen) {
            case SPLASHSCREEN:
                SplashScreen splashScreen = new SplashScreen(this);
                setScreen(splashScreen);
                break;

            case MAINMENU:
                MainMenu mainMenu = new MainMenu(this);
                setScreen(mainMenu);
                break;

            case SETTINGSSCREEN:
                SettingsScreen settingsScreen = new SettingsScreen(this);
                setScreen(settingsScreen);
                break;

            case MAPSCREEN:
                mapScreen = new MapScreen(this);
                mapScreenStatus = true;
                this.setScreen(mapScreen);
                break;

            case JUMPSCREEN:
                MoveScreenJump moveScreenJump = new MoveScreenJump(this, mapScreen.getMapScreen());
                moveScreenStatus = true;
                this.setScreen(moveScreenJump);
                break;

            case BACK:
                if(previousScreen == MAINMENU) {
                    mainMenu = new MainMenu(this);
                    this.setScreen(mainMenu);
                } else if (previousScreen == MAPSCREEN) {
                    mapScreen = mapScreen.getMapScreen();
                    this.setScreen(mapScreen);
                }
                break;

            case SQUATSCREEN:
                MoveScreenSquat moveScreenSquat = new MoveScreenSquat(this, mapScreen.getMapScreen());
                moveScreenStatus = true;
                this.setScreen(moveScreenSquat);
                break;
        }
    }

    void setPreviousScreen(int screen) {
        this.previousScreen = screen;
    }

    void setMoveScreenStatus(boolean status) {
        this.moveScreenStatus = status;
    }

    SpriteBatch getBatch() {
        return batch;
    }


    void addSteps() {
        // Is map screen open
        if(mapScreenStatus) {
            // Is movement screen open
            if(!moveScreenStatus) {
                mapScreen.addStep();
            }
        }
    }

    @Override
    public void dispose () {
        batch.dispose();
    }
}


