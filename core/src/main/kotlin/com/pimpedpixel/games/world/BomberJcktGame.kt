package com.pimpedpixel.games.world

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.ashley.signals.Signal
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Stage
import com.pimpedpixel.games.Input
import com.pimpedpixel.games.ScoringStateEnum
import com.pimpedpixel.games.gameplay.*
import com.pimpedpixel.games.gameplay.PlayerChoice.PLAYER_A
import com.pimpedpixel.games.gameplay.PlayerChoice.PLAYER_B
import com.pimpedpixel.games.hud.Alignment
import com.pimpedpixel.games.hud.BlinkingSimpleMessage
import com.pimpedpixel.games.hud.FrameRate
import com.pimpedpixel.games.hud.SimpleMessage
import com.pimpedpixel.games.scoring.GameState
import com.pimpedpixel.games.scoring.GameStateListener
import com.pimpedpixel.games.scoring.GameStateServiceImpl
import com.pimpedpixel.games.world.FlyingMachineDirection.LEFT_TO_RIGHT
import com.pimpedpixel.games.world.FlyingMachineDirection.RIGHT_TO_LEFT


private const val TIME_STEP = 1 / 60f

class BomberJcktGame : ApplicationAdapter(), CanyonStateListener, GameStateListener {
    private var blinkingSimpleMessage: BlinkingSimpleMessage? = null
    private var playerAMissesMessage: SimpleMessage? = null
    private var playerBMissesMessage: SimpleMessage? = null
    private var frameRate: FrameRate? = null
    private var debugRenderer: Box2DDebugRenderer? = null
    private var engine: PooledEngine? = null

    private var world: World? = null
    private var bombPool: BombPool? = null
    private var canyon: Canyon? = null
    private var flyingMachinePlayerA: FlyingMachine? = null
    private var flyingMachinePlayerB: FlyingMachine? = null

    private var stage: Stage? = null
    private var hudStage: Stage? = null


    private val backgroundColor: Color = Color.valueOf("#7382f7")
    private val activeBombs: MutableList<Bomb> = mutableListOf() // List to track active bombs
    private val assetManager: AssetManager = AssetManager()

    private val gamePhaseStateComponent: GamePhaseStateComponent = GamePhaseStateComponent()
    //TODO
    // - Add a HUD with "Press space to start"
    // - Make a json with brick properties that define color and score

    override fun create() {
        BrickAssetloader(assetManager).load()
        assetManager.finishLoading()

        world = World(Vector2(0f, -90f), true)
        engine = PooledEngine()
        engine!!.addEntity(Entity().add(gamePhaseStateComponent))
        engine!!.addEntity(Entity().add(TurnComponent()))
        engine!!.addEntity(Entity().add(PlayerStatisticsComponent(PLAYER_A)))
        engine!!.addEntity(Entity().add(PlayerStatisticsComponent(PLAYER_B)))
        val signal = Signal<PlayerChoice>()

        debugRenderer = Box2DDebugRenderer()

        stage = Stage()

        flyingMachinePlayerA = FlyingMachine(flyingMachineDirection = LEFT_TO_RIGHT, signal = signal)
        stage?.addActor(flyingMachinePlayerA)
        flyingMachinePlayerA!!.startMovement()

        flyingMachinePlayerB = FlyingMachine(flyingMachineDirection = RIGHT_TO_LEFT, signal = signal)
        stage?.addActor(flyingMachinePlayerB)

        signal.add(
            TurnChangedListener(
                engine!!,
                flyingMachinePlayerA!!,
                flyingMachinePlayerB!!
            )
        )

        canyon = Canyon(
            world = world!!,
            canyonStateListener = this,
            canyonLayoutPattern = "canyon",
            assetManager = assetManager
        )
        stage?.addActor(canyon)

        bombPool = BombPool(world!!, stage!!)

        // Details state transitions
        val gameStateServiceImpl = GameStateServiceImpl(this)
        val inputMultiplexer = InputMultiplexer()
        inputMultiplexer.addProcessor(Input(engine!!, gameStateServiceImpl))
        Gdx.input.inputProcessor = inputMultiplexer

        ComponentHelper.initInstance(engine!!)

        frameRate = FrameRate()
        hudStage = Stage()
        blinkingSimpleMessage = BlinkingSimpleMessage("Press space to start")

        hudStage?.addActor(blinkingSimpleMessage)

        playerAMissesMessage = SimpleMessage(Alignment.LEFT_BOTTOM)
        hudStage?.addActor(playerAMissesMessage)

        playerBMissesMessage = SimpleMessage(Alignment.RIGHT_BOTTOM)!!
        hudStage?.addActor(playerBMissesMessage)
    }

