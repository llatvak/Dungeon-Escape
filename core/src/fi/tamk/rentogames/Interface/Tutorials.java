package fi.tamk.rentogames.Interface;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class Tutorials {

    private Skin skin;
    private Table textTable;
    private Label textLabel;
    private String dialogTitle;

    public Tutorials() {
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));
        textLabel = new Label("",skin);
    }

    public void createTutorialTable( ) {
        Table textTable = new Table();
        textTable.add(textLabel).width(270f).height(90f).left();
    }
    public void createFirstTutorial() {
        dialogTitle = "You're finally awake.. ";
        textLabel.setText("You have been imprisoned with no memory of the past. " +
                "You have managed to free yourself from your cell but now you must find a way to escape the dungeon. "
        );

        textLabel.setWrap(true);
        textLabel.setWidth(100);
    }
    public void createSecondTutorial() {
        dialogTitle = "How to move.. ";
        textLabel.setText("You can move by tapping or pressing on the direction you want to go. To move upwards press the top indicator near the character."
        );

        textLabel.setWrap(true);
        textLabel.setWidth(100);
    }
    public void createThirdTutorial() {
        dialogTitle = "How to proceed.. ";
        textLabel.setText("Your goal is to find three keys to unlock the door to the next level. On top left you can see the key count."
        );

        textLabel.setWrap(true);
        textLabel.setWidth(100);
    }
    public Table getTable() {
        return this.textTable;
    }
    public String getDialogTitle() {
        return this.dialogTitle;
    }
    public Label getLabel() {
        return this.textLabel;
    }

}
