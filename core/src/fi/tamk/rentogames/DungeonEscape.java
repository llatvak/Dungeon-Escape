package fi.tamk.rentogames;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.Locale;

public class DungeonEscape extends Game {
    public final float screenWidth = 360f;
    public final float screenHeight = 640f;

    public final float gameWidth = 360f / 100f;
    public final float gameHeight = 640f / 100f;

	SpriteBatch batch;

	public OrthographicCamera screenCamera;
    public OrthographicCamera gameCamera;


	private SplashScreen splashScreen;

	private MainMenu mainMenu;
    private MapScreen mapScreen;
    private MoveScreenJump moveScreenJump;
    private SettingsScreen settingsScreen;
    private MoveScreenSquat moveScreenSquat;

    private boolean mapScreenStatus;
    private boolean moveScreenStatus;

    private int previousScreen;

    public final static int SPLASHSCREEN = 0;
    public final static int MAINMENU = 1;
    public final static int SETTINGSSCREEN = 2;
    public final static int MAPSCREEN = 3;
    public final static int JUMPSCREEN = 4;
    public final static int BACK = 5;
    public final static int SQUATSCREEN = 6;

    private static Locale locale;
    private static I18NBundle myBundle;
    private PedometerStatus pedometerStatus;

    public DungeonEscape() {
       // this.pedometerStatus = pedometerStatus;
       // pedometerStatus.setStatus(1);

    }


    @Override
    public void create () {
        System.out.println(Gdx.graphics.getWidth() +" x "+ Gdx.graphics.getHeight());

        setLanguage("fi", "FI", "MyBundle_fi_FI");
        createCameras();
        batch = new SpriteBatch();
        changeScreen(SPLASHSCREEN);

    }

    public void createCameras() {
        screenCamera = new OrthographicCamera();
        screenCamera.setToOrtho(false, screenWidth, screenHeight);

        gameCamera = new OrthographicCamera();
        gameCamera.setToOrtho(false, gameWidth, gameHeight);
    }

    public OrthographicCamera getScreenCamera() {
        return this.screenCamera;
    }

    public OrthographicCamera getGameCamera() {
        return this.gameCamera;
    }

    public static void setLanguage(String s1, String s2, String s3) {
        locale = new Locale(s1, s2);
        myBundle = I18NBundle.createBundle(Gdx.files.internal(s3), locale);
    }

    public static Locale getLocale() {
        return locale;
    }

    public static I18NBundle getMyBundle() {
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
                splashScreen = new SplashScreen(this);
                setScreen(splashScreen);
                break;

            case MAINMENU:
                mainMenu = new MainMenu(this);
                setScreen(mainMenu);
                break;

            case SETTINGSSCREEN:
                settingsScreen =  new SettingsScreen(this);
                setScreen(settingsScreen);
                break;

            case MAPSCREEN:
                mapScreen = new MapScreen(this);
                mapScreenStatus = true;
                this.setScreen(mapScreen);
                break;

            case JUMPSCREEN:
                moveScreenJump = new MoveScreenJump(this, mapScreen.getMapScreen());
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
                moveScreenSquat = new MoveScreenSquat(this, mapScreen.getMapScreen());
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


    public void addSteps() {
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