    override fun render() {
        // Step the physics simulation
        world!!.step(TIME_STEP, 1, 1)
        world!!.setContactListener(BombBrickContactListener(engine!!))
        engine!!.update(Gdx.graphics.deltaTime)

        val playerAStats = ComponentHelper.retrievePlayerStatisticsComponent(PLAYER_A)
        playerAMissesMessage?.setMessage("X".repeat(playerAStats!!.misses))
        val playerBStats = ComponentHelper.retrievePlayerStatisticsComponent(PLAYER_B)
        playerBMissesMessage?.setMessage("X".repeat(playerBStats!!.misses))

        if(playerAStats!!.misses >= 3 || playerBStats!!.misses >= 3){
            val gamePhaseStateComponent = ComponentHelper.retrieveGamePhaseStateComponent()
            gamePhaseStateComponent?.gamePhaseState = GamePhaseState.GAME_OVER
        }

        val iterator = activeBombs.iterator()
        while (iterator.hasNext()) {
            val bomb = iterator.next()
            val bombUserData = bomb.bombBody.userData as BombUserData
            if (bombUserData.destroyed) {
                bomb.isVisible = false
                bombPool?.freeBomb(bomb)
                iterator.remove()
            }
        }

        Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        stage?.act(Gdx.graphics.deltaTime)
        stage?.draw()

        if (gamePhaseStateComponent.gamePhaseState !== GamePhaseState.ATTRACT_SCREEN) {
            blinkingSimpleMessage?.isVisible = false
        }
        hudStage?.act(Gdx.graphics.deltaTime)
        hudStage?.draw()

        frameRate!!.update()
        frameRate!!.render()

        debugRenderer?.render(world, stage!!.camera.combined)
    }

    override fun dispose() {
        stage?.dispose()
        hudStage?.dispose()
        world?.dispose()
        debugRenderer?.dispose()
        assetManager.dispose()
    }

    override fun gameStateChanged(gameState: GameState?) {
        // Obtain a bomb from the pool
        val bomb = bombPool?.obtainBomb()
        if (bomb != null) {
            // Set the bomb's position and velocity as needed
            val bombBody = bomb.bombBody
            bombBody.isActive = true
            val currentVerticalVelocity = bombBody.linearVelocity.y
            val horizontalVelocity = 20f

            val turnComponent = ComponentHelper.retrieveTurnComponent()
            (bombBody.userData as BombUserData).thrownBy = turnComponent.playerChoice

            when(turnComponent.playerChoice) {
                PLAYER_A -> {
                    bombBody.linearVelocity = Vector2(horizontalVelocity, currentVerticalVelocity)
                    bomb.setInitialPosition(flyingMachinePlayerA!!.x, flyingMachinePlayerA!!.y)
                    bomb.color = Color.WHITE
                }
                PLAYER_B -> {
                    bombBody.linearVelocity = Vector2(-horizontalVelocity, currentVerticalVelocity)
                    bomb.setInitialPosition(flyingMachinePlayerB!!.x, flyingMachinePlayerB!!.y)
                    bomb.color = Color.BLACK
                }
                PlayerChoice.UNDEFINED -> Gdx.app.log("", "Nobodies turn yet")
            }

            // Add the bomb to the stage and the list of active bombs
            activeBombs.add(bomb)
        } else {
            println("Huh?")
        }
    }

    override fun canyonStateChanged(canyonState: CanyonState?, brick: Brick?, bomb: Bomb?) {
        Gdx.app.log("", "canyonStateChanged with brick and bomb")
    }

    override fun canyonStateChanged(canyonState: CanyonState?) {
        Gdx.app.log("", "canyonStateChanged")
    }

    override fun scoringStateChanged(canyonState: ScoringStateEnum?) {
        Gdx.app.log("", "scoringStateChanged")
    }
}
