package fi.tamk.rentogames.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import fi.tamk.rentogames.DungeonEscape;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 360;
		config.height = 640;
		config.resizable = false;

		new LwjglApplication(new DungeonEscape(), config);
	}
}
