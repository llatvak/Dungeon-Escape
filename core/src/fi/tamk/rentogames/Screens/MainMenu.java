package fi.tamk.rentogames.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.util.Map;

import fi.tamk.rentogames.DungeonEscape;
import fi.tamk.rentogames.Framework.GameAudio;
import fi.tamk.rentogames.Framework.Save;
import fi.tamk.rentogames.Map.MapPlayer;

public class MainMenu implements Screen {

    private Stage stage;
    private DungeonEscape game;

    private  Texture background;
    private Skin skin;

    private static boolean resetButtonInitialized = false;
    private boolean soundButtonInitialized = false;


    public MainMenu(DungeonEscape game) {
        this.game = game;
        onCreate();
    }

    private void onCreate() {
        background = new Texture("settings.png");

        //atlas = new TextureAtlas("skin.atlas");
        skin = new Skin( Gdx.files.internal("uiskin.json") );

        this.stage = new Stage(game.getGameViewport());
    }

    @Override
    public void show() {
        GameAudio.playMusic("menumusic");
        GameAudio.loopMusic("menumusic");
        playMenuAudio();

        Gdx.input.setInputProcessor(stage);

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

        //Create buttons
        ImageButton FinFlag = new ImageButton(new TextureRegionDrawable(new TextureRegion(finFlagTexture)));
        ImageButton EngFlag = new ImageButton(new TextureRegionDrawable(new TextureRegion(engFlagTexture)));
        final ImageButton soundButton = new ImageButton(skin, "sound");
        final TextButton resetButton = new TextButton("Reset", skin);

        //Add listeners to buttons
        FinFlag.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("Language", "Finnish");
                game.setLanguage("fi", "FI", "MyBundle_fi_FI");
                Save.saveLanguage("MyBundle_fi_FI");
                setLocaleFin();
            }
        });

        EngFlag.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("Language", "English");
                game.setLanguage("en", "US", "MyBundle_en_US");
                Save.saveLanguage("MyBundle_en_US");
                setLocaleEng();
            }
        });

        soundButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("Sound", "pressed");
                if(!soundButtonInitialized) {
                    System.out.println("muted");
                    Save.saveAudioSettings(0f);
                    soundButtonInitialized = true;
                    playMenuAudio();
                } else {
                    System.out.println("unmuted");
                    Save.saveAudioSettings(0.2f);
                    soundButtonInitialized = false;
                    playMenuAudio();
                }
            }
        });

        //Add buttons to table
        topTable.row().pad(10,5,0,5);
        topTable.add(FinFlag).width(40).height(40).fillX();
        topTable.add(EngFlag).width(40).height(40).fillX();
        topTable.add(soundButton).right().width(40).height(40).fillX().expandX();
        topTable.row().pad(10,5,0,5);


        // Gametesting reset button
        if(game.testing) {
            resetButton.addListener(new ChangeListener(){
                @Override
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    Gdx.app.log("Game", "Reset");
                    Save.saveCurrentLevel(1);
                    Save.saveMovementPoints(50);
                    Save.saveCurrentProgressbar(0);
                    Save.saveCurrentPlayerX(513f);
                    Save.saveCurrentPlayerY(65f);
                    resetButtonInitialized = true;

                }
            });

            topTable.add(resetButton).right().colspan(3).width(50).height(50).fillX().pad(520,10,0,5);
        }


        //Add table to stage
        stage.addActor(topTable);

        gameInit();
    }

    private void gameInit() {
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.setDebug(false);
        mainTable.center();

        TextButton playButton = new TextButton(game.getMyBundle().get("playbutton"), skin);
        TextButton exitButton = new TextButton(game.getMyBundle().get("exitbutton"), skin);
        ImageButton infoButton = new ImageButton(skin, "info");
        ImageButton helpButton = new ImageButton(skin, "help");

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

        infoButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("Info", "pressed");
            }
        });

        helpButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("Help", "pressed");
                // game.changeScreen(DungeonEscape.TUTORIALSCREEN);
            }
        });

        exitButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        mainTable.row().pad(0,0,20,0);
        mainTable.add(playButton).width(200).height(70).fillX().colspan(2);

        mainTable.row().pad(0,0,10,0);
        //mainTable.add(infoButton).right().width(95).height(50).fillX().pad(0,0,0,5);
        //mainTable.add(helpButton).left().width(95).height(50).fillX().pad(0,5,0,0);

        mainTable.row().pad(10,0,0,0);
        mainTable.add(exitButton).width(200).height(50).fillX().colspan(2);
        mainTable.row().pad(10,0,30,0);


        stage.addActor(mainTable);
    }

    private void setLocaleFin() {
        gameInit();
    }

    private void setLocaleEng() {
        gameInit();
    }

    public static Boolean getResetButtonInitialized() {
        return resetButtonInitialized;
    }

    public static void setResetButtonInitialized(boolean reset) {
        resetButtonInitialized = reset;
    }

    public void playMenuAudio() {
        GameAudio.setMusicVolume("menumusic", Save.getCurrentAudioSetting());
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