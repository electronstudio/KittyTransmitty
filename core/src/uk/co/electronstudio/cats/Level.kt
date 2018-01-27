package uk.co.electronstudio.cats

import com.badlogic.gdx.maps.tiled.TmxMapLoader

class Level(val name: String, val file: String) {
    val map = TmxMapLoader().load(file)!!

}