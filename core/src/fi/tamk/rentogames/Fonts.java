package fi.tamk.rentogames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class Fonts {

    private final static int SMALL = 0;
    private final static int MEDIUM = 1;

    private BitmapFont fontRobotoSm;
    private BitmapFont fontRobotoMed;

    private GlyphLayout layout;
    private FreeTypeFontGenerator fontGenerator;

    private OrthographicCamera fontCamera;

    Fonts(){
        createFontCamera();
    }

    private void createFontCamera(){
        fontCamera = new OrthographicCamera();
        fontCamera.setToOrtho(false, 360f, 640f);
    }

    OrthographicCamera getCamera() {
        return fontCamera;
    }

    public BitmapFont createSmallFont() {
        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("Roboto-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 14;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 2;
        fontRobotoSm = fontGenerator.generateFont(parameter); // Generates BitmapFont

        return fontRobotoSm;
    }

    BitmapFont createMediumFont() {
        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("Roboto-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 24;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 1;
        fontRobotoMed = fontGenerator.generateFont(parameter); // Generates BitmapFont

        return fontRobotoMed;
    }

    public BitmapFont getFont(int size) {
        if(size == SMALL) {
            return fontRobotoSm;
        }

        if (size == MEDIUM) {
            return  fontRobotoMed;
        }

        return fontRobotoMed;
    }

}
