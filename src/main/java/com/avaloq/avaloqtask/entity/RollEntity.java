package com.avaloq.avaloqtask.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("SIMULATION_RESULT")
public class RollEntity {

  @Id
  private Long simulationResultId;
  private final int sumRolls;
  private final int frequency;

  public RollEntity(int sumRolls, int frequency) {
    this.sumRolls = sumRolls;
    this.frequency = frequency;
  }

}
