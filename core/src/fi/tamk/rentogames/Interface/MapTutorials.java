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

/**
 * Creates tutorial windows for map screen.
 *
 *<p>
 * Creates map screen tutorial user interface that consist of pop-up windows with text and images.
 * Defines their size, position and looks.
 *</p>
 *
 * @author Lauri Latva-Kyyny
 * @author  Miko Kauhanen
 * @version 1.0
 */
public class MapTutorials {

    private DungeonEscape game;

    private Skin skin;

    private Stage stage;

    /**
     * Text inside windows.
     */
    private Label textLabel;

    /**
     * Window title.
     */
    private String dialogTitle;

    /**
     * Constructor that receives main game and user interface for map screen.
     * Creates skin and empty text for window. Gets stage from user interface.
     *
     * @param game main game object
     * @param userInterface map screen interface object
     */
    public MapTutorials(DungeonEscape game, MapScreenUI userInterface) {
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));
        this.game = game;
        this.stage = userInterface.getStage();
        textLabel = new Label("",skin);
    }

    /**
     * Creates new tutorial.
     *
     *<p>
     * Creates a new tutorial window.
     * Contents and window attributes depend on tutorial number given as parameter.
     *</p>
     * @param tutorial tutorial number
     */
    public void createTutorial(int tutorial) {
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

    private void createIntroTutorial() {
        dialogTitle = game.getMyBundle().get("mapintrotutorialtitle");
        textLabel.setText(game.getMyBundle().get("mapintrotutorialtext"));
        textLabel.setWrap(true);
        textLabel.setWidth(100);
    }

    private void createCharacterTutorial() {
        dialogTitle = game.getMyBundle().get("mapcharactertutorialtitle");
        textLabel.setText(game.getMyBundle().get("mapcharactertutorialtext"));
        textLabel.setWrap(true);
        textLabel.setWidth(100);
    }

    private void createKeysTutorial() {
        dialogTitle = game.getMyBundle().get("mapkeystutorialtitle");
        textLabel.setText(game.getMyBundle().get("mapkeystutorialtext"));
        textLabel.setWrap(true);
        textLabel.setWidth(100);
    }

    private void createMovementPointsTutorial() {
        dialogTitle = game.getMyBundle().get("mappointstutorialtitle");
        textLabel.setText(game.getMyBundle().get("mappointstutorialtext"));
        textLabel.setWrap(true);
        textLabel.setWidth(100);
    }

    private void createStepsTutorial() {
        dialogTitle = game.getMyBundle().get("mapstepstutorialtitle");
        textLabel.setText(game.getMyBundle().get("mapstepstutorialtext"));
        textLabel.setWrap(true);
        textLabel.setWidth(100);
    }

    private void createTrapsTutorial() {
        dialogTitle = game.getMyBundle().get("maptrapstutorialtitle");
        textLabel.setText(game.getMyBundle().get("maptrapstutorialtext"));
        textLabel.setWrap(true);
        textLabel.setWidth(100);
    }
    private void createTrapsTutorialTwo() {
        dialogTitle = game.getMyBundle().get("maptrapstutorialtitletwo");
        textLabel.setText(game.getMyBundle().get("maptrapstutorialtexttwo"));
        textLabel.setWrap(true);
        textLabel.setWidth(100);
    }
    private void createGoalTutorial() {
        dialogTitle = "Escape!";
        textLabel.setText("Now get going, the guards are coming. You must escape!"
        );
        textLabel.setWrap(true);
        textLabel.setWidth(100);
    }
    private void createNoKeysInfo() {
        dialogTitle = game.getMyBundle().get("mapnokeysinfotitle");
        textLabel.setText(game.getMyBundle().get("mapnokeysinfotext"));
        textLabel.setWrap(true);
        textLabel.setWidth(100);
    }

    private void createIntroTutorialWindow(){
        Table textTable = new Table();

        Dialog tutorialWindow = new Dialog(getDialogTitle(),skin );
        TextButton confirmButton = new TextButton("OK!", skin );

        textTable.setDebug(false);
        textTable.add(getLabel()).width(270f).height(90f).left();

        tutorialWindow.setMovable(false);
        tutorialWindow.setModal(true);
        tutorialWindow.setSize(320,350);
        tutorialWindow.setPosition(game.screenWidth / 2 - tutorialWindow.getWidth() / 2, 210);
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

    private void createCharacterTutorialWindow(){
        Table textTable = new Table();

        Texture arrowTexture = new Texture("arrowup.png");
        ImageButton arrowImage = new ImageButton(new TextureRegionDrawable(new TextureRegion(arrowTexture)));
        Dialog tutorialWindow = new Dialog(getDialogTitle(),skin );
        TextButton confirmButton = new TextButton("OK!", skin );

        textTable.setDebug(false);
        textTable.add(getLabel()).width(270f).height(150f).left();
        textTable.row();
        textTable.add(arrowImage).width(50).height(50);

        tutorialWindow.setMovable(false);
        tutorialWindow.setModal(true);
        tutorialWindow.setSize(320,330);
        tutorialWindow.setPosition(game.screenWidth / 2 - tutorialWindow.getWidth() / 2, 210);
        tutorialWindow.getContentTable().add(textTable);
        tutorialWindow.button(confirmButton);

        stage.addActor(tutorialWindow);
    }

    private void createKeysTutorialWindow(){
        Table textTable = new Table();
        Texture keyTexture = new Texture("tutorialkeys.png");
        ImageButton keyImage = new ImageButton(new TextureRegionDrawable(new TextureRegion(keyTexture)));
        Dialog tutorialWindow = new Dialog(getDialogTitle(),skin );
        TextButton confirmButton = new TextButton("OK!", skin );

        textTable.setDebug(false);
        textTable.add(getLabel()).width(270f).height(140f).left();
        textTable.row();
        textTable.add(keyImage);

        tutorialWindow.setMovable(false);
        tutorialWindow.setModal(true);
        tutorialWindow.setSize(320,330);
        tutorialWindow.setPosition(game.screenWidth / 2 - tutorialWindow.getWidth() / 2, 210);
        tutorialWindow.getContentTable().add(textTable);
        tutorialWindow.button(confirmButton);

        stage.addActor(tutorialWindow);
    }
    private void createMovementPointsTutorialWindow(){
        Table textTable = new Table();
        Texture movesArrowTexture = new Texture("tutorialmoves.png");
        ImageButton movesImage = new ImageButton(new TextureRegionDrawable(new TextureRegion(movesArrowTexture)));
        Dialog tutorialWindow = new Dialog(getDialogTitle(),skin );
        TextButton confirmButton = new TextButton("OK!", skin );

        textTable.setDebug(false);
        textTable.add(getLabel()).width(270f).height(150f).left();
        textTable.row();
        textTable.add(movesImage).width(70).height(70);

        tutorialWindow.setMovable(false);
        tutorialWindow.setModal(true);
        tutorialWindow.setSize(320,330);
        tutorialWindow.setPosition(game.screenWidth / 2 - tutorialWindow.getWidth() / 2, 210);
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
    private void createStepsTutorialWindow(){
        Table textTable = new Table();
        Texture stepsTexture = new Texture("tutorialsteps.png");
        ImageButton stepsImage = new ImageButton(new TextureRegionDrawable(new TextureRegion(stepsTexture)));
        Dialog tutorialWindow = new Dialog(getDialogTitle(),skin );
        TextButton confirmButton = new TextButton("OK!", skin );

        textTable.setDebug(false);
        textTable.add(getLabel()).width(270f).height(110f).left();
        textTable.row();
        textTable.add(stepsImage).width(70).height(70);

        tutorialWindow.setMovable(false);
        tutorialWindow.setModal(true);
        tutorialWindow.setSize(320,330);
        tutorialWindow.setPosition(game.screenWidth / 2 - tutorialWindow.getWidth() / 2, 210);
        tutorialWindow.getContentTable().add(textTable);
        tutorialWindow.button(confirmButton);

        stage.addActor(tutorialWindow);
    }
    private void createTrapsTutorialWindow(){
        Table textTable = new Table();
        Texture trapOneTexture = new Texture("tutorialtrapspikes.png");
        Texture trapTwoTexture = new Texture("tutorialtraparrow.png");
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
        tutorialWindow.setPosition(game.screenWidth / 2 - tutorialWindow.getWidth() / 2, game.screenHeight / 2 - 220f);
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
    private void createTrapsTutorialWindowTwo(){
        Table textTable = new Table();

        Dialog tutorialWindow = new Dialog(getDialogTitle(),skin );
        TextButton confirmButton = new TextButton("OK!", skin );

        textTable.setDebug(false);
        textTable.add(getLabel()).width(270f).height(90f).left();

        tutorialWindow.setMovable(false);
        tutorialWindow.setModal(true);
        tutorialWindow.setSize(300,300);
        tutorialWindow.setPosition(game.screenWidth / 2 - tutorialWindow.getWidth() / 2, game.screenHeight / 2 - 190f);
        tutorialWindow.getContentTable().add(textTable);
        tutorialWindow.button(confirmButton);

        stage.addActor(tutorialWindow);
    }

    private void createGoalTutorialWindow(){
        Table textTable = new Table();

        Dialog tutorialWindow = new Dialog(getDialogTitle(),skin );
        TextButton confirmButton = new TextButton("OK!", skin );

        textTable.setDebug(false);
        textTable.add(getLabel()).width(270f).height(90f).left();

        tutorialWindow.setMovable(false);
        tutorialWindow.setModal(true);
        tutorialWindow.setSize(300,190);
        tutorialWindow.setPosition(game.screenWidth / 2 - tutorialWindow.getWidth() / 2, game.screenHeight / 2 - 90f);
        tutorialWindow.getContentTable().add(textTable);
        tutorialWindow.button(confirmButton);

        stage.addActor(tutorialWindow);
    }

    private void createNoKeysWindow(){
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
        tutorialWindow.setPosition(game.screenWidth / 2 - tutorialWindow.getWidth() / 2, 230);
        tutorialWindow.getContentTable().add(textTable);
        tutorialWindow.button(confirmButton);

        stage.addActor(tutorialWindow);
    }

    private String getDialogTitle() {
        return this.dialogTitle;
    }
    private Label getLabel() {
        return this.textLabel;
    }

}
