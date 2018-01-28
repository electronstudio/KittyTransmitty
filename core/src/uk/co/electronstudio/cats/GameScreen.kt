package uk.co.electronstudio.cats

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import kotlin.concurrent.timer

class GameScreen(val level: Level) : Screen{



    lateinit var batch: SpriteBatch
    lateinit var background: Texture
    lateinit var cam: OrthographicCamera
    lateinit var mapRenderer: OrthogonalTiledMapRenderer
    lateinit var debugRenderer: ShapeRenderer
    lateinit var origin: Thing
    lateinit var goal: Thing
    lateinit var layer: TiledMapTileLayer


    lateinit var mirror0: TiledMapTile
    lateinit var mirror45: TiledMapTile
    lateinit var mirror90: TiledMapTile
    lateinit var mirror135: TiledMapTile

    val path = Path()

    enum class GameState{PLAYING, WON, LOST, FIRING}
    var gameState: GameState=GameState.PLAYING

    internal var font = BitmapFont(Gdx.files.internal("big.fnt"))

    var laserTime=0f
    var flickerFlag = false

    init {
        batch = SpriteBatch()
        background = Texture("template_backdrop_v1.png")
        cam = setupCam(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())


        val map = level.getMap()

        mapRenderer = OrthogonalTiledMapRenderer(map, 1f)
        debugRenderer = ShapeRenderer()


        layer = map.layers.last() as TiledMapTileLayer
        for (x: Int in 0..layer.width - 1) {
            for (y: Int in 0..layer.height - 1) {
                val cell = layer.getCell(x, y)
                //    println("$x $y")
                if (cell != null) {
                    if (cell.tile.properties.containsKey("origin")) {
                        origin = Thing(x, y)
                    }
                    if (cell.tile.properties.containsKey("goal")) {
                        goal = Thing(x, y)
                    }
                }
            }
        }
        println("origin $origin")

        for(tileset in map.tileSets) {
            for (tile: TiledMapTile in tileset) {
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

    class Thing(val x: Int, val y: Int)

    private fun calculatePath() {
        path.points.clear()
        var xVel = 0
        var yVel = 1
        var x = origin.x
        var y = origin.y
        path.points.add(Vector2(x * 128f + 64f, y * 128f + 64f))
        while (true) {
            x += xVel
            y += yVel

            println("checking cell $x $y")

            if (x > layer.width || x < 0 || y > layer.height || y < 0) {
                path.points.add(Vector2(x * 128f + 64f, y * 128f + 64f))
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
                path.points.add(Vector2(x * 128f + 64f, y * 128f + 64f))
                gameState=GameState.FIRING
                break //win
            }
            if (cell.tile.properties.containsKey("obstacle")) {
                path.points.add(Vector2(x * 128f + 64f, y * 128f + 64f))
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
                path.points.add(Vector2(x * 128f + 64f, y * 128f + 64f))
            }
            path.points.add(Vector2(x * 128f + 64f, y * 128f + 64f))

        }
    }

    var shots=0

    fun shotMissed(){
        level.shotLimit?.let{
            if(shots>=it){
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
        when(gameState){
            GameState.PLAYING ->{
                doInput()

            }
            GameState.WON ->{
                doWon()
            }
            GameState.FIRING ->{
                if(laserTime<=0f){
                    gameState=GameState.WON
                    CatGame.app.musicTheme.pause()
                    CatGame.app.musicWin.play()
                }
            }

        }

    }

    private fun drawStats() {
        batch.begin()
       level.shotLimit?.let {
            font.draw(batch, "LASER POWER: ${it-shots}", 0f, 20f)
        }
        batch.end()
    }

    private fun doWon() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY) || Gdx.input.justTouched()){

            CatGame.app.nextLevel()
        }
        batch.begin()
        batch.draw(CatGame.app.dialog1,0f,0f)
      //  font.draw(batch,"SIGNAL RECEIVED!", 400f, 500f)
        batch.end()
    }

    val colours = listOf<Color>(Color.RED, Color.PURPLE, Color.BLUE, Color.CYAN, Color.GREEN, Color.YELLOW)
    var c=0

    private fun drawLaser() {
        if(laserTime>0) {
            batch.begin()
            for (i: Int in 0..path.points.lastIndex - 1) {
                drawLine(path.points[i], path.points[i + 1], 15,  colours[c], cam.combined)
               // drawLine(path.points[i], path.points[i + 1], 3, , cam.combined)
            }
            batch.end()

            laserTime -= Gdx.graphics.deltaTime
            c++
            if(c>colours.lastIndex) c=0
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
        debugRenderer.setProjectionMatrix(projectionMatrix)
        debugRenderer.begin(ShapeRenderer.ShapeType.Line)
        debugRenderer.setColor(color)
        debugRenderer.line(start, end)
        debugRenderer.end()
        Gdx.gl.glLineWidth(1f)
    }

    private fun drawBackground() {
        batch.begin()
        batch.draw(background, 0f, 0f)
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
            Gdx.graphics.setFullscreenMode(Gdx.graphics.displayMode)
        }
        if (Gdx.input.justTouched()){

            val mouse = cam.unproject(Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f))
            val x = (mouse.x/128).toInt()
            val y = (mouse.y/128).toInt()
            println("pressed at $x $y")
            val cell = layer.getCell(x, y)
            if(cell!=null) {
                val tile = cell.tile
              //  println("tile ${tile.id} comparing to ${mirror0.id}")
                if(tile.id == mirror0.id){
                    cell.setTile(mirror45)
                }
                else if(tile.id == mirror45.id){
                    cell.setTile(mirror90)
                }
                else if(tile.id == mirror90.id){
                    cell.setTile(mirror135)
                }
                else if(tile.id == mirror135.id){
                    cell.setTile(mirror0)
                }
                else if(tile.properties.containsKey("fire")){
                    CatGame.app.soundLaser.play()
                    laserTime=5f
                    shots++
                    calculatePath()
                }
            }

        }
    }

    override fun dispose() {
        batch.dispose()
        background.dispose()
    }

    val WIDTH = 1920f
    val HEIGHT = 1080f

    fun setupCam(w: Float, h: Float): OrthographicCamera {

        val m: Float = findHighestScaleFactor(w, h)


        val cam = OrthographicCamera(w / m, h / m)

        cam.translate((WIDTH / 2), (HEIGHT / 2))

        cam.update()

        return cam

    }

    override fun resize(width: Int, height: Int) {
        cam = setupCam(width.toFloat(), height.toFloat())
    }

    fun findHighestScaleFactor(width: Float, height: Float): Float {

        val w = width / WIDTH
        val h = height / HEIGHT

        return if (w < h) w else h
    }


    override fun hide() {
    }

    override fun show() {
        //calculatePath()
    }

    override fun pause() {

    }

    override fun resume() {

    }
    fun Float.roundDown(): Float {
        return this.toInt().toFloat()
    }
}