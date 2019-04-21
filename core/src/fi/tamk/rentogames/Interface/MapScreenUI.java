package fi.tamk.rentogames.Interface;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import fi.tamk.rentogames.DungeonEscape;
import fi.tamk.rentogames.Map.MapPlayer;
import fi.tamk.rentogames.Screens.MapScreen;

import static fi.tamk.rentogames.Screens.MapScreen.KEYS_NEEDED;

/**
 * Creates user interface for map screen.
 *
 * Creates the textures, buttons and labels for map screen.
 * Designates their size, position and functionality.
 *
 * @author Miko Kauhanen
 * @author Lauri Latva-Kyyny
 * @version 1.0
 */
public class MapScreenUI {

    private DungeonEscape game;

    private MapScreen mapScreen;

    private MapPlayer player;

    private Skin skin;

    private Stage stage;

    // Buttons
    private ImageButton backButton;
    private ImageButton keyImage;
    private ImageButton footMarkImage;
    private ImageButton movesImage;
    private ImageButton leftControlsImage;
    private ImageButton rightControlsImage;
    private ImageButton upControlsImage;
    private ImageButton downControlsImage;

    // Textures
    private Texture keyTexture;
    private Texture footMarkTexture;
    private Texture movesArrowTexture;
    private Texture movesOutArrowTexture;
    private Texture leftArrowTexture;
    private Texture rightArrowTexture;
    private Texture upArrowTexture;
    private Texture downArrowTexture;

    //Text for UI elements
    private Label stepLabel;
    private Label movesLabel;
    private Label keyLabel;

    /**
     * Show red icon for movement points.
     */
    private boolean redMovesIcon;

    /**
     * Show white icon for movement points.
     */
    private boolean whitesMovesIcon;

    /**
     * Are trap confirmation buttons up.
     */
    private boolean buttonsUp;

    /**
     * Is back button initialized.
     */
    private boolean backButtonInitialized = false;

    /**
     * Constructor that receives the main game object,  map screen object and player object.
     * Creates stage. Calls create.
     *
     * @param game main game object
     * @param mapScreen map screen object
     * @param player player object
     */
    public MapScreenUI(DungeonEscape game, MapScreen mapScreen, MapPlayer player){
        this.game = game;
        this.mapScreen = mapScreen;
        this.player = player;

        // Get stage from main game
        this.stage = new Stage(game.getGameViewport());
        onCreate();
    }

    /**
     * Creates all static buttons and labels.
     *
     *<p>
     * Creates skin from assets. Creates all textures, buttons and labels used in the user interface.
     *</p>
     */
    private void onCreate() {
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        keyTexture = new Texture("keyicon.png");
        footMarkTexture = new Texture("footmarkicon.png");
        movesArrowTexture = new Texture("movesicon.png");
        movesOutArrowTexture = new Texture("movesouticon.png");
        leftArrowTexture = new Texture("arrowleft.png");
        rightArrowTexture = new Texture("arrowright.png");
        upArrowTexture = new Texture("arrowup.png");
        downArrowTexture = new Texture("arrowdown.png");

        //Create buttons
        backButton = new ImageButton(skin, "left");
        keyImage = new ImageButton(new TextureRegionDrawable(new TextureRegion(keyTexture)));
        footMarkImage = new ImageButton(new TextureRegionDrawable(new TextureRegion(footMarkTexture)));
        movesImage = new ImageButton(new TextureRegionDrawable(new TextureRegion(movesArrowTexture)));
        upControlsImage = new ImageButton(new TextureRegionDrawable(new TextureRegion(upArrowTexture)));
        downControlsImage = new ImageButton(new TextureRegionDrawable(new TextureRegion(downArrowTexture)));
        leftControlsImage = new ImageButton(new TextureRegionDrawable(new TextureRegion(leftArrowTexture)));
        rightControlsImage = new ImageButton(new TextureRegionDrawable(new TextureRegion(rightArrowTexture)));

        stepLabel = new Label("" + mapScreen.getStepTotal(), skin,"white");
        movesLabel = new Label("" + player.movementPoints, skin,"white");
        keyLabel = new Label("" + mapScreen.keyAmount + "/" + KEYS_NEEDED, skin,"white");

    }

