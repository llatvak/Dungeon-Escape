package fi.tamk.gameproject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapGroupLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;


public class MapScreen implements Screen {

    private final String MAIN_TITLE = "Map screen";
    final float WORLD_WIDTH = 360;
    final float WORLD_HEIGHT = 640;

    DungeonEscape game;
    MapPlayer player;
    SpriteBatch batch;

    // Camera
    OrthographicCamera camera;

    // Map
    TiledMap worldMap;
    OrthogonalTiledMapRenderer tiledMapRenderer;

    // Textures
    Texture background;

    // Fonts
    BitmapFont fontRoboto;
    GlyphLayout layout;
    float fontWidth;
    float fontHeight;

    public MapScreen(DungeonEscape game) {
        background = new Texture("brickwall.png");

        this.game = game;
        batch = game.getBatch();
        //moveCamera();
        worldMap = new TmxMapLoader().load("DungeonEscape_Map.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(worldMap);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);

        player = new MapPlayer(this);
    }

    public void moveCamera() {
        camera.position.x = player.getX();
        camera.position.y = player.getY();
        camera.update();
    }

    public void update() {

        //player.gesture();
        player.verticalMove();

//        if(player.gameEnd) {
//            pause();
//            // reset();
//        }

        player.horizontalMove();

        if(player.upRightCollision && player.downRightCollision) {
            player.translateX(1 * player.moveAmount);
        }


        if(Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            player.setUpMove(true);
            player.verticalMove();

        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            player.setDownMove(true);
            player.verticalMove();

        }


        if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            player.setRightMove(true);
            player.horizontalMove();

        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            player.setLeftMove(true);
            player.horizontalMove();

        }
        //checkCollisions();
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
