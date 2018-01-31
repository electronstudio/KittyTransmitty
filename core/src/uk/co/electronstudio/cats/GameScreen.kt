package uk.co.electronstudio.cats

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3


/**
 * Screen the game is played on.
 * One is created for each level
 */
class GameScreen(val level: Level) : ScreenWithCamera(1920f, 1080f) {

    val mapRenderer = OrthogonalTiledMapRenderer(level.getMap(), 1f)
    val laserRenderer = ShapeRenderer()

    val layer = level.getMap().layers.last() as TiledMapTileLayer


    lateinit var mirror0: TiledMapTile
    lateinit var mirror45: TiledMapTile
    lateinit var mirror90: TiledMapTile
    lateinit var mirror135: TiledMapTile
    lateinit var origin: Vec
    lateinit var goal: Vec

    val path = ArrayList<Vector2>()

    enum class GameState { PLAYING, WON, LOST, FIRING }

    var gameState: GameState = GameState.PLAYING

    var laserTime = 0f

    var bx = 0
    var by = 0


    init {


        val map = level.getMap()






        for (x: Int in 0..layer.width - 1) {
            for (y: Int in 0..layer.height - 1) {
                val cell = layer.getCell(x, y)
                if (cell != null) {
                    if (cell.tile.properties.containsKey("origin")) {
                        origin = Vec(x, y)
                    }
                    if (cell.tile.properties.containsKey("goal")) {
                        goal = Vec(x, y)
                    }
                }
            }
        }

        for (tileset in map.tileSets) {
            for (tile: TiledMapTile in tileset) {
                tile.getTextureRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
                if (tile.properties["mirror"] != null) {
                    val angle = tile.properties["mirror"] as Int
                    when (angle) {
                        0 -> mirror0 = tile
                        45 -> mirror45 = tile
                        90 -> mirror90 = tile
                        135 -> mirror135 = tile
                    }
                }
            }
        }


    }

    class Vec(val x: Int, val y: Int)

    var goalHit = false

    private fun calculatePath() {
        path.clear()
        var xVel = 0
        var yVel = 1
        var x = origin.x
        var y = origin.y
        path.add(Vector2(x * 128f + 64f, y * 128f + 64f))
        while (true) {
            x += xVel
            y += yVel

            println("checking cell $x $y")

            if (x > layer.width || x < 0 || y > layer.height || y < 0) {
                path.add(Vector2(x * 128f + 64f, y * 128f + 64f))
                println(" breaking width ${layer.width} height ${layer.height}")
                shotMissed()
                break
            }
            val cell = layer.getCell(x, y)

            if (cell == null) {
                continue
            }
            println("cell has properties ${cell.tile.properties}")
            if (cell.tile.properties.containsKey("goal")) {
                path.add(Vector2(x * 128f + 64f, y * 128f + 64f))
                gameState = GameState.FIRING
                goalHit = true
                break //win
            }
            if (cell.tile.properties.containsKey("obstacle")) {
                path.add(Vector2(x * 128f + 64f, y * 128f + 64f))
                shotMissed()
                break
            }
            if (cell.tile.properties.containsKey("mirror")) {
                val angle: Int = cell.tile.properties["mirror"] as Int
                println("cell is mirror $angle")
                when (angle) {
//                    0.0 ->{}
//                        45.0->{}
//
//                    90.0 -> return
                    0 -> {
                        yVel = -yVel
                    }
                    90 -> {
                        xVel = -xVel
                    }
                    45 -> {
                        println("found 45 mirror")
                        val t = xVel
                        xVel = yVel
                        yVel = t
                    }
                    135 -> {
                        println("found 135 mirror")
                        val t = xVel
                        xVel = -yVel
                        yVel = -t
                    }


                }
                path.add(Vector2(x * 128f + 64f, y * 128f + 64f))
            }
            path.add(Vector2(x * 128f + 64f, y * 128f + 64f))

        }
    }

    var shots = 0

    fun shotMissed() {
        gameState = GameState.FIRING
        level.shotLimit?.let {
            if (shots >= it) {
                CatGame.app.goBackToTitleScreen()
            }
        }

    }

