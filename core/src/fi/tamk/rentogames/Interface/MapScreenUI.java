package fi.tamk.rentogames.Interface;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import fi.tamk.rentogames.DungeonEscape;
import fi.tamk.rentogames.Map.MapPlayer;
import fi.tamk.rentogames.Screens.MapScreen;

import static fi.tamk.rentogames.Screens.MapScreen.KEYS_NEEDED;

public class MapScreenUI extends UI {

    DungeonEscape game;
    MapScreen mapScreen;
    MapPlayer player;

    private ProgressBar stepsProgressBar;
    //Create buttons and bars
    ImageButton settingsButton;
    ImageButton keyImage;
    ImageButton footmarkImage;
    ImageButton movesImage;

    private Label stepLabel;
    private Label movesLabel;
    private Label keyLabel;

    private Texture keyTexture;
    private Texture footMarkTexture;
    private Texture movesArrowTexture;


    public MapScreenUI(DungeonEscape game, MapScreen mapScreen, MapPlayer player){
        this.game = game;
        this.mapScreen = mapScreen;
        this.player = player;
        onCreate();
    }

    private void onCreate() {
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
//        skin = new Skin();
//        skin.add("fontRoboto-Med", fontRoboto);
//        skin.addRegions(new TextureAtlas(Gdx.files.internal("uiskin.atlas")));
//        skin.load(Gdx.files.internal("uiskin.json"));

        keyTexture = new Texture("key.png");
        footMarkTexture = new Texture("footmarkicon.png");
        movesArrowTexture = new Texture("movesicon.png");

        //Create buttons and bars
        settingsButton = new ImageButton(skin, "settings");
        keyImage = new ImageButton(new TextureRegionDrawable(new TextureRegion(keyTexture)));
        footmarkImage = new ImageButton(new TextureRegionDrawable(new TextureRegion(footMarkTexture)));
        movesImage = new ImageButton(new TextureRegionDrawable(new TextureRegion(movesArrowTexture)));

        stepLabel = new Label("" + mapScreen.getStepTotal(), skin,"white");
        movesLabel = new Label("" + player.movementPoints, skin,"white");
        keyLabel = new Label("" + mapScreen.keyAmount + "/" + KEYS_NEEDED, skin,"white");
    }
}
