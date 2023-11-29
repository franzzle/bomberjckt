package com.pimpedpixel.games.world

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Actor
import com.pimpedpixel.games.AssetManagerHolder
import com.pimpedpixel.games.gameplay.ComponentHelper
import com.pimpedpixel.games.gameplay.GamePhaseState

//Original 40 total width with 16 pixel wide bricks
//Original 12 layers with 8 pixel high bricks leaving
class Canyon(
    private val world: World,
    canyonLayoutPattern: String?
) : Actor() {
    private val canyonLayout: List<List<Brick>>
    private val sizeOfSideOfOneBrick: Int
    private val bricksDestroyed: MutableList<Brick>
    private val allBricks: List<Brick>

    private val leftFillerBricks: List<Brick>
    private val rightFillerBricks: List<Brick>
    private val bottomFillerBricks: List<Brick>

    private var totalNumberOfDestroyableBricksInCanyon: Int

    init {
        val canyon = CanyonLayoutParserImpl().parse(canyonLayoutPattern!!)
        sizeOfSideOfOneBrick = Gdx.graphics.width / canyon.width
        totalNumberOfDestroyableBricksInCanyon = 0
        bricksDestroyed = ArrayList()
        allBricks = ArrayList(canyon.height * canyon.width)
        canyonLayout = ArrayList(canyon.width)


        val canyonGrid = CanyonGrid(canyon)

        canyonGrid.grid.forEachIndexed { rowIndex, row ->
            val bricks = ArrayList<Brick>()
            canyonLayout.add(bricks)
            row.forEachIndexed { columnIndex, charBrick ->
                val brick = Brick(world)
                brick.width = sizeOfSideOfOneBrick
                brick.height = sizeOfSideOfOneBrick
                brick.brickColorTextureFileName = "brickcolor$charBrick.png"
                brick.score = charBrick.toString().toInt()
                val outerWall = charBrick == '0'
                if (!outerWall) {
                    totalNumberOfDestroyableBricksInCanyon++
                }
                brick.isOuterWall = outerWall
                brick.column = columnIndex
                brick.row = rowIndex
                brick.setDestroyedFromTheStart(charBrick == '9')
                bricks.add(brick)
                allBricks.add(brick)
            }
        }

        leftFillerBricks = ArrayList()
        for(i in 0..<canyon.height){
            makeOuterWallBrick().apply {
                row = -1
                column = i
                initGraphics(assetManager = AssetManagerHolder.assetManager)
                leftFillerBricks.add(this)
            }
        }

        rightFillerBricks = ArrayList()
        for(i in 0..<canyon.height){
            makeOuterWallBrick().apply {
                row = canyon.width
                column = i
                initGraphics(assetManager = AssetManagerHolder.assetManager)
                rightFillerBricks.add(this)
            }
        }

        bottomFillerBricks = ArrayList()
        for(i in 0..<canyon.width){
            makeOuterWallBrick().apply {
                row = i
                column = -1
                initGraphics(assetManager = AssetManagerHolder.assetManager)
                bottomFillerBricks.add(this)
            }
        }

        // Update the total number of destroyable bricks
        totalNumberOfDestroyableBricksInCanyon += 2 // Two extra bricks added

        for (bricks in canyonLayout) {
            for (brick in bricks) {
                brick.initGraphics(assetManager = AssetManagerHolder.assetManager)
            }
        }

        val name = this.javaClass.simpleName
        Gdx.app.log(name, "Total number of destroyable bricks $totalNumberOfDestroyableBricksInCanyon")
        val wallBricks = (canyon.height * canyon.width) - totalNumberOfDestroyableBricksInCanyon
        Gdx.app.log(name, "Total number of wall bricks $wallBricks")
        Gdx.app.log(name, "Width of bricks in canyon $canyon.width")
        Gdx.app.log(name, "Height of bricks in canyon $canyon.height")

    }

    private fun makeOuterWallBrick(): Brick {
        val brick = Brick(world)
        brick.width = sizeOfSideOfOneBrick
        brick.height = sizeOfSideOfOneBrick
        brick.brickColorTextureFileName = "brickcolor0.png"
        brick.score = 0
        brick.isOuterWall = true
        return brick
    }

    override fun act(delta: Float) {
        super.act(delta)
        ComponentHelper.retrieveGamePhaseStateComponent().apply {
            if (gamePhaseState == GamePhaseState.ATTRACT_SCREEN) {
                for (brick in allBricks) {
                    if(!brick.isDestroyedFromTheStart()){
                        brick.setDestroyed(false)
                    }
                }
            }
        }
    }

    @Override
    override fun draw(batch: Batch?, parentAlpha: Float) {
        for (bricks in leftFillerBricks){
            bricks.draw(batch)
        }
        for (bricks in bottomFillerBricks){
            bricks.draw(batch)
        }
        for (bricks in canyonLayout) {
            for (brick in bricks) {
                brick.draw(batch)
            }
        }
        for (bricks in rightFillerBricks){
            bricks.draw(batch)
        }
    }
}
