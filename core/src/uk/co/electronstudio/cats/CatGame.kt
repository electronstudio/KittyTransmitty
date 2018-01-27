package uk.co.electronstudio.cats

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3


class CatGame : Game() {

    lateinit var gameScreen: GameScreen
    lateinit var splashScreen: SplashScreen
    lateinit var levelScreen: SplashScreen

    override fun create() {
        app = this
        gameScreen = GameScreen()
        levelScreen = SplashScreen(gameScreen,text = "LEVEL 1", bg = Color.BLACK, WIDTH = 1920f, HEIGHT = 1080f, textX = 59f, textY=59f, time = 3f)

        splashScreen = SplashScreen(levelScreen, Texture("space.jpg"), "CATS OR SOMETHING", Color.BLACK, 1920f, 1080f)

        setScreen(splashScreen)

    }

companion object {
    lateinit var app: CatGame
}


}
