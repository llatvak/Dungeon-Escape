package fi.tamk.rentogames.Screens;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import fi.tamk.rentogames.DungeonEscape;
import fi.tamk.rentogames.Framework.Fonts;
import fi.tamk.rentogames.Framework.Save;
import fi.tamk.rentogames.Map.MapLevel;
import fi.tamk.rentogames.Map.MapPlayer;
import fi.tamk.rentogames.Interface.MapScreenUI;
import fi.tamk.rentogames.Framework.MyInputProcessor;

public class MapScreen implements Screen {

    private DungeonEscape game;
    private MapPlayer player;
    private MapLevel mapLevel;
    private MapScreenUI userInterface;
    private boolean paused;

    private boolean debugUI = true;

    private Texture keyTexture;
    private Texture footMarkTexture;
    private Texture movesArrowTexture;
    private Texture controlsArrowTexture;

    // Map
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer tiledMapRenderer;

    private BitmapFont fontRoboto;

    private Skin skin;
    private Stage stage;



    private int stepTotal = 0;
    private int oldStepTotal;
    private int savedSteps;
    private int leftOverSteps;

    private int level = 1;
    public static final int KEYS_NEEDED = 3;
    public int keyAmount;
    public boolean keysCollected = true;

    public boolean buttonUp;
    private ProgressBar stepsProgressBar;
    private boolean resetProgressBar = false;
    private int progressbarValue = 0;

    //Create buttons and bars
    ImageButton settingsButton;
    ImageButton keyImage;
    ImageButton footmarkImage;
    ImageButton movesImage;
    ImageButton sidecontrolsImage;


    private Label stepLabel;
    private Label movesLabel;
    private Label keyLabel;

    public MapScreen(DungeonEscape game) {
        this.game = game;
        onCreate();
    }

    private void onCreate() {
        progressbarValue = Save.getProgressBarValue();
        countMovementPoints();
        mapLevel = new MapLevel(game);
        tiledMap = mapLevel.getCurrentMap();
        tiledMapRenderer = mapLevel.getTiledMapRenderer();

        player = new MapPlayer(this, mapLevel);

        userInterface = new MapScreenUI(game, this, player);

        // Fonts
        Fonts fonts = new Fonts();
        fontRoboto = fonts.createMediumFont();

        this.stage = new Stage(game.getGameViewport());

        //skin = new Skin(Gdx.files.internal("uiskin.json"));

        skin = new Skin();
        skin.add("fontRoboto-Med", fontRoboto, BitmapFont.class);
        skin.addRegions(new TextureAtlas(Gdx.files.internal("uiskin.atlas")));
        skin.load(Gdx.files.internal("uiskin.json"));


        stepsProgressBar = new ProgressBar(0, player.STEPSTOMOVE,1,false,skin, "default-horizontal");
        stepsProgressBar.setAnimateDuration(0.5f);

        updateProgressBar();
        stepTotal = Save.getProgressBarValue();

        keyTexture = new Texture("keyicon.png");
        footMarkTexture = new Texture("footmarkicon.png");
        movesArrowTexture = new Texture("movesicon.png");
        controlsArrowTexture = new Texture("sidearrow.png");

        //Create buttons and bars
        settingsButton = new ImageButton(skin, "settings");
        keyImage = new ImageButton(new TextureRegionDrawable(new TextureRegion(keyTexture)));
        footmarkImage = new ImageButton(new TextureRegionDrawable(new TextureRegion(footMarkTexture)));
        movesImage = new ImageButton(new TextureRegionDrawable(new TextureRegion(movesArrowTexture)));
        sidecontrolsImage = new ImageButton(new TextureRegionDrawable(new TextureRegion(controlsArrowTexture)));


        stepLabel = new Label("" + stepTotal, skin,"white");
        movesLabel = new Label("" + player.movementPoints, skin,"white");
        keyLabel = new Label("" + keyAmount + "/" + KEYS_NEEDED, skin,"white");


    }

    private void update() {
        player.receiveSteps(stepTotal);
        countMovementPoints();
        player.checkAllowedMoves();

        if(!buttonUp) {
            if(player.moving) {
                player.move();
            }
        }

        checkKeyAmount();

        player.checkCollisions();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Which part of the map are we looking? Use camera's viewport
        tiledMapRenderer.setView(game.getGameCamera());
        // Render all layers to screen.
        tiledMapRenderer.render();

        // View font camera
        game.batch.setProjectionMatrix(game.getScreenCamera().combined);

        game.batch.begin();

        // Draw fonts
        // fontRoboto.draw(game.batch, game.getMyBundle().get("stepcounter") + ": " + stepTotal, 10f , game.screenHeight - 40f);
        // fontRoboto.draw(game.batch,"" + player.movementPoints, 320 , game.screenHeight - 12f);
        // fontRoboto.draw(game.batch,game.getMyBundle().get("keys") + ": " + keyAmount + "/3", 10f , game.screenHeight - 70f);

        // View game camera
        game.batch.setProjectionMatrix(game.getGameCamera().combined);

        game.batch.draw(player.getTexture(),
                game.getGameCamera().position.x - player.getTexture().getWidth()/100f/2,
                game.getGameCamera().position.y - player.getTexture().getHeight()/100f/2,
                player.getTexture().getWidth()/100f,
                player.getTexture().getHeight()/100f);
        game.batch.end();


        moveCamera();
        update();


        stage.act(Gdx.graphics.getDeltaTime());
        // Call draw on every actor
        stage.draw();

        // Update progress bar
        //updateProgressBar();
    }

