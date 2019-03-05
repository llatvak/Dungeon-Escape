package fi.tamk.gameproject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class MapPlayer extends Sprite {

    private MapScreen mapScreen;
    private TiledMap tiledMap;
    private Texture playerTexture;

    private final int TILE_SIZE = 64;
    private final float MAP_WIDTH = 29 * TILE_SIZE;
    private final float MAP_HEIGHT = 39 * TILE_SIZE;

    private float spriteWidth = 62f ;
    private float spriteHeight = 62f;

    private float startingX = 1f;
    private float startingY = 24 * TILE_SIZE + 1;

    private float spriteX = startingX;
    private float spriteY = startingY;

    boolean moving;
    private boolean goUp;
    private boolean goDown;
    private boolean goRight;
    private boolean goLeft;

    private float movementSpeed = 4f;
    private float movedDistance;
    float moveAmount = movementSpeed;


    // Collision checking
    public boolean upLeftCollision;
    public boolean downLeftCollision;
    public boolean upRightCollision;
    public boolean downRightCollision;
    private float downYPos;
    private float upYPos;
    private float leftXPos;
    private float rightXPos;


    public MapPlayer(MapScreen mapScreen) {
        super( new Texture("velho.png"));
        this.mapScreen = mapScreen;
        this.tiledMap = mapScreen.tiledMap;

        setSize(spriteWidth, spriteHeight);
        setPosition(startingX, startingY);
    }


    // Can this method be reduced in size?
    public void move(){

        if(goDown) {
            getMyCorners(spriteX, spriteY - 1 * moveAmount);
            if(downLeftCollision && downRightCollision) {
                if (movedDistance < TILE_SIZE) {
                    spriteY -= movementSpeed;
                    movedDistance += movementSpeed;
                } else {
                    goDown = false;
                    moving = false;
                    movedDistance = 0;
                }
            } else {
                goDown = false;
                moving = false;
            }
        }

        if(goUp) {
            getMyCorners(spriteX, spriteY + 1 * moveAmount);
            if(upLeftCollision && downRightCollision) {
                if (movedDistance < TILE_SIZE) {
                    spriteY += movementSpeed;
                    movedDistance += movementSpeed;
                } else {
                    goUp = false;
                    moving = false;
                    movedDistance = 0;
                }
            } else {
                goUp = false;
                moving = false;
            }
        }

        if(goLeft) {
            getMyCorners(spriteX - 2, spriteY);
            if(upLeftCollision && downLeftCollision) {
                if (movedDistance < TILE_SIZE) {
                    spriteX -= movementSpeed;
                    movedDistance += movementSpeed;
                } else {
                    goLeft = false;
                    moving = false;
                    movedDistance = 0;
                }
            } else {
                goLeft = false;
                moving = false;
            }
        }

        if(goRight) {
            getMyCorners(spriteX + 1 , spriteY);
            if(upRightCollision && downRightCollision) {
                if(movedDistance < TILE_SIZE) {
                    spriteX += movementSpeed;
                    movedDistance += movementSpeed;
                } else {
                    goRight = false;
                    moving = false;
                    movedDistance = 0;
                }
            } else {
                goRight = false;
                moving = false;
            }

        }
        setX(spriteX);
        setY(spriteY);
    }

    public void checkInput() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            if(!moving) {
                setUpMove(true);
            }
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            if(!moving){
                setDownMove(true);
            }
        }


        if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            if(!moving) {
                setRightMove(true);
            }

        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            if(!moving) {
                setLeftMove(true);
            }
        }
    }

    public void setLeftMove(boolean t) {
        goLeft = t;
        moving = t;
    }

    public void setRightMove(boolean t) {
        goRight = t;
        moving = t;
    }

    public void setDownMove(boolean t) {
        goDown = t;
        moving = t;
    }

    public void setUpMove(boolean t) {
        goUp = t;
        moving = t;
    }

    private boolean isFree(float x, float y) {

        // Calculate coordinates to tile coordinates
        // example, (34,34) => (1,1)

        int indexX = (int) x / TILE_SIZE;
        int indexY = (int) y / TILE_SIZE;
        System.out.println(indexX +" "+indexY );
        TiledMapTileLayer wallCells = (TiledMapTileLayer)
                tiledMap.getLayers().get("Walls");

        // Is the coordinate / cell free?
        if(wallCells.getCell(indexX, indexY) != null) {
            // There was a cell, it's not free
            System.out.println("WALL!");
            movedDistance = 0;
            //gameEnd = true;
            //setAlpha(0);
            return false;
        } else {
            // There was no cell, it's free
            return true;
        }
    }

    public void getMyCorners(float pX, float pY) {
        // calculate all the corners of the sprite
        downYPos = pY;
        upYPos = spriteHeight + downYPos;
        leftXPos = pX;
        rightXPos = spriteWidth + leftXPos;
        // update the attributes

        upLeftCollision = isFree(leftXPos, upYPos);
        downLeftCollision = isFree(leftXPos, downYPos);
        upRightCollision = isFree(rightXPos, upYPos);
        downRightCollision = isFree(rightXPos, downYPos);
    }
    /**
     * Checks if player has collided with event tiles
     */
    public void checkCollisions() {
        // Get the collectable rectangles layer
        MapLayer collisionObjectLayer = (MapLayer)tiledMap.getLayers().get("Down_trap");
        // All the rectangles of the layer
        MapObjects mapObjects = collisionObjectLayer.getObjects();
        // Cast it to RectangleObjects array
        Array<RectangleMapObject> rectangleObjects = mapObjects.getByType(RectangleMapObject.class);
        // Iterate all the rectangles
        for (RectangleMapObject rectangleObject : rectangleObjects) {
            Rectangle rectangle = rectangleObject.getRectangle();
            // SCALE given rectangle down if using world dimensions!
            if (getBoundingRectangle().overlaps(rectangle) && movedDistance == 64) {
                mapScreen.goToDownTrap();
            }
        }
    }

    public void dispose() {
        getTexture().dispose();

    }
}
