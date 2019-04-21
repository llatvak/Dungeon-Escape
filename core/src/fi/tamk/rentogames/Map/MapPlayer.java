package fi.tamk.rentogames.Map;

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

/**
 * Creates and controls of player character
 *
 * Sets player character size, textures, movement speed, position and starting location.
 * Check collisions with tiled map objects and walls. Allows movement in tiled map.
 *
 * @author Miko Kauhanen
 * @author Lauri Latva-Kyyny
 * @version 1.0
 */

public class MapPlayer extends Sprite {

    private MapScreen mapScreen;

    private TiledMap tiledMap;

    private MapLevel mapLevel;

    /**
     * Tiled map tile size
     */
    private final int TILE_SIZE = 64;

    /**
     * Player's sprite size in pixels
     */
    private float spriteWidth = 62f;
    private float spriteHeight = 62f;

    /**
     * Player's starting x location as
     * (tile count) * (tile size in pixels) + (1 px to center sprite inside tile)
     */
    private float startingX = 8 * TILE_SIZE + 1f;

    /**
     * Player's starting y location as
     * (tile count) * (tile size in pixels) + (1 px to center sprite inside tile)
     */
    private float startingY = TILE_SIZE + 1f;

    /**
     * Player's x position
     */
    private float spriteX;

    /**
     * Player's y position
     */
    private float spriteY;

    /**
     * Is player's sprite moving
     */
    public boolean moving;

    /**
     * Player's current movement direction
     */
    private String currentDirection;

    // Player directions
    private boolean goUp;
    private boolean goDown;
    private boolean goRight;
    private boolean goLeft;


    // Player textures for different directions
    private Texture playerDown;
    private Texture playerUp;
    private Texture playerRight;
    private Texture playerLeft;

    /**
     * Amount of steps needed to receive movement points
     */
    public final int STEPSTOMOVE = 50;

    /**
     * Amount of movement points added when threshold is reached
     */
    private final int ADDEDPOINTS = 5;

    /**
     * Current movement points
     */
    public int movementPoints;

    /**
     * Is movement allowed
     */
    public boolean allowMovement;

    /**
     * Sprite movement speed each frame
     */
    private float movementSpeed = 4f;

    /**
     * Distance moved in pixels
     */
    private float movedDistance;

    private float moveAmount = movementSpeed;

    // Collision checking
    private boolean upLeftCollision;
    private boolean downLeftCollision;
    private boolean upRightCollision;
    private boolean downRightCollision;

    // Tiled map layer names
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

    /**
     * Constructor that receives the map screen object and level object.
     *
     *<p>
     * Receives the map screen object {@link MapScreen} and level object {@link MapLevel}.
     * Sets current location to starting location. Sets player sprite's size and textures.
     * Gets saved movement points from preferences.
     *</p>
     * @param mapScreen map screen object
     * @param mapLevel map level object
     */
    public MapPlayer(MapScreen mapScreen, MapLevel mapLevel) {
        this.mapScreen = mapScreen;
        this.mapLevel = mapLevel;
        this.tiledMap = mapLevel.getCurrentMap();

        spawn(Save.getCurrentLevel());
        setSize(spriteWidth, spriteHeight);
        setPosition(spriteX, spriteY);
        setTextures();
        movementPoints = Save.getMovementPoints();
    }

    /**
     * Sets players location to each levels starting location.
     */
    public void spawn(int level) {

        switch(level) {
            case 1: spriteX = startingX;
                    spriteY = startingY;
                    break;
            case 2: spriteX = startingX;
                    spriteY = startingY;
                    break;
            case 3: spriteX = startingX;
                    spriteY = startingY;
                    break;
            case 4: spriteX = startingX;
                    spriteY = startingY;
                    break;
            case 5: spriteX = 2 * TILE_SIZE + 1f;
                    spriteY = 3 * TILE_SIZE + 1f;
                    break;
            case 6: spriteX = 8 * TILE_SIZE + 1f;
                    spriteY = 2 * TILE_SIZE + 1f;
                    break;
            case 7: spriteX = 7 * TILE_SIZE + 1f;
                    spriteY = 13 * TILE_SIZE + 1f;
                    break;
            case 8: spriteX = 11 * TILE_SIZE + 1f;
                    spriteY = 8 * TILE_SIZE + 1f;
                    break;
            case 9: spriteX = 2 * TILE_SIZE + 1f;
                    spriteY = 14 * TILE_SIZE + 1f;
                    break;
            case 10: spriteX = 8 * TILE_SIZE + 1f;
                     spriteY = 2 * TILE_SIZE + 1f;
                     break;
        }
        setPosition(spriteX, spriteY);
    }

