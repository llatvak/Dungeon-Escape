package fi.tamk.gameproject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

abstract class MoveScreenMove implements Screen {

    // Current screen and game
    private MapScreen mapScreen;
    private DungeonEscape game;

    private SpriteBatch batch;

    // Camera attributes
    public OrthographicCamera camera;
    private Box2DDebugRenderer debugRenderer;
    public OrthographicCamera fontCamera;

    // Textures
    private Texture backgroundTexture;

    // World attributes
    private static World world;
    public static final boolean DEBUG_PHYSICS = true;
    public  static final float WORLD_WIDTH = 3.6f;
    public  static final float WORLD_HEIGHT = 6.4f;
    private double accumulator = 0;
    private float TIME_STEP = 1/60f;

    // World objects
    private MoveScreenPlayer player;
    private MoveScreenGround ground;

    // Fonts
    private Fonts fonts;
    private BitmapFont fontRoboto;
    private GlyphLayout layout;
    private float fontWidth;
    private float fontHeight;

    public MoveScreenMove(DungeonEscape game, MapScreen mapScreen) {
        // Current game and screen
        this.game = game;
        this.mapScreen = mapScreen;
        onCreate();
    }


    public void onCreate() {
        // Sets Box2D attributes, gravitation etc.
        world = new World(new Vector2(0, -9.8f), true);

        // Debug renderer for Box2D
        debugRenderer = new Box2DDebugRenderer();

        // Sets all objects in world
        player = new MoveScreenPlayer(world);
        ground = new MoveScreenGround(world);

        backgroundTexture = new Texture(Gdx.files.internal("dungeonbg.png"));
        camera = new OrthographicCamera();
        fontCamera = new OrthographicCamera();
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
        fontCamera.setToOrtho(false, 360f, 640f);

        // Setting up fonts
        fonts = new Fonts();
        fonts.createMediumFont();
        fontRoboto = fonts.getFont(Fonts.MEDIUM);
    }

    public void debug() {
        // Is script working correctly
        if(DEBUG_PHYSICS) {
            debugRenderer.render(world, camera.combined);
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

    public Texture getBackgroundTexture() {
        return backgroundTexture;
    }


    public MoveScreenPlayer getPlayer() {
        return player;
    }

    public BitmapFont getFontRoboto() {
        return fontRoboto;
    }


    public MapScreen getMapScreen() {
        return mapScreen;
    }

    public void doPhysicsStep(float deltaTime) {
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
    }
}
