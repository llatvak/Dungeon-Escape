package fi.tamk.rentogames;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class Level {

    TiledMap tiledMap;



    TiledMap currentMap;
    OrthogonalTiledMapRenderer tiledMapRenderer;

    Level() {
        onCreate();
    }

    void onCreate() {
        createTiledMap();
        createTiledMapRenderer();
    }

    void createTiledMap() {
        tiledMap = new TmxMapLoader().load("DungeonEscape_Map.tmx");
        currentMap = tiledMap;
    }

    public TiledMap getCurrentMap() {
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
}
