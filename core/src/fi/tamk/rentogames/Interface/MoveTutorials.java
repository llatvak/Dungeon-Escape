package fi.tamk.rentogames.Interface;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import fi.tamk.rentogames.DungeonEscape;

public class MoveTutorials {

    private MoveScreenUI userInterface;
    private DungeonEscape game;
    private Skin skin;
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

    public void createFirstTutorial() {
        createSpikesTutorial();
        createSpikesTutorialWindow();
    }
    public void createIntroTutorial() {
        dialogTitle = "What is that?";
        textLabel.setText("Here you can see the trap in front of you."
        );

        textLabel.setWrap(true);
        textLabel.setWidth(100);
    }
    public void createSpikesTutorial() {
        dialogTitle = "What should I do?";
        textLabel.setText("Get over the spikes by doing 3 jumps."
        );

        textLabel.setWrap(true);
        textLabel.setWidth(100);
    }
    public void createPhoneTutorial() {
        dialogTitle = "Are you sure this works?";
        textLabel.setText("To maximize the accuracy of the sensor hold your phone in upright position so that the screen is facing towards you."
        );
        textLabel.setWrap(true);
        textLabel.setWidth(100);
    }
    public void createActionTutorial() {
        dialogTitle = "Time to exercise!";
        textLabel.setText("Let’s go!"
        );
        textLabel.setWrap(true);
        textLabel.setWidth(100);
    }

    public void createIntroTutorialWindow(){
        Gdx.app.log("Tutorial", "intro");
        Table textTable = new Table();

        Dialog tutorialWindow = new Dialog(getDialogTitle(),skin );
        TextButton confirmButton = new TextButton("OK!", skin );

        textTable.setDebug(false);
        textTable.add(getLabel()).width(270f).height(90f).left();

        tutorialWindow.setMovable(false);
        tutorialWindow.setModal(true);
        tutorialWindow.setSize(300,180);
        tutorialWindow.setPosition(game.screenWidth / 2 - tutorialWindow.getWidth() / 2, 250);
        tutorialWindow.getContentTable().add(textTable);
        tutorialWindow.button(confirmButton);

        confirmButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                createSpikesTutorial();
                createSpikesTutorialWindow();

            }
        });
        stage.addActor(tutorialWindow);
    }

    public void createSpikesTutorialWindow(){
        Gdx.app.log("Tutorial", "spikes");
        Table textTable = new Table();

        Texture arrowTexture = new Texture("arrowup.png");
        ImageButton arrowImage = new ImageButton(new TextureRegionDrawable(new TextureRegion(arrowTexture)));
        Dialog tutorialWindow = new Dialog(getDialogTitle(),skin );
        TextButton confirmButton = new TextButton("OK!", skin );

        textTable.setDebug(true);
        textTable.add(getLabel()).width(270f).height(50f).left();
       // textTable.row();
        //textTable.add(arrowImage).width(50).height(50);

        tutorialWindow.setMovable(false);
        tutorialWindow.setModal(true);
        tutorialWindow.setSize(300,150);
        tutorialWindow.setPosition(game.screenWidth / 2 - tutorialWindow.getWidth() / 2, 280);
        tutorialWindow.getContentTable().add(textTable);
        tutorialWindow.button(confirmButton);

        confirmButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                createPhoneTutorial();
                createPhoneTutorialWindow();

            }
        });
        stage.addActor(tutorialWindow);
    }

    public void createPhoneTutorialWindow(){
        Gdx.app.log("Tutorial", "phone");
        Table textTable = new Table();

        Texture arrowTexture = new Texture("arrowup.png");
        ImageButton arrowImage = new ImageButton(new TextureRegionDrawable(new TextureRegion(arrowTexture)));
        Dialog tutorialWindow = new Dialog(getDialogTitle(),skin );
        TextButton confirmButton = new TextButton("OK!", skin );

        textTable.setDebug(false);
        textTable.add(getLabel()).width(270f).height(90f).left();
       // textTable.row();
       // textTable.add(arrowImage).width(50).height(50);

        tutorialWindow.setMovable(false);
        tutorialWindow.setModal(true);
        tutorialWindow.setSize(300,270);
        tutorialWindow.setPosition(game.screenWidth / 2 - tutorialWindow.getWidth() / 2, 300);
        tutorialWindow.getContentTable().add(textTable);
        tutorialWindow.button(confirmButton);

        confirmButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
               // createActionTutorial();
               // createActionTutorialWindow();

            }
        });
        stage.addActor(tutorialWindow);
    }

    public void createActionTutorialWindow(){
        Gdx.app.log("Tutorial", "action");
        Table textTable = new Table();

        Texture arrowTexture = new Texture("arrowup.png");
        ImageButton arrowImage = new ImageButton(new TextureRegionDrawable(new TextureRegion(arrowTexture)));
        Dialog tutorialWindow = new Dialog(getDialogTitle(),skin );
        TextButton confirmButton = new TextButton("OK!", skin );

        textTable.setDebug(false);
        textTable.add(getLabel()).width(270f).height(30f);
        // textTable.row();
        // textTable.add(arrowImage).width(50).height(50);

        tutorialWindow.setMovable(false);
        tutorialWindow.setModal(true);
        tutorialWindow.setSize(300,150);
        tutorialWindow.setPosition(game.screenWidth / 2 - tutorialWindow.getWidth() / 2, 300);
        tutorialWindow.getContentTable().add(textTable);
        tutorialWindow.button(confirmButton);

    }
    public String getDialogTitle() {
        return this.dialogTitle;
    }
    public Label getLabel() {
        return this.textLabel;
    }

}
