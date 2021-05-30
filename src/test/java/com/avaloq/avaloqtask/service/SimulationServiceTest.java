package com.avaloq.avaloqtask.service;

import static org.mockito.Mockito.when;

import com.avaloq.avaloqtask.dto.SimulationResultRecord;
import com.avaloq.avaloqtask.dto.SimulationsAndRollsRecord;
import com.avaloq.avaloqtask.entity.SimulationEntity;
import com.avaloq.avaloqtask.exception.NoDataException;
import com.avaloq.avaloqtask.repository.SimulationRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

@SpringBootTest
class SimulationServiceTest {

  @Mock
  SimulationRepository simulationRepository;

  @InjectMocks
  SimulationService simulationService;

  @Test
  void when_run_simulation_then_should_return_simulation() {
    when(simulationRepository.save(ArgumentMatchers.any(SimulationEntity.class))).thenReturn(new SimulationEntity(3,6,1));
    Set<SimulationResultRecord> resultRecords = simulationService.runSimulation(3, 6, 1);
    for (SimulationResultRecord r : resultRecords) {
      Assertions.assertTrue(r.sumRolls() >= 3 && r.sumRolls() <= 18);
      Assertions.assertEquals(1, r.frequency());
    }
  }

  @Test
  void when_getTotalRollsForEveryDiceNumberAndDiceSide_with_empty_db_then_throw_exception() {
    when(simulationRepository.getRollsAndSimulations()).thenReturn(new ArrayList<>());
    Assertions.assertThrows(NoDataException.class, () -> simulationService.getTotalRollsForEveryDiceNumberAndDiceSide());
  }

  @Test
  void when_getTotalRollsForEveryDiceNumberAndDiceSide_then_ok() throws NoDataException {
    List<SimulationsAndRollsRecord> r = new ArrayList<>();
    r.add(new SimulationsAndRollsRecord(3, 6, 1, 1));
    when(simulationRepository.getRollsAndSimulations()).thenReturn(r);
    Assertions.assertIterableEquals(r,  simulationService.getTotalRollsForEveryDiceNumberAndDiceSide());
  }
}
