package fi.tamk.rentogames.Framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Disposable;

import fi.tamk.rentogames.Map.MapPlayer;

import static com.badlogic.gdx.Input.Keys.DOWN;
import static com.badlogic.gdx.Input.Keys.LEFT;
import static com.badlogic.gdx.Input.Keys.RIGHT;
import static com.badlogic.gdx.Input.Keys.SPACE;
import static com.badlogic.gdx.Input.Keys.UP;

//        Insert these to the class you use this InputProcessor

//        MyInputProcessor inputProcessor = new MyInputProcessor();
//        Gdx.input.setInputProcessor(inputProcessor);

public class MyInputProcessor implements InputProcessor, Disposable {

    private MapPlayer player;

    public MyInputProcessor(MapPlayer player) {
        this.player = player;
    }

    public boolean keyDown (int keycode) {
        if(!player.moving && player.allowMovement && keycode == UP) {
            player.setUpMove();
        }

        if(!player.moving && player.allowMovement && keycode == DOWN) {
            player.setDownMove();
        }

        if(!player.moving && player.allowMovement && keycode == LEFT) {
            player.setLeftMove();
        }

        if(!player.moving && player.allowMovement && keycode == RIGHT) {
            player.setRightMove();
        }

        if(keycode == SPACE) {
            player.addMovementPoint();
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
            player.setUpMove();
        }
        if(!player.moving && player.allowMovement && y > screenHeightHalf + 100) {
            player.setDownMove();
        }
        if(!player.moving && player.allowMovement && x < screenWidthHalf - 100  ) {
            player.setLeftMove();
        }
        if(!player.moving && player.allowMovement && x > screenWidthHalf + 100) {
            player.setRightMove();
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

    @Override
    public void dispose() {
        player.dispose();
    }
}