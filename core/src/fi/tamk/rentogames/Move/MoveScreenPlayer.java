package fi.tamk.rentogames.Move;

import com.badlogic.gdx.Gdx;
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
import fi.tamk.rentogames.Framework.Save;
import fi.tamk.rentogames.Framework.Utils;

/**
 * Creates the player in Box2D world and holds information for it.
 *
 * <p>
 * Class to create the player and control the movement and actions with variables.
 * Holds textures for different actions and uses animation when moving.
 * Box2D body used as a main player in game, uses physics for jumping.
 * </p>
 *
 * @author Lauri Latva-Kyyny
 * @author  Miko Kauhanen
 * @version 1.0
 */
public class MoveScreenPlayer {
    // Box2D player body
    /**
     * Player in move screen as a box2D body.
     */
    private Body playerBody;

    // player animation and texture
    /**
     * Texture for player when idle.
     */
    private Texture playerIdleTexture;
    /**
     * Player texture sheet when jumping.
     */
    private Texture playerJumpsheetTexture;
    /**
     * Player texture sheet when running.
     */
    private Texture playerRunsheetTexture;
    /**
     * Player texture sheet when squatting.
     */
    private Texture playerSquatsheetTexture;

    // Animations
    /**
     * Jump animation as texture region.
     */
    private Animation<TextureRegion> jumpAnimation;
    /**
     * Run animation as texture region.
     */
    private Animation<TextureRegion> runAnimation;
    /**
     * Squat animation as texture region.
     */
    private Animation<TextureRegion> squatAnimation;

    /**
     * State time used in animations.
     */
    private float stateTime;

    // Current frame textures
    /**
     * Current texture frame used for printing specific texture.
     */
    private TextureRegion currentFrameTexture;

    //player jump variables
    /**
     * Boolean value to check if player character already jumped.
     */
    private boolean playerJumped = false;
    /**
     * Int value to count jumps user made in real life.
     */
    private int countedJumps = 0;
    /**
     * Boolean value to check if player is moving.
     */
    private boolean playerMoving = false;

    /**
     * Boolean value to tell if player is squatting.
     */
    private boolean playerSquatting = false;
    /**
     * Boolean value to tell is player running currently.
     */
    private boolean playerRunning = false;
    /**
     * Boolean value to tell if player is in air or not.
     */
    private boolean playerJumping = false;

    /**
     * State time to stop squats from counting for specific time.
     */
    private int squatStateTime = 0;

    /**
     * Moves required to pass trap in game.
     */
    private int movesRequired = 3;


    /**
     * Constructor for creating player and animations.
     *
     * <p>
     * Receives world from {@link MoveScreenMove} and puts a player body to it with and creates definition for it.
     * Creates fixture to player and adds an idle texture.
     * Creates all texture sheets used in animations.
     * Creates all animations.
     * </p>
     *
     * @param w receives a world to place box2D bodies in
     */
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

    /**
     * Creates player's body definition and type, also sets positions and returns that definition.
     *
     * @return body definition of move screen's player
     */
    // Defines the player body type and sets position
    private BodyDef getDefinitionOfBody() {
        BodyDef myBodyDef = new BodyDef();
        myBodyDef.type = BodyDef.BodyType.DynamicBody;
        myBodyDef.position.set(0.56f, 0.25f);
        return myBodyDef;
    }

    /**
     * Creates a shape for player body and sets it to specific size and returns that.
     *
     * @return Return shape and size of the player in move screen
     */
    // Sets player body shape and size and returns the shape
    private PolygonShape getPlayerShape() {
        PolygonShape playerBox = new PolygonShape();
        playerBox.setAsBox(0.4f, 0.7f);
        return playerBox;
    }

    /**
     * Returns player body's x position
     *
     * @return player body's current x position
     */
    // Gets player X position and returns it
    public float getPlayerX() {
        return playerBody.getPosition().x;
    }

    /**
     * Returns player body's y position
     *
     * @return player body's current y position
     */
    // Gets player Y position and returns it
    public float getPlayerY() {
        return playerBody.getPosition().y;
    }

    /**
     * Draws player current frame texture from texture sheets or player with idle texture.
     *
     * @param b sprite batch to draw player's texture
     */
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

