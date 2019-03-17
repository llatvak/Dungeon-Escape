package fi.tamk.gameproject;

import com.badlogic.gdx.Game;
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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import javax.swing.text.View;


public class MapScreen implements Screen {

    private final String MAIN_TITLE = "Map screen";
    final float WORLD_WIDTH = 360f / 100f;
    final float WORLD_HEIGHT = 640f / 100f;

    DungeonEscape game;
    MapPlayer player;
    SpriteBatch batch;
    MoveScreen moveScreen;


    // Camera
    OrthographicCamera camera;
    OrthographicCamera fontCamera;
    Viewport viewport;
    // Map
    TiledMap tiledMap;
    OrthogonalTiledMapRenderer tiledMapRenderer;

    // Textures
    Texture background;

    // Fonts
    private Fonts fonts;
    private BitmapFont fontRoboto;

    protected Skin skin;
    private Stage stage;

    private int stepTotal;

    public MapScreen(DungeonEscape game) {
        this.game = game;
        onCreate();

    }

    public void onCreate() {
        batch = game.getBatch();
        tiledMap = new TmxMapLoader().load("DungeonEscape_Map.tmx");
        background = new Texture("brickwall.png");

        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1/100f);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);

        fontCamera = new OrthographicCamera();
        fontCamera.setToOrtho(false, 360f, 640f);

        player = new MapPlayer(this);

        fonts = new Fonts();
        fonts.createMediumFont();
        fontRoboto = fonts.getFont();


        viewport = new FitViewport(360f, 640f, fontCamera);
        viewport.apply();
        skin = new Skin( Gdx.files.internal("dark-peel-ui.json") );
        stage = new Stage(viewport, batch);
        fontCamera.position.set(fontCamera.viewportWidth / 2, fontCamera.viewportHeight / 2, 0);
        fontCamera.update();

    }

    public void update() {

        stepTotal = game.getStepTotal();
        player.receiveSteps(stepTotal);
        player.countMovementPoints();
        player.checkAllowedMoves();

        if(player.moving) {
            player.move();
        }

        player.checkCollisions();
        player.checkInput();

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        fontCamera.update();

        // Which part of the map are we looking? Use camera's viewport
        tiledMapRenderer.setView(camera);
        // Render all layers to screen.
        tiledMapRenderer.render();



        // View font camera
        batch.setProjectionMatrix(fontCamera.combined);

        batch.begin();

        //batch.draw(background,0,0, WORLD_WIDTH,WORLD_HEIGHT);

        fontRoboto.draw(batch, "Steps: " + stepTotal, 10 , 640f - 10f);
        fontRoboto.draw(batch, "Moves: " + player.movementPoints, 10 , 640f - 35f);

        // View game camera
        batch.setProjectionMatrix(camera.combined);

        //player.draw(batch);
        batch.draw(player.getTexture(),
                camera.position.x - player.getTexture().getWidth()/100f/2,
                camera.position.y - player.getTexture().getHeight()/100f/2,
                player.getTexture().getWidth()/100f,
                player.getTexture().getHeight()/100f);
        batch.end();

        stage.act(Gdx.graphics.getDeltaTime());
        // Call draw on every actor
        stage.draw();

        moveCamera();
        update();


    }

    public void moveCamera() {
        camera.position.x = player.getX()/100f + 32f/100f;
        camera.position.y = player.getY()/100f + 32f/100f;
        camera.update();

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
        Gdx.input.setInputProcessor(stage);

        //Create Table
        Table mainTable = new Table();
        //Set table to fill stage
        mainTable.setFillParent(true);

        // Debug lines
        mainTable.setDebug(true);

        //Set alignment of contents in the table.
        mainTable.top();
        mainTable.right();

        //Create buttons
        ImageButton settingsButton = new ImageButton(skin, "settings");

        //Add listeners to buttons

        settingsButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new OptionsScreen(game));
            }
        });


        //Add buttons to table
        mainTable.add(settingsButton).width(50).height(50).fillX().pad(5,5,5,5);
        mainTable.row();

        //Add table to stage
        stage.addActor(mainTable);
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
        tiledMap.dispose();
        batch.dispose();
    }
}
