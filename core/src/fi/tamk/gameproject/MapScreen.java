package fi.tamk.gameproject;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;


public class MapScreen implements Screen {

    private final String MAIN_TITLE = "Map screen";
    final float WORLD_WIDTH = 360f;
    final float WORLD_HEIGHT = 640f;

    DungeonEscape game;
    MapPlayer player;
    SpriteBatch batch;
    MoveScreen moveScreen;

    // Camera
    OrthographicCamera camera;
    OrthographicCamera fontCamera;

    // Map
    TiledMap tiledMap;
    OrthogonalTiledMapRenderer tiledMapRenderer;

    // Textures
    Texture background;

    // Fonts
    private BitmapFont fontRoboto;
    private GlyphLayout layout;
    private float fontWidth;
    private float fontHeight;


    int stepTotal;
    int movementPoints;

    private Stage stage;


    public MapScreen(DungeonEscape game) {
        this.game = game;
        onCreate();

    }

    public void onCreate() {
        batch = game.getBatch();
        tiledMap = new TmxMapLoader().load("DungeonEscape_Map.tmx");
        background = new Texture("brickwall.png");

        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        camera = new OrthographicCamera();
        fontCamera = new OrthographicCamera();
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
        fontCamera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
        player = new MapPlayer(this);

    }



    public void moveCamera() {
        camera.position.x = player.getX() + 32f;
        camera.position.y = player.getY() + 32f;
        camera.update();
    }

    long lastFrameId = -1;

    public void update() {

//        game.stage.act(Gdx.graphics.getDeltaTime());
//        game.stage.draw();
//        if(stepTotal%5 == 0) {
//            player.movementPoints++;
//        }
        //System.out.println(Gdx.graphics.getFrameId() +" "+ lastFrameId);

//        if(lastFrameId != Gdx.graphics.getFrameId()) {

//            lastFrameId = Gdx.graphics.getFrameId();
//        }


        stepTotal = game.getStepTotal();
        player.receiveSteps(stepTotal);
        player.countMovementPoints();
        player.checkAllowedMoves();

        if(player.moving) {
            player.move();
        }

        player.checkCollisions();
        player.checkInput();
        //player.checkGesture();

    }

    public void goToDownTrap() {
        System.out.println("DOWN TRAP!");
        moveScreen = new MoveScreen(game, this);
        game.setScreen(moveScreen);
    }
    public void goToUpTrap() {
        System.out.println("UP TRAP!");
        // Needs new class UpScreen
        moveScreen = new MoveScreen(game, this);
        game.setScreen(moveScreen);
    }
    public void goToStoryTile() {
        System.out.println("STORY TILE!");
        // Needs new class StoryScreen
        moveScreen = new MoveScreen(game, this);
        game.setScreen(moveScreen);
    }

    public void addStep() {
        game.stepTotal++;
    }

    public void subtractStep() {
        game.stepTotal--;
    }

    public TiledMap getWorldMap(){
        return tiledMap;
    }


    @Override
    public void show() {

    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Which part of the map are we looking? Use camera's viewport
        tiledMapRenderer.setView(camera);
        // Render all layers to screen.
        tiledMapRenderer.render();

        // View font camera
        batch.setProjectionMatrix(fontCamera.combined);

        batch.begin();

        //batch.draw(background,0,0, WORLD_WIDTH,WORLD_HEIGHT);

        fontRoboto = game.getFont();
        fontRoboto.draw(batch, "Steps: " + stepTotal, 10 , WORLD_HEIGHT - 10f);
        fontRoboto.draw(batch, "Moves: " + player.movementPoints, 200 , WORLD_HEIGHT - 10f);

//        layout = game.getLayout();
//        layout.setText(fontRoboto, MAIN_TITLE);
//        fontWidth = layout.width;
//        fontHeight = layout.height;

        // View game camera
        batch.setProjectionMatrix(camera.combined);

        player.draw(batch);

        batch.end();

        moveCamera();
        update();
        // System.out.println("X: " + player.getX() + " Y: " + player.getY());
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

    @Override
    public void dispose() {
        background.dispose();
        fontRoboto.dispose();
        player.dispose();
    }
}
