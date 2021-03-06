package cz.agents.agentpolis.simulator.visualization.googleearth.factories;

import com.google.inject.Injector;
import cz.agents.agentpolis.apgooglearth.regionbounds.RegionBounds;
import cz.agents.agentpolis.apgooglearth.updates.GraphUpdateGE;
import cz.agents.agentpolis.simmodel.environment.model.AgentPositionModel;
import cz.agents.agentpolis.simmodel.environment.model.VehiclePositionModel;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.GraphType;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks.TransportNetworks;
import cz.agents.agentpolis.simulator.visualization.googleearth.UpdateGEFactory;
import cz.agents.agentpolis.simulator.visualization.googleearth.entity.GraphGE;
import cz.agents.alite.googleearth.cameraalt.visibility.CameraAltVisibility;
import cz.agents.alite.googleearth.updates.UpdateKmlView;
import cz.agents.basestructures.Edge;
import cz.agents.basestructures.Graph;
import cz.agents.basestructures.Node;
import de.micromata.opengis.kml.v_2_2_0.Coordinate;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * 
 * The factory for the initialization of graph visualization for GE
 * 
 * @author Zbynek Moler
 * 
 */
public class GraphUpdateGEFactory extends UpdateGEFactory {

    private final GraphType graphType;
    private final Color graphColor;

    public GraphUpdateGEFactory(final GraphType graphType, final Color graphColor,
            CameraAltVisibility cameraAltVisibility, String nameOfUpdateKmlView) {
        super(cameraAltVisibility, nameOfUpdateKmlView);
        this.graphType = graphType;
        this.graphColor = graphColor;

    }

    public UpdateKmlView createUpdateKmlView(Injector injector, RegionBounds regionBounds) {
        Graph<?, ?> graph = injector.getInstance(TransportNetworks.class).getGraph(graphType);
        Collection<? extends Edge> edges = graph.getAllEdges();

        Map<String, List<Coordinate>> coordinates = new HashMap<String, List<Coordinate>>();

        for (Edge edge : edges) {
            List<Coordinate> coordinatesInner = new ArrayList<Coordinate>();

            Node fromNode = graph.getNode(edge.fromId);
            Node toNode = graph.getNode(edge.toId);

            coordinatesInner.add(new Coordinate(fromNode.getLongitude(), fromNode.getLatitude()));
            coordinatesInner.add(new Coordinate(toNode.getLongitude(), toNode.getLatitude()));

            coordinates.put(String.valueOf(edge.fromId + "-" + edge.toId),
                    coordinatesInner);
        }

        GraphGE graphGE = new GraphGE(injector.getInstance(AgentPositionModel.class),
                injector.getInstance(VehiclePositionModel.class), edges);

        return new GraphUpdateGE(coordinates, graphGE, graphColor);
    }

}