    private void checkProgressBar() {
        if(stepTotal > oldStepTotal){
            if(resetProgressBar) {
                progressbarValue = 0;
                resetProgressBar = false;
            } else {
                progressbarValue++;
            }
        }
        oldStepTotal = stepTotal;
        updateProgressBar();
    }

    private void updateProgressBar() {
        stepsProgressBar.setValue(progressbarValue);
    }

    private void moveCamera() {
        game.getGameCamera().position.x = player.getX()/100f + 32f/100f;
        game.getGameCamera().position.y = player.getY()/100f + 32f/100f;
        game.getGameCamera().update();
    }

    private void checkKeyAmount() {
        keysCollected = keyAmount >= KEYS_NEEDED;
    }

    public void notEnoughKeys() {
        System.out.println("No keys");
    }

    public void changeLevel() {
        if(Save.getCurrentLevel() < 3) {
            level = Save.getCurrentLevel() + 1;
        } else {
            level = 1;
        }
        Save.saveCurrentLevel(level);
        mapLevel.setLevel(level);
        changeMap();
    }

    private void changeMap() {
        Gdx.app.log("MapLevel", ": " + level);
        keyAmount = 0;
        updateKeyLabel();
        keysCollected = false;
        mapLevel.resetMap();
        mapLevel.createTiledMap();
        player.setMap();
        player.spawn();
        tiledMapRenderer = mapLevel.getTiledMapRenderer();
    }

