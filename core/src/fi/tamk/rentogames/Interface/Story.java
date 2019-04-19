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
 * @author
 * @author
 * @version
 */
public class Story {

    private DungeonEscape game;
    private Skin skin;
    private Label textLabel;
    private Texture storyBackground;
    private Stage stage;

    /**
     * @param game
     * @param userInterface
     */
    public Story(DungeonEscape game,MapScreenUI userInterface) {
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));
        this.game = game;
        this.stage = userInterface.getStage();
        textLabel = new Label("",skin);
        textLabel.setColor(1,1,1,1);
    }

    /**
     * @param tutorial
     */
    public void createStoryPart(int tutorial) {
        switch(tutorial) {
            case 1: createStoryTextOne();
                break;
            case 2: createStoryTextTwo();
                break;
            case 3: createStoryTextThree();
                break;
            case 4: createStoryTextFour();
                break;
            case 5: createStoryTextFive();
                break;
        }
        createStoryWindow();
    }

    public void createStoryTextOne() {
        textLabel.setText(game.getMyBundle().get("story1"));
        textLabel.setWrap(true);
        storyBackground = new Texture("story1.png");
    }
    public void createStoryTextTwo() {
        textLabel.setText(game.getMyBundle().get("story2"));
        textLabel.setWrap(true);
        storyBackground = new Texture("story1.png");
    }
    public void createStoryTextThree() {
        textLabel.setText(game.getMyBundle().get("story3"));
        textLabel.setWrap(true);
        storyBackground = new Texture("story3.png");
    }
    public void createStoryTextFour() {
        textLabel.setText(game.getMyBundle().get("story4"));
        textLabel.setWrap(true);
        storyBackground = new Texture("story4.png");
    }
    public void createStoryTextFive() {
        textLabel.setText(game.getMyBundle().get("story5"));
        textLabel.setWrap(true);
        storyBackground = new Texture("story5.png");
    }

    public void createStoryWindow(){
        Gdx.app.log("Story", "part one");
        Table textTable = new Table();

        Drawable windowbg = new TextureRegionDrawable(getStoryBackground());
        Dialog tutorialWindow = new Dialog("",skin );
        TextButton confirmButton = new TextButton(game.getMyBundle().get("storyreturn"), skin );

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
    public Label getLabel() {
        return this.textLabel;
    }
    private Texture getStoryBackground() {
        return storyBackground;
    }
}
