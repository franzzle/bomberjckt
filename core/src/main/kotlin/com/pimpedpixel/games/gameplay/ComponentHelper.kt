package com.pimpedpixel.games.gameplay

import com.badlogic.ashley.core.Family
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.utils.GdxRuntimeException

class ComponentHelper private constructor(private val engine: PooledEngine) {
    // ... existing code ...

    companion object {
        private var instance: ComponentHelper? = null

        @JvmStatic
        fun initInstance(engine: PooledEngine) {
            if (instance == null) {
                instance = ComponentHelper(engine)
            }
        }

        @JvmStatic
        fun getInstance(): ComponentHelper {
            requireNotNull(instance) { "ComponentHelper has not been initialized. Call initInstance first." }
            return instance!!
        }

        @JvmStatic
        fun retrieveGamePhaseStateComponent(): GamePhaseStateComponent {
            val entitiesFor = instance?.engine?.getEntitiesFor(Family.all(GamePhaseStateComponent::class.java).get())
            if (entitiesFor != null && entitiesFor.size() > 0) {
                return entitiesFor.first().getComponent(GamePhaseStateComponent::class.java)
            }
            throw GdxRuntimeException("No TurnComponent found")
        }

        @JvmStatic
        fun retrieveTurnComponent(): TurnComponent {
            val entitiesFor = instance?.engine?.getEntitiesFor(Family.all(TurnComponent::class.java).get())
            if (entitiesFor != null && entitiesFor.size() > 0) {
                return entitiesFor.first().getComponent(TurnComponent::class.java)
            }
            throw GdxRuntimeException("No TurnComponent found")
        }

        @JvmStatic
        fun retrievePlayerStatisticsComponent(playerChoice: PlayerChoice): PlayerStatisticsComponent {
            val entitiesFor = instance?.engine?.getEntitiesFor(Family.all(PlayerStatisticsComponent::class.java).get())
            if (entitiesFor != null) {
                val iterator = entitiesFor.iterator()
                while(iterator.hasNext()){
                    val psEntity = iterator.next()
                    val playerStatsComponent: PlayerStatisticsComponent = psEntity.getComponent(PlayerStatisticsComponent::class.java)
                    if (playerStatsComponent.playerChoice == playerChoice) {
                        return playerStatsComponent
                    }
                }
            }
            throw GdxRuntimeException("No Player Stats found")
        }

    }
}
