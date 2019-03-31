package fi.tamk.rentogames.Interface;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import fi.tamk.rentogames.DungeonEscape;
import fi.tamk.rentogames.Move.MoveScreenPlayer;
import fi.tamk.rentogames.Screens.MoveScreenJump;
import fi.tamk.rentogames.Screens.MoveScreenSquat;

public class MoveScreenUI extends UI {

    // set this to true to see debug lines
    private boolean debugUI = true;

    private DungeonEscape game;
    private MoveScreenPlayer player;
    private MoveScreenJump moveScreenJump;
    private MoveScreenSquat moveScreenSquat;

    private Skin skin;
    private Stage stage;

    private ImageButton backButton;
    private Label counterLabel;
    private Label exerciseLabel;
    private TextButton skipButton;

    public MoveScreenUI(DungeonEscape game, MoveScreenPlayer player) {
        this.game = game;
        this.player = player;
        this.stage = new Stage(game.getGameViewport());
        onCreate();
    }
    private void onCreate() {


        skin = new Skin(Gdx.files.internal("uiskin.json"));

        if(game.getActiveScreen() == DungeonEscape.JUMPSCREEN) {
            exerciseLabel = new Label(game.getMyBundle().get("jumptext"), skin, "title-white");
        }
        if(game.getActiveScreen() == DungeonEscape.SQUATSCREEN) {
            exerciseLabel = new Label(game.getMyBundle().get("squattext"), skin, "title-white");
        }

        counterLabel = new Label("" + player.getCountedJumps() + "/" + player.getMovesRequired(), skin, "title-white");
        backButton = new ImageButton(skin, "left");
        skipButton = new TextButton("Skip", skin);

    }
    public void createUI() {

        // set to true to show skip screen button
        boolean testing = true;
        if(testing) {
            skipButton.addListener(new ChangeListener(){
                @Override
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    Gdx.app.log("Screen", "going to mapscreen");
                    game.changeScreen(DungeonEscape.MAPSCREEN);
                }
            });

            stage.addActor(skipButton);
        }

        backButton.setPosition(10, game.screenHeight - 60);
        exerciseLabel.setPosition(game.screenWidth / 2 - exerciseLabel.getWidth() / 2 ,game.screenHeight - 110);
        counterLabel.setPosition(game.screenWidth / 2 - counterLabel.getWidth() / 2,game.screenHeight - 170);

        backButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                Gdx.app.log("Screen", "going to mapscreen");
                game.changeScreen(DungeonEscape.MAPSCREEN);
            }
        });

        stage.addActor(backButton);
        stage.addActor(exerciseLabel);
        stage.addActor(counterLabel);
    }

    public void updateCounterLabel() {
        counterLabel.setText("" + player.getCountedJumps() + "/" + player.getMovesRequired());
    }

    public Stage getStage() {
        return this.stage;
    }

    public void dispose(){
        //stage.dispose();
        skin.dispose();
    }
}
