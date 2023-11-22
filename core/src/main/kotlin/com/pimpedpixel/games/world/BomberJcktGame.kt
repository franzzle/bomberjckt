package com.pimpedpixel.games.world

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Stage
import com.pimpedpixel.games.Input
import com.pimpedpixel.games.ScoringStateEnum
import com.pimpedpixel.games.hud.FrameRate
import com.pimpedpixel.games.scoring.GameState
import com.pimpedpixel.games.scoring.GameStateListener
import com.pimpedpixel.games.scoring.GameStateServiceImpl

class BomberJcktGame : ApplicationAdapter(), CanyonStateListener, GameStateListener {
    private var batch: SpriteBatch? = null
    private var hudBatch: SpriteBatch? = null
    private var stage: Stage? = null
    private var flyingMachine: FlyingMachine? = null
    private var bombPool: BombPool? = null
    private var canyon: Canyon? = null
    private var backgroundColor: Color = Color.valueOf("#7382f7")
    private var frameRate: FrameRate? = null
    private val assetManager: AssetManager = AssetManager()
    private var world: World? = null
    private var debugRenderer: Box2DDebugRenderer? = null

    private val activeBombs: MutableList<Bomb> = mutableListOf() // List to track active bombs

    override fun create() {
        batch = SpriteBatch()
        hudBatch = SpriteBatch()

        BrickAssetloader(assetManager).load()
        assetManager.finishLoading()

        world = World(Vector2(0f, -10f), true)

        debugRenderer = Box2DDebugRenderer()

        stage = Stage()

        flyingMachine = FlyingMachine("plane.png", FlyingMachineDirection.LEFT_TO_RIGHT)
        stage?.addActor(flyingMachine)
        flyingMachine?.initBackandForthMovement()

        canyon = Canyon(
            world = world!!,
            canyonStateListener = this,
            canyonLayoutPattern = "canyon",
            assetManager = assetManager)
        stage?.addActor(canyon)

        bombPool = BombPool(world!!, stage!!)

        // Details state transitions
        val gameStateServiceImpl = GameStateServiceImpl(this)
        val inputMultiplexer = InputMultiplexer()
        inputMultiplexer.addProcessor(Input(gameStateServiceImpl))
        Gdx.input.inputProcessor = inputMultiplexer

        frameRate = FrameRate()
    }

    override fun render() {
        // Step the physics simulation
        world!!.step(1 / 60f, 1, 1)

        world!!.setContactListener(BombBrickContactListener())

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
        if(bomb != null){
            // Set the bomb's position and velocity as needed
            val bombBody = bomb.bombBody
            bombBody.isActive = true
            val currentVerticalVelocity = bombBody.linearVelocity.y

            var horizontalVelocity = 4f
            if(flyingMachine?.flyingMachineDirection === FlyingMachineDirection.RIGHT_TO_LEFT){
                horizontalVelocity *= -1
                bomb.color = Color.BLACK
            }
            bombBody.linearVelocity = Vector2(horizontalVelocity, currentVerticalVelocity)
            bomb.setPosition(flyingMachine!!.x, flyingMachine!!.y)
            bombBody.setTransform(flyingMachine!!.x / PIXELS_PER_METER, flyingMachine!!.y / PIXELS_PER_METER, 0f)
            // Add the bomb to the stage and the list of active bombs
            activeBombs.add(bomb)
        }else{
            println("Huh?")
        }
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
