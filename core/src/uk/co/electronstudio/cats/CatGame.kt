package uk.co.electronstudio.cats

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.*


/*
 * Main applicaton class, initialised by GDX
 */
class CatGame : Game() {

    lateinit var gameScreen: GameScreen
    lateinit var levelScreen: SplashScreen
    lateinit var levels: ArrayList<Level>

    var level = 0

    override fun create() {
        app = this
        resources = Resources()
        setMousePointer()
        initGame()
        setDisplay()
        resources.musicTheme.volume = 0f

    }

    private fun setDisplay() {
        Gdx.graphics.setFullscreenMode(Gdx.graphics.displayMode)
        Gdx.graphics.setVSync(true)
        setScreen(resources.splashScreen)
        resources.musicTheme.play()
    }

    private fun setMousePointer() {
        val pm = Pixmap(Gdx.files.internal("pawprint.png"))
        Gdx.graphics.setCursor(Gdx.graphics.newCursor(pm, pm.width / 2, pm.height / 2))
        pm.dispose()
    }

    fun initGame() {
        levels = arrayListOf<Level>(
                SingleLevel("LEVEL 1\nCLICK THE LASER", "level1_2.tmx"),
                RandomLevel("LEVEL 2\nCLICK THE MIRRORS", arrayListOf(
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
                )),
                SingleLevel("LEVEL 4\n\nYOU ONLY HAVE 3 SHOTS!", "level4_1.tmx", shotLimit = 3),
                RandomLevel("LEVEL 5", arrayListOf(
                        "level5_1.tmx",
                        "level6_1.tmx",
                        "level7_1.tmx",
                        "level8_1.tmx"
                )),
                RandomLevel("LEVEL 6", arrayListOf(
                        "level10_2.tmx",
                        "level11_1.tmx",
                        "level12_1.tmx",
                        "level13_1.tmx",
                        "level58_1.tmx"
                )),
                SingleLevel("FINAL LEVEL", "level88_1.tmx", shotLimit = 3)
        )
        gameScreen = GameScreen(levels[0])
        createLevelScreen()

    }

    private fun createLevelScreen() {
        levelScreen = SplashScreen(gameScreen, text = levels[level].name, logo = Texture("dialogue_clear_box.png"), bg = Color.BLACK, WIDTH = 1920f, HEIGHT = 1080f, textX = 670f, textY = 1080 - 508f, time = 4f)


    }

    fun nextLevel() {
        resources.musicWin.stop()
        resources.musicTheme.play()

        level++
        if (level > levels.lastIndex) {
            goBackToTitleScreen()
        } else {
            gameScreen = GameScreen(levels[level])
            createLevelScreen()
            setScreen(levelScreen)
        }
    }

    fun goBackToTitleScreen() {
        level = 0
        initGame()
        setScreen(resources.titleScreen)
    }

    companion object {
        lateinit var app: CatGame
        lateinit var resources: Resources
    }

}
