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

/**
 *
 * Creates the windows for story content.
 *
 * <p>
 * Creates story user interface including all the windows and story text
 * and background images inside the windows. Defines their size, position and looks.
 * </p>
 *
 * @author Lauri Latva-Kyyny
 * @author Miko Kauhanen
 * @version 1.0
 */
public class Story {

    private DungeonEscape game;

    private Skin skin;

    private Stage stage;

    /**
     * Text inside windows.
     */
    private Label textLabel;

    /**
     * Background image for windows.
     */
    private Texture storyBackground;

    /**
     * Constructor that receives main game and user interface.
     * Creates skin. Receives stage from user interface. Sets empty text to window.
     *
     * @param game main game object
     * @param userInterface map screen user interface object
     */
    public Story(DungeonEscape game, MapScreenUI userInterface) {
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));
        this.game = game;
        this.stage = userInterface.getStage();
        textLabel = new Label("",skin);
        textLabel.setColor(1,1,1,1);
    }

    /**
     * Changes the story text and background image inside the window according to parameter.
     * Afterwards creates the window with this text and image.
     *
     * @param tutorial tutorial part
     */
    public void createStoryPart(int tutorial) {
        switch(tutorial) {
            case 1: createStoryOne();
                break;
            case 2: createStoryTwo();
                break;
            case 3: createStoryThree();
                break;
            case 4: createStoryFour();
                break;
            case 5: createStoryFive();
                break;
        }
        createStoryWindow();
    }

    private void createStoryOne() {
        textLabel.setText(game.getMyBundle().get("story1"));
        textLabel.setWrap(true);
        storyBackground = new Texture("story1.png");
    }
    private void createStoryTwo() {
        textLabel.setText(game.getMyBundle().get("story2"));
        textLabel.setWrap(true);
        storyBackground = new Texture("story2.png");
    }
    private void createStoryThree() {
        textLabel.setText(game.getMyBundle().get("story3"));
        textLabel.setWrap(true);
        storyBackground = new Texture("story3.png");
    }
    private void createStoryFour() {
        textLabel.setText(game.getMyBundle().get("story4"));
        textLabel.setWrap(true);
        storyBackground = new Texture("story4.png");
    }
    private void createStoryFive() {
        textLabel.setText(game.getMyBundle().get("story5"));
        textLabel.setWrap(true);
        storyBackground = new Texture("story5.png");
    }

    private void createStoryWindow(){
        Table textTable = new Table();

        Drawable windowBackground = new TextureRegionDrawable(getStoryBackground());
        Dialog tutorialWindow = new Dialog("",skin );
        TextButton confirmButton = new TextButton(game.getMyBundle().get("storyreturn"), skin );

        tutorialWindow.setBackground(windowBackground);
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
    private Label getLabel() {
        return this.textLabel;
    }
    private Texture getStoryBackground() {
        return storyBackground;
    }
}
