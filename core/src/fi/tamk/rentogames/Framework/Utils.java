package fi.tamk.rentogames.Framework;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

/**
 * Class for useful methods accessible from all classes.
 *
 * @author Lauri Latva-Kyyny
 * @author  Miko Kauhanen
 * @version 1.0
 */
public class Utils {
    /**
     * @param tr texture region containing animation texture sheet in 2D-array
     * @param cols amount of columns in animation texture sheet
     * @param rows amount of rows in animation texture sheet
     * @return animation textures split to 1D-array
     */
    // Method for making 2D array to 1D array
    public static TextureRegion[] toTextureArray(TextureRegion[][] tr, int cols, int rows)  {
        TextureRegion[] frames = new TextureRegion[cols * rows];
        int index = 0;
        for(int i=0; i < rows; i++) {
            for(int j=0; j < cols; j++) {
                frames[index++] = tr[i][j];
            }
        }
        return frames;
    }

}
