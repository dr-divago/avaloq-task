package com.avaloq.avaloqtask.repository;

import com.avaloq.avaloqtask.dto.SimulationResultRecord;
import com.avaloq.avaloqtask.dto.SimulationsAndRollsRecord;
import com.avaloq.avaloqtask.entity.RollEntity;
import com.avaloq.avaloqtask.entity.SimulationEntity;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
class SimulationRepositoryTest {
  @Autowired
  SimulationRepository simulationRepository;

  @Test
  @Sql(scripts = "/data.sql")
  void getRollsAndSimulationsTest() {
    prepare();
    List<SimulationsAndRollsRecord> simulationsAndRollsRecords = simulationRepository.getRollsAndSimulations();
    simulationsAndRollsRecords.forEach(s -> Assertions.assertEquals(8, s.totalRolls(), "It should be 8 rolls"));
  }

  @Test
  @Sql(scripts = "/data.sql")
  void getRollsAndSimulationsNullTest() {
    List<SimulationsAndRollsRecord> simulationsAndRollsRecords = simulationRepository.getRollsAndSimulations();
    Assertions.assertTrue(simulationsAndRollsRecords.isEmpty(), "it should be empty");
  }

  @Test
  @Sql(scripts = "/data.sql")
  void getTotalRolls() {
    prepare1();
    int totalRolls = simulationRepository.getTotalRolls(3, 6);
    Assertions.assertEquals(7, totalRolls);
  }

  @Test
  @Sql(scripts = "/data.sql")
  void getTotalRollsNull() {
    Integer totalRolls = simulationRepository.getTotalRolls(3, 6);
    Assertions.assertNull(totalRolls);
  }

  @Test
  @Sql(scripts = "/data.sql")
  void getSimulationResultByNumberDiceAndDiceSides() {
    prepare1();
    List<SimulationResultRecord> simulationResultRecords = simulationRepository.getSimulationResultByNumberDiceAndDiceSides(3, 6);

    Assertions.assertEquals(2, simulationResultRecords.size());
    SimulationResultRecord simulationResultRecord = simulationResultRecords.get(0);

    Assertions.assertEquals(3, simulationResultRecord.sumRolls() );
    Assertions.assertEquals(2, simulationResultRecord.frequency());

    SimulationResultRecord simulationResultRecord1 = simulationResultRecords.get(1);

    Assertions.assertEquals(4, simulationResultRecord1.sumRolls());
    Assertions.assertEquals(4, simulationResultRecord1.frequency());
  }

  @Test
  @Sql(scripts = "/data.sql")
  void getSimulationResultByNumberDiceAndDiceSidesNull() {
    List<SimulationResultRecord> simulationResultRecords = simulationRepository.getSimulationResultByNumberDiceAndDiceSides(3, 6);
    Assertions.assertTrue(simulationResultRecords.isEmpty());
  }

  private void prepare() {
    SimulationEntity simulationEntity1 = buildSimulationEntity(3, 6, 3);
    Set<RollEntity> s1 = new HashSet<>();
    buildRoll(s1, 3, 1);
    buildRoll(s1, 4, 2);
    simulationEntity1.setRollEntitySet(s1);

    SimulationEntity simulationEntity2 = new SimulationEntity(3, 6, 5);
    Set<RollEntity> s2 = new HashSet<>();
    buildRoll(s2, 3, 2);
    buildRoll(s2, 7, 3);
    simulationEntity2.setRollEntitySet(s2);

    simulationRepository.save(simulationEntity1);
    simulationRepository.save(simulationEntity2);
  }

  private void prepare1() {
    SimulationEntity simulationEntity1 = buildSimulationEntity(3, 6, 3);
    Set<RollEntity> s1 = new HashSet<>();
    buildRoll(s1, 3, 1);
    buildRoll(s1, 4, 2);
    simulationEntity1.setRollEntitySet(s1);

    SimulationEntity simulationEntity2 = buildSimulationEntity(3, 6, 4);
    Set<RollEntity> s2 = new HashSet<>();
    buildRoll(s2, 3, 1);
    buildRoll(s2, 4, 2);
    simulationEntity2.setRollEntitySet(s2);

    SimulationEntity simulationEntity3 = new SimulationEntity(4, 6, 5);
    Set<RollEntity> s3 = new HashSet<>();
    buildRoll(s3, 5, 2);
    buildRoll(s3, 7, 3);
    simulationEntity3.setRollEntitySet(s3);

    simulationRepository.save(simulationEntity1);
    simulationRepository.save(simulationEntity2);
    simulationRepository.save(simulationEntity3);
  }

  private SimulationEntity buildSimulationEntity(int numberDice, int diceSides, int totalRolls) {
    return new SimulationEntity(numberDice, diceSides, totalRolls);
  }

  private void buildRoll(Set<RollEntity> s, int sumRolls, int freq) {
    s.add(new RollEntity(sumRolls, freq));
  }
}
