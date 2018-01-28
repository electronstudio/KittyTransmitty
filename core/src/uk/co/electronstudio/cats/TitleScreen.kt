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
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector3
import net.dermetfan.gdx.Typewriter



class TitleScreen(val bg: Color = Color.BLACK,  WIDTH: Float,  HEIGHT: Float) : ScreenWithCamera(WIDTH, HEIGHT) {



    val logo: Texture = Texture("Splash_page.png")


    internal var font = BitmapFont(Gdx.files.internal("big.fnt"))


    val buttons = listOf<Button>(Button(Rectangle(235f,1080f-784f,383f,89f),action = { CatGame.app.setScreen(CatGame.app.levelScreen)}),
            Button(Rectangle(777f,1080f-784f,383f,89f),action = { CatGame.app.setScreen(CatGame.app.instructions1)}),
            Button(Rectangle(1325f,1080f-784f,383f,89f),action = { Gdx.app.exit()}),
            Button(Rectangle(1062f,1080f-887f,383f,89f),action = {CatGame.app.setScreen(CatGame.app.credits)})

    )



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

        if(Gdx.input.justTouched()) {
            val mouse = cam.unproject(Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f))
            buttons.forEach{it.check(mouse.x, mouse.y)}
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
