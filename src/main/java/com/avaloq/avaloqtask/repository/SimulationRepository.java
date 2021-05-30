package com.avaloq.avaloqtask.repository;

import com.avaloq.avaloqtask.dto.SimulationResultRecord;
import com.avaloq.avaloqtask.dto.SimulationsAndRollsRecord;
import com.avaloq.avaloqtask.entity.SimulationEntity;
import java.util.List;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SimulationRepository extends CrudRepository<SimulationEntity, Long> {

  @Query( value =
      "SELECT NUMBER_DICE, DICE_SIDES, SUM(TOTAL_ROLLS) as TOTAL_ROLLS, COUNT(TOTAL_ROLLS) as simulations "
      + "FROM SIMULATION "
      + "GROUP BY NUMBER_DICE, DICE_SIDES")
  List<SimulationsAndRollsRecord> getRollsAndSimulations();

  @Query( value = "SELECT SUM(TOTAL_ROLLS) "
      + "FROM SIMULATION "
      + "WHERE NUMBER_DICE = :numberDice AND DICE_SIDES = :diceSides")
  Integer getTotalRolls(int numberDice, int diceSides);

  @Query( value =
        "SELECT SUM_ROLLS, SUM(FREQUENCY) as frequency "
      + "FROM SIMULATION, SIMULATION_RESULT "
      + "WHERE SIMULATION.SIMULATION_ID = SIMULATION_RESULT.SIMULATION_ID AND NUMBER_DICE = :numberDice AND DICE_SIDES = :diceSides "
      + "GROUP BY SUM_ROLLS")
  List<SimulationResultRecord> getSimulationResultByNumberDiceAndDiceSides(
      @Param("numberDice") int numberDice,
      @Param("diceSides") int diceSides);

}
