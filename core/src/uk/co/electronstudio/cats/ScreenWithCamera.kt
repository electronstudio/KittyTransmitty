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
abstract class ScreenWithCamera(val WIDTH: Float, val HEIGHT: Float) : ScreenAdapter() {

    val batch = SpriteBatch()

     var cam: OrthographicCamera


    init {




        cam = setupCam(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())


    }

    fun setupCam(w: Float, h: Float): OrthographicCamera {

        val m: Float = findHighestScaleFactor(w, h)


        val cam = OrthographicCamera(w / m, h / m)

        cam.translate((WIDTH / 2), (HEIGHT / 2))

        cam.update()

        return cam

    }

    fun findHighestScaleFactor(width: Float, height: Float): Float {

        val w = width / WIDTH
        val h = height / HEIGHT

        return if (w < h) w else h
    }





     override fun render(delta: Float) {
        super.render(delta)
        cam.update();
        batch.setProjectionMatrix(cam.combined);

    }


    override fun resize(width: Int, height: Int) {
        cam = setupCam(width.toFloat(), height.toFloat())
    }


}
