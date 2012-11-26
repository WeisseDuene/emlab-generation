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
package emlab.role.operating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import agentspring.role.Role;
import agentspring.role.RoleComponent;
import emlab.domain.agent.EnergyProducer;
import emlab.domain.agent.Government;
import emlab.domain.agent.NationalGovernment;
import emlab.domain.contract.CashFlow;
import emlab.domain.technology.PowerPlant;
import emlab.repository.Reps;
import emlab.role.AbstractEnergyProducerRole;

/**
 * {@link EnergyProducer}s pay CO2 taxes to the {@link Government}.
 * 
 * @author <a href="mailto:A.Chmieliauskas@tudelft.nl">Alfredas Chmieliauskas</a> @author <a href="mailto:E.J.L.Chappin@tudelft.nl">Emile Chappin</a>
 */
@RoleComponent
public class PayCO2AuctionRole extends AbstractEnergyProducerRole implements Role<EnergyProducer> {

    @Autowired
    Reps reps;

    public Reps getReps() {
        return reps;
    }

    @Transactional
    public void act(EnergyProducer producer) {
        logger.info("Pay for the CO2 credits");

        Government government = reps.genericRepository.findFirst(Government.class);

        for (PowerPlant plant : reps.powerPlantRepository.findOperationalPowerPlantsByOwner(producer, getCurrentTick())) {
            double money = calculateCO2MarketCost(plant);
            CashFlow cf = reps.nonTransactionalCreateRepository.createCashFlow(producer, government, money, CashFlow.CO2AUCTION,
                    getCurrentTick(), plant);
            logger.info("Cash flow created: {}", cf);
            double minCO2Money = calculatePaymentEffictiveCO2NationalMinimumPriceCost(plant);
            NationalGovernment nationalGovernment = reps.nationalGovernmentRepository.findNationalGovernmentByPowerPlant(plant);
            CashFlow cf2 = reps.nonTransactionalCreateRepository.createCashFlow(producer, nationalGovernment, minCO2Money,
                    CashFlow.NATIONALMINCO2, getCurrentTick(), plant);
            logger.info("Cash flow created: {}", cf2);
        }

        // for (PowerPlantDispatchPlan plan : reps.powerPlantDispatchPlanRepository
        // .findAllAcceptedPowerPlantDispatchPlansForEnergyProducerForTime(producer, getCurrentTick())) {
        // double money = plan.getPrice() - plan.getBidWithoutCO2();
        // CashFlow cf = reps.nonTransactionalCreateRepository.createCashFlow(producer, government, money, CashFlow.CO2AUCTION,
        // getCurrentTick(), plan.getPowerPlant());
        // }
    }
}
