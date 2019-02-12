package com.softplan.transport.service;

import com.softplan.transport.SoftplanApp;
import com.softplan.transport.domain.AdjustmentFactor;
import com.softplan.transport.domain.RoadType;
import com.softplan.transport.domain.Trajectory;
import com.softplan.transport.domain.Vehicle;
import com.softplan.transport.repository.TrajectoryRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SoftplanApp.class)
@Transactional
public class TrajectoryServiceIntTest {

    @Autowired
    private TrajectoryRepository trajectoryRepository;

    @Autowired
    private TrajectoryService trajectoryService;
    private Trajectory trajectory;
    private RoadType roadTypePavimentada;
    private RoadType roadTypeNPavimentada;
    private Vehicle vehicle;


    @Before
    public void init() {

        trajectory = new Trajectory();
        trajectory.setName("Calculo de Custo Teste");
        trajectory.setLoadWeight(4);
        trajectory.setUnitFirstRoad(0.0);
        trajectory.setUnitSecondRoad(60.0);

        Set<RoadType> roadTypes = new HashSet<RoadType>();
        roadTypePavimentada = new RoadType();
        roadTypePavimentada.setId(1L);
        roadTypePavimentada.setName("Pavimentada");
        roadTypePavimentada.setCost(0.54);
        roadTypePavimentada.setUnit("km");
        roadTypes.add(roadTypePavimentada);

        roadTypeNPavimentada = new RoadType();
        roadTypeNPavimentada = new RoadType();
        roadTypeNPavimentada.setId(2L);
        roadTypeNPavimentada.setName("Não-pavimentada");
        roadTypeNPavimentada.setCost(0.62);
        roadTypeNPavimentada.setUnit("km");
        roadTypes.add(roadTypeNPavimentada);

        trajectory.setRoadTypes(roadTypes);



        Set<AdjustmentFactor> setAdjustmentFactorSet = new HashSet<AdjustmentFactor>();
        AdjustmentFactor adjustmentFactor = new AdjustmentFactor();
        adjustmentFactor.setName("Fator de ajuste para carga maior que cinco toneladas");
        adjustmentFactor.setCondition("Aplicar a cada tonelada excedente a 5 toneladas");
        adjustmentFactor.setConditionOperator("5");

        vehicle = new Vehicle();
        vehicle.setCost(1.00);
        vehicle.setName("Caminhão baú");
        trajectory.setVehicle(vehicle);
        trajectory.setAdjustementFactors(setAdjustmentFactorSet);


    }
    @Test
    @Transactional
    public void createTrajectory(){
        System.out.println("Calculo da trajetória: " + trajectoryService.calculateTotalCostTrajectory(trajectory));
        assertThat(trajectoryService.calculateTotalCostTrajectory(trajectory)).isEqualTo(37.20);

        }

    }