    /**
     * Sets player sprite's textures.
     *
     *<p>
     * Sets textures for different directions. Sets first texture direction to up.
     *</p>
     */
    private void setTextures() {
        playerDown = new Texture("playerdown.png");
        playerUp = new Texture("playerup.png");
        playerRight = new Texture("playerright.png");
        playerLeft = new Texture("playerleft.png");
        setTexture(playerUp);
    }

    /**
     * Changes textures based on sprite direction.
     *
     *<p>
     * Changes textures according to the direction player is moving.
     *</p>
     */
    private void setSpriteDirection(int direction) {
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

    /**
     * Moves player sprite to designated direction.
     *
     *<p>
     * When any of the directional boolean is set to true it checks that that directions corners
     * for walls. If way is free it moves sprite to that direction with every frame. Also changes
     * sprite texture direction. Sprite moves until moved distance equals tile size in pixels.
     * When moving to one direction it can't move to any other direction until reached tile size distance.
     * Once fully moved one tile its removes one movement point.
     *</p>
     */
    public void move(){

        int spriteDown = 1;
        int spriteUp = 2;
        int spriteRight = 3;
        int spriteLeft = 4;

        if(goDown) {
            getMyCorners(spriteX, spriteY - 1 * moveAmount);
            if(downLeftCollision && downRightCollision) {
                if (movedDistance < TILE_SIZE) {
                    setSpriteDirection(spriteDown);
                    spriteY -= movementSpeed;
                    movedDistance += movementSpeed;
                    if(movedDistance == TILE_SIZE) {
                        currentDirection = "down";
                        removeOneMovementPoint();
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
                    setSpriteDirection(spriteUp);
                    spriteY += movementSpeed;
                    movedDistance += movementSpeed;
                    if(movedDistance == TILE_SIZE) {
                        currentDirection = "up";
                        removeOneMovementPoint();
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
                    setSpriteDirection(spriteLeft);
                    spriteX -= movementSpeed;
                    movedDistance += movementSpeed;
                    if(movedDistance == TILE_SIZE) {
                        currentDirection = "left";
                        removeOneMovementPoint();
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
                    setSpriteDirection(spriteRight);
                    spriteX += movementSpeed;
                    movedDistance += movementSpeed;
                    if(movedDistance == TILE_SIZE) {
                        currentDirection = "right";
                        removeOneMovementPoint();
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

    /**
     * Adds one movement point.
     */
    public void addOneMovementPoint() {
        movementPoints = movementPoints + 1;
        Save.saveMovementPoints(movementPoints);
    }

    /**
     * Adds default amount of movement points.
     */
    public void addMovementPoints() {
        movementPoints = movementPoints + ADDEDPOINTS;
        Save.saveMovementPoints(movementPoints);
    }

    /**
     *
     * Adds movement points.
     *
     * @param points amount of points added
     */
    public void addMovementPoints(int points) {
        movementPoints = movementPoints + points * ADDEDPOINTS;
        Save.saveMovementPoints(movementPoints);
    }

    /**
     * Removes one movement point if current amount is positive.
     */
    public void removeOneMovementPoint() {
        if(movementPoints > 0) {
            movementPoints--;
            Save.saveMovementPoints(movementPoints);
        }
    }

    /**
     * Removes default amount of movement points.
     */
    public void removeMovementPoints() {
        if(movementPoints > 0) {
            if(movementPoints >= ADDEDPOINTS) {
                movementPoints = movementPoints - ADDEDPOINTS + 1;
            } else {
                movementPoints = 0;
            }

            Save.saveMovementPoints(movementPoints);
        }
    }

    /**
     * Removes multiple movement points at once.
     *
     * @param pointsToRemove amount of movement points to remove
     */
    public void removeMultipleMovementPoints(int pointsToRemove) {
        movementPoints = movementPoints - pointsToRemove - 1;
        if(movementPoints <= 0) {
            movementPoints = 1;
        }
        Save.saveMovementPoints(movementPoints);
    }

    /**
     * Checks if player has movement points available.
     */
    public void checkAllowedMoves() {
        allowMovement = movementPoints > 0;
    }

    /**
     * Sets player's movement direction to left. Plays walking sound.
     */
    public void setLeftMove() {
        goLeft = true;
        moving = true;
        GameAudio.playSound("walksound", Save.getCurrentAudioSetting());
    }

    /**
     * Sets player's movement direction to right. Plays walking sound.
     */
    public void setRightMove() {
        goRight = true;
        moving = true;
        GameAudio.playSound("walksound", Save.getCurrentAudioSetting());
    }

    /**
     * Sets player's movement direction to down. Plays walking sound.
     */
    public void setDownMove() {
        goDown = true;
        moving = true;
        GameAudio.playSound("walksound", Save.getCurrentAudioSetting());
    }

    /**
     * Sets player's movement direction to up. Plays walking sound.
     */
    public void setUpMove() {
        goUp = true;
        moving = true;
        GameAudio.playSound("walksound", Save.getCurrentAudioSetting());
    }

    /**
     * Sets player's movement direction to up. Plays walking sound.
     */
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
     * Checks if coordinate cell has walls.
     *
     * @param x coordinate in pixels
     * @param y coordinate in pixels
     * @return is cell free or not
     */
    private boolean isFree(float x, float y) {

        // Calculate coordinates to tile coordinates
        // example, (34,34) => (1,1)

        int indexX = (int) x / TILE_SIZE;
        int indexY = (int) y / TILE_SIZE;

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

    /**
     * Checks if player has collided with tiled map objects.
     *
     *<p>
     * Checks all tiled map layers for collision. Send layer names as a parameter
     * to checkObjectCollision. Checks some layers only on certain levels.
     *</p>
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
            if(Save.getCurrentLevel() == 4) {
                checkObjectCollision(storyObjectTwo);
            }
            if(Save.getCurrentLevel() == 6) {
                checkObjectCollision(storyObjectThree);
            }
            if(Save.getCurrentLevel() == 8) {
                checkObjectCollision(storyObjectFour);
            }
            if(Save.getCurrentLevel() == 10) {
                checkObjectCollision(storyObjectFive);
            }

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

    /**
     * Checks if player has collided with tiled map object layers.
     *
     *<p>
     * Checks all tiled map layers if player sprite's rectangle overlaps any of the objects in given layer parameter.
     * If the collided object is the same as in given parameter acts accordingly to given circumstance.
     *</p>
     *
     * @param layer tiled map layer that is checked for collision
     */
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
            // Check collision only when at the end of movement
            if (getBoundingRectangle().overlaps(rectangle) && movedDistance == TILE_SIZE) {

                // if on jumping trap open buttons to confirm or deny exercising
                if(layer.equals(jumpingTrap) ) {
                    onSquatTrap = false;
                    onJumpTrap = true;
                    if(!mapScreen.trapButtonsUp) {
                        mapScreen.trapConfirm(onSquatTrap, onJumpTrap);
                    }
                }

                // if on squatting trap open buttons to confirm or deny exercising
                if(layer.equals(squatTrap) ) {
                    onJumpTrap = false;
                    onSquatTrap = true;
                    if(!mapScreen.trapButtonsUp) {
                        mapScreen.trapConfirm(onSquatTrap, onJumpTrap);
                    }
                }

                // if on top of key, increase key amount, play sound and remove key object from map
                if(layer.equals(keyObject) ) {
                    mapScreen.keyAmount++;
                    if (getBoundingRectangle().overlaps(rectangle)) {
                        GameAudio.playSound("keysound", Save.getCurrentAudioSetting());
                        clearKeys(rectangle.getX(), rectangle.getY());
                        //TODO Better way to clear rectangle from tiled map object
                        rectangle.setPosition(1,1);
                    }
                }

                // if on on top of door check if all keys are collected
                // if keys collected go to next level
                // if not, create info window to remind player to collect all keys
                if(layer.equals(levelChangeObject) ) {
                    if(mapScreen.keysCollected) {
                        mapScreen.changeLevel();
                    } else {
                        mapScreen.createTutorial(6);
                    }
                }

                // if on top story object, creates story window
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

                // if on top of tutorial object, created tutorial window
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

    /**
     * Clears key sprite from tiled map when player collides with it.
     *
     * @param xCoord player' x coordinate
     * @param yCoord player's y coordinate
     */
    private void clearKeys(float xCoord, float yCoord) {
        int indexX = (int) xCoord / TILE_SIZE;
        int indexY = (int) yCoord / TILE_SIZE;

        TiledMapTileLayer wallCells = (TiledMapTileLayer) tiledMap.getLayers().get("Keys");
        wallCells.setCell(indexX, indexY, null);
    }

    /**
     * Moves player to previous direction.
     */
    public void moveToPreviousTile() {
        if(currentDirection.equals("up")) {
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

    /**
     * Sets map to current level.
     */
    public void setMap() {
        this.tiledMap = mapLevel.getCurrentMap();
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