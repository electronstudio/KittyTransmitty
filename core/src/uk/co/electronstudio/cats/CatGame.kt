package uk.co.electronstudio.cats

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.*


class CatGame : Game() {

    lateinit var gameScreen: GameScreen
    lateinit var splashScreen: SplashScreen
    lateinit var levelScreen: SplashScreen
    lateinit var levels: ArrayList<Level>
    lateinit var musicTheme: Music
    lateinit var musicWin: Music
    lateinit var musicLose: Music
    lateinit var soundLaser: Sound
    lateinit var dialog1: Texture
    lateinit var dialog2: Texture
    lateinit var titleScreen: TitleScreen

    var level=0

    override fun create() {
        app = this

        musicTheme= Gdx.audio.newMusic(Gdx.files.internal("mainTheme.mp3"))
        musicWin= Gdx.audio.newMusic(Gdx.files.internal("victory.mp3"))
        musicLose= Gdx.audio.newMusic(Gdx.files.internal("loss.mp3"))
    //    soundLaser= Gdx.audio.newSound(Gdx.files.internal("loss.mp3"))
       soundLaser= Gdx.audio.newSound(Gdx.files.internal("laserChargeandFire.wav"))
        titleScreen = TitleScreen(WIDTH = 1920f, HEIGHT = 1080f)
        dialog1 = Texture("dialogue_1.png")
        dialog2 = Texture("dialogue_2.png")
        musicTheme.setLooping(true)

        val pm = Pixmap(Gdx.files.internal("pawprint.png"))
        Gdx.graphics.setCursor(Gdx.graphics.newCursor(pm, pm.width/2, pm.height/2))
        pm.dispose()
        initGame()


        Gdx.graphics.setFullscreenMode(Gdx.graphics.displayMode)
        setScreen(splashScreen)
        musicTheme.play()

    }

    fun initGame() {
        levels = arrayListOf<Level>(

                SingleLevel("LEVEL 1\n\nCLICK THE LASER", "level1_2.tmx"),
                RandomLevel("LEVEL 2\n\nCLICK THE MIRRORS", arrayListOf(
                        "level2_0.tmx",
                        "level2_1.tmx",
                        "level2_2.tmx",
                        "level2_3.tmx",
                        "level2_4.tmx",
                        "level2_5.tmx",
                        "level2_6.tmx",
                        "level2_7.tmx",
                        "level2_8.tmx",
                        "level2_9.tmx"

                )),
                RandomLevel("LEVEL 3", arrayListOf(
                        "level3_1.tmx",
                        "level3_2.tmx",
                        "level3_3.tmx",
                        "level3_4.tmx",
                        "level3_5.tmx",
                        "level3_6.tmx"
                )), SingleLevel("LEVEL 4\n\nYOU ONLY HAVE 3 SHOTS!", "level4_1.tmx",shotLimit = 3)
        )
        gameScreen = GameScreen(levels[0])
        levelScreen = SplashScreen(gameScreen,text = levels[level].name, bg = Color.BLACK, WIDTH = 1920f, HEIGHT = 1080f, textX = 59f, textY=59f, time = 4f)
        titleScreen = TitleScreen(WIDTH = 1920f, HEIGHT = 1080f)
        splashScreen = SplashScreen(titleScreen, Texture("space.jpg"), "KITTY TRANSMITTY", Color.BLACK, 1920f, 1080f, textX = 500f, textY = 500f)

    }

    fun nextLevel(){
        CatGame.app.musicWin.stop()
        CatGame.app.musicTheme.play()

        level++
        if(level>levels.lastIndex){
            level=0
            initGame()
            setScreen(splashScreen)
        }else {
            gameScreen = GameScreen(levels[level])
            levelScreen = SplashScreen(gameScreen, text = levels[level].name, bg = Color.BLACK, WIDTH = 1920f, HEIGHT = 1080f, textX = 59f, textY = 59f, time = 4f)
            setScreen(levelScreen)
        }
    }

companion object {
    lateinit var app: CatGame
}


}
