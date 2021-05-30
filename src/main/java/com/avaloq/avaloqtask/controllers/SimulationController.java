package com.avaloq.avaloqtask.controllers;

import com.avaloq.avaloqtask.dto.DistributionByDiceNumberAndDiceSide;
import com.avaloq.avaloqtask.dto.SimulationResultRecord;
import com.avaloq.avaloqtask.dto.SimulationsAndRollsRecord;
import com.avaloq.avaloqtask.exception.InputParameterException;
import com.avaloq.avaloqtask.exception.NoDataException;
import com.avaloq.avaloqtask.exception.NoDistributionException;
import com.avaloq.avaloqtask.service.SimulationService;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class SimulationController {

  @Autowired
  private SimulationService simulationService;

  @GetMapping("/simulation")
  public ResponseEntity<Set<SimulationResultRecord>> simulation(
      @RequestParam(value="diceSides") int diceSides,
      @RequestParam(value = "totalRolls") int totalRolls,
      @RequestParam(value = "numberDice") int numberDice) {

    try {
      validateParams(diceSides, numberDice, totalRolls);
      return ResponseEntity.ok(simulationService.runSimulation(numberDice, diceSides, totalRolls));
    } catch (InputParameterException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong input parameter", e);
    }
  }


  @GetMapping("/getSimulationsAndRolls")
  public ResponseEntity<List<SimulationsAndRollsRecord>> getSimulationsAndRolls() {

    try {
      return ResponseEntity.ok(simulationService.getTotalRollsForEveryDiceNumberAndDiceSide());
    } catch (NoDataException e) {
      throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No simulation data", e);
    }
  }

  @GetMapping("/getDistribution")
  public ResponseEntity<DistributionByDiceNumberAndDiceSide> distribution(
      @RequestParam(value="diceSides") int diceSides,
      @RequestParam(value = "numberDice") int numberDice) {

    try {
      validateParams(diceSides, numberDice);
      DistributionByDiceNumberAndDiceSide distribution = simulationService.getDistribution(numberDice, diceSides);
      return ResponseEntity.ok(distribution);

    } catch (InputParameterException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong input parameter", e);
    } catch (NoDistributionException e) {
      throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No distribution", e);
    }
  }

  private void validateParams(int diceSides, int numberDice) throws InputParameterException {
    if (diceSides < 4)
      throw new InputParameterException("Sides of dice should be 4 or greater");
    if (numberDice < 1)
      throw new InputParameterException("Number of dice should be 1 or greater");
  }

  private void validateParams(int diceSides,  int numberDice, int totalRolls) throws InputParameterException {
    validateParams(diceSides, numberDice);
    if (totalRolls < 1)
      throw new InputParameterException("Total numbers of rolls should be 1 or greater");
  }
}