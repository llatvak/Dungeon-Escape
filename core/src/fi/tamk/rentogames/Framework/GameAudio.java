package fi.tamk.rentogames.Framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;

import java.util.HashMap;

/**
 * Contains methods and hash maps to control sound settings.
 *
 * <p>
 * Static methods and hash maps that are accessed from other classes to control sound settings.
 * Using key pairs controls which sound or background music to control.
 * </p>
 *
 * @author Lauri Latva-Kyyny
 * @author  Miko Kauhanen
 * @version 1.0
 */
public class GameAudio implements Disposable {

    /**
     * Map that uses key pairs to control specific sound.
     */
    private static HashMap<String, Sound> sounds;

    /**
     * Map that uses key paris to control specific music.
     */
    private static HashMap<String, Music> musics;

    static {
        sounds = new HashMap<String, Sound>();
        musics = new HashMap<String, Music>();
    }

    /**
     * Creates sound and sets it to hash map.
     *
     * <p>
     * Called from main class {@link fi.tamk.rentogames.DungeonEscape} to create a sound from path and name
     * given as string.
     * Puts the sound to hash map and gives it a key pair.
     * </p>
     *
     * @param path receives a string path from main game used to find the file
     * @param name receives a string from main game that is used as a name for the file
     */
    public static void loadSound(String path, String name) {
        Sound sound = Gdx.audio.newSound(Gdx.files.internal(path));
        sounds.put(name, sound);
    }

    /**
     * Creates music and sets it to hash map.
     *
     * <p>
     * Called from main class {@link fi.tamk.rentogames.DungeonEscape} to create a music from path and name
     * given as string.
     * Puts the music to hash map and gives it a key pair.
     * </p>
     *
     * @param path receives a string path from main game used to find the file
     * @param name receives a string from main game that is used as a name for the file
     */
    public static void loadMusic(String path, String name) {
        Music music = Gdx.audio.newMusic(Gdx.files.internal(path));
        musics.put(name, music);
    }

    /**
     * Plays specific sound from hash map using key pairs with received volume.
     *
     * <p>
     * Receives a name that is used to find the sound file from hash map using key pairs.
     * Gets the file from hash map and plays it with received volume as a float.
     * </p>
     *
     * @param name name of the sound file used in hash map
     * @param volume float number to play sound with that volume setting
     */
    public static void playSound(String name, float volume) {
        sounds.get(name).play(volume);
    }

    /**
     * Sets the volume of specific music from hash map using key pairs.
     *
     * <p>
     * Receives a name that is used to find the music file from hash map using key pairs.
     * Gets the file from hash map and sets it's volume using received float value.
     * </p>
     *
     * @param name name of the music file used in hash map
     * @param volume float number to set the music to that volume setting
     */
    public static void setMusicVolume(String name, float volume) {
        musics.get(name).setVolume(volume);
    }

    /**
     * Plays specific music from hash map using key pairs.
     *
     * @param name name of the music file that is wanted to play
     */
    public static void playMusic(String name) {
        musics.get(name).play();
    }

    /**
     * Loops music from hash map using key pairs.
     *
     * @param name name of the music file wanted to loop
     */
    public static void loopMusic(String name) {
        musics.get(name).setLooping(true);
    }

    /**
     * Stops specific music using hash map's key pairs.
     *
     * @param name name of the music file wanted to stop
     */
    public static void stopMusic(String name) {
        musics.get(name).stop();
    }

    @Override
    public void dispose() {
        //Disposes sound files from hash map
        for(Sound s: sounds.values()) {
            s.dispose();
        }

        // Disposes music files from has map
        for(Music m: musics.values()) {
            m.dispose();
        }
    }
}
