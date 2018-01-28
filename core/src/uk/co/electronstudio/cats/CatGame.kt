package uk.co.electronstudio.cats

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.*


class CatGame : Game() {

    lateinit var gameScreen: GameScreen
    lateinit var splashScreen: SplashScreen
    lateinit var levelScreen: SplashScreen
    lateinit var levels: ArrayList<Level>

    var level=0

    override fun create() {
        app = this

        levels = arrayListOf<Level>(
                Level("LEVEL 1", "level1_1.tmx"),
                Level("LEVEL 2", "level1.tmx"),
                Level("choo choo", "level1.tmx"
        ))

        gameScreen = GameScreen(levels[0])
        levelScreen = SplashScreen(gameScreen,text = levels[level].name, bg = Color.BLACK, WIDTH = 1920f, HEIGHT = 1080f, textX = 59f, textY=59f, time = 3f)

        splashScreen = SplashScreen(levelScreen, Texture("space.jpg"), "KITTY TRANSMITTY", Color.BLACK, 1920f, 1080f)

        setScreen(splashScreen)

    }

    fun nextLevel(){
        level++
        gameScreen = GameScreen(levels[level])
        levelScreen = SplashScreen(gameScreen,text = levels[level].name, bg = Color.BLACK, WIDTH = 1920f, HEIGHT = 1080f, textX = 59f, textY=59f, time = 3f)
        setScreen(levelScreen)
    }

companion object {
    lateinit var app: CatGame
}


}
