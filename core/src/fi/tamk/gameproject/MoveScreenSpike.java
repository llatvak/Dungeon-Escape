package fi.tamk.gameproject;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class MoveScreenSpike {
    private static World world;
    private static Texture spikeTexture;
    private Body spikeBody;

    public MoveScreenSpike(World w) {
        world = w;
        spikeTexture = new Texture("spikes.png");
        spikeBody = world.createBody(getSpikeBodyDef());
        spikeBody.createFixture(getSpikeShape(), 0.0f);
        spikeBody.setUserData(spikeTexture);
    }

    private BodyDef getSpikeBodyDef() {
        BodyDef myBodyDef = new BodyDef();
        myBodyDef.type = BodyDef.BodyType.StaticBody;
        myBodyDef.position.set(MoveScreen.WORLD_WIDTH/2 + spikeTexture.getWidth()/100f/2, 0.25f + spikeTexture.getHeight()/100f/2);
        return myBodyDef;
    }

    private PolygonShape getSpikeShape() {
        PolygonShape spikeBox = new PolygonShape();
        spikeBox.setAsBox(spikeTexture.getWidth()/100f/2,0.25f + spikeTexture.getHeight()/100f/2);
        return spikeBox;
    }

    public void draw(SpriteBatch b) {
        b.draw(spikeTexture, spikeBody.getPosition().x - spikeTexture.getWidth()/100f/2, spikeBody.getPosition().y, spikeTexture.getWidth()/100f, spikeTexture.getHeight()/100f);
    }

    public void dispose() {
        spikeTexture.dispose();
    }
}
