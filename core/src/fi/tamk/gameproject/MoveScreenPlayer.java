package fi.tamk.gameproject;

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

import static com.badlogic.gdx.Input.Keys.SPACE;
import static com.badlogic.gdx.Input.Keys.UP;

public class MoveScreenPlayer {
    private Body playerBody;
    private boolean playerHasNotJumped = false;
    private Texture playerSheetTexture;
    private Animation<TextureRegion> jumpAnimation;
    private float stateTime;
    private TextureRegion currentFrameTexture;

    public MoveScreenPlayer(World w) {
        playerBody = w.createBody(getDefinitionOfBody());
        playerBody.createFixture(getPlayerShape(), 0.0f);
        playerSheetTexture = new Texture("jumpsheet.png");
        createJumpAnimation();
    }

    public BodyDef getDefinitionOfBody() {
        BodyDef myBodyDef = new BodyDef();
        myBodyDef.type = BodyDef.BodyType.DynamicBody;
        myBodyDef.position.set(MoveScreen.WORLD_WIDTH/5, MoveScreen.WORLD_HEIGHT/2);
        return myBodyDef;
    }

    public PolygonShape getPlayerShape() {
        PolygonShape playerBox = new PolygonShape();
        playerBox.setAsBox(0.4f, 0.7f);
        return playerBox;
    }

    public float getPlayerX() {
        float x = playerBody.getPosition().x;
        return x;
    }

    public float getPlayerY() {
        float y = playerBody.getPosition().y;
        return y;
    }

    public void draw(Batch b) {
        b.draw(currentFrameTexture, playerBody.getPosition().x - 1f/2, playerBody.getPosition().y - 1.5f/2,
                1f, 1.5f);
    }

    public void createJumpAnimation() {
        final int FRAME_COLS = 2;
        final int FRAME_ROWS = 1;

        int tileWidth = playerSheetTexture.getWidth() / FRAME_COLS;
        int tileHeight = playerSheetTexture.getHeight() / FRAME_ROWS;
        TextureRegion [][] tmp = TextureRegion.split(playerSheetTexture, tileWidth, tileHeight);
        TextureRegion [] allFrames = toTextureArray(tmp, FRAME_COLS, FRAME_ROWS);
        jumpAnimation = new Animation(4 / 60f, allFrames);
        currentFrameTexture = jumpAnimation.getKeyFrame(stateTime, true);
    }

    public static TextureRegion[] toTextureArray(TextureRegion[][] tr, int cols, int rows)  {
        TextureRegion[] frames = new TextureRegion[cols * rows];
        int index = 0;
        for(int i=0; i < rows; i++) {
            for(int j=0; j < cols; j++) {
                frames[index++] = tr[i][j];
            }
        }
        return frames;
    }

    public void playerJump() {
        stateTime += Gdx.graphics.getDeltaTime();
        //System.out.println(Gdx.input.getAccelerometerY());
        if(Gdx.input.getAccelerometerY() > 14 && playerHasNotJumped == false) {
            playerBody.applyLinearImpulse(new Vector2(3.7f, 5f),
                    playerBody.getWorldCenter(), true);
            playerHasNotJumped = true;
        }
        currentFrameTexture = jumpAnimation.getKeyFrame(stateTime, false);
    }

    public void checkInput() {
        Gdx.input.setInputProcessor(new InputAdapter() {

            // Keyboard controls
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == UP) {
                    playerBody.applyLinearImpulse(new Vector2(3.7f, 5f),
                            playerBody.getWorldCenter(), true);
                    playerHasNotJumped = true;
                    currentFrameTexture = jumpAnimation.getKeyFrame(stateTime, false);
                }
                return true;
            }


        });
    }

    public void dispose() {
        playerSheetTexture.dispose();
    }
}
