package uk.co.electronstudio.cats.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import uk.co.electronstudio.cats.CatGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		//config.width=1920;
	//	config.height=1080;
	//g 	config.fullscreen=true;
		new LwjglApplication(new CatGame(), config);
	}
}
