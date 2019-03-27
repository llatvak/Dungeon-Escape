package fi.tamk.rentogames.Framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.Disposable;

public class Fonts implements Disposable {

    private final static int SMALL = 0;
    private final static int MEDIUM = 1;

    private BitmapFont fontRobotoSm;
    private BitmapFont fontRobotoMed;

    private GlyphLayout layout;
    private FreeTypeFontGenerator fontGenerator;

    public BitmapFont createSmallFont() {
        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("Roboto-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 14;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 2;
        fontRobotoSm = fontGenerator.generateFont(parameter); // Generates BitmapFont

        return fontRobotoSm;
    }

    public BitmapFont createMediumFont() {
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

    @Override
    public void dispose() {
        fontRobotoMed.dispose();
        fontRobotoSm.dispose();
        fontGenerator.dispose();
    }
}
