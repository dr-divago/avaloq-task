package com.avaloq.avaloqtask.service;

import com.avaloq.avaloqtask.dto.DistributionByDiceNumberAndDiceSide;
import com.avaloq.avaloqtask.dto.DistributionRecord;
import com.avaloq.avaloqtask.dto.SimulationResultRecord;
import com.avaloq.avaloqtask.dto.SimulationsAndRollsRecord;
import com.avaloq.avaloqtask.entity.RollEntity;
import com.avaloq.avaloqtask.exception.NoDataException;
import com.avaloq.avaloqtask.exception.NoDistributionException;
import com.avaloq.avaloqtask.entity.SimulationEntity;
import com.avaloq.avaloqtask.repository.SimulationRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SimulationService {

  private SimulationRepository simulationRepository;

  @Autowired
  public SimulationService(final SimulationRepository simulationRepository) {
    this.simulationRepository = simulationRepository;
  }

  public List<SimulationsAndRollsRecord> getTotalRollsForEveryDiceNumberAndDiceSide()
      throws NoDataException {
    List<SimulationsAndRollsRecord> simulationsAndRollsRecords = simulationRepository.getRollsAndSimulations();

    if (simulationsAndRollsRecords.isEmpty())
      throw new NoDataException();
    return simulationRepository.getRollsAndSimulations();
  }

  public Set<SimulationResultRecord> runSimulation(int diceNumber, int diceSides, int totalRolls) {

    var simulationEngine = new SimulationEngine(totalRolls, diceNumber, diceSides);
    Set<SimulationResultRecord> simulationResultRecords = simulationEngine.start();

    Set<RollEntity> simulationResultEntities = mapToEntity(simulationResultRecords);
    var simulationEntity = new SimulationEntity(diceNumber, diceSides, totalRolls);
    simulationEntity.setRollEntitySet(simulationResultEntities);
    simulationRepository.save(simulationEntity);

    return simulationResultRecords;
  }

  private Set<RollEntity> mapToEntity(Set<SimulationResultRecord> simulationResultRecords) {
    return simulationResultRecords
        .stream()
        .map( f -> new RollEntity(f.sumRolls(), f.frequency()))
        .collect(Collectors.toSet());
  }

  public DistributionByDiceNumberAndDiceSide getDistribution(int numberDice, int diceSides)
      throws NoDistributionException {

    Integer totalRolls = simulationRepository.getTotalRolls(numberDice, diceSides);
    if (totalRolls == null) {
      throw new NoDistributionException();
    }

    List<SimulationResultRecord> simulationResultRecords =  simulationRepository.getSimulationResultByNumberDiceAndDiceSides(numberDice, diceSides);
    List<DistributionRecord> distributionRecords = simulationResultRecords
        .stream()
        .map( r -> new DistributionRecord(r.sumRolls(), ((double)r.frequency()/(double)totalRolls)*100))
        .collect(Collectors.toList());

    return new DistributionByDiceNumberAndDiceSide(
        numberDice,
        diceSides,
        totalRolls,
        distributionRecords);
  }
}
