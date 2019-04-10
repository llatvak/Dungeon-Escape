package fi.tamk.rentogames.Interface;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import fi.tamk.rentogames.DungeonEscape;

public class Story {

    private DungeonEscape game;
    private MapScreenUI userInterface;

    private Skin skin;
    private Label textLabel;
    private String dialogTitle;
    private Stage stage;

    public Story(DungeonEscape game,MapScreenUI userInterface) {
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));
        this.game = game;
        this.userInterface = userInterface;
        this.stage = userInterface.getStage();
        textLabel = new Label("",skin);
    }

    public void createStoryPart(int tutorial) {
        switch(tutorial) {
            case 1: createStoryTextOne();
                    createStoryWindowOne();
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
        }
    }

    public void createStoryTextOne() {
        dialogTitle = " ";
        textLabel.setText("You have been imprisoned with no memory of the past. \n" +
                "You have managed to free yourself from the cell but now you must find a way to escape the dungeon. \n" +
                "Maybe you will regain your memory along the way.\n" + "You have been imprisoned with no memory of the past. \n" +
                "You have managed to free yourself from the cell but now you must find a way to escape the dungeon. \n" +
                "Maybe you will regain your memory along the way."
        );

        textLabel.setWrap(true);
    }
    public void createStoryTextTwo() {
        dialogTitle = "How to move.. ";
        textLabel.setText("You can move by tapping or pressing on the direction you want to go. To move upwards press the top indicator near the character."
        );

        textLabel.setWrap(true);
    }
    public void createStoryTextThree() {
        dialogTitle = "How to proceed.. ";
        textLabel.setText("Your goal is to find three keys to unlock the door to the next level. On top left you can see the key count."
        );
        textLabel.setWrap(true);
    }

    public void createStoryWindowOne(){
        Gdx.app.log("Story", "part one");
        Table textTable = new Table();


        Texture storyTexture = new Texture("story1.png");
        Drawable windowbg = new TextureRegionDrawable(storyTexture);
        Dialog tutorialWindow = new Dialog(getDialogTitle(),skin );
        TextButton confirmButton = new TextButton("Return", skin );

        tutorialWindow.setBackground(windowbg);
        textTable.setDebug(false);
        textTable.add(getLabel()).width(330).height(330).center();

        tutorialWindow.setMovable(false);
        tutorialWindow.setModal(true);
        tutorialWindow.setSize(360,640);
        tutorialWindow.setPosition(0, 0);
        tutorialWindow.getContentTable().add(textTable);
        tutorialWindow.button(confirmButton);

        confirmButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){


            }
        });
        stage.addActor(tutorialWindow);
    }

    public void createStoryWindowTwo(){
        Gdx.app.log("Story", "part two");
        Table textTable = new Table();


        Texture storyTexture = new Texture("story1bg.png");
        Drawable windowbg = new TextureRegionDrawable(storyTexture);
        Dialog tutorialWindow = new Dialog(getDialogTitle(),skin );
        TextButton confirmButton = new TextButton("OK!", skin );

        tutorialWindow.setBackground(windowbg);
        textTable.setDebug(false);
        textTable.add(getLabel()).width(330).height(330).center();

        tutorialWindow.setMovable(false);
        tutorialWindow.setModal(true);
        tutorialWindow.setSize(360,640);
        tutorialWindow.setPosition(0, 0);
        tutorialWindow.getContentTable().add(textTable);
        tutorialWindow.button(confirmButton);

        confirmButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){


            }
        });
        stage.addActor(tutorialWindow);
    }

    public void createStoryWindowThree(){
        Gdx.app.log("Story", "part three");
        Table textTable = new Table();


        Texture storyTexture = new Texture("story1bg.png");
        Drawable windowbg = new TextureRegionDrawable(storyTexture);
        Dialog tutorialWindow = new Dialog(getDialogTitle(),skin );
        TextButton confirmButton = new TextButton("OK!", skin );

        tutorialWindow.setBackground(windowbg);
        textTable.setDebug(false);
        textTable.add(getLabel()).width(330).height(330).center();

        tutorialWindow.setMovable(false);
        tutorialWindow.setModal(true);
        tutorialWindow.setSize(360,640);
        tutorialWindow.setPosition(0, 0);
        tutorialWindow.getContentTable().add(textTable);
        tutorialWindow.button(confirmButton);

        confirmButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){


            }
        });
        stage.addActor(tutorialWindow);
    }

    public String getDialogTitle() {
        return this.dialogTitle;
    }
    public Label getLabel() {
        return this.textLabel;
    }
}
