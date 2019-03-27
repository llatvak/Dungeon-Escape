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
    private MapLevel mapLevel;

    // Map size
    private final int TILE_SIZE = 64;

    // Player size
    private float spriteWidth = 62f;
    private float spriteHeight = 62f;

    // Starting location
    private float startingX = 8 * TILE_SIZE + 1f;
    private float startingY = TILE_SIZE + 1f;

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

    // Layer names
    private String jumpingTrap = "jump-trap";
    private String squatTrap = "squat-trap";
    private String levelChangeObject = "level-change";
    private String storyObject = "story-object";
    private String keyObject = "keys";




    MapPlayer(MapScreen mapScreen, MapLevel mapLevel) {
        super( new Texture("velho.png"));
        this.mapScreen = mapScreen;
        this.mapLevel = mapLevel;
        this.tiledMap = mapLevel.getCurrentMap();


        setSize(spriteWidth, spriteHeight);
        setPosition(startingX, startingY);

        movementPoints = 50;
    }

    void setMap() {
        this.tiledMap = mapLevel.getCurrentMap();
    }

    void spawn() {
        spriteX = startingX;
        spriteY = startingY;
        setPosition(spriteX,spriteY);
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
        mapScreen.updateMovesLabel();

    }

    private void removeMovementPoint() {
        if(movementPoints > 0) {
            movementPoints--;
            mapScreen.updateMovesLabel();
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
        checkObjectCollision(jumpingTrap);
        checkObjectCollision(squatTrap);
        checkObjectCollision(keyObject);
        checkObjectCollision(storyObject);
        checkObjectCollision(levelChangeObject);
    }



    private void checkObjectCollision(String layer) {
        // Boolean values for stepping on up/down trap
        boolean onJumpTrap;
        boolean onSquatTrap;

        // Get the down trap rectangles layer
        MapLayer downTrapObjectLayer = tiledMap.getLayers().get(layer);
        // All the rectangles of the layer
        MapObjects mapObjects = downTrapObjectLayer.getObjects();
        // Cast it to RectangleObjects array
        Array<RectangleMapObject> rectangleObjects = mapObjects.getByType(RectangleMapObject.class);
        // Iterate all the rectangles
        for (RectangleMapObject rectangleObject : rectangleObjects) {
            Rectangle rectangle = rectangleObject.getRectangle();
            // SCALE given rectangle down if using world dimensions!
            if (getBoundingRectangle().overlaps(rectangle) && movedDistance == TILE_SIZE) {

                if(layer.equals(jumpingTrap) ) {
                    onSquatTrap = false;
                    onJumpTrap = true;
                    if(!mapScreen.buttonUp) {
                        mapScreen.trapConfirm(onSquatTrap, onJumpTrap);
                    }
                }

                if(layer.equals(squatTrap) ) {
                    onJumpTrap = false;
                    onSquatTrap = true;
                    if(!mapScreen.buttonUp) {
                        mapScreen.trapConfirm(onSquatTrap, onJumpTrap);
                    }
                }

                if(layer.equals(keyObject) ) {
                    mapScreen.keyAmount++;
                    mapScreen.updateKeyLabel();
                    if (getBoundingRectangle().overlaps(rectangle)) {
                        clearKeys(rectangle.getX(), rectangle.getY());
                    }
                    Gdx.app.log("Collected", "keys: " + mapScreen.keyAmount);
                }

                if(layer.equals(levelChangeObject) ) {
                    if(mapScreen.keysCollected) {
                        mapScreen.changeLevel();
                    } else {
                        mapScreen.notEnoughKeys();
                    }
                }

                if(layer.equals(storyObject) ) {
                    //mapScreen.goToStoryScreen();
                }
            }
        }
    }

    private void clearKeys(float xCoord, float yCoord) {
        int indexX = (int) xCoord / TILE_SIZE;
        int indexY = (int) yCoord / TILE_SIZE;

        TiledMapTileLayer wallCells = (TiledMapTileLayer) tiledMap.getLayers().get("Keys");
        wallCells.setCell(indexX, indexY, null);
    }


    public void dispose() {
        getTexture().dispose();
        tiledMap.dispose();
        mapLevel.dispose();
    }
}
