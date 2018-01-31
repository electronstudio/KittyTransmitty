package uk.co.electronstudio.cats

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Screen
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import net.dermetfan.gdx.Typewriter


/**
 * Created by richard on 23/06/2016.
 * A GDX screen, i.e. a render loop, used to show a logo plus text before the game starts
 */
class SplashScreen(val nextScreen: Screen, val logo: Texture? = null, val text: String? = null,
                   val bg: Color = Color.BLACK,  WIDTH: Float = 1920f,  HEIGHT: Float = 1080f,
                    val time:Float = 7.4f, val textX:Float=100f, val textY:Float=100f) : ScreenWithCamera(WIDTH, HEIGHT) {



    private val typewriter = Typewriter()

    internal var glyphLayout = GlyphLayout()




    fun end() {
        CatGame.app.setScreen(nextScreen)
    }

    init {
        typewriter.charsPerSecond = 20f
        typewriter.isCursorWhileTyping = true
        typewriter.isCursorAfterTyping = true
        logo?.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
    }



    override fun show() {
        timer = 0f
    }

    var timer: Float = 0f
    var count = 0

    override fun render(delta: Float) {
        super.render(delta)
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit()
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY) || Gdx.input.justTouched()){
            end()
        }

        timer += delta

        if (timer > time) end()

        cam.update();
        batch.setProjectionMatrix(cam.combined);

        Gdx.gl.glClearColor(bg.r, bg.g, bg.b, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        batch.begin()

        if(logo!=null) {
            batch.draw(logo, ((WIDTH - logo.width) / 2), ((HEIGHT - logo.height) / 2))
        }

        batch.end()

        val c = Color(bg.r, bg.g, bg.b, Math.cos(timer.toDouble() / 1.25).toFloat())
        //renderer.darkenScreen(c)

        batch.begin()

      //  glyphLayout.setText(font, typewriter.updateAndType(text, delta))
      //  font.draw(batch, glyphLayout, (WIDTH - glyphLayout.width) / 2, HEIGHT)
        if(text!=null) {
            CatGame.resources.font.draw(batch, typewriter.updateAndType(text, delta), textX, textY)

        }
        batch.end()

    }

    override fun pause() {

    }

    override fun resume() {

    }

    override fun hide() {

    }

    override fun dispose() {

    }

}