    override fun render(delta: Float) {
        cam.update();
        batch.setProjectionMatrix(cam.combined);
        Gdx.gl.glClearColor(0f, 0f, 0.0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        drawBackground()
        drawMapTiles()
        drawLaser()
        drawStats()
        when (gameState) {
            GameState.PLAYING -> {
                doInput()

            }
            GameState.WON -> {
                doWon()
            }
            GameState.FIRING -> {
                updateAndDrawBullet()
                if (laserTime <= 0f) {
                    if (goalHit) {
                        gameState = GameState.WON
                        CatGame.resources.musicTheme.pause()
                        CatGame.resources.musicWin.play()
                    } else {
                        gameState = GameState.PLAYING
                    }
                }
            }

        }

    }

    var pathCounter = 0

    private fun updateAndDrawBullet() {

        for (i in 0..10) {
            bulletLogic()
        }

        batch.begin()
        batch.draw(CatGame.resources.bullet, (bx - 12).toFloat(), (by - 12).toFloat())
        batch.end()
    }


    private fun bulletLogic() {
        val target = bulletTarget
        if (target != null) {
            if (bx < target.x) {
                bx++
            }
            if (by < target.y) {
                by++
            }
            if (bx > target.x) {
                bx--
            }
            if (by > target.y) {
                by--
            }
            if (bx == target.x.toInt() && by == target.y.toInt()) {
                pathCounter++
                if (pathCounter <= path.lastIndex) {
                    bulletTarget = path[pathCounter]
                }
            }
        }
    }

    private fun drawStats() {
        batch.begin()
        level.shotLimit?.let {
            CatGame.resources.font.draw(batch, "LASER POWER: ${it - shots}", 0f, 50f)
        }
        batch.end()
    }

    private fun doWon() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY) ||
                Gdx.input.justTouched()) {
            CatGame.app.nextLevel()
        }
        batch.begin()
        batch.draw(CatGame.resources.dialogSignalReceived, 0f, 0f)
        batch.end()
    }

    val colours = listOf<Color>(Color.RED, Color.PURPLE, Color.BLUE, Color.CYAN, Color.GREEN, Color.YELLOW)
    var c = 0

    private fun drawLaser() {
        if (laserTime > 0) {
            batch.begin()
            for (i: Int in 0..path.lastIndex - 1) {
                drawLine(path[i], path[i + 1], 15, colours[c], cam.combined)
                // drawLine(path.points[i], path.points[i + 1], 3, , cam.combined)
            }
            batch.end()

            laserTime -= Gdx.graphics.deltaTime
            c++
            if (c > colours.lastIndex) c = 0
        }
    }

    private fun drawMapTiles() {
        batch.begin()
        mapRenderer.setView(cam)
        mapRenderer.render()
        batch.end()
    }


    fun drawLine(start: Vector2, end: Vector2, lineWidth: Int, color: Color, projectionMatrix: Matrix4) {
        Gdx.gl.glLineWidth(lineWidth.toFloat())
        laserRenderer.setProjectionMatrix(projectionMatrix)
        laserRenderer.begin(ShapeRenderer.ShapeType.Line)
        laserRenderer.setColor(color)
        laserRenderer.line(start, end)
        laserRenderer.end()
        Gdx.gl.glLineWidth(1f)
    }

    private fun drawBackground() {
        batch.begin()
        batch.draw(CatGame.resources.background, 0f, 0f)
        batch.end()
    }

    private fun doInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit()
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            CatGame.app.nextLevel()
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            if (Gdx.graphics.isFullscreen) {
                Gdx.graphics.setWindowedMode(1200, 800)
                Gdx.graphics.setVSync(true)
            } else {
                Gdx.graphics.setFullscreenMode(Gdx.graphics.displayMode)
                Gdx.graphics.setVSync(true)
            }
        }
        if (Gdx.input.justTouched()) {
            val mouse = cam.unproject(Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f))
            val x = (mouse.x / 128).toInt()
            val y = (mouse.y / 128).toInt()
            println("pressed at $x $y")
            val cell = layer.getCell(x, y)
            if (cell != null) {  // mirror clicked, flip the tile
                val tile = cell.tile
                if (tile.id == mirror0.id) {
                    cell.setTile(mirror45)
                } else if (tile.id == mirror45.id) {
                    cell.setTile(mirror90)
                } else if (tile.id == mirror90.id) {
                    cell.setTile(mirror135)
                } else if (tile.id == mirror135.id) {
                    cell.setTile(mirror0)
                } else if (tile.properties.containsKey("fire")) {
                    beginFiringLaser()
                }
            }

        }
    }

    var bulletTarget: Vector2? = null

    private fun beginFiringLaser() {
        CatGame.resources.soundLaser.play()
        laserTime = 5f
        shots++
        bx = origin.x * 128
        by = origin.y * 128

        calculatePath()
        bulletTarget = path.first()
        println("bullet target: $bulletTarget")
        pathCounter = 0
    }

    override fun dispose() {
        batch.dispose()
    }


    override fun hide() {
    }

    override fun show() {
    }

    override fun pause() {
    }

    override fun resume() {

    }

    fun Float.roundDown(): Float {
        return this.toInt().toFloat()
    }
}