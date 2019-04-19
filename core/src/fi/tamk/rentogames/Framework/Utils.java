package fi.tamk.rentogames.Framework;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

/*
This class holds general utilities
 */
/**
 * @author
 * @author
 * @version
 */
public class Utils {
    // Method for creating a random number
    private float randomNumber(float min, float max) {
        return MathUtils.random(min, max);
    }

    /**
     * @param tr
     * @param cols
     * @param rows
     * @return
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
