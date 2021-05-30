package com.avaloq.avaloqtask.service;

import com.avaloq.avaloqtask.dto.SimulationResultRecord;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;

public class SimulationEngine {

  private final Random random = new Random();
  private final Map<Integer, Integer> s = new HashMap<>();
  private final int totalRolls;
  private final int diceNumber;
  private final int diceSides;

  public SimulationEngine(int totalRolls, int diceNumber, int diceSides) {
    this.totalRolls = totalRolls;
    this.diceNumber = diceNumber;
    this.diceSides = diceSides;
  }

  public Set<SimulationResultRecord> start() {
    IntStream
        .range(0, totalRolls)
        .map(i -> runSimulation(diceNumber, diceSides))
        .forEach( sum -> s.put(sum, s.getOrDefault(sum, 0)+1));

    Set<SimulationResultRecord> simulationResults = new HashSet<>();
    s.forEach( (k, v) -> simulationResults.add(new SimulationResultRecord(k, v)));

    return simulationResults;
  }

  /**
   * Roll one dice with diceSides sides diceNumber of times, can be parallelized (every roll is independent)
   * if diceNumber is high
   * @param diceNumber
   * @param diceSides
   * @return
   */
  private int runSimulation(int diceNumber, int diceSides) {
    return IntStream
        .range(0, diceNumber)
        .map( i-> rollDice(diceSides))
        .reduce(0, Integer::sum);
  }


  /**
   * Roll a dice with diceSides number of sides
   * @param diceSides sides of a dice
   * @return return a random number from 1 to diceSides
   */
  private int rollDice(int diceSides) {
    return random.ints(1, diceSides+1).findFirst().getAsInt();
  }
}
