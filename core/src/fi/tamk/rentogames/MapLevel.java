package fi.tamk.rentogames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Disposable;

public class MapLevel implements Disposable {

    DungeonEscape game;

    private TiledMap tiledMap;

    private TiledMap currentMap;
    private OrthogonalTiledMapRenderer tiledMapRenderer;

    private int level = 1;

    MapLevel(DungeonEscape game) {
        this.game = game;
        onCreate();
    }

    private void onCreate() {
        setLevel(1);
        createTiledMap();
    }

    void createTiledMap() {
        tiledMap = new TmxMapLoader().load("map"+level+".tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1/100f);
        currentMap = tiledMap;
    }

    void resetMap(){
        Gdx.app.log("MapLevel", "reset");
        // reset all the vars that should be reset before the next mapLevel

    }

    void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }


    TiledMap getCurrentMap() {
        return currentMap;
    }

    public void createTiledMapRenderer() {
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1/100f);
    }

    TiledMap getTiledMap() {
        return this.tiledMap;
    }

    OrthogonalTiledMapRenderer getTiledMapRenderer() {
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
