package fi.tamk.rentogames.Framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;

import java.util.HashMap;

/**
 * @author
 * @author
 * @version
 */
public class GameAudio implements Disposable {

    /**
     *
     */
    private static HashMap<String, Sound> sounds;
    /**
     *
     */
    private static HashMap<String, Music> musics;

    static {
        sounds = new HashMap<String, Sound>();
        musics = new HashMap<String, Music>();
    }

    /**
     * @param path
     * @param name
     */
    public static void loadSound(String path, String name) {
        Sound sound = Gdx.audio.newSound(Gdx.files.internal(path));
        sounds.put(name, sound);
    }

    /**
     * @param path
     * @param name
     */
    public static void loadMusic(String path, String name) {
        Music music = Gdx.audio.newMusic(Gdx.files.internal(path));
        musics.put(name, music);
    }

    /**
     * @param name
     * @param volume
     */
    public static void playSound(String name, float volume) {
        sounds.get(name).play(volume);
    }

    public static void loopSound(String name) {
        sounds.get(name).loop();
    }

    public static void stopSound(String name) {
        sounds.get(name).stop();
    }

    /**
     * @param name
     * @param volume
     */
    public static void setMusicVolume(String name, float volume) {
        musics.get(name).setVolume(volume);
    }

    /**
     * @param name
     */
    public static void playMusic(String name) {
        musics.get(name).play();
    }

    /**
     * @param name
     */
    public static void loopMusic(String name) {
        musics.get(name).setLooping(true);
    }

    /**
     * @param name
     */
    public static void stopMusic(String name) {
        musics.get(name).stop();
    }

    @Override
    public void dispose() {
        for(Sound s: sounds.values()) {
            s.dispose();
        }

        for(Music m: musics.values()) {
            m.dispose();
        }
    }
}
