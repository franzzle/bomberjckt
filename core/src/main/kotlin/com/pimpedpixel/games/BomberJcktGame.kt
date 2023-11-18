package com.pimpedpixel.games

import com.badlogic.gdx.Application
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.pimpedpixel.games.controls.CombinedDelegate
import com.pimpedpixel.games.controls.PauseButton
import com.pimpedpixel.games.controls.PauseUI
import com.pimpedpixel.games.fx.ScreenShake
import com.pimpedpixel.games.hud.BlinkingSimpleMessage
import com.pimpedpixel.games.hud.FrameRate
import com.pimpedpixel.games.hud.MessageProvider
import com.pimpedpixel.games.hud.SimpleMessage
import com.pimpedpixel.games.scoring.*
import com.pimpedpixel.games.scoring.Score.Companion.forInteger

class BomberJcktGame : ApplicationAdapter(), CanyonStateListener, GameStateListener, CombinedDelegate {
    var bomb: Bomb? = null
    var gameStateService: GameStateServiceImpl? = null
    var blimp: Blimp? = null
    private var scoringService: ScoringServiceImpl? = null
    private var preferenceService: PreferenceServiceImpl? = null

    //TODO Move to screenmanager state management
    var paused = false
    private var batch: SpriteBatch? = null
    private var hudBatch: SpriteBatch? = null
    private var canyon: Canyon? = null
    private var backgroundColor: Color? = null

    //TODO Move all to HUD
    private var startMessage: BlinkingSimpleMessage? = null
    private var gameOverMessage: BlinkingSimpleMessage? = null
    private var numberOfMissesMessage: SimpleMessage? = null
    private var scoreMessage: SimpleMessage? = null
    private var frameRate: FrameRate? = null
    private var pauseButton: PauseButton? = null
    private var pauseUI: PauseUI? = null
    private var screenShake: ScreenShake? = null
    private var viewport: Viewport? = null
    private var camera: OrthographicCamera? = null
    private var messages: GameProperties? = null
    private var stage: Stage? = null
    override fun create() {
        messages = GameProperties("messages")
        camera = OrthographicCamera()
        camera!!.setToOrtho(false)
        camera!!.position[Gdx.graphics.width * 0.5f, Gdx.graphics.height * 0.5f] = 0f
        viewport = FitViewport(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat(), camera)
        viewport?.apply()
        batch = SpriteBatch()
        hudBatch = SpriteBatch()
        gameStateService = GameStateServiceImpl(this)
        scoringService = ScoringServiceImpl()
        preferenceService = PreferenceServiceImpl()
        stage = Stage(viewport, batch)
        canyon = Canyon(this, "canyon")
        stage!!.addActor(canyon)
        blimp = Blimp()
        stage!!.addActor(blimp)
        bomb = Bomb(preferenceService!!)
        stage!!.addActor(bomb)
        startMessage = BlinkingSimpleMessage(messageProvider = object : MessageProvider {
            override fun message(): String {
                return messageToStartGame
            }
        })
        gameOverMessage = BlinkingSimpleMessage(messageProvider = object : MessageProvider {
            override fun message(): String {
                return messages!!.getPropAsString("message.gameover")
            }
        })
        numberOfMissesMessage = SimpleMessage(messageProvider = object : MessageProvider {
            override fun message(): String {
                return gameStateService!!.numberOfMisses
            }
        })

        scoreMessage = SimpleMessage(messageProvider = object : MessageProvider {
            override fun message(): String {
                return  gameStateService!!.score
            }
        })
        val averagLenghtOfACharacter = numberOfMissesMessage!!.size.getWidth() / numberOfMissesMessage!!.message.length
        val positionNumberOfMisses = Vector2(
            2 * averagLenghtOfACharacter,
            Gdx.graphics.height - 2.1f * numberOfMissesMessage!!.size.getHeight()
        )
        numberOfMissesMessage!!.position = positionNumberOfMisses
        scoreMessage!!.position = Vector2(
            2 * averagLenghtOfACharacter,
            Gdx.graphics.height - scoreMessage!!.size.getHeight()
        )
        pauseButton = PauseButton()
        pauseButton!!.setPauseDelegate(this)
        pauseUI = PauseUI(this, scoringService!!, preferenceService!!)
        screenShake = ScreenShake()
        addMoveActionsToBlimp()
        val colorBlueSky = "#7382f7"
        backgroundColor = Color.valueOf(colorBlueSky)
        val inputMultiplexer = InputMultiplexer()
        inputMultiplexer.addProcessor(pauseButton)
        inputMultiplexer.addProcessor(Input(this))
        inputMultiplexer.addProcessor(pauseUI)
        Gdx.input.inputProcessor = inputMultiplexer
        frameRate = FrameRate()
    }

