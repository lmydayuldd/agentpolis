package cz.agents.agentpolis.simmodel.agent.activity.movement;

import com.google.inject.Inject;
import cz.agents.agentpolis.siminfrastructure.logger.agent.activity.MovementActivityLogger;
import cz.agents.agentpolis.siminfrastructure.planner.trip.DepartureTripItem;
import cz.agents.agentpolis.simmodel.environment.model.action.AgentPositionAction;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks.TransportNetworks;
import cz.agents.agentpolis.simmodel.environment.model.delaymodel.action.DelayAction;

/**
 * The movement activity where executed trip does depends on a time constrains
 * on a particular trip item
 * 
 * @author Zbynek Moler
 * 
 */
public class MovementActivityDepartureTripItem extends MovementActivity<DepartureTripItem> {

    @Inject
    public MovementActivityDepartureTripItem(DelayAction queueAction,
            AgentPositionAction agentPositionAction, TransportNetworks transportNetworks,
            MovementActivityLogger movementActivityLogger) {
        super(queueAction, agentPositionAction, transportNetworks, movementActivityLogger);
        // TODO Auto-generated constructor stub
    }

}
