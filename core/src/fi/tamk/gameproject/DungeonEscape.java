package fi.tamk.gameproject;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class DungeonEscape extends Game {
    int screenResolutionWidth;
    int screenResolutionHeight;

	SpriteBatch batch;

	public OrthographicCamera camera;

	private SplashScreen splashScreen;
	private MainMenu mainMenu;
    private MapScreen mapScreen;
    private MoveScreen moveScreen;
    private SettingsScreen settingsScreen;

    private int previousScreen;

    public final static int SPLASHSCREEN = 0;
    public final static int MAINMENU = 1;
    public final static int SETTINGSSCREEN = 2;
    public final static int MAPSCREEN = 3;
    public final static int MOVESCREEN = 4;
    public final static int BACK = 5;

    int stepTotal;
    int oldStepTotal;

    @Override
    public void create () {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, screenResolutionWidth, screenResolutionHeight);

        // Get device screen resolution
        screenResolutionWidth = Gdx.graphics.getWidth();
        screenResolutionHeight = Gdx.graphics.getHeight();
        System.out.println(Gdx.graphics.getWidth() +" x "+ Gdx.graphics.getHeight());

        batch = new SpriteBatch();

        changeScreen(SPLASHSCREEN);

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
                this.setScreen(mapScreen);
                break;

            case MOVESCREEN:
                moveScreen = new MoveScreen(this, mapScreen.getMapScreen());
                this.setScreen(moveScreen);
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
        }
    }

    public void setPreviousScreen(int screen) {
        this.previousScreen = screen;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public void receiveSteps(int stepCount) {
        System.out.println("Steps: " + stepCount);
        this.stepTotal = stepCount;
    }

    public int getStepTotal() {
        return stepTotal;
    }

    @Override
    public void dispose () {
        batch.dispose();
    }
}


