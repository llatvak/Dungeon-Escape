package fi.tamk.rentogames.Map;

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

import fi.tamk.rentogames.DungeonEscape;
import fi.tamk.rentogames.Framework.GameAudio;
import fi.tamk.rentogames.Framework.Save;
import fi.tamk.rentogames.Screens.MapScreen;

public class MapPlayer extends Sprite {
    private MapScreen mapScreen;
    private TiledMap tiledMap;
    private MapLevel mapLevel;

    // Map size
    private final int TILE_SIZE = 64;

    // Player size
    private float spriteWidth = 62f;
    private float spriteHeight = 62f;

    private final int SPRITEDOWN = 1;
    private final int SPRITEUP = 2;
    private final int SPRITERIGHT = 3;
    private final int SPRITELEFT = 4;

    // Starting location
    private float startingX = 8 * TILE_SIZE + 1f;
    private float startingY = TILE_SIZE + 1f;

    private float spriteX = startingX;
    private float spriteY = startingY;

    // Movement direction
    public boolean moving;
    private String currentDirection;
    private boolean goUp;
    private boolean goDown;
    private boolean goRight;
    private boolean goLeft;

    // Amount of steps to move one tile
    public final int STEPSTOMOVE = 10;
    public final int ADDEDPOINTS = 5;
    public int movementPoints;
    public boolean allowMovement;
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
    private String storyObjectOne = "story-1";
    private String storyObjectTwo = "story-2";
    private String storyObjectThree = "story-3";
    private String storyObjectFour = "story-4";
    private String storyObjectFive = "story-5";
    private String keyObject = "keys";
    private String tutorialObjectIntro = "tutorial-intro";
    private String tutorialObjectKeys = "tutorial-keys";
    private String tutorialObjectMove = "tutorial-movement";
    private String tutorialObjectTraps = "tutorial-traps";

    private Texture playerDown;
    private Texture playerUp;
    private Texture playerRight;
    private Texture playerLeft;

    public MapPlayer(MapScreen mapScreen, MapLevel mapLevel) {
        playerDown = new Texture("playerdown.png");
        playerUp = new Texture("playerup.png");
        playerRight = new Texture("playerright.png");
        playerLeft = new Texture("playerleft.png");
        setTexture(playerUp);


        this.mapScreen = mapScreen;
        this.mapLevel = mapLevel;
        this.tiledMap = mapLevel.getCurrentMap();

        setSize(spriteWidth, spriteHeight);
        spriteX = startingX;
        spriteY = startingY;
        setPosition(startingX, startingY);
        movementPoints = Save.getMovementPoints();
    }

    public void setSpriteDirection(int direction) {
        switch (direction) {
            case 1: setTexture(playerDown);
            break;
            case 2: setTexture(playerUp);
            break;
            case 3: setTexture(playerRight);
                    this.flip(true,false);
            break;
            case 4: setTexture(playerLeft);
                    this.flip(true,false);
            break;
        }
    }

    public void setMap() {
        this.tiledMap = mapLevel.getCurrentMap();
    }

    public void spawn() {
        spriteX = startingX;
        spriteY = startingY;
        setPosition(spriteX,spriteY);
    }

