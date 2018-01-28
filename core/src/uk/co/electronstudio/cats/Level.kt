package uk.co.electronstudio.cats

import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.math.MathUtils

abstract class Level(val name:String, val timeLimit: Float? = null, val shotLimit: Int? = null){
    abstract fun getMap(): TiledMap
}

class SingleLevel(name: String, val file: String, timeLimit: Float? = null, shotLimit: Int? = null):Level(name, timeLimit, shotLimit) {
    val m = TmxMapLoader().load(file)!!

    override fun getMap(): TiledMap{
        return m
    }

}

class RandomLevel(name: String, files: List<String>): Level(name){
    val maps = files.map { TmxMapLoader().load(it)!! }

    override fun getMap(): TiledMap{
        return maps[MathUtils.random(maps.lastIndex)]
    }
}