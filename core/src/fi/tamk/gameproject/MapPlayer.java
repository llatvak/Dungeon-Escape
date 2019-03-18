package fi.tamk.gameproject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import static com.badlogic.gdx.Input.Keys.DOWN;
import static com.badlogic.gdx.Input.Keys.LEFT;
import static com.badlogic.gdx.Input.Keys.RIGHT;
import static com.badlogic.gdx.Input.Keys.SPACE;
import static com.badlogic.gdx.Input.Keys.UP;

public class MapPlayer extends Sprite {

    private DungeonEscape game;
    private MapScreen mapScreen;
    private TiledMap tiledMap;

    // Map size
    private final int TILE_SIZE = 64;
    private final float MAP_WIDTH = 29 * TILE_SIZE;
    private final float MAP_HEIGHT = 39 * TILE_SIZE;

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

    // Movement
    private int stepTotal;
    int movementPoints = 200;
    boolean allowMovement;
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

    public void receiveSteps(int stepTotal){
        this.stepTotal = stepTotal;
    }


    public void countMovementPoints() {
        // Amount of steps to move one tile
        int stepsNeededToMove = 5;
        // Checks if total step amount is divisible by the amount needed to move
        if(stepTotal > 0) {
            if(stepTotal % stepsNeededToMove == 0) {
                addMovementPoint();
                mapScreen.addStep();
            }
        }
    }

    public void addMovementPoint() {
        movementPoints++;
    }

    public void removeMovementPoint() {
        if(movementPoints > 0) {
            movementPoints--;
        }

    }

    public void checkAllowedMoves() {
        if(movementPoints > 0) {
            allowMovement = true;
        } else {
            allowMovement = false;
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
        // Can these methods be merged into one single method?
        checkDownTraps();
        checkUpTraps();
        checkStoryTiles();
    }

    public void checkDownTraps() {
        // Get the down trap rectangles layer
        MapLayer downTrapObjectLayer = (MapLayer)tiledMap.getLayers().get("Down_trap");
        // All the rectangles of the layer
        MapObjects mapObjects = downTrapObjectLayer.getObjects();
        // Cast it to RectangleObjects array
        Array<RectangleMapObject> rectangleObjects = mapObjects.getByType(RectangleMapObject.class);
        // Iterate all the rectangles
        for (RectangleMapObject rectangleObject : rectangleObjects) {
            Rectangle rectangle = rectangleObject.getRectangle();
            // SCALE given rectangle down if using world dimensions!
            if (getBoundingRectangle().overlaps(rectangle) && movedDistance == TILE_SIZE) {
                addMovementPoint();
                mapScreen.goToDownTrap();
            }
        }
    }

    public void checkUpTraps() {
        MapLayer upTrapObjectLayer = (MapLayer)tiledMap.getLayers().get("Up_trap");
        // All the rectangles of the layer
        MapObjects mapObjects = upTrapObjectLayer.getObjects();
        // Cast it to RectangleObjects array
        Array<RectangleMapObject> rectangleObjects = mapObjects.getByType(RectangleMapObject.class);
        // Iterate all the rectangles
        for (RectangleMapObject rectangleObject : rectangleObjects) {
            Rectangle rectangle = rectangleObject.getRectangle();
            // SCALE given rectangle down if using world dimensions!
            if (getBoundingRectangle().overlaps(rectangle) && movedDistance == TILE_SIZE) {
                addMovementPoint();
                mapScreen.goToUpTrap();
            }
        }
    }
    public void checkStoryTiles() {
        MapLayer storyTileObjectLayer = (MapLayer)tiledMap.getLayers().get("Story_tiles");
        // All the rectangles of the layer
        MapObjects mapObjects = storyTileObjectLayer.getObjects();
        // Cast it to RectangleObjects array
        Array<RectangleMapObject> rectangleObjects = mapObjects.getByType(RectangleMapObject.class);
        // Iterate all the rectangles
        for (RectangleMapObject rectangleObject : rectangleObjects) {
            Rectangle rectangle = rectangleObject.getRectangle();
            // SCALE given rectangle down if using world dimensions!
            if (getBoundingRectangle().overlaps(rectangle) && movedDistance == TILE_SIZE) {
                addMovementPoint();
                mapScreen.goToStoryTile();
            }
        }
    }

    public void dispose() {
        getTexture().dispose();
        tiledMap.dispose();
    }
}
