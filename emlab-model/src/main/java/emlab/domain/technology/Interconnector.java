/*******************************************************************************
 * Copyright 2012 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package emlab.domain.technology;

import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import agentspring.simulation.SimulationParameter;

@NodeEntity
public class Interconnector {

    @RelatedTo(type = "INTERCONNECTIONS", elementClass = PowerGridNode.class, direction = Direction.OUTGOING)
    // TODO: Limit the set to the size of two.
    private Set<PowerGridNode> connections;

    @SimulationParameter(description = "Interconnector capacity MW", label = "Interconnector capacity MW", from = 0, to = 300000, step = 10000)
    private double capacity;

    public Set<PowerGridNode> getConnections() {
        return connections;
    }

    public void setConnections(Set<PowerGridNode> connections) {
        this.connections = connections;
    }

    public double getCapacity() {
        return capacity;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

}
