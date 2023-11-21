package com.pimpedpixel.games

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction
import com.pimpedpixel.games.hud.FrameRate
import com.pimpedpixel.games.scoring.GameState
import com.pimpedpixel.games.scoring.GameStateListener
import com.pimpedpixel.games.scoring.GameStateServiceImpl

/** [com.badlogic.gdx.ApplicationListener] implementation shared by all platforms.  */
class BomberJcktGame : ApplicationAdapter(), CanyonStateListener, GameStateListener {
    private var batch: SpriteBatch? = null
    private var hudBatch: SpriteBatch? = null
    private var stage: Stage? = null
    private var blimp: Blimp? = null
    private var bombPool: BombPool? = null
    private var canyon: Canyon? = null
    private var backgroundColor: Color? = null
    private var frameRate: FrameRate? = null
    private val assetmanager: AssetManager = AssetManager()
    private var world: World? = null
    private var debugRenderer: Box2DDebugRenderer? = null

    private val activeBombs: MutableList<Bomb> = mutableListOf() // List to track active bombs


    override fun create() {
        val colorBlueSky = "#7382f7"
        backgroundColor = Color.valueOf(colorBlueSky)

        batch = SpriteBatch()
        hudBatch = SpriteBatch()

        BrickAssetloader(assetmanager).load()

        world = World(Vector2(0f, -1.0f), true)
        bombPool = BombPool(world!!) // Initialize the bomb pool
        debugRenderer = Box2DDebugRenderer()

        stage = Stage()
        blimp = Blimp()
        addMoveActionsToBlimp()
        stage?.addActor(blimp)
        canyon = Canyon(this, "canyon", assetmanager)
        stage?.addActor(canyon)

        // Details state transitions
        val gameStateServiceImpl = GameStateServiceImpl(this)
        val inputMultiplexer = InputMultiplexer()
        inputMultiplexer.addProcessor(Input(gameStateServiceImpl))
        Gdx.input.inputProcessor = inputMultiplexer

        frameRate = FrameRate()
    }

    private fun addMoveActionsToBlimp() {
        val blimpWidth = blimp!!.width
        val screenHeight = Gdx.graphics.height.toFloat()

        // Randomly generate the vertical position within the specified range
        val startY = screenHeight * 0.7f
        val endY = screenHeight * 0.8f
        var randomY = startY + MathUtils.random() * (endY - startY)

        blimp!!.setPosition(-blimpWidth, randomY)

        val forth: MoveToAction = moveTo(Gdx.graphics.width.toFloat(), randomY, 7f)

        // When the blimp reaches the right side, reset its position to the left
        val reset: RunnableAction = Actions.run {
            randomY = startY + MathUtils.random() * (endY - startY)
            Gdx.app.log("","Moving to $randomY")
            blimp!!.setPosition(-blimpWidth, randomY)
        }

        blimp!!.addAction(sequence(forth, reset, delay(1f), // Add a delay for better visibility
            Actions.run { addMoveActionsToBlimp() }))  // Recursively call the function to repeat the behavior
    }



    override fun render() {
        // Step the physics simulation
        world!!.step(1 / 60f, 2, 1)

        val iterator = activeBombs.iterator()
        while (iterator.hasNext()) {
            val bomb = iterator.next()
            if (bomb.isOffScreen()) {
                // Remove bomb from the stage, free it back to the pool, and remove from the active list
                stage?.root?.removeActor(bomb)
                bombPool?.freeBomb(bomb)
                iterator.remove()
            }
        }

        Gdx.gl.glClearColor(backgroundColor!!.r, backgroundColor!!.g, backgroundColor!!.b, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        batch!!.begin()

        stage?.act(Gdx.graphics.deltaTime)
        stage?.draw()

        batch!!.end()

//        hudBatch!!.begin()
//        hudBatch!!.end()

        frameRate!!.update()
        frameRate!!.render()

        debugRenderer?.render(world, stage!!.camera.combined)
    }

    override fun dispose() {
        batch!!.dispose()
    }

    override fun gameStateChanged(gameState: GameState?) {
        // Obtain a bomb from the pool
        val bomb = bombPool?.obtainBomb()

        // Set the bomb's position and velocity as needed
        val bombBody = bomb?.bombBody
        bombBody?.isActive = true
        val currentVerticalVelocity = bombBody?.linearVelocity?.y ?: 0f
        val horizontalVelocity = 0.2f // Adjust this value as needed
        bombBody?.linearVelocity = Vector2(horizontalVelocity, currentVerticalVelocity)
        bombBody?.setTransform(blimp!!.x / PIXELS_PER_METER, blimp!!.y / PIXELS_PER_METER, 0f)

        bomb?.isVisible = true

        // Add the bomb to the stage and the list of active bombs
        stage?.addActor(bomb)
        activeBombs.add(bomb!!)
    }

    override fun canyonStateChanged(canyonState: CanyonState?, brick: Brick?, bomb: Bomb?) {
        Gdx.app.log("","canyonStateChanged with brick and bomb")
    }

    override fun canyonStateChanged(canyonState: CanyonState?) {
        Gdx.app.log("","canyonStateChanged")
    }

    override fun scoringStateChanged(canyonState: ScoringStateEnum?) {
        Gdx.app.log("","scoringStateChanged")
    }
}
