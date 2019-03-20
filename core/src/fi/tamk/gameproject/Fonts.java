package fi.tamk.gameproject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class Fonts {

    public final static int SMALL = 0;
    public final static int MEDIUM = 1;

    BitmapFont fontRobotoSm;
    BitmapFont fontRobotoMed;

    GlyphLayout layout;
    FreeTypeFontGenerator fontGenerator;

    public void createSmallmFont() {
        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("Roboto-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 14;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 2;
        fontRobotoSm = fontGenerator.generateFont(parameter); // Generates BitmapFont
    }

    public void createMediumFont() {
        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("Roboto-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 24;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 1;
        fontRobotoMed = fontGenerator.generateFont(parameter); // Generates BitmapFont
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
