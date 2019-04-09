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
            case 1: createIntroTutorial();
                    createIntroTutorialWindow();
                break;
            case 2: createKeysTutorial();
                    createKeysTutorialWindow();
                break;
            case 3: createMovementPointsTutorial();
                    createMovementPointsTutorialWindow();
                break;
            case 4: createTrapsTutorial();
                    createTrapsTutorialWindow();
                break;
            case 5: createGoalTutorial();
                    createGoalTutorialWindow();
                break;
            case 6: createNoKeysInfo();
                    createNoKeysWindow();
                break;
        }
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
    public void createStepsTutorial() {
        dialogTitle = "I need to... walk?! ";
        textLabel.setText("To gather movement points you need to walk in real life. For every X amount of steps you will receive 5 movement points."
        );
        textLabel.setWrap(true);
        textLabel.setWidth(100);
    }
    public void createTrapsTutorial() {
        dialogTitle = "I smell danger... ";
        textLabel.setText("Watch out! The dungeon is riddled with traps like the one in front of you."
        );
        textLabel.setWrap(true);
        textLabel.setWidth(100);
    }
    public void createTrapsTutorialTwo() {
        dialogTitle = "...or is that sweat? ";
        textLabel.setText("To get past these traps you need to either jump or squat multiple times.\n\n" +
                "If you don’t feel like doing the exercises right now you can also skip the trap by spending X amount of movement points."
        );
        textLabel.setWrap(true);
        textLabel.setWidth(100);
    }
    public void createGoalTutorial() {
        dialogTitle = "Escape!";
        textLabel.setText("Now get going, the guards are coming. You must escape!"
        );
        textLabel.setWrap(true);
        textLabel.setWidth(100);
    }
    public void createNoKeysInfo() {
        dialogTitle = "Not enough keys.";
        textLabel.setText("You need to collect all three keys to open the door."
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
        tutorialWindow.setSize(300,280);
        tutorialWindow.setPosition(game.screenWidth / 2 - tutorialWindow.getWidth() / 2, 10);
        tutorialWindow.getContentTable().add(textTable);
        tutorialWindow.button(confirmButton);

        confirmButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                createCharacterTutorial();
                createCharacterTutorialWindow();

            }
        });
        stage.addActor(tutorialWindow);
    }

    public void createCharacterTutorialWindow(){
        Gdx.app.log("Tutorial", "character movement");
        Table textTable = new Table();

        Texture arrowTexture = new Texture("arrowup.png");
        ImageButton arrowImage = new ImageButton(new TextureRegionDrawable(new TextureRegion(arrowTexture)));
        Dialog tutorialWindow = new Dialog(getDialogTitle(),skin );
        TextButton confirmButton = new TextButton("OK!", skin );

        textTable.setDebug(false);
        textTable.add(getLabel()).width(270f).height(90f).left();
        textTable.row();
        textTable.add(arrowImage).width(50).height(50);

        tutorialWindow.setMovable(false);
        tutorialWindow.setModal(true);
        tutorialWindow.setSize(300,270);
        tutorialWindow.setPosition(game.screenWidth / 2 - tutorialWindow.getWidth() / 2, 10);
        tutorialWindow.getContentTable().add(textTable);
        tutorialWindow.button(confirmButton);

        stage.addActor(tutorialWindow);
    }

    public void createKeysTutorialWindow(){
        Gdx.app.log("Tutorial", "character movement");
        Table textTable = new Table();
        Texture keyTexture = new Texture("tutorialkey.png");
        ImageButton keyImage = new ImageButton(new TextureRegionDrawable(new TextureRegion(keyTexture)));
        Dialog tutorialWindow = new Dialog(getDialogTitle(),skin );
        TextButton confirmButton = new TextButton("OK!", skin );

        textTable.setDebug(false);
        textTable.add(getLabel()).width(270f).height(90f).left();
        textTable.row();
        textTable.add(keyImage);

        tutorialWindow.setMovable(false);
        tutorialWindow.setModal(true);
        tutorialWindow.setSize(300,250);
        tutorialWindow.setPosition(game.screenWidth / 2 - tutorialWindow.getWidth() / 2, game.screenHeight / 2 - windowHeightPos);
        tutorialWindow.getContentTable().add(textTable);
        tutorialWindow.button(confirmButton);

        stage.addActor(tutorialWindow);
    }
    public void createMovementPointsTutorialWindow(){
        Gdx.app.log("Tutorial", "movement points");
        Table textTable = new Table();
        Texture movesArrowTexture = new Texture("tutorialmoves.png");
        ImageButton movesImage = new ImageButton(new TextureRegionDrawable(new TextureRegion(movesArrowTexture)));
        Dialog tutorialWindow = new Dialog(getDialogTitle(),skin );
        TextButton confirmButton = new TextButton("OK!", skin );

        textTable.setDebug(false);
        textTable.add(getLabel()).width(270f).height(100f).left();
        textTable.row();
        textTable.add(movesImage).width(70).height(70);

        tutorialWindow.setMovable(false);
        tutorialWindow.setModal(true);
        tutorialWindow.setSize(300,280);
        tutorialWindow.setPosition(game.screenWidth / 2 - tutorialWindow.getWidth() / 2, game.screenHeight / 2 - windowHeightPos);
        tutorialWindow.getContentTable().add(textTable);
        tutorialWindow.button(confirmButton);

        confirmButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                createStepsTutorial();
                createStepsTutorialWindow();
            }
        });
        stage.addActor(tutorialWindow);
    }
    public void createStepsTutorialWindow(){
        Gdx.app.log("Tutorial", "steps");
        Table textTable = new Table();
        Texture stepsTexture = new Texture("tutorialsteps.png");
        ImageButton stepsImage = new ImageButton(new TextureRegionDrawable(new TextureRegion(stepsTexture)));
        Dialog tutorialWindow = new Dialog(getDialogTitle(),skin );
        TextButton confirmButton = new TextButton("OK!", skin );

        textTable.setDebug(false);
        textTable.add(getLabel()).width(270f).height(90f).left();
        textTable.row();
        textTable.add(stepsImage).width(70).height(70);

        tutorialWindow.setMovable(false);
        tutorialWindow.setModal(true);
        tutorialWindow.setSize(300,260);
        tutorialWindow.setPosition(game.screenWidth / 2 - tutorialWindow.getWidth() / 2, game.screenHeight / 2 - windowHeightPos);
        tutorialWindow.getContentTable().add(textTable);
        tutorialWindow.button(confirmButton);

        stage.addActor(tutorialWindow);
    }
    public void createTrapsTutorialWindow(){
        Gdx.app.log("Tutorial", "traps");
        Table textTable = new Table();
        Texture trapOneTexture = new Texture("tutorialtrap1.png");
        Texture trapTwoTexture = new Texture("tutorialtrap2.png");
        ImageButton trapOneImage = new ImageButton(new TextureRegionDrawable(new TextureRegion(trapOneTexture)));
        ImageButton trapTwoImage = new ImageButton(new TextureRegionDrawable(new TextureRegion(trapTwoTexture)));
        Dialog tutorialWindow = new Dialog(getDialogTitle(),skin );
        TextButton confirmButton = new TextButton("OK!", skin );

        textTable.setDebug(false);
        textTable.add(getLabel()).width(270f).height(90f).left().colspan(2);
        textTable.row();
        textTable.add(trapOneImage).width(70).height(70);
        textTable.add(trapTwoImage).width(70).height(70);

        tutorialWindow.setMovable(false);
        tutorialWindow.setModal(true);
        tutorialWindow.setSize(300,270);
        tutorialWindow.setPosition(game.screenWidth / 2 - tutorialWindow.getWidth() / 2, game.screenHeight / 2 - windowHeightPos - 150f);
        tutorialWindow.getContentTable().add(textTable);
        tutorialWindow.button(confirmButton);

        confirmButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                createTrapsTutorialTwo();
                createTrapsTutorialWindowTwo();
            }
        });

        stage.addActor(tutorialWindow);
    }
    public void createTrapsTutorialWindowTwo(){
        Gdx.app.log("Tutorial", "traps 2");
        Table textTable = new Table();

        Dialog tutorialWindow = new Dialog(getDialogTitle(),skin );
        TextButton confirmButton = new TextButton("OK!", skin );

        textTable.setDebug(false);
        textTable.add(getLabel()).width(270f).height(90f).left();

        tutorialWindow.setMovable(false);
        tutorialWindow.setModal(true);
        tutorialWindow.setSize(300,300);
        tutorialWindow.setPosition(game.screenWidth / 2 - tutorialWindow.getWidth() / 2, game.screenHeight / 2 - windowHeightPos - 100f);
        tutorialWindow.getContentTable().add(textTable);
        tutorialWindow.button(confirmButton);

        stage.addActor(tutorialWindow);
    }

    public void createGoalTutorialWindow(){
        Gdx.app.log("Tutorial", "goal");
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
        Texture keyTexture = new Texture("keyicon.png");
        ImageButton keyImageOne = new ImageButton(new TextureRegionDrawable(new TextureRegion(keyTexture)));
        ImageButton keyImageTwo = new ImageButton(new TextureRegionDrawable(new TextureRegion(keyTexture)));
        ImageButton keyImageThree = new ImageButton(new TextureRegionDrawable(new TextureRegion(keyTexture)));
        Dialog tutorialWindow = new Dialog(getDialogTitle(),skin );
        TextButton confirmButton = new TextButton("OK!", skin );

        textTable.setDebug(false);
        textTable.add(getLabel()).width(270f).height(70f).left().pad(2,2,2,2).colspan(3);
        textTable.row();
        textTable.add(keyImageOne).right();
        textTable.add(keyImageTwo);
        textTable.add(keyImageThree).left();


        tutorialWindow.setMovable(false);
        tutorialWindow.setModal(true);
        tutorialWindow.setSize(300,210);
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
