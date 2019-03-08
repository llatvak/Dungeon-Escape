package fi.tamk.gameproject;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import fi.tamk.gameproject.writer.BitmapFontWriter;



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
    BitmapFont fontRoboto;
    GlyphLayout layout;
    float fontWidth;
    float fontHeight;
    private Stage stage;


    int stepCount;



    public MapScreen(DungeonEscape game) {
        background = new Texture("brickwall.png");

        this.game = game;
        batch = game.getBatch();
        //this.stage = game.getStage();
        //moveCamera();
        tiledMap = new TmxMapLoader().load("DungeonEscape_Map.tmx");
        // worldMap = new TmxMapLoader().load("tilemap.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);

        //onCreate();

        player = new MapPlayer(this);
    }



    public void moveCamera() {
        camera.position.x = player.getX() + 32f;
        camera.position.y = player.getY() + 32f;
        camera.update();
    }

    public void update() {

        game.stage.act(Gdx.graphics.getDeltaTime());
        game.stage.draw();

        stepCount = game.getStepCount();
        player.checkSteps(stepCount);

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

    public TiledMap getWorldMap(){
        return tiledMap;
    }


    @Override
    public void show() {

    }


    @Override
    public void render(float delta) {
        batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);



        batch.setProjectionMatrix(camera.combined);
        // Which part of the map are we looking? Use camera's viewport
        tiledMapRenderer.setView(camera);
        // Render all layers to screen.
        tiledMapRenderer.render();

        batch.begin();
        //batch.draw(background,0,0, WORLD_WIDTH,WORLD_HEIGHT);


//        fontRoboto = game.getFont();
//        fontRoboto.draw(batch, MAIN_TITLE, WORLD_WIDTH /2 , WORLD_HEIGHT / 2);
//        layout = game.getLayout();
//        layout.setText(fontRoboto, MAIN_TITLE);
//        fontWidth = layout.width;
//        fontHeight = layout.height;

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