    // Can this method be reduced in size?
    public void move(){
        if(goDown) {
            getMyCorners(spriteX, spriteY - 1 * moveAmount);
            if(downLeftCollision && downRightCollision) {
                if (movedDistance < TILE_SIZE) {
                    setSpriteDirection(SPRITEDOWN);
                    spriteY -= movementSpeed;
                    movedDistance += movementSpeed;
                    if(movedDistance == TILE_SIZE) {
                        currentDirection = "down";
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
                    setSpriteDirection(SPRITEUP);
                    spriteY += movementSpeed;
                    movedDistance += movementSpeed;
                    if(movedDistance == TILE_SIZE) {
                        currentDirection = "up";
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
                    setSpriteDirection(SPRITELEFT);
                    spriteX -= movementSpeed;
                    movedDistance += movementSpeed;
                    if(movedDistance == TILE_SIZE) {
                        currentDirection = "left";
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
                    setSpriteDirection(SPRITERIGHT);
                    spriteX += movementSpeed;
                    movedDistance += movementSpeed;
                    if(movedDistance == TILE_SIZE) {
                        currentDirection = "right";
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

    public void addOneMovementPoint() {
        movementPoints = movementPoints + 1;
        Save.saveMovementPoints(movementPoints);
    }

    public void addMovementPoints() {
        Gdx.app.log("Movementpoint", "added");
        movementPoints = movementPoints + ADDEDPOINTS;
        Save.saveMovementPoints(movementPoints);
    }

    public void addMultipleMovementPoints(int points) {
        movementPoints = movementPoints + points * ADDEDPOINTS;
        Save.saveMovementPoints(movementPoints);
    }

    public void removeMovementPoint() {
        if(movementPoints > 0) {
            movementPoints --;
            Save.saveMovementPoints(movementPoints);
        }
    }

    public void removeMultipleMovementPoints() {
        if(movementPoints > 0) {
            if(movementPoints >= ADDEDPOINTS) {
                movementPoints = movementPoints - ADDEDPOINTS + 1;
            } else {
                movementPoints = 0;
            }

            Save.saveMovementPoints(movementPoints);
        }
    }

    public void checkAllowedMoves() {
        allowMovement = movementPoints > 0;
    }

    public void setLeftMove() {
        goLeft = true;
        moving = true;
        GameAudio.playSound("walksound", Save.getCurrentAudioSetting());
    }

    public void setRightMove() {
        goRight = true;
        moving = true;
        GameAudio.playSound("walksound", Save.getCurrentAudioSetting());
    }

    public void setDownMove() {
        goDown = true;
        moving = true;
        GameAudio.playSound("walksound", Save.getCurrentAudioSetting());
    }

    public void setUpMove() {
        goUp = true;
        moving = true;
        GameAudio.playSound("walksound", Save.getCurrentAudioSetting());
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
    public void checkCollisions() {
        checkObjectCollision(jumpingTrap);
        checkObjectCollision(squatTrap);
        checkObjectCollision(keyObject);
        checkObjectCollision(levelChangeObject);

        if(DungeonEscape.story) {
            if(Save.getCurrentLevel() == 2) {
                checkObjectCollision(storyObjectOne);
            }
            if(Save.getCurrentLevel() == 5) {
                checkObjectCollision(storyObjectTwo);
            }

//            checkObjectCollision(storyObjectOne);
//            checkObjectCollision(storyObjectTwo);
//            checkObjectCollision(storyObjectThree);
//            checkObjectCollision(storyObjectFour);
//            checkObjectCollision(storyObjectFive);
        }

        if(DungeonEscape.tutorials) {
            if(Save.getCurrentLevel() == 1) {
                checkObjectCollision(tutorialObjectIntro);
                checkObjectCollision(tutorialObjectKeys);
                checkObjectCollision(tutorialObjectMove);
            }
            if(Save.getCurrentLevel() == 2) {
                checkObjectCollision(tutorialObjectTraps);
            }
        }

    }

    private void checkObjectCollision(String layer) {
        // Boolean values for stepping on up/down trap
        boolean onJumpTrap;
        boolean onSquatTrap;

        // Get the down trap rectangles layer
        MapLayer objectLayer = tiledMap.getLayers().get(layer);
        // All the rectangles of the layer
        MapObjects mapObjects = objectLayer.getObjects();
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
                   // mapScreen.updateKeyLabel();
                    if (getBoundingRectangle().overlaps(rectangle)) {
                        clearKeys(rectangle.getX(), rectangle.getY());
                        //TODO Better way to clear rectangle from tiled map object
                        rectangle.setPosition(1,1);
                    }
                    Gdx.app.log("Collected", "keys: " + mapScreen.keyAmount);
                }

                if(layer.equals(levelChangeObject) ) {
                    if(mapScreen.keysCollected) {
                        mapScreen.changeLevel();
                    } else {
                        mapScreen.createTutorial(6);
                    }
                }
                if(layer.equals(storyObjectOne) ) {
                    mapScreen.createStoryWindow(1);
                }
                if(layer.equals(storyObjectTwo) ) {
                    mapScreen.createStoryWindow(2);
                }
                if(layer.equals(storyObjectThree) ) {
                    mapScreen.createStoryWindow(3);
                }
                if(layer.equals(storyObjectFour) ) {
                    mapScreen.createStoryWindow(4);
                }
                if(layer.equals(storyObjectFive) ) {
                    mapScreen.createStoryWindow(5);
                }
                if(layer.equals(tutorialObjectIntro) ) {
                    mapScreen.createTutorial(1);
                }
                if(layer.equals(tutorialObjectKeys) ) {
                    mapScreen.createTutorial(2);
                }
                if(layer.equals(tutorialObjectMove) ) {
                    mapScreen.createTutorial(3);
                }
                if(layer.equals(tutorialObjectTraps) ) {
                    mapScreen.createTutorial(4);
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

    public String getCurrentDirection() {
        return currentDirection;
    }

    public void moveToPreviousTile() {
        if(currentDirection.equals("up")) {
            System.out.println(movedDistance);
            goUp = false;
            movedDistance = 0;
            setDownMove();
        } else if(currentDirection.equals("down")) {
            goDown = false;
            movedDistance = 0;
            setUpMove();
        } else if(currentDirection.equals("left")) {
            goLeft = false;
            movedDistance = 0;
            setRightMove();
        } else if(currentDirection.equals("right")) {
            goRight = false;
            movedDistance = 0;
            setLeftMove();
        }
    }

    public Texture getPlayerUpTexture() {
        return playerUp;
    }

    public void dispose() {
        getTexture().dispose();
        playerDown.dispose();
        playerUp.dispose();
        playerRight.dispose();
        playerLeft.dispose();
        tiledMap.dispose();
        mapLevel.dispose();
    }

}