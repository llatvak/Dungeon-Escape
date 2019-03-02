package fi.tamk.gameproject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;

public class MapPlayer extends Sprite {

    private boolean freeMovement = false;
    private boolean tileMovement = true;


    private final float TILE_SIZE = 64f;

    private MapScreen world;
    private Texture playerTexture;

    private float mapWidth = 29 * 64f;
    private float mapHeight = 39 * 64f;

    private float spriteWidth = 47f ;
    private float spriteHeight = 64f;

    private float speed = 300f;

    private float startingX = 0f;
    private float startingY = 24 * 64f;

    private float spriteX = startingX;
    private float spriteY = startingY;

    private boolean leftMove;
    private boolean rightMove = true;
    private boolean goUp;
    private boolean goDown;
    private boolean goRight;
    private boolean goLeft;


    float moveAmount;

    public boolean upLeftCollision;
    public boolean downLeftCollision;
    public boolean upRightCollision;
    public boolean downRightCollision;


    public MapPlayer(MapScreen world) {
        super( new Texture("velho.png"));
        this.world = world;
        System.out.println(startingY);
        setSize(spriteWidth, spriteHeight);
        setPosition(startingX, startingY);
    }

    public void resetMove() {
        goUp = false;
        goDown = false;
        goRight = false;
        goLeft = false;
    }

    public void horizontalMove() {

        if(freeMovement) {
            if (goRight) {
                spriteX += speed * Gdx.graphics.getDeltaTime();
            }

            if (goLeft) {
                spriteX -= speed * Gdx.graphics.getDeltaTime();
            }
        }

        if(tileMovement) {
            if (goRight) {
                spriteX += TILE_SIZE;
            }

            if (goLeft) {
                spriteX -= TILE_SIZE;
            }
        }


        setX(spriteX);
        resetMove();
    }

    public void verticalMove() {


        if(freeMovement) {
            if (goDown) {
                spriteY -= speed * Gdx.graphics.getDeltaTime();
            }

            if (goUp) {
                spriteY += speed * Gdx.graphics.getDeltaTime();
            }
        }

        if(tileMovement) {
            if (goUp) {
                spriteY += TILE_SIZE;
            }

            if (goDown) {
                spriteY -= TILE_SIZE;
            }
        }

        setY(spriteY);
        resetMove();
    }

    public void setLeftMove(boolean t) {
        if(rightMove && t) rightMove = false;
        goLeft = t;
    }

    public void setRightMove(boolean t) {
        if(leftMove && t) leftMove = false;
        goRight = t;
    }

    public void setDownMove(boolean t) {
        if(goUp && t) goUp = false;
        goDown = t;
    }

    public void setUpMove(boolean t) {
        if(goDown && t) goDown = false;
        goUp = t;
    }

    public void dispose() {
        getTexture().dispose();
    }
}
