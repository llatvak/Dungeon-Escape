package fi.tamk.rentogames;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class Level {

    TiledMap tiledMap;
    OrthogonalTiledMapRenderer tiledMapRenderer;

    public Level() {
        onCreate();
    }

    public void onCreate() {
        createTiledMap();
        createTiledMapRenderer();
    }

    public void createTiledMap() {
        tiledMap = new TmxMapLoader().load("DungeonEscape_Map.tmx");
    }

    public void createTiledMapRenderer() {
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1/100f);
    }

    public TiledMap getTiledMap() {
        return this.tiledMap;
    }

    public OrthogonalTiledMapRenderer getTiledMapRenderer() {
        return this.tiledMapRenderer;
    }
}