    /**
     * Sets user interface elements to screen.
     *
     * <p>
     * Creates two tables to hold all UI elements. Set their sizes and positions and adds listeners to buttons.
     * One table holds all buttons and information elements. Other holds the controller buttons.
     * Adds tables to stage as actors.
     * </p>
     */
    public void createUI() {
        //Create Table
        Table topTable = new Table();
        Table controlsTable = new Table();
        //Set table to fill stage
        topTable.setFillParent(true);
        controlsTable.setFillParent(true);
        //Set alignment of contents in the table.
        topTable.top();
        controlsTable.bottom();

        // Debug lines
        topTable.setDebug(false);
        controlsTable.setDebug(false);

        //Add listeners to buttons
        backButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                if(!backButtonInitialized) {
                    game.setPreviousScreen(DungeonEscape.MAPSCREEN);
                    game.changeScreen(DungeonEscape.MAINMENU);
                }
                backButtonInitialized = true;
            }
        });
        upControlsImage.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                if(!player.moving && player.allowMovement) {
                    player.setUpMove();
                }
            }
        });
        downControlsImage.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                if(!player.moving && player.allowMovement) {
                    player.setDownMove();
                }
            }
        });
        leftControlsImage.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                if(!player.moving && player.allowMovement) {
                    player.setLeftMove();
                }
            }
        });
        rightControlsImage.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                if(!player.moving && player.allowMovement) {
                    player.setRightMove();
                }
            }
        });

        stage.addActor(backButton);

        //Add buttons to table
        topTable.add(backButton).left().width(35).height(35).pad(5,15,0,0);
        topTable.add(footMarkImage).colspan(2).right().expandX().width(25).height(40).fillX().fillY().pad(5,5,0,0);

        topTable.add(stepLabel).fillX().fillY().pad(5,0,0,5);
        topTable.row();

        topTable.add(keyImage).width(40).height(40).fillX().fillY().pad(15,15,5,0);
        topTable.add(keyLabel).width(30).fillX().fillY().pad(0,0,5,5);

        topTable.add(movesImage).expandX().right().width(30).height(40).fillX().fillY().pad(10,5,5,5);
        topTable.add(movesLabel).width(40).fillY().pad(10,2,5,5);
        topTable.row();

        float arrowSize = 45;
        controlsTable.add(upControlsImage).colspan(2).center().height(arrowSize).width(arrowSize).pad(0,0,30,0);
        controlsTable.row();

        controlsTable.add(leftControlsImage).expandX().right().height(arrowSize).width(arrowSize).pad(0,0,0,50);
        controlsTable.add(rightControlsImage).expandX().left().height(arrowSize).width(arrowSize).pad(0,50,0,0);
        controlsTable.row();

        controlsTable.add(downControlsImage).colspan(2).center().height(arrowSize).width(arrowSize).pad(30,0,0,0);

        //Add tables to stage
        stage.addActor(topTable);
        stage.addActor(controlsTable);
    }

    /**
     * Creates trap confirmation buttons.
     *
     * <p>
     * Creates the buttons and label that show up when player is on top of a trap.
     * Set their sizes and positions and adds listeners to buttons.
     * </p>
     *
     * @param onSquat is player on arrow trap
     * @param onJump is player on spikes trap
     */
    public void createConfirmButtons(final boolean onSquat, final boolean onJump){
        buttonsUp = true;

        final TextButton confirmButton = new TextButton(game.getMyBundle().get("confirmbutton"), skin);
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
                confirmButton.remove();
                cancelButton.remove();
                trapLabel.remove();
                readyLabel.remove();
                buttonsUp = false;
                mapScreen.trapButtonsUp = false;
                // Using boolean values checks trapscreen
                if(onSquat) {
                    mapScreen.goToSquatTrap();
                }
                if(onJump) {
                    mapScreen.goToJumpTrap();
                }
                player.addOneMovementPoint();
            }
        });

        cancelButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                confirmButton.remove();
                cancelButton.remove();
                trapLabel.remove();
                readyLabel.remove();
                buttonsUp = false;
                mapScreen.trapButtonsUp = false;
                player.moveToPreviousTile();
                player.addOneMovementPoint();
            }
        });
    }

    /**
     * Changes movement point icon if no movement points remaining.
     */
    public void setOutOfMovesIcon() {
        if(!redMovesIcon) {
            movesImage = new ImageButton(new TextureRegionDrawable(new TextureRegion(movesOutArrowTexture)));
            createUI();
            redMovesIcon = true;
            whitesMovesIcon = false;
        }
    }

    /**
     * Changes movement point icon when player has movement points remaining.
     */
    public void setMovesIcon() {
        if(!whitesMovesIcon) {
            movesImage = new ImageButton(new TextureRegionDrawable(new TextureRegion(movesArrowTexture)));
            createUI();
            whitesMovesIcon = true;
            redMovesIcon = false;
        }
    }

    /**
     * Updates movement points amount on screen.
     */
    public void updateMovesLabel() {
        movesLabel.setText("" + player.movementPoints);
    }

    /**
     * Updates steps amount on screen.
     */
    public void updateStepsLabel() {
        stepLabel.setText("" + mapScreen.getStepTotal());
    }

    /**
     * Updates keys amount on screen.
     */
    public void updateKeyLabel() {
        keyLabel.setText("" + mapScreen.getKeyAmount() + "/" + KEYS_NEEDED);
    }

    /**
     * Are trap confirm buttons up.
     *
     * @return are buttons up
     */
    public boolean isButtonUp() {
        return buttonsUp;
    }

    public Stage getStage() {
        return stage;
    }

    public void setBackButtonInitialized(boolean initialized) {
        backButtonInitialized = initialized;
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