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
 * @author Lauri Latva-Kyyny
 * @author  Miko Kauhanen
 * @version 1.0
 */
public class MoveTutorials {

    private DungeonEscape game;
    private Skin skin;
    private Label textLabel;
    private String dialogTitle;
    private Stage stage;

    /**
     * @param game
     * @param userInterface
     */
    public MoveTutorials(DungeonEscape game, MoveScreenUI userInterface) {
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));
        this.game = game;
        this.stage = userInterface.getStage();
        textLabel = new Label("",skin);
    }

    /**
     *
     */
    public void createJumpTutorial() {
        createSpikesTutorial();
        createSpikesTutorialWindow();
    }

    /**
     *
     */
    public void createSquatTutorial() {
        createArrowTutorial();
        createArrowTutorialWindow();
    }
    public void createSpikesTutorial() {
        dialogTitle = game.getMyBundle().get("movestutorialtitle");
        textLabel.setText(game.getMyBundle().get("jumptutorialtext"));
        textLabel.setWrap(true);
        textLabel.setWidth(100);
    }
    public void createArrowTutorial() {
        dialogTitle = game.getMyBundle().get("movestutorialtitle");
        textLabel.setText(game.getMyBundle().get("squattutorialtext"));

        textLabel.setWrap(true);
        textLabel.setWidth(100);
    }
    public void createPhoneTutorial() {
        dialogTitle = game.getMyBundle().get("phonetutorialtitle");
        textLabel.setText(game.getMyBundle().get("phonetutorialtext"));
        textLabel.setWrap(true);
        textLabel.setWidth(100);
    }

    public void createSpikesTutorialWindow(){
        Gdx.app.log("Tutorial", "spikes");
        Table textTable = new Table();
        Texture standTexture = new Texture("tutorialstand.png");
        Texture jumpTexture = new Texture("tutorialjump.png");
        ImageButton standImage = new ImageButton(new TextureRegionDrawable(new TextureRegion(standTexture)));
        ImageButton jumpImage = new ImageButton(new TextureRegionDrawable(new TextureRegion(jumpTexture)));
        Dialog tutorialWindow = new Dialog(getDialogTitle(),skin );
        TextButton confirmButton = new TextButton("OK!", skin );

        textTable.setDebug(false);
        textTable.add(getLabel()).width(270f).height(50f).left().colspan(2);
        textTable.row();
        textTable.add(standImage).width(100).height(100).right();
        textTable.add(jumpImage).width(100).height(100).left();

        tutorialWindow.setMovable(false);
        tutorialWindow.setModal(true);
        tutorialWindow.setSize(300,260);
        tutorialWindow.setPosition(game.screenWidth / 2 - tutorialWindow.getWidth() / 2, 300);
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

    public void createArrowTutorialWindow(){
        Gdx.app.log("Tutorial", "arrow");
        Table textTable = new Table();
        Texture standTexture = new Texture("tutorialstand.png");
        Texture squatTexture = new Texture("tutorialsquat.png");
        ImageButton standImage = new ImageButton(new TextureRegionDrawable(new TextureRegion(standTexture)));
        ImageButton squatImage = new ImageButton(new TextureRegionDrawable(new TextureRegion(squatTexture)));
        Dialog tutorialWindow = new Dialog(getDialogTitle(),skin );
        TextButton confirmButton = new TextButton("OK!", skin );

        textTable.setDebug(false);
        textTable.add(getLabel()).width(270f).height(50f).left().colspan(2);
        textTable.row();
        textTable.add(standImage).width(100).height(100).right();
        textTable.add(squatImage).width(100).height(100).left();

        tutorialWindow.setMovable(false);
        tutorialWindow.setModal(true);
        tutorialWindow.setSize(300,260);
        tutorialWindow.setPosition(game.screenWidth / 2 - tutorialWindow.getWidth() / 2, 300);
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

        Texture phoneTexture = new Texture("phone.png");
        ImageButton phoneImage = new ImageButton(new TextureRegionDrawable(new TextureRegion(phoneTexture)));
        Dialog tutorialWindow = new Dialog(getDialogTitle(),skin );
        TextButton confirmButton = new TextButton("OK!", skin );

        textTable.setDebug(false);
        textTable.add(getLabel()).width(270f).height(100f).left();
        textTable.row();
        textTable.add(phoneImage).width(90).height(90);

        tutorialWindow.setMovable(false);
        tutorialWindow.setModal(true);
        tutorialWindow.setSize(300,300);
        tutorialWindow.setPosition(game.screenWidth / 2 - tutorialWindow.getWidth() / 2, 280);
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
