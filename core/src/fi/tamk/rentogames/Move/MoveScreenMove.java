package fi.tamk.rentogames.Move;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import fi.tamk.rentogames.DungeonEscape;
import fi.tamk.rentogames.Framework.Fonts;
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

    /**
     * Constructor that receives the game and map screen {@link MapScreen}.
     *
     * <p>
     * Receives the game and map screen screen from main class {@link DungeonEscape}.
     * Map screen is used to switch the screen back and game is used to access
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

    private void onCreate() {
        // Sets Box2D attributes, gravitation etc.
        world = new World(new Vector2(0, -9.8f), true);

        // Debug renderer for Box2D
        debugRenderer = new Box2DDebugRenderer();

        // Sets all objects in world
        player = new MoveScreenPlayer(world);
        new MoveScreenGround(world, game.gameWidth);

        backgroundTexture = new Texture(Gdx.files.internal("backgrounddungeon.png"));

        game.getGameCamera().setToOrtho(false, game.gameWidth, game.gameHeight);

        // Setting up fonts
        Fonts fonts = new Fonts();
        fontRoboto = fonts.createMediumFont();

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

    public DungeonEscape getGame() {
        return game;
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
        fontRoboto.dispose();
        player.dispose();
        debugRenderer.dispose();
    }
}
