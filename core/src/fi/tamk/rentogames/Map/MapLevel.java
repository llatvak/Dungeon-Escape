package fi.tamk.rentogames.Map;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Disposable;

import fi.tamk.rentogames.DungeonEscape;
import fi.tamk.rentogames.Framework.Save;

/**
 * Class to control map screen level information.
 *
 * @author Lauri Latva-Kyyny
 * @author  Miko Kauhanen
 * @version 1.0
 */
public class MapLevel implements Disposable {

    /**
     * Main game game used to access all methods there.
     */
    private DungeonEscape game;

    /**
     * Tiled map used for levels.
     */
    private TiledMap tiledMap;

    /**
     * Current map as tiled map.
     */
    private TiledMap currentMap;
    /**
     * Tiled map renderer to render map in specific camera angle.
     */
    private OrthogonalTiledMapRenderer tiledMapRenderer;


    /**
     * Current level as in value.
     */
    private int level = 1;

    /**
     * Constructor used to create map levels.
     *
     * @param game main class game used to access all methods there
     */
    public MapLevel(DungeonEscape game) {
        this.game = game;
        onCreate();
    }


    /**
     * Sets level from saved preferences and calls method to create tiled map for that level.
     */
    private void onCreate() {
        setLevel(Save.getCurrentLevel());
        createTiledMap();
    }

    /**
     * Creates tiled map for specific level and sets camera position and scale to units.
     */
    public void createTiledMap() {
        tiledMap = new TmxMapLoader().load("map"+level+".tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1/100f);
        currentMap = tiledMap;
    }

    public void resetMap(){
        // reset all the vars that should be reset before the next mapLevel

    }

    /**
     * Sets specific level.
     *
     * @param level specific level in int value
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * Returns current level in game.
     *
     * @return current level as int value
     */
    public int getLevel() {
        return level;
    }

    /**
     * Returns current tiled map.
     *
     * @return current level's tiled map
     */
    public TiledMap getCurrentMap() {
        return currentMap;
    }

    public void createTiledMapRenderer() {
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1/100f);
    }

    public TiledMap getTiledMap() {
        return this.tiledMap;
    }

    /**
     * Returns tiled map renderer to set camera angle.
     *
     * @return tiled map renderer to set camera angle and render tiled map
     */
    public OrthogonalTiledMapRenderer getTiledMapRenderer() {
        return this.tiledMapRenderer;
    }

    @Override
    public void dispose() {
        game.dispose();
        tiledMap.dispose();
        currentMap.dispose();
        tiledMapRenderer.dispose();
    }
}