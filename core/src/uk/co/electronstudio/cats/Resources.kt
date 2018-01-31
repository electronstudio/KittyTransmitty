package uk.co.electronstudio.cats

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.Sprite


class Resources {

    val musicTheme = Gdx.audio.newMusic(Gdx.files.internal("mainTheme.mp3"))
    val musicWin = Gdx.audio.newMusic(Gdx.files.internal("victory.mp3"))
    val musicLose = Gdx.audio.newMusic(Gdx.files.internal("loss.mp3"))
    val soundLaser= Gdx.audio.newSound(Gdx.files.internal("laserChargeandFire.wav"))

    val dialogSignalReceived = Texture("dialogue_1.png")
    val dialog2 = Texture("dialogue_2.png")
    val titleScreen = TitleScreen()

    lateinit var gameOver: SplashScreen  //TODO
    val instructions2 = SplashScreen(titleScreen, logo = Texture("instruction_2.png"))
    val instructions1 = SplashScreen(instructions2, logo = Texture("instruction_1.png"))
    val credits = SplashScreen(titleScreen, logo = Texture("Credits.png"))

    val splashScreen = SplashScreen(titleScreen, Texture("GGJ00_GameCredits_SplashScreen.png"), textX = 500f, textY = 500f)

    val font = BitmapFont(Gdx.files.internal("joy.fnt"))
    val bullet = Texture("ball_laser.png")

    val background: Texture = Texture("template_backdrop_v1.png")

    init {
        musicTheme.setLooping(true)
        font.color= Color.YELLOW
        dialogSignalReceived.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
        dialog2.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)

    }

}