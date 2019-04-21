package fi.tamk.rentogames.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

import fi.tamk.rentogames.DungeonEscape;
import fi.tamk.rentogames.Framework.GameAudio;
import fi.tamk.rentogames.Framework.Save;

/**
 * Creates main menu screen and all main menu user interface elements.
 *
 *<p>
 * Creates main menu screen. Implements Screen to render and main menu elements.
 * Creates buttons and windows to control game starting, changing languages,
 * changing sound option, and viewing credits.
 *</p>
 * @author Lauri Latva-Kyyny
 * @author  Miko Kauhanen
 * @version 1.0
 */
public class MainMenu implements Screen {

    private DungeonEscape game;

    private Stage stage;

    private Skin skin;

    /**
     * Background image
     */
    private Texture background;

    /**
     * Is new game button initialized
     */
    private static boolean resetButtonInitialized = false;

    /**
     * Is sound button initialized
     */
    private boolean soundButtonInitialized = false;

    /**
     * Constructor for main menu. Receives main game object. Creates stage, background image and skin.
     *
     * @param game main game object
     */
    public MainMenu(DungeonEscape game) {
        this.game = game;
        this.stage = new Stage(game.getGameViewport());
        background = new Texture("menu.png");
        skin = new Skin( Gdx.files.internal("uiskin.json") );
    }

    @Override
    public void show() {
        GameAudio.playMusic("menumusic");
        GameAudio.loopMusic("menumusic");
        playMenuAudio();

        Gdx.input.setInputProcessor(stage);

        createTopButtons();
        createMenuButtons();
    }


