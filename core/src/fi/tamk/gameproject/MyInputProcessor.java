package fi.tamk.gameproject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;


import static com.badlogic.gdx.Input.Keys.DOWN;
import static com.badlogic.gdx.Input.Keys.LEFT;
import static com.badlogic.gdx.Input.Keys.RIGHT;
import static com.badlogic.gdx.Input.Keys.SPACE;
import static com.badlogic.gdx.Input.Keys.UP;

//        Insert these to the class you use this InputProcessor

//        MyInputProcessor inputProcessor = new MyInputProcessor();
//        Gdx.input.setInputProcessor(inputProcessor);

public class MyInputProcessor implements InputProcessor {

    MapPlayer player;

    public MyInputProcessor(MapPlayer player) {
        this.player = player;
    }

    public boolean keyDown (int keycode) {
        if(!player.moving && player.allowMovement && keycode == UP) {
            player.setUpMove(true);
        }

        if(!player.moving && player.allowMovement && keycode == DOWN) {
            player.setDownMove(true);
        }

        if(!player.moving && player.allowMovement && keycode == LEFT) {
            player.setLeftMove(true);
        }

        if(!player.moving && player.allowMovement && keycode == RIGHT) {
            player.setRightMove(true);
        }

        if(keycode == SPACE) {
            //player.addStep();
        }

        if(keycode == Input.Keys.BACK){
            Gdx.app.log("Back", "going back");
            return true;
        }
        return true;
    }

    public boolean keyUp (int keycode) {
        return false;
    }

    public boolean keyTyped (char character) {
        return false;
    }

    public boolean touchDown (int x, int y, int pointer, int button) {
        int screenWidthHalf = Gdx.graphics.getWidth() / 2;
        int screenHeightHalf = Gdx.graphics.getHeight() / 2;

        if(!player.moving && player.allowMovement && y < screenHeightHalf - 100) {
            player.setUpMove(true);
        }
        if(!player.moving && player.allowMovement && y > screenHeightHalf + 100) {
            player.setDownMove(true);
        }
        if(!player.moving && player.allowMovement && x < screenWidthHalf - 100  ) {
            player.setLeftMove(true);
        }
        if(!player.moving && player.allowMovement && x > screenWidthHalf + 100) {
            player.setRightMove(true);
        }

        return true;
    }

    public boolean touchUp (int x, int y, int pointer, int button) {
        return false;
    }

    public boolean touchDragged (int x, int y, int pointer) {
        return false;
    }

    public boolean mouseMoved (int x, int y) {
        return false;
    }

    public boolean scrolled (int amount) {
        return false;
    }
}