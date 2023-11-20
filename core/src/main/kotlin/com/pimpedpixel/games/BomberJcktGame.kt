package com.pimpedpixel.games

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction
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
    private var canyon: Canyon? = null
    private var backgroundColor: Color? = null
    private var frameRate: FrameRate? = null

    override fun create() {
        val colorBlueSky = "#7382f7"
        backgroundColor = Color.valueOf(colorBlueSky)

        batch = SpriteBatch()
        hudBatch = SpriteBatch()
        stage = Stage()
        blimp = Blimp()
        addMoveActionsToBlimp()
        stage?.addActor(blimp)
        canyon = Canyon(this, "canyon")
        stage?.addActor(canyon)

        val gameStateServiceImpl = GameStateServiceImpl(this)
        val inputMultiplexer = InputMultiplexer()
        inputMultiplexer.addProcessor(Input(gameStateServiceImpl))
        Gdx.input.inputProcessor = inputMultiplexer

        frameRate = FrameRate()
    }

    private fun addMoveActionsToBlimp() {
        val screenHeight = Gdx.graphics.height.toFloat()
        val blimpWidth = blimp!!.width
        val startY = screenHeight * 0.7f
        val endY = screenHeight * 0.8f
        val endX = -blimpWidth  // Move the blimp to the left of the screen

        // Randomly generate the vertical position within the specified range
        val randomY = startY + MathUtils.random() * (endY - startY)

        blimp!!.setPosition(Gdx.graphics.width.toFloat(), randomY)

        val forth: MoveToAction = moveTo(endX, randomY, 7f)
        val flipAction = FlipAction(blimp!!)

        // When the blimp reaches the left side, move it to the right to reset
        val reset: MoveToAction = moveTo(Gdx.graphics.width.toFloat(), randomY, 0f)

        blimp!!.addAction(sequence(forth, flipAction, reset, flipAction, delay(1f), // Add a delay for better visibility
            Actions.run { addMoveActionsToBlimp() }))  // Recursively call the function to repeat the behavior
    }

    override fun render() {
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
    }

    override fun dispose() {
        batch!!.dispose()
    }

    override fun gameStateChanged(gameState: GameState?) {
        Gdx.app.log("", "gameStateChanged")
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
