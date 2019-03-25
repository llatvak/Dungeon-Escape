package fi.tamk.rentogames;

import com.badlogic.gdx.Gdx;
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

    // Map size
    private final int TILE_SIZE = 64;

    // Player size
    private float spriteWidth = 62f;
    private float spriteHeight = 62f;

    // Starting location
    private float startingX = 2 * TILE_SIZE + 1f;
    private float startingY = 26 * TILE_SIZE + 1f;

    private float spriteX = startingX;
    private float spriteY = startingY;

    // Movement direction
    boolean moving;
    private boolean goUp;
    private boolean goDown;
    private boolean goRight;
    private boolean goLeft;

    // Amount of steps to move one tile
    final int STEPSTOMOVE = 10;
    int movementPoints;
    boolean allowMovement;
    private float movementSpeed = 4f;
    private float movedDistance;
    private float moveAmount = movementSpeed;

    // Collision checking
    private boolean upLeftCollision;
    private boolean downLeftCollision;
    private boolean upRightCollision;
    private boolean downRightCollision;

    // Boolean values for stepping on up/down trap
    private boolean onUpTrap = false;
    private boolean onDownTrap = false;

    MapPlayer(MapScreen mapScreen) {
        super( new Texture("velho.png"));
        this.mapScreen = mapScreen;
        this.tiledMap = mapScreen.tiledMap;

        setSize(spriteWidth, spriteHeight);
        setPosition(startingX, startingY);

        movementPoints = 10;
    }

    // Can this method be reduced in size?
    void move(){
        if(goDown) {
            getMyCorners(spriteX, spriteY - 1 * moveAmount);
            if(downLeftCollision && downRightCollision) {
                if (movedDistance < TILE_SIZE) {
                    spriteY -= movementSpeed;
                    movedDistance += movementSpeed;
                    if(movedDistance == TILE_SIZE) {
                        removeMovementPoint();
                    }
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
                    if(movedDistance == TILE_SIZE) {
                        removeMovementPoint();
                    }
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
                    if(movedDistance == TILE_SIZE) {
                        removeMovementPoint();
                    }
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
                    if(movedDistance == TILE_SIZE) {
                        removeMovementPoint();
                    }
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

    void receiveSteps(int stepTotal){
        // Movement
    }

    void addMovementPoint() {
        Gdx.app.log("Movementpoint", "added");
        movementPoints++;

    }

    private void removeMovementPoint() {
        if(movementPoints > 0) {
            movementPoints--;
        }

    }

    void checkAllowedMoves() {
        allowMovement = movementPoints > 0;
    }

    void setLeftMove() {
        goLeft = true;
        moving = true;
    }

    void setRightMove() {
        goRight = true;
        moving = true;
    }

    void setDownMove() {
        goDown = true;
        moving = true;
    }

    void setUpMove() {
        goUp = true;
        moving = true;
    }

    private boolean isFree(float x, float y) {

        // Calculate coordinates to tile coordinates
        // example, (34,34) => (1,1)

        int indexX = (int) x / TILE_SIZE;
        int indexY = (int) y / TILE_SIZE;
        //System.out.println(indexX +" "+indexY );
        TiledMapTileLayer wallCells = (TiledMapTileLayer)
                tiledMap.getLayers().get("Walls");

        // Is the coordinate / cell free?
        if(wallCells.getCell(indexX, indexY) != null) {
            // There was a cell, it's not free
            movedDistance = 0;
            return false;
        } else {
            // There was no cell, it's free
            return true;
        }
    }

    private void getMyCorners(float pX, float pY) {

        float downYPos;
        float upYPos;
        float leftXPos;
        float rightXPos;

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
    void checkCollisions() {
        // Can these methods be merged into one single method?
        checkDownTraps();
        checkUpTraps();
        checkStoryTiles();
    }

    private void checkDownTraps() {
        // Get the down trap rectangles layer
        MapLayer downTrapObjectLayer = tiledMap.getLayers().get("Down_trap");
        // All the rectangles of the layer
        MapObjects mapObjects = downTrapObjectLayer.getObjects();
        // Cast it to RectangleObjects array
        Array<RectangleMapObject> rectangleObjects = mapObjects.getByType(RectangleMapObject.class);
        // Iterate all the rectangles
        for (RectangleMapObject rectangleObject : rectangleObjects) {
            Rectangle rectangle = rectangleObject.getRectangle();
            // SCALE given rectangle down if using world dimensions!
            if (getBoundingRectangle().overlaps(rectangle) && movedDistance == TILE_SIZE) {
                onDownTrap = true;
                onUpTrap = false;
                if(!mapScreen.buttonUp) {
                    mapScreen.trapConfirm(onDownTrap, onUpTrap);
                }
            }
        }
    }

    private void checkUpTraps() {
        MapLayer upTrapObjectLayer = tiledMap.getLayers().get("Up_trap");
        // All the rectangles of the layer
        MapObjects mapObjects = upTrapObjectLayer.getObjects();
        // Cast it to RectangleObjects array
        Array<RectangleMapObject> rectangleObjects = mapObjects.getByType(RectangleMapObject.class);
        // Iterate all the rectangles
        for (RectangleMapObject rectangleObject : rectangleObjects) {
            Rectangle rectangle = rectangleObject.getRectangle();
            // SCALE given rectangle down if using world dimensions!
            if (getBoundingRectangle().overlaps(rectangle) && movedDistance == TILE_SIZE) {
                onUpTrap = true;
                onDownTrap = false;
                if(!mapScreen.buttonUp) {
                    mapScreen.trapConfirm(onDownTrap, onUpTrap);
                }
            }
        }
    }

    private void checkStoryTiles() {
        MapLayer storyTileObjectLayer = tiledMap.getLayers().get("Story_tiles");
        // All the rectangles of the layer
        MapObjects mapObjects = storyTileObjectLayer.getObjects();
        // Cast it to RectangleObjects array
        Array<RectangleMapObject> rectangleObjects = mapObjects.getByType(RectangleMapObject.class);
        // Iterate all the rectangles
        for (RectangleMapObject rectangleObject : rectangleObjects) {
            Rectangle rectangle = rectangleObject.getRectangle();
            // SCALE given rectangle down if using world dimensions!
            if (getBoundingRectangle().overlaps(rectangle) && movedDistance == TILE_SIZE) {
                mapScreen.goToStoryTile();
            }
        }
    }

    public void dispose() {
        getTexture().dispose();
        tiledMap.dispose();
    }
}
