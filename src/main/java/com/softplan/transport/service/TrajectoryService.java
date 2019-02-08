package com.softplan.transport.service;


import com.softplan.transport.domain.AdjustmentFactor;
import com.softplan.transport.domain.RoadType;
import com.softplan.transport.domain.Trajectory;
import com.softplan.transport.repository.AdjustmentFactorRepository;
import com.softplan.transport.repository.RoadTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;

@Service
@Transactional
public class TrajectoryService{

    private final Logger log = LoggerFactory.getLogger(TrajectoryService.class);

    @Autowired
    private RoadTypeRepository roadTypeRepository;
    @Autowired
    private AdjustmentFactorRepository adjustmentFactorRepository;


    public TrajectoryService(RoadTypeRepository roadTypeRepository, AdjustmentFactorRepository adjustmentFactorRepository) {
        this.roadTypeRepository = roadTypeRepository;
        this.adjustmentFactorRepository = adjustmentFactorRepository;
    }

    public TrajectoryService(){
    }

    //Atribui o custo pelo tipos de rota
    public Double calculateTotalCostTrajectory(Trajectory trajectory){
        Double trajectoryTotalCost = 0.0;
        Iterator<RoadType> roadTypesAsIterator = trajectory.getRoadTypes().iterator();
        while (roadTypesAsIterator.hasNext()){
            RoadType roadTypeIt = roadTypesAsIterator.next();
            if(roadTypeIt.getId() == 1 ) {
                trajectoryTotalCost += (roadTypeIt.getCost() * trajectory.getUnitFirstRoad());
            }
            if(roadTypeIt.getId() == 2 ) {
                trajectoryTotalCost += (roadTypeIt.getCost() * trajectory.getUnitSecondRoad());
            }
        }
        trajectoryTotalCost = trajectoryTotalCost * trajectory.getVehicle().getCost();

        trajectoryTotalCost += calculateTotalAdjustementFator(trajectory);

        return trajectoryTotalCost;
    }
    //Atribui o custo pelo fator de ajuste
    public Double calculateTotalAdjustementFator(Trajectory trajectory){
        Double totalAdjustementFator = 0.0;
        Iterator<AdjustmentFactor> adjustementFactorsAsIterator = trajectory.getAdjustementFactors().iterator();
        while(adjustementFactorsAsIterator.hasNext()) {
            AdjustmentFactor adjustmentFactor = adjustementFactorsAsIterator.next();
            Integer conditionOperator = Integer.parseInt(adjustmentFactor.getConditionOperator());
            if (trajectory.getLoadWeight() > conditionOperator) {
                totalAdjustementFator += (((trajectory.getLoadWeight() - conditionOperator) * adjustmentFactor.getCost())
                    * (trajectory.getUnitFirstRoad() + trajectory.getUnitSecondRoad()));
            }
        }
        return totalAdjustementFator;
    }


}
