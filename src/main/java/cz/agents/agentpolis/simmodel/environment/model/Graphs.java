/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simmodel.environment.model;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.GraphType;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationEdge;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;
import cz.agents.basestructures.Graph;
import java.util.Map;

/**
 *
 * @author F-I-D-O
 */
@Singleton
public class Graphs {
	private Map<GraphType, Graph<SimulationNode, SimulationEdge>> graphs;

	
	
	
	
	public Map<GraphType, Graph<SimulationNode, SimulationEdge>> getGraphs() {
		return graphs;
	}

	public void setGraphs(Map<GraphType, Graph<SimulationNode, SimulationEdge>> graphs) {
		this.graphs = graphs;
	}
	
	
	
	
	@Inject
	public Graphs() {
	}
	
	
}
