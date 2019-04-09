package fi.tamk.rentogames.Interface;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import fi.tamk.rentogames.DungeonEscape;

public class MoveTutorials {

    private MoveScreenUI userInterface;
    private DungeonEscape game;
    private Skin skin;
    private Table textTable;
    private Label textLabel;
    private String dialogTitle;
    private Stage stage;

    public MoveTutorials(DungeonEscape game, MoveScreenUI userInterface) {
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));
        this.game = game;
        this.userInterface = userInterface;
        this.stage = userInterface.getStage();
        textLabel = new Label("",skin);
    }
    public void createIntroTutorial() {
        dialogTitle = "You're finally awake.. ";
        textLabel.setText("You have been imprisoned with no memory of the past. \n" +
                "You have managed to free yourself from the cell but now you must find a way to escape the dungeon. \n" +
                "Maybe you will regain your memory along the way."
        );

        textLabel.setWrap(true);
        textLabel.setWidth(100);
    }
    public void createCharacterTutorial() {
        dialogTitle = "How to move.. ";
        textLabel.setText("You can move by tapping or pressing on the direction you want to go. To move upwards press the top indicator near the character."
        );

        textLabel.setWrap(true);
        textLabel.setWidth(100);
    }
    public void createKeysTutorial() {
        dialogTitle = "How to proceed.. ";
        textLabel.setText("Your goal is to find three keys to unlock the door to the next level. On top left you can see the key count."
        );
        textLabel.setWrap(true);
        textLabel.setWidth(100);
    }
    public void createMovementPointsTutorial() {
        dialogTitle = "So many numbers..";
        textLabel.setText("Movement requires that you have movement points. These are shown on the top right of the screen next to the four-way arrow icon."
        );
        textLabel.setWrap(true);
        textLabel.setWidth(100);
    }

}
