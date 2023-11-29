import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.pimpedpixel.games.gameplay.GamePhaseState
import com.pimpedpixel.games.gameplay.GamePhaseStateComponent

class TransitionFromGameOverSystem() :
    IteratingSystem(Family.all(GamePhaseStateComponent::class.java).get()) {
    private val gamePhaseMapper = ComponentMapper.getFor(GamePhaseStateComponent::class.java)
    private val transitionDelayInSeconds = 5f
    private var timeInGameOverState = 0f

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val gamePhaseComponent = gamePhaseMapper.get(entity)

        if (gamePhaseComponent.gamePhaseState == GamePhaseState.GAME_OVER) {
            timeInGameOverState += deltaTime

            if (timeInGameOverState >= transitionDelayInSeconds) {
                gamePhaseComponent.transitionToNextState()
                timeInGameOverState = 0f
            }
        }
    }
}
