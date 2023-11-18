package com.pimpedpixel.games

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.Stage
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
        val verticalPositionBlimp: Float = Gdx.app.graphics.height.toFloat() * 0.75f
        blimp!!.setPosition(0f, verticalPositionBlimp)
        val endOfTheScreenRight: Float = Gdx.app.graphics.width.toFloat() - blimp!!.width
        val endOfTheScreenLeft = 0f
        val forth: MoveToAction = moveTo(endOfTheScreenRight, verticalPositionBlimp, 7f)
        val back: MoveToAction = moveTo(endOfTheScreenLeft, verticalPositionBlimp, 7f)
        val flipAction = FlipAction(blimp!!)
        blimp!!.addAction(forever(sequence(forth, flipAction, back, flipAction)))
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
