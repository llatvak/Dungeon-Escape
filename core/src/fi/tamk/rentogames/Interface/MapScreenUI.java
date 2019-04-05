package fi.tamk.rentogames.Interface;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import fi.tamk.rentogames.DungeonEscape;
import fi.tamk.rentogames.Framework.Save;
import fi.tamk.rentogames.Map.MapPlayer;
import fi.tamk.rentogames.Screens.MapScreen;

import static fi.tamk.rentogames.Screens.MapScreen.KEYS_NEEDED;

public class MapScreenUI {

    private boolean debugUI = false;

    private DungeonEscape game;
    private MapScreen mapScreen;
    private MapPlayer player;
    private Tutorials tutorials;

    private Skin skin;
    private Stage stage;

    //Create buttons and bars
    private ProgressBar stepsProgressBar;

    private ImageButton backButton;
    private ImageButton keyImage;
    private ImageButton footmarkImage;
    private ImageButton movesImage;
    private ImageButton movesOutImage;
    private ImageButton leftControlsImage;
    private ImageButton rightControlsImage;
    private ImageButton upControlsImage;
    private ImageButton downControlsImage;

    private Texture keyTexture;
    private Texture footMarkTexture;
    private Texture movesArrowTexture;
    private Texture movesOutArrowTexture;
    private Texture leftArrowTexture;
    private Texture rightArrowTexture;
    private Texture upArrowTexture;
    private Texture downArrowTexture;

    private Label stepLabel;
    private Label movesLabel;
    private Label keyLabel;

    private boolean resetProgressBar = false;
    private int progressbarValue = 0;

    private boolean buttonUp;

    public MapScreenUI(DungeonEscape game, MapScreen mapScreen, MapPlayer player){
        this.game = game;
        this.mapScreen = mapScreen;
        this.player = player;
        this.tutorials = new Tutorials();
        this.stage = new Stage(game.getGameViewport());
        onCreate();
    }

    private void onCreate() {
        progressbarValue = Save.getProgressBarValue();

        skin = new Skin(Gdx.files.internal("uiskin.json"));

        keyTexture = new Texture("keyicon.png");
        footMarkTexture = new Texture("footmarkicon.png");
        movesArrowTexture = new Texture("movesicon.png");
        movesOutArrowTexture = new Texture("movesouticon.png");
        leftArrowTexture = new Texture("arrowleft.png");
        rightArrowTexture = new Texture("arrowright.png");
        upArrowTexture = new Texture("arrowup.png");
        downArrowTexture = new Texture("arrowdown.png");

        //Create buttons and bars
        backButton = new ImageButton(skin, "left");
        keyImage = new ImageButton(new TextureRegionDrawable(new TextureRegion(keyTexture)));
        footmarkImage = new ImageButton(new TextureRegionDrawable(new TextureRegion(footMarkTexture)));
        movesImage = new ImageButton(new TextureRegionDrawable(new TextureRegion(movesArrowTexture)));
        movesOutImage = new ImageButton(new TextureRegionDrawable(new TextureRegion(movesOutArrowTexture)));
        upControlsImage = new ImageButton(new TextureRegionDrawable(new TextureRegion(upArrowTexture)));
        downControlsImage = new ImageButton(new TextureRegionDrawable(new TextureRegion(downArrowTexture)));
        leftControlsImage = new ImageButton(new TextureRegionDrawable(new TextureRegion(leftArrowTexture)));
        rightControlsImage = new ImageButton(new TextureRegionDrawable(new TextureRegion(rightArrowTexture)));

        stepLabel = new Label("" + mapScreen.getStepTotal(), skin,"white");
        movesLabel = new Label("" + player.movementPoints, skin,"white");
        keyLabel = new Label("" + mapScreen.keyAmount + "/" + KEYS_NEEDED, skin,"white");

        // stepsProgressBar = new ProgressBar(0, player.STEPSTOMOVE,1,false,skin, "default-horizontal");
        // stepsProgressBar.setAnimateDuration(0.5f);
    }

