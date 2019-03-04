package fi.tamk.gameproject;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class MoveScreenGround {
    private static World world;

    public MoveScreenGround(World w) {
        world = w;
        Body groundBody = world.createBody(getGroundBodyDef());
        groundBody.createFixture(getGroundShape(), 0.0f);
    }

    private BodyDef getGroundBodyDef() {
        BodyDef myBodyDef = new BodyDef();
        myBodyDef.type = BodyDef.BodyType.StaticBody;
        myBodyDef.position.set(MoveScreen.WORLD_WIDTH/2, 0.25f);
        return myBodyDef;
    }

    private PolygonShape getGroundShape() {
        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(MoveScreen.WORLD_WIDTH/2, 0.25f);
        return groundBox;
    }
}