    /**
     * Creates top buttons.
     *
     * <p>
     * Creates buttons for language settings, credits window and sound settings.
     * Defines their size, position, presentation and usage.
     * </p>
     */
    private void createTopButtons() {
        //Create Table
        Table topTable = new Table();

        //Set table to fill stage
        topTable.setFillParent(true);
        // Debug lines
        topTable.setDebug(false);

        //Set alignment of contents in the table.
        topTable.top();
        topTable.left();

        Texture finFlagTexture = new Texture("FIN.png");
        Texture engFlagTexture = new Texture("ENG.png");

        // Create buttons for languages info
        ImageButton FinFlag = new ImageButton(new TextureRegionDrawable(new TextureRegion(finFlagTexture)));
        ImageButton EngFlag = new ImageButton(new TextureRegionDrawable(new TextureRegion(engFlagTexture)));
        final ImageButton soundButton = new ImageButton(skin, "sound");
        final ImageButton infoButton = new ImageButton(skin, "info");

        if(Save.getCurrentAudioSetting() == 0f) {
            soundButton.setChecked(true);
            soundButtonInitialized = true;
        }

        //Add buttons to table
        topTable.row().pad(10,5,0,5);
        topTable.add(FinFlag).width(40).height(40).fillX();
        topTable.add(EngFlag).width(40).height(40).fillX();
        topTable.add(infoButton).right().width(40).height(40).fillX().expandX();
        topTable.add(soundButton).right().width(40).height(40).fillX();
        topTable.row().pad(10,5,0,5);

        //Add listeners to buttons
        FinFlag.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setLanguage("fi", "FI", "MyBundle_fi_FI");
                Save.saveLanguage("MyBundle_fi_FI");
                setLocaleFin();
            }
        });

        EngFlag.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setLanguage("en", "US", "MyBundle_en_US");
                Save.saveLanguage("MyBundle_en_US");
                setLocaleEng();
            }
        });

        infoButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                createCreditsWindow();
            }
        });

        soundButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(!soundButtonInitialized) {
                    Save.saveAudioSettings(0f);
                    soundButtonInitialized = true;
                    playMenuAudio();
                } else {
                    Save.saveAudioSettings(1f);
                    soundButtonInitialized = false;
                    playMenuAudio();
                }
            }
        });

        //Add table to stage
        stage.addActor(topTable);
    }

    /**
     * Creates middle main menu buttons.
     *
     * <p>
     * Creates buttons for playing the game, starting a new game and exiting the game.
     * Defines their size, position, presentation and usage.
     * </p>
     */
    private void createMenuButtons() {
        final Table menuTable = new Table();
        final Table newGameTable = new Table();
        menuTable.setFillParent(true);
        menuTable.setDebug(false);
        menuTable.center();

        newGameTable.setFillParent(true);
        newGameTable.setDebug(false);

        final TextButton playButton = new TextButton(game.getMyBundle().get("playbutton"), skin);
        final TextButton resetButton = new TextButton(game.getMyBundle().get("newgame"), skin);
        final TextButton confirmNewGameButton = new TextButton(game.getMyBundle().get("confirmbutton"), skin);
        final TextButton cancelNewGameButton = new TextButton(game.getMyBundle().get("cancelbutton"), skin);
        final Dialog newGameWindow = new Dialog(game.getMyBundle().get("confirmnewgame"), skin);

        newGameTable.add(cancelNewGameButton).pad(10,10,10,10).width(80);
        newGameTable.add(confirmNewGameButton).pad(10,10,10,10).width(80);

        newGameWindow.setMovable(false);
        newGameWindow.setModal(true);
        newGameWindow.setSize(200,170);
        newGameWindow.setPosition(game.screenWidth / 2 - newGameWindow.getWidth() / 2,  230f);
        newGameWindow.getContentTable().add(newGameTable);

        menuTable.row().pad(0,0,20,0);
        menuTable.add(playButton).width(200).height(70).fillX().colspan(2).padTop(50);

        menuTable.row().pad(0,0,10,0);
        menuTable.add(resetButton).right().colspan(2).width(200).height(50).fillX();
        menuTable.row().pad(0,0,10,0);


        playButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(game.getPreviousScreen() == DungeonEscape.MAPSCREEN) {
                    game.changeScreen(DungeonEscape.BACK);
                } else {
                    game.changeScreen(DungeonEscape.MAPSCREEN);
                }
            }
        });

        resetButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                stage.addActor(newGameWindow);
            }
        });

        confirmNewGameButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                Save.saveCurrentLevel(1);
                Save.saveMovementPoints(40);
                Save.saveCurrentPlayerX(513f);
                Save.saveCurrentPlayerY(65f);
                resetButtonInitialized = true;
                newGameWindow.remove();
                game.changeScreen(DungeonEscape.MAPSCREEN);
            }
        });

        cancelNewGameButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                newGameWindow.remove();
            }
        });

        stage.addActor(menuTable);
    }

    /**
     * Creates credits window.
     *
     * <p>
     * Creates window credits text and image. Defines its size, position, presentation and usage.
     * </p>
     */
    private void createCreditsWindow() {
        Table creditsTable = new Table();
        final Dialog creditsWindow = new Dialog(game.getMyBundle().get("createdby"), skin);

        Texture rentoLogo = new Texture("rentologosmall.png");
        Image rentoImage = new Image(rentoLogo);

        Label creatorsText = new Label(game.getMyBundle().get("creatorstext"), skin);
        creatorsText.setAlignment(Align.center);
        creatorsText.setWrap(true);

        Label extraText = new Label(game.getMyBundle().get("musicCredits"), skin);
        extraText.setWrap(true);

        Label uiskinText = new Label(game.getMyBundle().get("uiskincredits"), skin);
        uiskinText.setWrap(true);

        TextButton confirmButton = new TextButton("OK!", skin );

        creditsTable.setDebug(false);
        creditsTable.add(rentoImage).height(144).width(256);
        creditsTable.row();
        creditsTable.add(creatorsText).width(345);
        creditsTable.row();
        creditsTable.add(extraText).width(345).padTop(50);
        creditsTable.row();
        creditsTable.add(uiskinText).width(345).padTop(50);

        final ScrollPane scroller = new ScrollPane(creditsTable);
        final Table table = new Table();
        table.setFillParent(true);
        table.add(scroller).fill().expand();

        creditsWindow.setMovable(false);
        creditsWindow.setModal(true);
        creditsWindow.setSize(360,640);
        creditsWindow.setPosition(game.screenWidth / 2 - creditsWindow.getWidth() / 2, 10);
        creditsWindow.getContentTable().add(table);
        creditsWindow.button(confirmButton);

        confirmButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                creditsWindow.remove();
            }
        });
        stage.addActor(creditsWindow);
    }

    /**
     * Plays menu music
     */
    private void playMenuAudio() {
        GameAudio.setMusicVolume("menumusic", Save.getCurrentAudioSetting());
    }

    private void setLocaleFin() {
        createMenuButtons();
    }

    private void setLocaleEng() {
        createMenuButtons();
    }

    static Boolean getResetButtonInitialized() {
        return resetButtonInitialized;
    }

    static void setResetButtonInitialized(boolean reset) {
        resetButtonInitialized = reset;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.setProjectionMatrix(game.getScreenCamera().combined);

        game.batch.begin();
        game.batch.draw(background,0,0, game.screenWidth, game.screenHeight);
        game.batch.end();

        stage.act(delta);
        // Call draw on every actor
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        GameAudio.stopMusic("menumusic");
    }

    @Override
    public void dispose() {
        stage.dispose();
        background.dispose();
        skin.dispose();
        game.dispose();
    }
}