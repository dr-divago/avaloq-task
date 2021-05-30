package com.avaloq.avaloqtask.entity;

import java.util.Set;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("SIMULATION")
public class SimulationEntity {
  @Id
  private long simulationId;
  private int numberDice;
  private int diceSides;
  private int totalRolls;

  @MappedCollection(keyColumn = "SIMULATION_ID", idColumn = "SIMULATION_ID")
  private Set<RollEntity> rollEntitySet;

  public SimulationEntity(int numberDice, int diceSides,  int totalRolls) {
    this.numberDice = numberDice;
    this.diceSides = diceSides;
    this.totalRolls = totalRolls;
  }
}
