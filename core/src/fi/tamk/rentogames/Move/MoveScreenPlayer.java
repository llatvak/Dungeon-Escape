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

import fi.tamk.rentogames.Framework.GameAudio;
import fi.tamk.rentogames.Framework.Utils;

import static com.badlogic.gdx.Input.Keys.SPACE;
import static com.badlogic.gdx.Input.Keys.UP;

public class MoveScreenPlayer {
    // Box2D player body
    private Body playerBody;

    // player animation and texture
    private Texture playerIdleTexture;
    private Texture playerJumpsheetTexture;
    private Texture playerRunsheetTexture;
    private Texture playerSquatsheetTexture;

    // Animations
    private Animation<TextureRegion> jumpAnimation;
    private Animation<TextureRegion> runAnimation;
    private Animation<TextureRegion> squatAnimation;

    private float stateTime;

    // Current frame textures
    private TextureRegion currentFrameTexture;

    //player jump variables
    private boolean playerJumped = false;
    private int countedJumps = 0;
    private boolean playerMoving = false;

    private boolean playerSquatting = false;
    private boolean playerRunning = false;
    private boolean playerJumping = false;

    private int squatStateTime = 0;

    private int movesRequired = 3;


    public MoveScreenPlayer(World w) {
        // Creates body to world and gets definitions and fixtures to it
        playerBody = w.createBody(getDefinitionOfBody());
        playerBody.createFixture(getPlayerShape(), 0.0f);
        playerIdleTexture = new Texture("idleplayer.png");
        playerJumpsheetTexture = new Texture("newjumpsheet.png");
        playerRunsheetTexture = new Texture("runsheet.png");
        playerSquatsheetTexture = new Texture("squatsheet.png");
        createJumpAnimation();
        createRunAnimation();
        createSquatAnimation();
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
        if(playerRunning || playerJumping || playerSquatting) {
            b.draw(currentFrameTexture, playerBody.getPosition().x - 1.5f/2, playerBody.getPosition().y - 2f/2,
                    1.5f, 2f);
        } else {
            b.draw(playerIdleTexture, playerBody.getPosition().x - 1.5f/2, playerBody.getPosition().y - 2f/2,
                    1.5f, 2f);
        }

    }

    // Creates jump animation
    private void createJumpAnimation() {
        // Rows and columns in texture sheet
        final int FRAME_COLS = 4;
        final int FRAME_ROWS = 1;
        // Defines the size of texture sheet frames and saves width and height to variables
        int tileWidth = playerJumpsheetTexture.getWidth() / FRAME_COLS;
        int tileHeight = playerJumpsheetTexture.getHeight() / FRAME_ROWS;
        // Using tile width and height values splits the texture sheet
        TextureRegion [][] tmp = TextureRegion.split(playerJumpsheetTexture, tileWidth, tileHeight);
        // Using Utils class method makes texture sheet from 2D array to 1D
        TextureRegion [] allFrames = Utils.toTextureArray(tmp, FRAME_COLS, FRAME_ROWS);
        // Initializes the animation
        jumpAnimation = new Animation<TextureRegion>(4 / 60f, allFrames);
        // Current split frame texture from texture sheet
        currentFrameTexture = jumpAnimation.getKeyFrame(stateTime, true);
    }

    // Creates run animation
    private void createRunAnimation() {
        // Rows and columns in texture sheet
        final int FRAME_COLS = 5;
        final int FRAME_ROWS = 1;
        // Defines the size of texture sheet frames and saves width and height to variables
        int tileWidth = playerRunsheetTexture.getWidth() / FRAME_COLS;
        int tileHeight = playerRunsheetTexture.getHeight() / FRAME_ROWS;
        // Using tile width and height values splits the texture sheet
        TextureRegion [][] tmp = TextureRegion.split(playerRunsheetTexture, tileWidth, tileHeight);
        // Using Utils class method makes texture sheet from 2D array to 1D
        TextureRegion [] allFrames = Utils.toTextureArray(tmp, FRAME_COLS, FRAME_ROWS);
        // Initializes the animation
        runAnimation = new Animation<TextureRegion>(4 / 60f, allFrames);
        // Current split frame texture from texture sheet
        currentFrameTexture = runAnimation.getKeyFrame(stateTime, true);
    }

    // Creates squat animation
    private void createSquatAnimation() {
        // Rows and columns in texture sheet
        final int FRAME_COLS = 3;
        final int FRAME_ROWS = 1;
        // Defines the size of texture sheet frames and saves width and height to variables
        int tileWidth = playerSquatsheetTexture.getWidth() / FRAME_COLS;
        int tileHeight = playerSquatsheetTexture.getHeight() / FRAME_ROWS;
        // Using tile width and height values splits the texture sheet
        TextureRegion [][] tmp = TextureRegion.split(playerSquatsheetTexture, tileWidth, tileHeight);
        // Using Utils class method makes texture sheet from 2D array to 1D
        TextureRegion [] allFrames = Utils.toTextureArray(tmp, FRAME_COLS, FRAME_ROWS);
        // Initializes the animation
        squatAnimation = new Animation<TextureRegion>(4 / 60f, allFrames);
        // Current split frame texture from texture sheet
        currentFrameTexture = squatAnimation.getKeyFrame(stateTime, true);
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
            currentFrameTexture = jumpAnimation.getKeyFrame(stateTime, false);
            playerBody.applyLinearImpulse(new Vector2(4f, 6f),
                    playerBody.getWorldCenter(), true);
            playerMoving = true;
            playerJumping = true;
            GameAudio.playSound("jumpsound");
        }
        // Run animation when move screen comes visible
        if(playerBody.getPosition().y <= 1.25f && playerBody.getPosition().x < 1) {
            currentFrameTexture = runAnimation.getKeyFrame(stateTime, true);
            playerRunning = true;
        } else {
            playerRunning = false;
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
            currentFrameTexture = runAnimation.getKeyFrame(stateTime, false);
            playerRunning = true;
        } else {
            playerRunning = false;
        }
    }

    public void playerDodge() {
        currentFrameTexture = squatAnimation.getKeyFrame(stateTime, false);
        playerSquatting = true;
    }

    // Method for running after arrow is passed
    public void playerRun() {
        playerBody.setLinearVelocity(3f, 0);
        currentFrameTexture = runAnimation.getKeyFrame(stateTime, true);
        playerRunning = true;
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
                if (keycode == SPACE) {
                    countedJumps++;
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

    public void setPlayerSquat(boolean dodgeTrap) {

        playerSquatting = dodgeTrap;
    }

    public void dispose() {
        playerJumpsheetTexture.dispose();
        playerRunsheetTexture.dispose();
        playerSquatsheetTexture.dispose();
        playerIdleTexture.dispose();
    }
}
