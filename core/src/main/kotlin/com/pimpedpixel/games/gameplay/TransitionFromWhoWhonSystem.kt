import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.pimpedpixel.games.gameplay.ComponentHelper
import com.pimpedpixel.games.gameplay.GamePhaseState
import com.pimpedpixel.games.gameplay.GamePhaseStateComponent
import com.pimpedpixel.games.gameplay.PlayerChoice

class TransitionFromWhoWhonSystem() :
    IteratingSystem(Family.all(GamePhaseStateComponent::class.java).get()) {
    private val gamePhaseMapper = ComponentMapper.getFor(GamePhaseStateComponent::class.java)
    private val transitionDelayInSeconds = 5f
    private var timeInGameOverState = 0f

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val gamePhaseComponent = gamePhaseMapper.get(entity)

        if (gamePhaseComponent.gamePhaseState == GamePhaseState.WHO_WON) {
            timeInGameOverState += deltaTime

            if (timeInGameOverState >= transitionDelayInSeconds) {
                // TODO Add Highscore to preferences
                ComponentHelper.retrievePlayerStatisticsComponent(PlayerChoice.PLAYER_A).reset()
                ComponentHelper.retrievePlayerStatisticsComponent(PlayerChoice.PLAYER_B).reset()

                ComponentHelper.retrieveTurnComponent().reset()

                gamePhaseComponent.transitionToNextState()
                timeInGameOverState = 0f
            }
        }
    }
}
