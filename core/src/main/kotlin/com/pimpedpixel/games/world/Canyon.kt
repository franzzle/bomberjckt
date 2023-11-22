package com.pimpedpixel.games.world

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Actor
import com.pimpedpixel.games.GameProperties

//Original 40 total width with 16 pixel wide bricks
//Original 12 layers with 8 pixel high bricks leaving
class Canyon(
    private val world: World,
    private val canyonStateListener: CanyonStateListener,
    canyonLayoutPattern: String?,
    assetManager: AssetManager
) : Actor() {
    private val canyonLayout: List<List<Brick>>
    private val sizeOfSideOfOneBrick: Int
    private val boundingRectPlayfield: Rectangle
    private val bricksDestroyed: MutableList<Brick>
    private val allBricks: List<Brick>

    private var totalNumberOfDestroyableBricksInCanyon: Int

    init {
        val brickColorProperties = GameProperties(canyonLayoutPattern!!)
        val canyon = CanyonLayoutParserImpl().parse(canyonLayoutPattern)
        sizeOfSideOfOneBrick = Gdx.graphics.width / canyon.width
        boundingRectPlayfield = Rectangle()
        boundingRectPlayfield.width = Gdx.graphics.width.toFloat()
        boundingRectPlayfield.height = Gdx.graphics.height.toFloat()
        boundingRectPlayfield.x = 0f
        boundingRectPlayfield.y = 0f
        totalNumberOfDestroyableBricksInCanyon = 0
        bricksDestroyed = ArrayList()
        allBricks = ArrayList(canyon.height * canyon.width)
        canyonLayout = ArrayList(canyon.width)

        val canyonGrid = CanyonGrid(canyon, brickColorProperties)

        canyonGrid.grid.forEachIndexed { rowIndex, row ->
            val bricks = ArrayList<Brick>()
            canyonLayout.add(bricks)
            row.forEachIndexed { columnIndex, brickColorTextureFileName ->
                val brick = Brick(world)
                brick.width = sizeOfSideOfOneBrick
                brick.height = sizeOfSideOfOneBrick
                brick.brickColorTextureFileName = brickColorTextureFileName
                val outerWall = brickColorTextureFileName.equals("brickcolor0.png")
                if (!outerWall) {
                    totalNumberOfDestroyableBricksInCanyon++
                }
                brick.isOuterWall = outerWall
                brick.column = columnIndex
                brick.row = rowIndex
                brick.updatePosition(rowIndex * sizeOfSideOfOneBrick, columnIndex * sizeOfSideOfOneBrick)
                bricks.add(brick)
                allBricks.add(brick)
            }
        }

        for (bricks in canyonLayout) {
            for (brick in bricks) {
                brick.initGraphics(assetManager = assetManager)
            }
        }

        val name = this.javaClass.simpleName
        Gdx.app.log(name, "Total number of destroyable bricks $totalNumberOfDestroyableBricksInCanyon")
        val wallBricks = (canyon.height * canyon.width) - totalNumberOfDestroyableBricksInCanyon
        Gdx.app.log(name, "Total number of wall bricks $wallBricks")
        Gdx.app.log(name, "Width of bricks in canyon $canyon.width")
        Gdx.app.log(name, "Height of bricks in canyon $canyon.height")

    }

    @Override
    override fun draw(batch: Batch?, parentAlpha: Float) {
        for (bricks in canyonLayout) {
            for (brick in bricks) {
                brick.draw(batch)
            }
        }
    }

    private fun getCollidees(bomb: Bomb): List<Brick> {
        val bricksThatBombCollidesWith: MutableList<Brick> = ArrayList()
        for (bricks in canyonLayout) {
            for (brick in bricks) {
                if (!brick.isDestroyed() && brick.boundingRectangle.overlaps(bomb.boundingRectangle)) {
                    bricksThatBombCollidesWith.add(brick)
                }
            }
        }
        return bricksThatBombCollidesWith
    }

    //TODO Fix passing bomb
    fun update(bomb: Bomb) {
        var newBricksDestroyed = 0
        var outerwallHit = 0
        var numberOfDestroyableBricksThatAreDestroyed = 0
        val collidees = getCollidees(bomb)
        if (collidees.size > 1) {
            for (brick in collidees) {
                if (brick.isOuterWall) {
                    outerwallHit++
                    canyonStateListener.canyonStateChanged(CanyonState.OUTER_WALL_HIT, brick, bomb)
                } else {
                    newBricksDestroyed++
                    bricksDestroyed.add(brick)
                    Gdx.app.log(
                        this.javaClass.getSimpleName(),
                        "Brick on row " + brick.row + " + and col : " + brick.column
                    )
                    canyonStateListener.canyonStateChanged(CanyonState.BRICK_DESTROYED, brick, bomb)
                }
            }
        }
        for (bricks in canyonLayout) {
            for (brick in bricks) {
                if (brick.isDestroyed()) {
                    numberOfDestroyableBricksThatAreDestroyed++
                }
            }
        }

        // TODO Make Box2D work with Canyon and Bomb
//        if (numberOfDestroyableBricksThatAreDestroyed == totalNumberOfDestroyableBricksInCanyon) {
//            canyonStateListener.canyonStateChanged(CanyonState.ALL_BRICKS_DESTROYED)
//        }
//        if (newBricksDestroyed > 0) {
//            canyonStateListener.scoringStateChanged(ScoringStateEnum.HIT)
//        } else if (outerwallHit > 0) {
//            val message = "Miss by hitting outer wall and bomb hit is ${bomb.numberOfBricksDestroyed}"
//            Gdx.app.log(this.javaClass.getSimpleName(), message)
//            canyonStateListener.scoringStateChanged(ScoringStateEnum.MISS)
//        } else if (!bomb.boundingRectangle.overlaps(boundingRectPlayfield)) {
//            Gdx.app.log(this.javaClass.getSimpleName(), "Miss by leaving the field")
//            canyonStateListener.scoringStateChanged(ScoringStateEnum.MISS)
//        }
    }

    fun restoreAllBricks() {
        for (bricks in canyonLayout) {
            for (brick in bricks) {
                brick.setDestroyed(false)
            }
        }
    }
}
