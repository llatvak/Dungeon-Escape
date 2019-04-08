package fi.tamk.rentogames.Interface;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import fi.tamk.rentogames.DungeonEscape;

public class Tutorials {

    private MapScreenUI userInterface;
    private DungeonEscape game;
    private Skin skin;
    private Table textTable;
    private Label textLabel;
    private String dialogTitle;
    private Stage stage;

    private float windowHeightPos = 70f;

    public Tutorials(DungeonEscape game, MapScreenUI userInterface) {
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));
        this.game = game;
        this.userInterface = userInterface;
        this.stage = userInterface.getStage();
        textLabel = new Label("",skin);
    }
    public void changeTutorialLabel(int tutorial) {
        switch(tutorial) {
            case 1: createTutorialOne();
                createTutorialWindowOne();
                break;
            case 2: createTutorialTwo();
                createTutorialWindowTwo();
                break;
            case 3: createTutorialThree();
                createTutorialWindowThree();
                break;
            case 4: createTutorialFour();
                createTutorialWindowFour();
                break;
            case 5: createTutorialFive();
                createTutorialWindowFive();
                break;
            case 6: createNoKeysInfo();
                createNoKeysWindow();
                break;
        }
    }

    public void createTutorialOne() {
        dialogTitle = "You're finally awake.. ";
        textLabel.setText("You have been imprisoned with no memory of the past. \n" +
                "You have managed to free yourself from the cell but now you must find a way to escape the dungeon. \n" +
                "Maybe you will regain your memory along the way."
        );

        textLabel.setWrap(true);
        textLabel.setWidth(100);
    }
    public void createTutorialOneNext() {
        dialogTitle = "How to move.. ";
        textLabel.setText("You can move by tapping or pressing on the direction you want to go. To move upwards press the top indicator near the character."
        );

        textLabel.setWrap(true);
        textLabel.setWidth(100);
    }
    public void createTutorialTwo() {
        dialogTitle = "How to proceed.. ";
        textLabel.setText("Your goal is to find three keys to unlock the door to the next level. On top left you can see the key count."
        );
        textLabel.setWrap(true);
        textLabel.setWidth(100);
    }
    public void createTutorialThree() {
        dialogTitle = "So many numbers..";
        textLabel.setText("Movement requires that you have movement points. These are shown on the top right of the screen next to the four-way arrow icon."
        );
        textLabel.setWrap(true);
        textLabel.setWidth(100);
    }
    public void createTutorialThreeNext() {
        dialogTitle = "I need to... walk?! ";
        textLabel.setText("To gather movement points you need to walk in real life. For every X amount of steps you will receive 5 movement points."
        );
        textLabel.setWrap(true);
        textLabel.setWidth(100);
    }
    public void createTutorialFour() {
        dialogTitle = "I smell danger... or is that sweat? ";
        textLabel.setText("Watch out! The dungeon is riddled with traps like the one in front of you. \n" +
                "To get past these traps you need to either jump or squat multiple times.\n" +
                "If you don’t feel like doing the exercises right now you can also skip the trap by spending X amount of movement points."
        );
        textLabel.setWrap(true);
        textLabel.setWidth(100);
    }
    public void createTutorialFive() {
        dialogTitle = "Escape!";
        textLabel.setText("Now get going, the guards are coming. You must escape!"
        );
        textLabel.setWrap(true);
        textLabel.setWidth(100);
    }
    public void createNoKeysInfo() {
        dialogTitle = "Not enough keys.";
        textLabel.setText("You need to collect all 3 keys to open the door."
        );
        textLabel.setWrap(true);
        textLabel.setWidth(100);
    }

    public void createTutorialWindowOne(){
        Gdx.app.log("Tutorial", "created");
        Table textTable = new Table();

        Dialog tutorialWindow = new Dialog(getDialogTitle(),skin );
        TextButton confirmButton = new TextButton("OK!", skin );

        textTable.setDebug(false);
        textTable.add(getLabel()).width(270f).height(90f).left();

        tutorialWindow.setMovable(false);
        tutorialWindow.setModal(true);
        tutorialWindow.setSize(300,280);
        tutorialWindow.setPosition(game.screenWidth / 2 - tutorialWindow.getWidth() / 2, 10);
        tutorialWindow.getContentTable().add(textTable);
        tutorialWindow.button(confirmButton);

        confirmButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                createTutorialOneNext();
                createSecondTutorialWindow();

            }
        });
        stage.addActor(tutorialWindow);
    }

    public void createSecondTutorialWindow(){
        Gdx.app.log("Tutorial", "intro");
        Table textTable = new Table();

        Dialog tutorialWindow = new Dialog(getDialogTitle(),skin );
        TextButton confirmButton = new TextButton("OK!", skin );

        textTable.setDebug(false);
        textTable.add(getLabel()).width(270f).height(90f).left();

        tutorialWindow.setMovable(false);
        tutorialWindow.setModal(true);
        tutorialWindow.setSize(300,270);
        tutorialWindow.setPosition(game.screenWidth / 2 - tutorialWindow.getWidth() / 2, 10);
        tutorialWindow.getContentTable().add(textTable);
        tutorialWindow.button(confirmButton);

        stage.addActor(tutorialWindow);
    }

    public void createTutorialWindowTwo(){
        Gdx.app.log("Tutorial", "character movement");
        Table textTable = new Table();

        Dialog tutorialWindow = new Dialog(getDialogTitle(),skin );
        TextButton confirmButton = new TextButton("OK!", skin );

        textTable.setDebug(false);
        textTable.add(getLabel()).width(270f).height(90f).left();

        tutorialWindow.setMovable(false);
        tutorialWindow.setModal(true);
        tutorialWindow.setSize(300,250);
        tutorialWindow.setPosition(game.screenWidth / 2 - tutorialWindow.getWidth() / 2, game.screenHeight / 2 - windowHeightPos);
        tutorialWindow.getContentTable().add(textTable);
        tutorialWindow.button(confirmButton);

        stage.addActor(tutorialWindow);
    }
    public void createTutorialWindowThree(){
        Gdx.app.log("Tutorial", "movement points");
        Table textTable = new Table();

        Dialog tutorialWindow = new Dialog(getDialogTitle(),skin );
        TextButton confirmButton = new TextButton("OK!", skin );

        textTable.setDebug(false);
        textTable.add(getLabel()).width(270f).height(90f).left();

        tutorialWindow.setMovable(false);
        tutorialWindow.setModal(true);
        tutorialWindow.setSize(300,210);
        tutorialWindow.setPosition(game.screenWidth / 2 - tutorialWindow.getWidth() / 2, game.screenHeight / 2 - windowHeightPos);
        tutorialWindow.getContentTable().add(textTable);
        tutorialWindow.button(confirmButton);

        confirmButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                createTutorialThreeNext();
                createTutorialWindowThreeNext();
            }
        });
        stage.addActor(tutorialWindow);
    }
    public void createTutorialWindowThreeNext(){
        Gdx.app.log("Tutorial", "walking");
        Table textTable = new Table();

        Dialog tutorialWindow = new Dialog(getDialogTitle(),skin );
        TextButton confirmButton = new TextButton("OK!", skin );

        textTable.setDebug(false);
        textTable.add(getLabel()).width(270f).height(90f).left();

        tutorialWindow.setMovable(false);
        tutorialWindow.setModal(true);
        tutorialWindow.setSize(300,210);
        tutorialWindow.setPosition(game.screenWidth / 2 - tutorialWindow.getWidth() / 2, game.screenHeight / 2 - windowHeightPos);
        tutorialWindow.getContentTable().add(textTable);
        tutorialWindow.button(confirmButton);

        stage.addActor(tutorialWindow);
    }
    public void createTutorialWindowFour(){
        Gdx.app.log("Tutorial", "traps");
        Table textTable = new Table();

        Dialog tutorialWindow = new Dialog(getDialogTitle(),skin );
        TextButton confirmButton = new TextButton("OK!", skin );

        textTable.setDebug(false);
        textTable.add(getLabel()).width(270f).height(90f).left();

        tutorialWindow.setMovable(false);
        tutorialWindow.setModal(true);
        tutorialWindow.setSize(300,350);
        tutorialWindow.setPosition(game.screenWidth / 2 - tutorialWindow.getWidth() / 2, game.screenHeight / 2 - windowHeightPos - 100f);
        tutorialWindow.getContentTable().add(textTable);
        tutorialWindow.button(confirmButton);

        stage.addActor(tutorialWindow);
    }

    public void createTutorialWindowFive(){
        Gdx.app.log("Tutorial", "go");
        Table textTable = new Table();

        Dialog tutorialWindow = new Dialog(getDialogTitle(),skin );
        TextButton confirmButton = new TextButton("OK!", skin );

        textTable.setDebug(false);
        textTable.add(getLabel()).width(270f).height(90f).left();

        tutorialWindow.setMovable(false);
        tutorialWindow.setModal(true);
        tutorialWindow.setSize(300,190);
        tutorialWindow.setPosition(game.screenWidth / 2 - tutorialWindow.getWidth() / 2, game.screenHeight / 2 - windowHeightPos);
        tutorialWindow.getContentTable().add(textTable);
        tutorialWindow.button(confirmButton);

        stage.addActor(tutorialWindow);
    }

    public void createNoKeysWindow(){
        Gdx.app.log("Tutorial", "created");
        Table textTable = new Table();

        Dialog tutorialWindow = new Dialog(getDialogTitle(),skin );
        TextButton confirmButton = new TextButton("OK!", skin );

        textTable.setDebug(false);
        textTable.add(getLabel()).width(270f).height(90f).left().pad(2,2,2,2);

        tutorialWindow.setMovable(false);
        tutorialWindow.setModal(true);
        tutorialWindow.setSize(300,190);
        tutorialWindow.setPosition(game.screenWidth / 2 - tutorialWindow.getWidth() / 2, 10);
        tutorialWindow.getContentTable().add(textTable);
        tutorialWindow.button(confirmButton);

        stage.addActor(tutorialWindow);
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
