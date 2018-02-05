package uk.co.electronstudio.cats.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import uk.co.electronstudio.cats.CatGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		//config.width=1920;
	//	config.height=1080;
	//g 	config.fullscreen=true;
		new Lwjgl3Application(new CatGame(), config);
	}
}
