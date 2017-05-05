package cz.agents.agentpolis.siminfrastructure.planner;


import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.agents.agentpolis.siminfrastructure.planner.TripPlannerException;
import cz.agents.agentpolis.siminfrastructure.planner.path.ShortestPathPlanner;
import cz.agents.agentpolis.siminfrastructure.planner.path.ShortestPathPlanners;
import cz.agents.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.agents.agentpolis.siminfrastructure.planner.trip.TripItem;
import cz.agents.agentpolis.siminfrastructure.planner.trip.VehicleTrip;
import cz.agents.agentpolis.simmodel.entity.vehicle.PhysicalVehicle;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.EGraphType;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.GraphType;
import cz.agents.basestructures.Node;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author fido
 */
@Singleton
public class TripsUtil {
    
    protected static final Set<GraphType> GRAPH_TYPES = new HashSet(Arrays.asList(EGraphType.HIGHWAY));
    
    
    
    protected final ShortestPathPlanners pathPlanners;
    
    
    protected ShortestPathPlanner pathPlanner;

    
    
    
    @Inject
    public TripsUtil(ShortestPathPlanners pathPlanners) {
        this.pathPlanners = pathPlanners;
    }

    
    
    
    public VehicleTrip locationsToVehicleTrip(List<Node> locations, boolean precomputedPaths, PhysicalVehicle vehicle){
        if(!precomputedPaths && pathPlanner == null){
            pathPlanner = pathPlanners.getPathPlanner(GRAPH_TYPES);
        }
        
        VehicleTrip finalTrip = null;
        LinkedList<TripItem> tripItems = new LinkedList<>();
        
        int startNodeId = locations.get(0).getId();
        
        if(precomputedPaths){
            tripItems.add(new TripItem(startNodeId));
        }
		
		for (int i = 1; i < locations.size(); i++) {
			int targetNodeId = locations.get(i).getId();
            if(startNodeId == targetNodeId){
                try {
                    throw new Exception("There can't be two identical locations in a row");
                } catch (Exception ex) {
                    Logger.getLogger(TripsUtil.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
			if(precomputedPaths){
                tripItems.add(new TripItem(targetNodeId));
			}
			else{
				try {
					VehicleTrip partialTrip = pathPlanner.findTrip(vehicle.getId(), startNodeId, targetNodeId);
                    while (partialTrip.hasNextTripItem()) {
                        tripItems.add(partialTrip.getAndRemoveFirstTripItem());
                    }
					
				} catch (TripPlannerException ex) {
					Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
				}
			}
			startNodeId = targetNodeId;
		}
        
		finalTrip = new VehicleTrip(tripItems, EGraphType.HIGHWAY, vehicle.getId());
    
        return finalTrip;
    }
    
    public VehicleTrip createTrip(int startNodeId, int targetNodeId, PhysicalVehicle vehicle){
        if(startNodeId == targetNodeId){
            try {
                throw new Exception("Start node cannot be the same as end node");
            } catch (Exception ex) {
                Logger.getLogger(TripsUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(pathPlanner == null){
            pathPlanner = pathPlanners.getPathPlanner(GRAPH_TYPES);
        }
        
        VehicleTrip finalTrip = null;
        try {
            finalTrip = pathPlanner.findTrip(vehicle.getId(), startNodeId, targetNodeId);
        } catch (TripPlannerException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    
        return finalTrip;
    }
    
     public Trip<Node> createTrip(int startNodeId, int targetNodeId){
        if(startNodeId == targetNodeId){
            try {
                throw new Exception("Start node cannot be the same as end node");
            } catch (Exception ex) {
                Logger.getLogger(TripsUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(pathPlanner == null){
            pathPlanner = pathPlanners.getPathPlanner(GRAPH_TYPES);
        }
        
        Trip finalTrip = null;
        try {
            finalTrip = pathPlanner.findTrip(startNodeId, targetNodeId);
        } catch (TripPlannerException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    
        return finalTrip;
    }
	
	public static VehicleTrip mergeTrips(VehicleTrip<TripItem>... trips){
		int i = 0;
		VehicleTrip firstTrip = null;
		do{
			firstTrip = trips[i];
			i++;
		}while(firstTrip == null);
		
		VehicleTrip<TripItem> newTrip = new VehicleTrip<>(new LinkedList<>(), firstTrip.getGraphType(), firstTrip.getVehicleId());
		
		for(int j = 0; j < trips.length; j++){
			VehicleTrip<TripItem> trip = trips[j];
			if(trip != null){
				for (TripItem location : trip.getLocations()) {
					newTrip.extendTrip(location);
				}
			}
		}
		return newTrip;
	}
}