    public void trapConfirm(final boolean onSquat, final boolean onJump) {
        Gdx.app.log("Button", "created");
        buttonUp = true;

        final TextButton confirmButton = new TextButton(game.getMyBundle().get("readybutton"), skin);
        final TextButton cancelButton = new TextButton(game.getMyBundle().get("cancelbutton"), skin, "maroon");

        final Label trapLabel = new Label(game.getMyBundle().get("traplabel"),skin,"title-white");
        final Label readyLabel = new Label(game.getMyBundle().get("trapreadiness"),skin,"title-white");

        trapLabel.setPosition(game.screenWidth / 2 - trapLabel.getWidth() / 2, game.screenHeight / 2 + 170f);
        readyLabel.setPosition(game.screenWidth / 2 - readyLabel.getWidth() / 2, game.screenHeight / 2 + 140f);


        confirmButton.setWidth(110f);
        confirmButton.setHeight(60f);
        cancelButton.setColor(48,192,12,1);
        confirmButton.setPosition(game.screenWidth / 2 + 60f, game.screenHeight / 2 + 70f);

        cancelButton.setWidth(110f);
        cancelButton.setHeight(60f);
        cancelButton.setColor(185,22,22,1);
        cancelButton.setPosition(game.screenWidth / 2 - 170f, game.screenHeight / 2 + 70f);


        stage.addActor(trapLabel);
        stage.addActor(readyLabel);
        stage.addActor(cancelButton);
        stage.addActor(confirmButton);

        confirmButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                Gdx.app.log("Trap", "going");
                confirmButton.remove();
                cancelButton.remove();
                trapLabel.remove();
                readyLabel.remove();
                buttonUp = false;
                // Using boolean values checks trapscreen
                if(onSquat) {
                    goToSquatTrap();
                }
                if(onJump) {
                    goToJumpTrap();
                }
                player.addMovementPoint();
            }
        });

        cancelButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                Gdx.app.log("Trap", "cancel");
                confirmButton.remove();
                cancelButton.remove();
                trapLabel.remove();
                readyLabel.remove();
                buttonUp = false;
                player.addMovementPoint();
            }
        });
    }

    private void goToSquatTrap() {
        Gdx.app.log("Down trap", "going to crouching trap");
        game.changeScreen(DungeonEscape.SQUATSCREEN);
    }
    private void goToJumpTrap() {
        Gdx.app.log("Up trap", "going to jumping trap");
        game.changeScreen(DungeonEscape.JUMPSCREEN);
    }
    private void goToStoryScreen() {
        Gdx.app.log("Story", "going to story screen");
        // Needs new class StoryScreen
        game.changeScreen(DungeonEscape.JUMPSCREEN);
    }

    public void addStep() {
        stepTotal++;
        updateStepsLabel();
        checkProgressBar();
        System.out.println("Steps: " + stepTotal);
        if(stepTotal > 10) {
            Save.saveCurrentProgressbar(progressbarValue + 1);
        } else {
            Save.saveCurrentProgressbar(progressbarValue);
        }

        if(!paused) {
            leftOverSteps++;
            //System.out.println("Steps to point: " + leftOverSteps);
        }
    }

    public MapScreen getMapScreen() {
        return this;
    }

    @Override
    public void show() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        MyInputProcessor inputProcessor = new MyInputProcessor(player);
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(inputProcessor);
        Gdx.input.setInputProcessor(multiplexer);

        //Create Table
        Table topTable = new Table();
        //Set table to fill stage
        topTable.setFillParent(true);

        // Debug lines
        if(debugUI) {
            topTable.setDebug(true);
        }


        //Set alignment of contents in the table.
        topTable.top();

        //Add listeners to buttons
        settingsButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                Gdx.app.log("Settings", "going to settings");
                game.setPreviousScreen(DungeonEscape.MAPSCREEN);
                game.changeScreen(DungeonEscape.SETTINGSSCREEN);
            }
        });

        stage.addActor(settingsButton);

        //Window window = new Window("Window", skin);
        //TextButton button = new TextButton("TRAP!", skin);


        //Add buttons and progress bar to table
        topTable.add(settingsButton).left().width(35).height(35).pad(5,10,0,0);
        topTable.add(footmarkImage).width(25).height(40).fillX().fillY().pad(5,5,0,0);
        topTable.add(stepsProgressBar).width(240).fillX().pad(5,0,0,5);
        topTable.add(stepLabel).expandX().fillX().fillY().pad(5,0,0,5);
        topTable.row();

        topTable.add(keyImage).width(40).height(40).fillX().fillY().pad(5,10,5,0);
        topTable.add(keyLabel).width(30).fillX().fillY().pad(0,0,5,5)     ;
        topTable.add(movesImage).right().width(30).height(40).fillX().fillY().pad(0,5,5,5);
        topTable.add(movesLabel).width(40).fillY().pad(0,0,5,5);
        topTable.row();
        topTable.add(sidecontrolsImage).center().height(20).width(20).pad(10,10,5,5);

        //Add table to stage
        stage.addActor(topTable);
    }
    @Override
    public void resize(int width, int height) {
        // Updates the stage viewport where font is
        stage.getViewport().update(width, height);
        // Updates game viewport
        game.getGameViewport().update(width, height);
    }

    @Override
    public void pause() {
        paused = true;
        Gdx.app.log("Mapscreen", "paused");
        saveSteps();
    }

    @Override
    public void resume() {
        paused = false;
        Gdx.app.log("Mapscreen", "resume");
        countMovementPointsDelta();
        //System.out.println(stepTotal);
    }

    private void countMovementPoints() {
        // Checks if total step amount is divisible by the amount needed to move
        if(stepTotal > 0) {
            if(stepTotal % player.STEPSTOMOVE == 0) {
                leftOverSteps = 0;
                player.addMovementPoint();
                resetProgressBar = true;
                addStep();
            }
        }
    }

    private void countMovementPointsDelta() {
        int stepsDuringPause = getStepsDelta();
        int pointsToAdd;

        pointsToAdd = stepsDuringPause / player.STEPSTOMOVE;
        // System.out.println("added steps: " + addedSteps);
        // System.out.println("background steps: " + steps);
        // System.out.println("points to add: " + pointsToAdd);

        addMultipleMovementPoints(pointsToAdd);
        leftOverSteps = 0;
    }

    private void addMultipleMovementPoints(int points) {
        player.movementPoints = player.movementPoints + points;
    }

    public void resetSteps() {
        stepTotal = 0;
    }

    // Save current step count
    public void saveSteps() {
        savedSteps = stepTotal;
    }

    // Make saved steps actual step count
    public void subtractSteps() {
        stepTotal = savedSteps;
    }

    // Get difference between total steps and saved steps
    private int getStepsDelta() {
        return stepTotal - savedSteps;
    }

    public void updateMovesLabel() {
        movesLabel.setText("" + player.movementPoints);
    }

    public void updateStepsLabel() {
        stepLabel.setText("" + stepTotal);
    }

    public void updateKeyLabel() {
        keyLabel.setText("" + keyAmount + "/" + KEYS_NEEDED);
    }
    public int getStepTotal() {
        return stepTotal;
    }

    public int getProgressbarValue() {
        return progressbarValue;
    }

    @Override
    public void hide() {
        Gdx.app.log("Mapscreen", "hidden");
    }

    @Override
    public void dispose() {
        player.dispose();
        stage.dispose();
        keyTexture.dispose();
        footMarkTexture.dispose();
        movesArrowTexture.dispose();
        tiledMapRenderer.dispose();
        mapLevel.dispose();
        game.dispose();
        skin.dispose();
        keyTexture.dispose();
        footMarkTexture.dispose();
        movesArrowTexture.dispose();
    }
}