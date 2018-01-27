package uk.co.electronstudio.cats

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.sun.deploy.uitoolkit.ToolkitStore.dispose
import com.badlogic.gdx.graphics.Pixmap



class CatGame : ApplicationAdapter() {
    lateinit var batch: SpriteBatch
    lateinit var background: Texture
    lateinit var cam: OrthographicCamera

    override fun create() {
        batch = SpriteBatch()
        background = Texture("space.jpg")
        cam = setupCam(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        val pm = Pixmap(Gdx.files.internal("pawprint.png"))
        Gdx.graphics.setCursor(Gdx.graphics.newCursor(pm, 0, 0))
        pm.dispose()
    }

    override fun render() {
        doInput()
        cam.update();
        batch.setProjectionMatrix(cam.combined);
        Gdx.gl.glClearColor(0f, 0f, 0.5f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        drawBackground()
        drawSprites()
    }

    private fun drawSprites() {

    }

    private fun drawBackground() {
        batch.begin()
        batch.draw(background, 0f, 0f)
        batch.end()
    }

    private fun doInput() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            Gdx.app.exit()
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.F)){
            Gdx.graphics.setFullscreenMode(Gdx.graphics.displayMode)
        }
    }

    override fun dispose() {
        batch.dispose()
        background.dispose()
    }

    val WIDTH=1920f
    val HEIGHT=1080f

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

    fun Float.roundDown(): Float {
        return this.toInt().toFloat()
    }
}
