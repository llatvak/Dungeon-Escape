package fi.tamk.rentogames.Move;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * @author
 * @author
 * @version
 */
public class MoveScreenGround {
    private float gameWidth;

    /**
     * @param w
     * @param gW
     */
    public MoveScreenGround(World w, Float gW) {
        // Current world
        gameWidth = gW;
        // Creates body to world and gets definitions and fixtures to it
        Body groundBody = w.createBody(getGroundBodyDef());
        groundBody.createFixture(getGroundShape(), 0.0f);
    }

    // Defines the ground body type and sets position
    private BodyDef getGroundBodyDef() {
        BodyDef myBodyDef = new BodyDef();
        myBodyDef.type = BodyDef.BodyType.StaticBody;
        myBodyDef.position.set(gameWidth/2, 0.25f);
        return myBodyDef;
    }

    // Sets ground body shape and size and returns the shape
    private PolygonShape getGroundShape() {
        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(gameWidth/2, 0.25f);
        return groundBox;
    }
}
