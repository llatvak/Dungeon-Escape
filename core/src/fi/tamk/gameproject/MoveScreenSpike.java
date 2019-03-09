package fi.tamk.gameproject;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class MoveScreenSpike {
    private static World world;

    // Spike variables
    private static Texture spikeTexture;
    private Body spikeBody;

    // Creates body to world and gets definitions and fixtures to it
    public MoveScreenSpike(World w) {
        world = w;
        spikeTexture = new Texture("spikes.png");
        spikeBody = world.createBody(getSpikeBodyDef());
        spikeBody.createFixture(getSpikeShape(), 0.0f);
        spikeBody.setUserData(spikeTexture);
    }

    // Defines the player body type and sets position
    private BodyDef getSpikeBodyDef() {
        BodyDef myBodyDef = new BodyDef();
        myBodyDef.type = BodyDef.BodyType.StaticBody;
        myBodyDef.position.set(MoveScreen.WORLD_WIDTH/2 + spikeTexture.getWidth()/100f/2, 0.25f + spikeTexture.getHeight()/100f/2);
        return myBodyDef;
    }

    // Sets spike body shape and size and returns the shape
    private PolygonShape getSpikeShape() {
        PolygonShape spikeBox = new PolygonShape();
        spikeBox.setAsBox(spikeTexture.getWidth()/100f/2,0.25f + spikeTexture.getHeight()/100f/2);
        return spikeBox;
    }

    // Draws the spike on spike body
    public void draw(SpriteBatch b) {
        b.draw(spikeTexture, spikeBody.getPosition().x - spikeTexture.getWidth()/100f/2, spikeBody.getPosition().y, spikeTexture.getWidth()/100f, spikeTexture.getHeight()/100f);
    }

    public void dispose() {
        spikeTexture.dispose();
    }
}