    private val messageToStartGame: String
        private get() = if (Gdx.app.type == Application.ApplicationType.iOS) {
            messages!!.getPropAsString("message.start.touch")
        } else messages!!.getPropAsString("message.start.key")

    private fun addMoveActionsToBlimp() {
        val verticalPositionBlimp = Gdx.graphics.height * 0.75f
        blimp!!.setPosition(0f, verticalPositionBlimp)
        val endOfTheScreenRight = Gdx.graphics.width.toFloat() - blimp!!.width
        val endOfTheScreenLeft = 0f
        val forth = Actions.moveTo(endOfTheScreenRight, verticalPositionBlimp, 7f)
        val back = Actions.moveTo(endOfTheScreenLeft, verticalPositionBlimp, 7f)
        val flipAction: Action = object : Action() {
            override fun act(delta: Float): Boolean {
                blimp!!.flip()
                return true
            }
        }
        blimp!!.addAction(Actions.forever(Actions.sequence(forth, flipAction, back, flipAction)))
    }

    override fun render() {
        Gdx.gl.glClearColor(backgroundColor!!.r, backgroundColor!!.g, backgroundColor!!.b, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        screenShake!!.update(camera!!)
        camera!!.update()
        gameStateService!!.update()
        updateGameWorld()
        if (!paused) {
            startMessage!!.update()
            gameOverMessage!!.update()
            numberOfMissesMessage!!.update()
            scoreMessage!!.update()
        }
        stage!!.act(Gdx.graphics.deltaTime)
        stage!!.draw()
        hudBatch!!.begin()
        if (!gameStateService!!.gameState.isStarted && !paused) {
            startMessage!!.draw(hudBatch)
        } else {
            numberOfMissesMessage!!.draw(hudBatch)
            scoreMessage!!.draw(hudBatch)
        }
        if (gameStateService!!.gameState.isFinished && !paused) {
            gameOverMessage!!.draw(hudBatch)
        }
        pauseButton!!.draw(hudBatch!!)
        hudBatch!!.end()
        if (preferenceService!!.forKey("fps.visible", java.lang.Boolean.FALSE)) {
            frameRate!!.update()
            frameRate!!.render()
        }
        if (paused) {
            pauseUI!!.render()
        }
    }

    private fun updateGameWorld() {
        if (!paused) {
            stage!!.act(Gdx.graphics.deltaTime)
        }
    }

    // TODO This can move to canyon too
    override fun canyonStateChanged(canyonState: CanyonState?, brick: Brick?, bomb: Bomb?) {
        Gdx.app.log("CanyonStateEvent", canyonState.toString())
        if (canyonState == CanyonState.BRICK_DESTROYED) {
            bomb!!.setHitSomething(true)
            gameStateService!!.addScore(10)
            gameStateService!!.gameState.isPlayerAllowedToDropAnotherBomb = true
            screenShake!!.startShaking()
            Gdx.app.log(this.javaClass.getSimpleName(), "Brick collides ")
            bomb.addOneToBrickAnnihilationCount()
            if (bomb.areEnoughBricksDestroyed()) {
                gameStateService!!.gameState.isPlayerAllowedToDropAnotherBomb = true
                bomb.reset()
            }
            brick!!.setDestroyed(true)
        } else if (canyonState == CanyonState.OUTER_WALL_HIT) {
            bomb!!.bombCollidedWithCanyon()
            gameStateService!!.gameState.isPlayerAllowedToDropAnotherBomb = true
            bomb.reset()
        }
    }

    override fun canyonStateChanged(canyonState: CanyonState?) {
        canyon!!.restoreAllBricks()
    }

    override fun scoringStateChanged(scoringStateEnum: ScoringStateEnum?) {
        if (scoringStateEnum == ScoringStateEnum.MISS) {
            Gdx.app.log(this.javaClass.getSimpleName(), "A miss occured")
            gameStateService!!.addMiss()
            bomb!!.reset()
            gameStateService!!.gameState.isPlayerAllowedToDropAnotherBomb = true
        }
    }

    override fun resize(width: Int, height: Int) {
        viewport!!.update(width, height, true)
    }

    override fun dispose() {
        batch!!.dispose()
    }

    override fun pause() {
        paused = !paused
    }

    override fun resume() {
        super.resume()
    }

    override fun gameStateChanged(gameState: GameState?) {
        if (gameState!!.isFinished) {
            scoringService!!.add(forInteger(gameState.score))
        }
        if (gameState.isGameOverShownLongEnough) {
            gameState.isStarted = false
            gameState.isFinished = false
            canyon!!.restoreAllBricks()
            gameStateService!!.resetGameState()
        }
    }

    override fun triggerEvent(event: String?) {
        canyon!!.restoreAllBricks()
    }
}
