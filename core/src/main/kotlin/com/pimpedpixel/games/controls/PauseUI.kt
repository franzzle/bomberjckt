package com.pimpedpixel.games.controls

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.pimpedpixel.games.common.ScreenManager.Companion.getInstance
import com.pimpedpixel.games.scoring.PreferenceServiceImpl
import com.pimpedpixel.games.scoring.ScoreFormatter.toScoreWithLeadingZeroes
import com.pimpedpixel.games.scoring.ScoringServiceImpl

class PauseUI(
    pauseDelegate: PauseDelegate,
    private val scoringService: ScoringServiceImpl,
    preferenceService: PreferenceServiceImpl
) : Stage(ScreenViewport()) {
    private val hiScoreLabel: Label

    init {
        val skin = Skin(Gdx.files.internal("skin/skin.json"))
        val root = Table()
        root.width = Gdx.app.graphics.width * 0.9f
        //        root.setDebug(true);
        root.setFillParent(true)
        addActor(root)
        val mainTable = Table()
        val checkboxSounds = CheckBox(" Sounds", skin)
        checkboxSounds.isChecked = preferenceService.forKey("sounds.active", java.lang.Boolean.TRUE)
        checkboxSounds.addListener(object : EventListener {
            override fun handle(event: Event): Boolean {
                if (event is InputEvent) {
                    val inputEvent = event
                    if ("touchDown".equals(inputEvent.type.name, ignoreCase = true)) {
                        Gdx.app.log(this.javaClass.simpleName, inputEvent.type.name)
                        val soundsActive = preferenceService.forKey("sounds.active", java.lang.Boolean.TRUE)
                        preferenceService.setForKey("sounds.active", !soundsActive)
                        return true
                    }
                }
                return true
            }
        })
        mainTable.add(checkboxSounds).left().row()
        val fpsSwitch = CheckBox(" Show Fps", skin)
        fpsSwitch.addListener(object : EventListener {
            override fun handle(event: Event): Boolean {
                if (event is InputEvent) {
                    val inputEvent = event
                    if ("touchDown".equals(inputEvent.type.name, ignoreCase = true)) {
                        Gdx.app.log(this.javaClass.simpleName, inputEvent.type.name)
                        val previousState = preferenceService.forKey("fps.visible", java.lang.Boolean.FALSE)
                        preferenceService.setForKey("fps.visible", !previousState)
                        return true
                    }
                }
                return true
            }
        })
        mainTable.add(fpsSwitch).left().row()
        val play = TextButton("Back", skin)
        mainTable.add(Label("  ", skin)).row()
        hiScoreLabel = Label(hiscoreDescription, skin)
        mainTable.add(hiScoreLabel).center().row()
        mainTable.add(Label("  ", skin)).row()
        val textButtonContainer = Container(play)
        textButtonContainer.isTransform = true
        textButtonContainer.setOrigin(Gdx.app.graphics.width - play.width, play.height / 2)
        textButtonContainer.setSize(play.width, play.height)
        mainTable.add(textButtonContainer.right()).row()
        play.addListener(object : EventListener {
            override fun handle(event: Event): Boolean {
                if (event is InputEvent) {
                    val inputEvent = event
                    if ("touchDown".equals(inputEvent.type.name, ignoreCase = true)) {
                        Gdx.app.log(this.javaClass.simpleName, inputEvent.type.name)
                        pauseDelegate.pause()
                        return true
                    }
                }
                return false
            }
        })
        root.row()
        val scale = getInstance()!!.scale.toFloat()
        if (scale > 1.0f) {
            mainTable.isTransform = true
            mainTable.setScale(scale)
        }
        root.add(mainTable)
    }

    private val hiscoreDescription: String
        private get() = toScoreWithLeadingZeroes("Hiscore", 0)
//        private get() = toScoreWithLeadingZeroes("Hiscore", scoringService?.currentHighScore!!)

    fun render() {
        hiScoreLabel.setText(hiscoreDescription)
        this.act(Gdx.graphics.deltaTime)
        draw()
    }
}
