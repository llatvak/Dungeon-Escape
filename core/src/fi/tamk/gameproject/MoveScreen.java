package fi.tamk.gameproject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class MoveScreen implements Screen {
    // Current screen and game
    private MapScreen mapScreen;
    private DungeonEscape game;

    private SpriteBatch batch;

    // Camera attributes
    private OrthographicCamera camera;
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera fontCamera;


    // Textures
    private Texture backgroundTexture;

    // World attributes
    private static World world;
    public static final boolean DEBUG_PHYSICS = true;
    private final String MAIN_TITLE = "Move Screen";
    public  static final float WORLD_WIDTH = 5f;
    public  static final float WORLD_HEIGHT = 10f;
    private double accumulator = 0;
    private float TIME_STEP = 1/60f;

    // World objects
    private MoveScreenPlayer player;
    private MoveScreenGround ground;
    private MoveScreenSpike spike;
    // Saves all bodies in Box2D world to this array
    private Array<Body> bodies = new Array<Body>();

    // Fonts
    private Fonts fonts;
    private BitmapFont fontRoboto;
    private GlyphLayout layout;
    private float fontWidth;
    private float fontHeight;


    public MoveScreen(DungeonEscape game, MapScreen mapScreen) {
        // Current game and screen
        this.game = game;
        this.mapScreen = mapScreen;
        onCreate();
    }

    // Sets up the world for box2D and camera used
    public void onCreate() {
        batch = game.getBatch();

        // Sets Box2D attributes, gravitation etc.
        world = new World(new Vector2(0, -9.8f), true);

        // Debug renderer for Box2D
        debugRenderer = new Box2DDebugRenderer();

        // Sets all objects in world
        player = new MoveScreenPlayer(world);
        ground = new MoveScreenGround(world);
        spike = new MoveScreenSpike(world);

        // Setting the background texture and camera
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

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Is script working correctly
        if(DEBUG_PHYSICS) {
            debugRenderer.render(world, camera.combined);
        }

        // World camera
        batch.setProjectionMatrix(camera.combined);

        // Gets all Box2D bodies from the array
        world.getBodies(bodies);

        batch.begin();

        batch.draw(backgroundTexture, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
        player.draw(batch);
        spike.draw(batch);

        // Font camera
        batch.setProjectionMatrix(fontCamera.combined);
        fontRoboto.draw(batch, "Jump six times!",80 , 640f - 100f);
        fontRoboto.draw(batch, "Jumps: " + player.getCountedJumps(), 80, 640f - 200f);

        batch.end();

        update();

        doPhysicsStep(Gdx.graphics.getDeltaTime());
    }

    public void update() {
        // Player jumping and checking user input
        player.playerJump();
        player.checkInput();

        // When player sprite moves out of boundaries go to map screen
        if(player.getPlayerY() < -1f) {
            game.setScreen(mapScreen);
        }
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

    private void doPhysicsStep(float deltaTime) {
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
    // Disposes all from move screen
    public void dispose() {
        backgroundTexture.dispose();
        world.dispose();
        player.dispose();
        spike.dispose();
        fontRoboto.dispose();
    }
}

