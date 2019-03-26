package fi.tamk.rentogames;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

class Level {

    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer tiledMapRenderer;

    Level() {
        onCreate();
    }

    private void onCreate() {
        createTiledMap();
        createTiledMapRenderer();
    }

    private void createTiledMap() {
        tiledMap = new TmxMapLoader().load("DungeonEscape_Map.tmx");
    }

    private void createTiledMapRenderer() {
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1/100f);
    }

    TiledMap getTiledMap() {
        return this.tiledMap;
    }

    OrthogonalTiledMapRenderer getTiledMapRenderer() {
        return this.tiledMapRenderer;
    }
}
