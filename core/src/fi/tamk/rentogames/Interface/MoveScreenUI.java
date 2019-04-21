package fi.tamk.rentogames.Interface;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import fi.tamk.rentogames.DungeonEscape;
import fi.tamk.rentogames.Move.MoveScreenPlayer;
import fi.tamk.rentogames.Screens.MapScreen;

/**
 * Creates user interface for move screen.
 *
 * Creates the textures, buttons and labels for movement(exercise) screen.
 * Designates their size, position and functionality.
 *
 * @author Miko Kauhanen
 * @author Lauri Latva-Kyyny
 * @version 1.0
 */
public class MoveScreenUI {

    private DungeonEscape game;

    private MapScreen mapScreen;

    private MoveScreenPlayer player;

    private Skin skin;

    private Stage stage;

    private ImageButton backButton;

    private TextButton skipButton;

    /**
     * Text for amount of exercises done
     */
    private Label counterLabel;

    /**
     * Text for current exercise
     */
    private Label exerciseLabel;

    /**
     * Constructor that receives the main game object, player object and map screen object.
     * Creates stage. Calls create.
     *
     * @param game main game object
     * @param player move screen player object
     * @param mapScreen map screen object
     */
    public MoveScreenUI(DungeonEscape game, MoveScreenPlayer player, MapScreen mapScreen) {
        this.game = game;
        this.player = player;
        this.mapScreen = mapScreen;
        this.stage = new Stage(game.getGameViewport());
        onCreate();
    }
    /**
     * Creates all static buttons and labels.
     *
     *<p>
     * Creates skin from assets. Creates all buttons and labels used in the user interface.
     *</p>
     */
    private void onCreate() {
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        // Changes labels depending on which move screen game is in
        if(game.getActiveScreen() == DungeonEscape.JUMPSCREEN) {
            exerciseLabel = new Label(game.getMyBundle().get("jumptext"), skin, "title-white");
        }
        if(game.getActiveScreen() == DungeonEscape.SQUATSCREEN) {
            exerciseLabel = new Label(game.getMyBundle().get("squattext"), skin, "title-white");
        }

        // Amount of jumps or squats
        counterLabel = new Label("" + player.getCountedJumps() + "/" + player.getMovesRequired(), skin, "title-white");
        // Go back to map without going over trap
        backButton = new ImageButton(skin, "left");
        // Skip trap and go to map
        skipButton = new TextButton(game.getMyBundle().get("skipbutton"), skin);
    }

    /**
     * Sets user interface elements to screen.
     *
     * <p>
     * Set buttons and labels sizes and positions and adds listeners to buttons.
     * Adds them stage as actors.
     * </p>
     */
    public void createUI() {
        skipButton.setPosition(game.screenWidth - skipButton.getWidth() - 5, game.screenHeight - skipButton.getHeight() - 5);

        skipButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                if(mapScreen.getMapPlayer().movementPoints < 6) {
                    System.out.println(mapScreen.getMapPlayer().movementPoints);
                    createSkipWarningWindow();
                } else {
                    mapScreen.getMapPlayer().removeMultipleMovementPoints(5);
                    game.setScreen(mapScreen);
                }
            }
        });

        stage.addActor(skipButton);


        backButton.setPosition(5, game.screenHeight - 45);
        backButton.setSize(35,35);
        exerciseLabel.setPosition(game.screenWidth / 2 - exerciseLabel.getWidth() / 2,game.screenHeight - 110);
        counterLabel.setPosition(game.screenWidth / 2 - counterLabel.getWidth() / 2,game.screenHeight - 170);

        backButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                Gdx.app.log("Screen", "going to mapscreen");
                mapScreen.getMapPlayer().moveToPreviousTile();
                game.setScreen(mapScreen);
            }
        });

        stage.addActor(backButton);
        stage.addActor(exerciseLabel);
        stage.addActor(counterLabel);
    }

    /**
     * Updates jump or squat count label
     */
    public void updateCounterLabel() {
        counterLabel.setText("" + player.getCountedJumps() + "/" + player.getMovesRequired());
    }

    public Stage getStage() {
        return this.stage;
    }

    private void createSkipWarningWindow() {
        Table skipWarningTable = new Table();
        final Dialog skipWarningWindow = new Dialog(game.getMyBundle().get("skiplabel"),skin );
        Label createdText = new Label(game.getMyBundle().get("skipwarningtext"),skin);
        createdText.setAlignment(Align.center);
        createdText.setWrap(true);
        TextButton confirmButton = new TextButton("OK!", skin );

        skipWarningTable.setDebug(false);
        skipWarningTable.row();
        skipWarningTable.add(createdText).width(330);

        final Table table = new Table();
        table.setFillParent(true);

        skipWarningWindow.setMovable(false);
        skipWarningWindow.setModal(true);
        skipWarningWindow.setSize(340,300);
        skipWarningWindow.setPosition(game.screenWidth / 2 - skipWarningWindow.getWidth() / 2, 200);
        skipWarningWindow.getContentTable().add(table);
        skipWarningWindow.getContentTable().add(skipWarningTable);
        skipWarningWindow.button(confirmButton);

        confirmButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                skipWarningWindow.remove();
            }
        });
        stage.addActor(skipWarningWindow);
    }

    public void dispose(){
        skin.dispose();
    }
}