    public void createUI() {
        //Create Table
        Table topTable = new Table();
        Table controlsTable = new Table();
        //Set table to fill stage
        topTable.setFillParent(true);
        controlsTable.setFillParent(true);
        //Set alignment of contents in the table.
        topTable.top();
        controlsTable.center();
        // Debug lines
        if(debugUI) {
            topTable.setDebug(true);
            controlsTable.setDebug(true);
        }

        //Add listeners to buttons
        backButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                Gdx.app.log("Button", "back");
                Save.saveCurrentPlayerX(player.getX());
                Save.saveCurrentPlayerY(player.getY());
                game.setPreviousScreen(DungeonEscape.MAPSCREEN);
                game.changeScreen(DungeonEscape.MAINMENU);
            }
        });

        stage.addActor(backButton);

        //Add buttons and progress bar to table
        topTable.add(backButton).left().width(35).height(35).pad(5,15,0,0);
        topTable.add(footmarkImage).width(25).height(40).fillX().fillY().pad(5,5,0,0);
        // topTable.add(stepsProgressBar).width(240).fillX().pad(5,0,0,5);
        topTable.add(stepLabel).expandX().fillX().fillY().pad(5,0,0,5);
        topTable.row();

        topTable.add(keyImage).width(40).height(40).fillX().fillY().pad(15,15,5,0);
        topTable.add(keyLabel).width(30).fillX().fillY().pad(0,0,5,5)     ;
        topTable.add(movesImage).right().width(30).height(40).fillX().fillY().pad(10,5,5,5);
        topTable.add(movesLabel).width(40).fillY().pad(10,2,5,5);
        topTable.row();

        controlsTable.add(upControlsImage).colspan(2).center().height(20).width(20).pad(0,0,70,0);
        controlsTable.row();

        controlsTable.add(leftControlsImage).expandX().center().height(20).width(20).pad(0,0,0,0);
        controlsTable.add(rightControlsImage).expandX().center().height(20).width(20).pad(0,0,0,0);
        controlsTable.row();

        controlsTable.add(downControlsImage).colspan(2).center().height(20).width(20).pad(70,0,0,0);

        //Add table to stage
        stage.addActor(topTable);
        stage.addActor(controlsTable);
    }

    public void createConfirmButtons(final boolean onSquat, final boolean onJump){
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
                mapScreen.buttonUp = false;
                // Using boolean values checks trapscreen
                if(onSquat) {
                    mapScreen.goToSquatTrap();
                }
                if(onJump) {
                    mapScreen.goToJumpTrap();
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
                mapScreen.buttonUp = false;
                player.addMovementPoint();
            }
        });
    }

    public void changeTutorialLabel(int tutorial) {
        switch(tutorial) {
            case 1: tutorials.createFirstTutorial();
                break;
            case 2: tutorials.createSecondTutorial();
                break;
            case 3: tutorials.createThirdTutorial();
                break;
            case 4: tutorials.createFirstTutorial();
                break;
        }
    }

    public void createTutorialWindow(){
        Gdx.app.log("Tutorial", "created");

        tutorials.createTutorialTable();
        Table textTable = new Table();

        Dialog tutorialWindow = new Dialog(tutorials.getDialogTitle(),skin );
        TextButton confirmButton = new TextButton("OK!", skin );

        textTable.setDebug(false);
        textTable.add(tutorials.getLabel()).width(270f).height(90f).left();

        tutorialWindow.setMovable(false);
        tutorialWindow.setModal(true);
        tutorialWindow.setSize(300,280);
        tutorialWindow.setPosition(game.screenWidth / 2 - tutorialWindow.getWidth() / 2, game.screenHeight / 2 - 60);
        tutorialWindow.getContentTable().add(textTable);
        tutorialWindow.button(confirmButton);

        confirmButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                tutorials.createSecondTutorial();

            }
        });
        stage.addActor(tutorialWindow);
    }

    public boolean isButtonUp() {
        return buttonUp;
    }

    public int getProgressbarValue() {
        return progressbarValue;
    }

    public void addProgressBarValue(int valueToAdd) {
        progressbarValue = progressbarValue + valueToAdd;
        updateProgressBar();
    }

    public void resetProgressBar() {
        progressbarValue = 0;
        updateProgressBar();
    }

    public void updateProgressBar() {
        stepsProgressBar.setValue(progressbarValue);
    }

    public boolean redMovesIcon = false;
    public boolean whitesMovesIcon = false;

    public void setOutOfMovesIcon() {
        if(!redMovesIcon) {
            movesImage = new ImageButton(new TextureRegionDrawable(new TextureRegion(movesOutArrowTexture)));
            createUI();
            redMovesIcon = true;
            whitesMovesIcon = false;
        }
    }

    public void setMovesIcon() {
        if(!whitesMovesIcon) {
            movesImage = new ImageButton(new TextureRegionDrawable(new TextureRegion(movesArrowTexture)));
            createUI();
            whitesMovesIcon = true;
            redMovesIcon = false;
        }
    }

    public void updateMovesLabel() {
        movesLabel.setText("" + player.movementPoints);
    }

    public void updateStepsLabel() {
        stepLabel.setText("" + mapScreen.getStepTotal());
    }

    public void updateKeyLabel() {
        keyLabel.setText("" + mapScreen.getKeyAmount() + "/" + KEYS_NEEDED);
    }

    public Stage getStage() {
        return stage;
    }

    public void dispose(){
        stage.dispose();
        skin.dispose();
        keyTexture.dispose();
        footMarkTexture.dispose();
        movesArrowTexture.dispose();
        leftArrowTexture.dispose();
        rightArrowTexture.dispose();
        upArrowTexture.dispose();
        downArrowTexture.dispose();
    }
}