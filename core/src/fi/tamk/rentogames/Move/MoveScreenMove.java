package fi.tamk.rentogames.Move;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import fi.tamk.rentogames.DungeonEscape;
import fi.tamk.rentogames.Framework.GameAudio;
import fi.tamk.rentogames.Framework.Save;
import fi.tamk.rentogames.Screens.MapScreen;

/**
 * Abstract class to hold all common variables and methods for move screens.
 *
 * <p>
 * Used for creating Box2D world and player to each move screen.
 * Generates background music and camera to each move screen.
 * Implements interface screen which is used to switch screens when necessary.
 * </p>
 *
 * @author Lauri Latva-Kyyny
 * @author  Miko Kauhanen
 * @version 1.0
 * @see <a href="https://libgdx.badlogicgames.com/ci/nightlies/docs/api/com/badlogic/gdx/Screen.html">https://libgdx.badlogicgames.com/ci/nightlies/docs/api/com/badlogic/gdx/Screen.html</a>
 */
public abstract class MoveScreenMove implements Screen {

    // Current screen and game
    /**
     * Map screen that is received from main class to switch screens in game.
     */
    private MapScreen mapScreen;
    /**
     * Current game received from main class used to access non-static methods.
     */
    private DungeonEscape game;

    // Camera attributes
    /**
     * For debugging box2D body shapes.
     *
     * @see
     */
    private Box2DDebugRenderer debugRenderer;

    // Textures
    /**
     * Background texture used in move screens.
     *
     * <a href="https://libgdx.badlogicgames.com/ci/nightlies/docs/api/com/badlogic/gdx/physics/box2d/Box2DDebugRenderer.html"> https://libgdx.badlogicgames.com/ci/nightlies/docs/api/com/badlogic/gdx/physics/box2d/Box2DDebugRenderer.html> </a>
     */
    private Texture backgroundTexture;

    // World attributes
    /**
     * Box2D world to place box2D bodies.
     */
    private static World world;
    /**
     * Static final boolean variable used for setting box2D body debugging on and off.
     */
    private static final boolean DEBUG_PHYSICS = true;
    /**
     * Used in world stepping to set when to step to loop.
     */
    private double accumulator = 0;

    // World objects
    /**
     * Player object in game, used to create Box2D body to world.
     */
    private MoveScreenPlayer player;


    /**
     * Constructor that receives the game and map screen {@link MapScreen}.
     *
     * <p>
     * Receives the game and map screen screen from main class {@link DungeonEscape}.
     * Map screen is used to switch the screen back and game is used to access the game.
     * Uses screen interface to generate move screens.
     * Calls create method to create all variables needed to generate move screens.
     * </p>
     *
     * @param game current game received from main class
     * @param mapScreen map screen received from main class to switch screens
     */
    public MoveScreenMove(DungeonEscape game, MapScreen mapScreen) {
        // Current game and screen
        this.game = game;
        this.mapScreen = mapScreen;
        onCreate();
    }

    /**
     * Creates all common objects for move screens in game.
     *
     * <p>
     * Creates world for box2D objects, such as player and ground.
     * Sets the texture for background and screen's camera.
     * Generates the font and music.
     * </p>
     */
    private void onCreate() {
        // Sets Box2D attributes, gravitation etc.
        world = new World(new Vector2(0, -9.8f), true);

        // Debug renderer for Box2D
        debugRenderer = new Box2DDebugRenderer();

        // Sets all objects in world
        player = new MoveScreenPlayer(world);
        new MoveScreenGround(world, game.gameWidth);

        // Sets the background texture
        backgroundTexture = new Texture(Gdx.files.internal("backgrounddungeon.png"));

        // Uses main game camera to set up the screen's camera.
        game.getGameCamera().setToOrtho(false, game.gameWidth, game.gameHeight);


        // Called when move screen comes visible and background music starts playing.
        GameAudio.playMusic("movescreenmusic");
        GameAudio.setMusicVolume("movescreenmusic", Save.getCurrentAudioSetting());
        GameAudio.loopMusic("movescreenmusic");
    }

    /**
     * Enables debug for Box2D bodies.
     *
     * <p>
     * If variable debug physics is true renders the debug shapes else continues without rendering.
     * </p>
     *
     * @see <a href="https://libgdx.badlogicgames.com/ci/nightlies/docs/api/com/badlogic/gdx/physics/box2d/Box2DDebugRenderer.html"> https://libgdx.badlogicgames.com/ci/nightlies/docs/api/com/badlogic/gdx/physics/box2d/Box2DDebugRenderer.html> </a>
     */
    public void debug() {
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
        game.getGameViewport().update(width, height);
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


    /**
     * Called when access to current game is wanted, only returns the game.
     *
     * @return current game to move screens when needed
     */
    public DungeonEscape getGame() {
        return game;
    }

    /**
     * Returns background texture when called.
     *
     * @return background texture is in move screens
     */
    public Texture getBackgroundTexture() {
        return backgroundTexture;
    }

    /**
     * Called when access to player is wanted.
     *
     * @return player character in move screen as a box2D body
     */
    public MoveScreenPlayer getPlayer() {
        return player;
    }

    /**
     * Called from move screens to enable easy access for screen switching.
     *
     * @return map screen used when switching screens from move screen
     */
    public MapScreen getMapScreen() {
        return mapScreen;
    }

    /**
     * Updates the world with steps
     *
     * <p>
     * Calls the step several times with fixed value.
     * When game renders slowly stays in while loop longer and when game runs
     * faster stays less in loop.
     * </p>
     *
     * @param deltaTime how long was it from last render call
     */
    public void doPhysicsStep(float deltaTime) {
        // Current frame rate in game
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
        player.dispose();
        debugRenderer.dispose();
    }
}
