package com.pimpedpixel.games.world

import TransitionFromGameOverSystem
import TransitionFromWhoWhonSystem
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.ashley.signals.Signal
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Stage
import com.pimpedpixel.games.*
import com.pimpedpixel.games.fx.ScreenShakeSystem
import com.pimpedpixel.games.gameplay.*
import com.pimpedpixel.games.gameplay.GamePhaseState.GAME_OVER
import com.pimpedpixel.games.gameplay.GamePhaseState.GAME_RUNNING
import com.pimpedpixel.games.gameplay.PlayerChoice.PLAYER_A
import com.pimpedpixel.games.gameplay.PlayerChoice.PLAYER_B
import com.pimpedpixel.games.world.FlyingMachineDirection.LEFT_TO_RIGHT
import com.pimpedpixel.games.world.FlyingMachineDirection.RIGHT_TO_LEFT


private const val TIME_STEP = 1 / 60f

class BomberJcktGame : ApplicationAdapter(), CanyonStateListener  {
    private var debugRenderer: Box2DDebugRenderer? = null
    private var engine: PooledEngine? = null

    private var world: World? = null
    private var bombPool: BombPool? = null
    private var canyon: Canyon? = null
    private var flyingMachinePlayerA: FlyingMachine? = null
    private var flyingMachinePlayerB: FlyingMachine? = null

    private var stage: Stage? = null
    private var hudStage: HudStage? = null

    private val backgroundColor: Color = Color.valueOf("#7382f7")
    private val activeBombs: MutableList<Bomb> = mutableListOf() // List to track active bombs

    // TODO
    // - Make a json with brick properties that define color and score

    override fun create() {
        BrickAssetLoader().load()
        BitmapFontLoader().load()
        SoundLoader().load()
        AssetManagerHolder.assetManager.finishLoading()

        world = World(Vector2(0f, -90f), true)

        stage = Stage()

        engine = PooledEngine()
        engine!!.addSystem(ScreenShakeSystem(stage!!))
        engine!!.addEntity(Entity().add(GamePhaseStateComponent()))
        engine!!.addEntity(Entity().add(TurnComponent()))
        engine!!.addEntity(Entity().add(PlayerStatisticsComponent(PLAYER_A)))
        engine!!.addEntity(Entity().add(PlayerStatisticsComponent(PLAYER_B)))

        val playerChoiceSignal = Signal<PlayerChoice>()

        debugRenderer = Box2DDebugRenderer()

        flyingMachinePlayerA = FlyingMachine(flyingMachineDirection = LEFT_TO_RIGHT,
            changePlayersTurnSignal = playerChoiceSignal,
            PLAYER_A)
        stage?.addActor(flyingMachinePlayerA)

        flyingMachinePlayerB = FlyingMachine(
            flyingMachineDirection = RIGHT_TO_LEFT,
            changePlayersTurnSignal = playerChoiceSignal,
            PLAYER_B)
        stage?.addActor(flyingMachinePlayerB)

        playerChoiceSignal.add(
            TurnChangedListener(
                flyingMachinePlayerA!!,
                flyingMachinePlayerB!!
            )
        )

        canyon = Canyon(
            world = world!!,
            canyonLayoutPattern = "canyon"
        )
        stage?.addActor(canyon)

        bombPool = BombPool(world!!, stage!!)

        engine!!.addSystem(BombThrowingSystem(bombPool!!,
            flyingMachinePlayerA,
            flyingMachinePlayerB,
            activeBombs))

        engine!!.addSystem(TransitionFromGameOverSystem())
        engine!!.addSystem(TransitionFromWhoWhonSystem())

        // Details state transitions
        val inputMultiplexer = InputMultiplexer()
        val throwBombSignal = Signal<GamePhaseState>()
        throwBombSignal.add(BombThrowLogicListener(engine!!))
        inputMultiplexer.addProcessor(Input(engine!!, throwBombSignal, playerChoiceSignal))
        Gdx.input.inputProcessor = inputMultiplexer

        ComponentHelper.initInstance(engine!!)

        hudStage = HudStage()
    }

    override fun render() {
        // Step the physics simulation
        world!!.step(TIME_STEP, 1, 1)
        world!!.setContactListener(BombBrickContactListener(engine!!))
        engine!!.update(Gdx.graphics.deltaTime)

        val playerAStats = ComponentHelper.retrievePlayerStatisticsComponent(PLAYER_A)
        val playerBStats = ComponentHelper.retrievePlayerStatisticsComponent(PLAYER_B)

        val gamePhaseStateComponent = ComponentHelper.retrieveGamePhaseStateComponent()
        val gamePhaseState = gamePhaseStateComponent?.gamePhaseState
        if(playerAStats.isOut && playerBStats.isOut && gamePhaseState == GAME_RUNNING) {
            gamePhaseStateComponent?.gamePhaseState = GAME_OVER
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

        hudStage?.act()
        hudStage?.draw()

//        debugRenderer?.render(world, stage!!.camera.combined)
    }

    override fun dispose() {
        stage?.dispose()
        hudStage?.dispose()
        world?.dispose()
        debugRenderer?.dispose()
        AssetManagerHolder.assetManager.dispose()
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
