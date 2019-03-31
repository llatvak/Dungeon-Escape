package fi.tamk.rentogames.Move;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import fi.tamk.rentogames.Framework.Utils;

import static com.badlogic.gdx.Input.Keys.UP;

public class MoveScreenPlayer {
    // Box2D player body
    private Body playerBody;

    // player animation and texture
    private Texture playerSheetTexture;
    private Animation<TextureRegion> jumpAnimation;
    private float stateTime;
    private TextureRegion currentFrameTexture;

    //player jump variables
    private boolean playerJumped = false;
    private int countedJumps = 0;
    private boolean playerMoving = false;

    private int squatStateTime = 0;

    private int movesRequired = 3;


    public MoveScreenPlayer(World w) {
        // Creates body to world and gets definitions and fixtures to it
        playerBody = w.createBody(getDefinitionOfBody());
        playerBody.createFixture(getPlayerShape(), 0.0f);
        playerSheetTexture = new Texture("jumpsheet.png");
        createJumpAnimation();
    }

    // Defines the player body type and sets position
    private BodyDef getDefinitionOfBody() {
        BodyDef myBodyDef = new BodyDef();
        myBodyDef.type = BodyDef.BodyType.DynamicBody;
        myBodyDef.position.set(0.56f, 0.25f);
        return myBodyDef;
    }

    // Sets player body shape and size and returns the shape
    private PolygonShape getPlayerShape() {
        PolygonShape playerBox = new PolygonShape();
        playerBox.setAsBox(0.4f, 0.7f);
        return playerBox;
    }

    // Gets player X position and returns it
    public float getPlayerX() {
        return playerBody.getPosition().x;
    }

    // Gets player Y position and returns it
    public float getPlayerY() {
        return playerBody.getPosition().y;
    }

    // Draws the player using current animation texture
    // Also draws the player on the player body in Box2D
    public void draw(Batch b) {
        b.draw(currentFrameTexture, playerBody.getPosition().x - 1.5f/2, playerBody.getPosition().y - 2f/2,
                1.5f, 2f);
    }

    // Creates animation from player texture sheet
    private void createJumpAnimation() {
        // Rows and columns in texture sheet
        final int FRAME_COLS = 6;
        final int FRAME_ROWS = 1;
        // Defines the size of texture sheet frames and saves width and height to variables
        int tileWidth = playerSheetTexture.getWidth() / FRAME_COLS;
        int tileHeight = playerSheetTexture.getHeight() / FRAME_ROWS;
        // Using tile width and height values splits the texture sheet
        TextureRegion [][] tmp = TextureRegion.split(playerSheetTexture, tileWidth, tileHeight);
        // Using Utils class method makes texture sheet from 2D array to 1D
        TextureRegion [] allFrames = Utils.toTextureArray(tmp, FRAME_COLS, FRAME_ROWS);
        // Initializes the animation
        jumpAnimation = new Animation<TextureRegion>(4 / 60f, allFrames);
        // Current split frame texture from texture sheet
        currentFrameTexture = jumpAnimation.getKeyFrame(stateTime, true);
    }

    // Method for player jumping
    public void playerJump() {
        // Counts time of program running
        stateTime += Gdx.graphics.getDeltaTime();
        // Moves the player at the start to position X
        if(playerBody.getPosition().x < 1) {
            playerBody.setLinearVelocity(1f, 0);
        }
        // Counts player jumps
        countJumps();
        // When X amount of jumps is done player character jumps over trap
        if(Gdx.input.getAccelerometerY() > 14 && countedJumps >= movesRequired * 2 && !playerMoving) {
            playerBody.applyLinearImpulse(new Vector2(4f, 6f),
                    playerBody.getWorldCenter(), true);
            playerMoving = true;
        }
        // Run animation when move screen comes visible
        if(playerBody.getPosition().y <= 1.25f && playerBody.getPosition().x < 1) {
            currentFrameTexture = jumpAnimation.getKeyFrame(stateTime, false);
        }
    }

    // Counts player jumps
    private void countJumps() {
        // Counts jumps when player jumps and when player drops using accelerometer Y values
        // To control jump counting boolean values are used
        if(Gdx.input.getAccelerometerY() > 14 && !playerJumped) {
            playerJumped = true;
            countedJumps ++;
        }
        if(Gdx.input.getAccelerometerY() < 14) {
            playerJumped = false;
        }
    }

    // Counts player squats
    private void countSquats() {
        squatStateTime ++;
        // Counts squats when player squats
        if(Gdx.input.getAccelerometerY() > 13 && !playerJumped) {
            if (squatStateTime >= 60) {
                countedJumps += 2;
                squatStateTime = 0;
            }
            playerJumped = true;
        }
        if(Gdx.input.getAccelerometerY() < 13) {
            playerJumped = false;
        }
    }

    public void playerSquat() {
        // Counts time of program running
        stateTime += Gdx.graphics.getDeltaTime();
        // Moves the player at the start to position X
        if(playerBody.getPosition().x < 1) {
            playerBody.setLinearVelocity(1f, 0);
        }
        // Counts player squats
        countSquats();

        // Run animation when move screen comes visible
        if(playerBody.getPosition().y <= 1.25f && playerBody.getPosition().x < 1) {
            currentFrameTexture = jumpAnimation.getKeyFrame(stateTime, false);
        }
    }

    // Method for running after arrow is passed
    public void playerRun() {
        playerBody.setLinearVelocity(3f, 0);
        currentFrameTexture = jumpAnimation.getKeyFrame(stateTime, true);
    }

    // For desktop testing
    public void checkInput() {
        Gdx.input.setInputProcessor(new InputAdapter() {

            // Keyboard controls
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == UP && !playerMoving && playerBody.getPosition().x > 1.215f) {
                    playerBody.applyLinearImpulse(new Vector2(4f, 6f),
                            playerBody.getWorldCenter(), true);
                    playerMoving = true;
                }
                return true;
            }
        });
    }

    // Returns counted jumps
    public int getCountedJumps() {
        return countedJumps/2;
    }

    public int getMovesRequired() {
        return movesRequired;
    }

    public void dispose() {
        playerSheetTexture.dispose();
    }
}