    /**
     * Creates jump animation.
     *
     * <p>
     * Texture sheet row and column amount required to split the texture to specific size texture frames.
     * Splits the jump texture sheet into 2D-array using split width and height values.
     * Creates 1D-array and uses {@link Utils} class to transform 2D array of textures to 1D-array using rows and columns given.
     * Creates the jump animation and set's duration for one frame and puts 1D-array of textures to texture region.
     * Creates current frame texture from animation using state time and sets it to loop or not with boolean value.
     * </p>
     */
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

    /**
     * Creates run animation.
     *
     * <p>
     * Texture sheet row and column amount required to split the texture to specific size texture frames.
     * Splits the run texture sheet into 2D-array using split width and height values.
     * Creates 1D-array and uses {@link Utils} class to transform 2D array of textures to 1D-array using rows and columns given.
     * Creates the run animation and set's duration for one frame and puts 1D-array of textures to texture region.
     * Creates current frame texture from animation using state time and sets it to loop or not with boolean value.
     * </p>
     */
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

    /**
     * Creates squat animation.
     *
     * <p>
     * Texture sheet row and column amount required to split the texture to specific size texture frames.
     * Splits the squat texture sheet into 2D-array using split width and height values.
     * Creates 1D-array and uses {@link Utils} class to transform 2D array of textures to 1D-array using rows and columns given.
     * Creates the squat animation and set's duration for one frame and puts 1D-array of textures to texture region.
     * Creates current frame texture from animation using state time and sets it to loop or not with boolean value.
     * </p>
     */
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

    /**
     * Method used to control data from player jumping in real life.
     *
     * <p>
     * Updates state time by adding delta time to float variable.
     * Sets linear velocity for player in move screen if x position under specific value.
     * Calls method to count player's jumps made in real life.
     * </p>
     *
     * <p>
     * If player is in air and enough moves have been done sets and player is not moving currently,
     * loops jump animation once and sets a linear impulse to create player body's jump using box2D physics.
     * Plays sound when player has jumped and sets boolean values for next steps in move screen.
     * Sets player to run and animation for player when move screen comes visible.
     * </p>
     *
     */
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
            GameAudio.playSound("jumpsound", Save.getCurrentAudioSetting());
        }
        // Run animation when move screen comes visible
        if(playerBody.getPosition().y <= 1.25f && playerBody.getPosition().x < 1) {
            currentFrameTexture = runAnimation.getKeyFrame(stateTime, true);
            playerRunning = true;
        } else {
            playerRunning = false;
        }
    }

    /**
     * Counts jumps player has made in real life.
     *
     * <p>
     * Using accelerometer Y-values counts player jumps when going up and down.
     * Only counts jumps if player has not gone up or down.
     * </p>
     */
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

    /**
     * Counts squats player has made in real life.
     *
     * <p>
     * Uses state time to control squat counting constantly.
     * Counts squats if player has gone down and player has not jumped.
     * </p>
     */
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

    /**
     * Method used for to control data from player squatting in real life.
     *
     * <p>
     * Updates state time by adding delta time to float variable.
     * Sets linear velocity for player in move screen if x position under specific value.
     * Calls method to count player's jumps made in real life.
     * </p>
     *
     */
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

    /**
     * Sets squat animation to dodge arrow trap in {@link fi.tamk.rentogames.Screens.MoveScreenSquat}
     */
    public void playerDodge() {
        currentFrameTexture = squatAnimation.getKeyFrame(stateTime, false);
        playerSquatting = true;
    }

    /**
     * Sets a linear x velocity to player and loops run animation when arrow is passed in {@link fi.tamk.rentogames.Screens.MoveScreenSquat}
     */
    // Method for running after arrow is passed
    public void playerRun() {
        playerBody.setLinearVelocity(3f, 0);
        currentFrameTexture = runAnimation.getKeyFrame(stateTime, true);
        playerRunning = true;
    }

    /**
     * Returns counted jumps by dividing them by 2.
     *
     * @return squat amount in int value
     */
    // Returns counted jumps
    public int getCountedJumps() {
        return countedJumps/2;
    }

    /**
     * Returns required move amount that player has to do in real life.
     *
     * @return move amount for player to do in real life as int value
     */
    public int getMovesRequired() {
        return movesRequired;
    }

    /**
     * Sets player to squat using boolean value.
     *
     * @param dodgeTrap boolean value received from {@link fi.tamk.rentogames.Screens.MoveScreenSquat} to know when to squat
     */
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
