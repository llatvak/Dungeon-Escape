package fi.tamk.gameproject;

import com.badlogic.gdx.math.MathUtils;

/*
This class holds general utilities
 */

public class Utils {


    private float randomNumber(float min, float max) {
        float random = MathUtils.random(min, max);
        return random;
    }

}
