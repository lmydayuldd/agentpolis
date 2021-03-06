/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.vividsolutions.jts.geom.Coordinate;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks.TransportNetworks;
import cz.agents.agentpolis.utils.nearestelement.NearestElementUtil;
import cz.agents.basestructures.GPSLocation;
import cz.agents.geotools.Transformer;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author fido
 */
@Singleton
public class NearestElementUtils {
    private final HashMap<GraphType, NearestElementUtil<SimulationNode>> nearestElementUtilsMappedByGraphType;
    
    private final TransportNetworks transportNetworks;
    
    private final Transformer transformer;
    
//    private final Map<GraphType,Network> networksMappedByGraphType;

     //TODO clear constructor usage Tranformer injection or not?
    @Inject
    public NearestElementUtils(TransportNetworks transportNetworks, @Named("mapSrid") int srid, Transformer transformer) {
        this.transportNetworks = transportNetworks;
        this.transformer = transformer;
//        this.networksMappedByGraphType = new HashMap<>();
        this.nearestElementUtilsMappedByGraphType = new HashMap<>();
    }
    
    public SimulationNode getNearestElement(GPSLocation location, GraphType graphType){
        if(!nearestElementUtilsMappedByGraphType.containsKey(graphType)){
            createNearestElementUtil(graphType);
        }
        
        NearestElementUtil<SimulationNode> nearestElementUtil = nearestElementUtilsMappedByGraphType.get(graphType);
        
        return nearestElementUtil.getNearestElement(location);
    }
    
    public SimulationNode[] getNearestElements(GPSLocation location, GraphType graphType, int numberOfNearestElements){
        if(!nearestElementUtilsMappedByGraphType.containsKey(graphType)){
            createNearestElementUtil(graphType);
        }
        
        NearestElementUtil<SimulationNode> nearestElementUtil = nearestElementUtilsMappedByGraphType.get(graphType);
        
        return (SimulationNode[]) nearestElementUtil.getKNearestElements(location, numberOfNearestElements);
    }

    private void createNearestElementUtil(GraphType graphType) {
        List<Pair<Coordinate,SimulationNode>> pairs = new ArrayList<>();
		
		for (SimulationNode node : transportNetworks.getGraph(graphType).getAllNodes()) {
			pairs.add(new Pair<>(new Coordinate(node.getLongitude(), node.getLatitude()), node));
		}
		
		NearestElementUtil<SimulationNode> nearestElementUtil = new NearestElementUtil<>(pairs, transformer, 
				new NodeArrayConstructor());
        
        nearestElementUtilsMappedByGraphType.put(graphType, nearestElementUtil);
    }
    
    private static class NodeArrayConstructor implements NearestElementUtil.SerializableIntFunction<SimulationNode[]>{

        @Override
        public SimulationNode[] apply(int value) {
            return new SimulationNode[value];
        }

    }
    
    
    
}
