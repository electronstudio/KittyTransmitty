package uk.co.electronstudio.cats

import com.badlogic.gdx.math.Rectangle

/** A clickable menu button */
class Button(val box: Rectangle, val action: ()->Unit ){
    fun check(x: Float, y: Float){
        if(box.contains(x, y)){
            action()
        }
    }

}