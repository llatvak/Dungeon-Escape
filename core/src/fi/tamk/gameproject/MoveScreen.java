package fi.tamk.gameproject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class MoveScreen implements Screen {
    MapScreen mapScreen;

    SpriteBatch batch;
    private OrthographicCamera camera;
    private Box2DDebugRenderer debugRenderer;

    private Texture backgroundTexture;

    public static final boolean DEBUG_PHYSICS = true;
    private final String MAIN_TITLE = "Move Screen";
    public  static final float WORLD_WIDTH = 5f;
    public  static final float WORLD_HEIGHT = 10f;

    private DungeonEscape game;
    private static World world;
    private MoveScreenPlayer player;
    private MoveScreenGround ground;
    private MoveScreenSpike spike;

    public MoveScreen(DungeonEscape game, MapScreen mapScreen) {
        this.game = game;
        this.mapScreen = mapScreen;
        batch = game.getBatch();
        world = new World(new Vector2(0, -9.8f), true);
        debugRenderer = new Box2DDebugRenderer();
        player = new MoveScreenPlayer(world);
        ground = new MoveScreenGround(world);
        spike = new MoveScreenSpike(world);
        backgroundTexture = new Texture(Gdx.files.internal("dungeon.png"));
        camera = new OrthographicCamera();
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
    }

    @Override
    public void show() {

    }

    Array<Body> bodies = new Array<Body>();

    @Override
    public void render(float delta) {
        batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(DEBUG_PHYSICS) {
            debugRenderer.render(world, camera.combined);
        }

        world.getBodies(bodies);

        batch.begin();
        batch.draw(backgroundTexture, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
        player.draw(batch);
        spike.draw(batch);
        batch.end();

        update();

        doPhysicsStep(Gdx.graphics.getDeltaTime());
    }

    public void update() {

        player.playerJump();
        player.checkInput();

        // When player sprite moves out of boundaries go to map
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

    private double accumulator = 0;
    private float TIME_STEP = 1/60f;

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
    public void dispose() {
        backgroundTexture.dispose();
        world.dispose();
        player.dispose();
        spike.dispose();
    }
}

