package fi.tamk.rentogames.Move;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Creates Box2D body that is used as a ground.
 *
 * <p>
 * Creates definitions and shape for Box2D object that covers the whole
 * bottom of the game screen.
 * Used as a ground to stop player falling out of the screen.
 * </p>
 *
 * @author Lauri Latva-Kyyny
 * @author  Miko Kauhanen
 * @version 1.0
 */
public class MoveScreenGround {
    /**
     * Current game width to set up the width of the ground.
     */
    private float gameWidth;

    /**
     * Constructor that creates the body to the world.
     *
     * <p>
     * Creates the body using received game width and world.
     * Calls methods to set body ground body definition and fixture.
     * </p>
     *
     * @param boxWorld receives box2d world from {@link MoveScreenMove} to create body
     * @param gameScreenWidth uses game width in metrics from main class to set the floor
     */
    public MoveScreenGround(World boxWorld, Float gameScreenWidth) {
        // Current world
        gameWidth = gameScreenWidth;
        // Creates body to world and gets definitions and fixtures to it
        Body groundBody = boxWorld.createBody(getGroundBodyDef());
        groundBody.createFixture(getGroundShape(), 0.0f);
    }

    /**
     * Defines ground body type and sets position for it.
     *
     * <p>
     * Ground's body is set to static so it doesn't move when another body collides with it.
     * Uses the game screen width to set up the position for the ground.
     * </p>
     *
     * @return box2D body to create a body with definition
     */
    // Defines the ground body type and sets position
    private BodyDef getGroundBodyDef() {
        BodyDef myBodyDef = new BodyDef();
        myBodyDef.type = BodyDef.BodyType.StaticBody;
        myBodyDef.position.set(gameWidth/2, 0.25f);
        return myBodyDef;
    }

    /**
     * Defines body shape and sets the size.
     *
     * <p>
     * Sets body shape to polygon and also sets size for the box.
     * </p>
     *
     * @return ground as a polygon shape
     */
    // Sets ground body shape and size and returns the shape
    private PolygonShape getGroundShape() {
        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(gameWidth/2, 0.25f);
        return groundBox;
    }
}
