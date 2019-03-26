package fi.tamk.rentogames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.I18NBundle;

abstract class MoveScreenMove implements Screen {

    // Current screen and game
    private MapScreen mapScreen;
    private DungeonEscape game;

    // Camera attributes
    private Box2DDebugRenderer debugRenderer;

    // Textures
    private Texture backgroundTexture;

    // World attributes
    private static World world;
    private static final boolean DEBUG_PHYSICS = true;
    private double accumulator = 0;

    // World objects
    private MoveScreenPlayer player;

    // Fonts
    private BitmapFont fontRoboto;

    private I18NBundle myBundle;

    MoveScreenMove(DungeonEscape game, MapScreen mapScreen) {
        // Current game and screen
        this.game = game;
        this.mapScreen = mapScreen;
        onCreate();
    }

    private void onCreate() {
        // Sets Box2D attributes, gravitation etc.
        world = new World(new Vector2(0, -9.8f), true);

        // Debug renderer for Box2D
        debugRenderer = new Box2DDebugRenderer();

        // Sets all objects in world
        player = new MoveScreenPlayer(world);
        new MoveScreenGround(world, game.gameWidth);

        backgroundTexture = new Texture(Gdx.files.internal("dungeonbg.png"));

        game.getGameCamera().setToOrtho(false, game.gameWidth, game.gameHeight);

        // Setting up fonts
        Fonts fonts = new Fonts();
        fontRoboto = fonts.createMediumFont();

        myBundle = DungeonEscape.getMyBundle();
    }

    void debug() {
        // Is script working correctly
        if(DEBUG_PHYSICS) {
            debugRenderer.render(world, game.getGameCamera().combined);
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    public DungeonEscape getGame() {
        return game;
    }

    public void setGame(DungeonEscape game) {
        this.game = game;
    }

    I18NBundle getMyBundle() {
        return myBundle;
    }

    Texture getBackgroundTexture() {
        return backgroundTexture;
    }


    MoveScreenPlayer getPlayer() {
        return player;
    }

    BitmapFont getFontRoboto() {
        return fontRoboto;
    }


    MapScreen getMapScreen() {
        return mapScreen;
    }

    void doPhysicsStep(float deltaTime) {
        float TIME_STEP = 1/60f;
        float frameTime = deltaTime;
        if(deltaTime > 1/4f) {
            frameTime = 1/4f;
        }
        accumulator += frameTime;
        while(accumulator >= TIME_STEP) {
            world.step(TIME_STEP, 8, 3);
            accumulator -= TIME_STEP;
        }
    }

    @Override
    public void dispose() {
        world.dispose();
        mapScreen.dispose();
        backgroundTexture.dispose();
        game.dispose();
        fontRoboto.dispose();
        getPlayer().dispose();
    }
}
