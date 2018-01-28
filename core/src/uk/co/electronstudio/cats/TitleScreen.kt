package uk.co.electronstudio.cats

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Screen
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import net.dermetfan.gdx.Typewriter


/**
 * Created by richard on 23/06/2016.
 * A GDX screen, i.e. a render loop, used to show a logo plus text before the game starts
 */
class TitleScreen(val bg: Color = Color.BLACK,  WIDTH: Float,  HEIGHT: Float) : ScreenWithCamera(WIDTH, HEIGHT) {



    val logo: Texture = Texture("space.jpg")


    internal var font = BitmapFont(Gdx.files.internal("big.fnt"))




    init {



        logo?.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest)


        font.region.texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest)



    }

    override fun show() {


      //  App.playTitleMusic()

    }



    override fun render(delta: Float) {
        super.render(delta)
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit()
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY) || Gdx.input.justTouched()){
            CatGame.app.setScreen(CatGame.app.levelScreen)
        }



//        val batch = renderer.beginFBO()
//
        Gdx.gl.glClearColor(bg.r, bg.g, bg.b, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        batch.begin()

        if(logo!=null) {
            batch.draw(logo, ((WIDTH - logo.width) / 2), ((HEIGHT - logo.height) / 2))
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
