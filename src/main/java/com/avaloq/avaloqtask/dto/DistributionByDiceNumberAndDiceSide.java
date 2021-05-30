package com.avaloq.avaloqtask.dto;

import java.util.List;

public record DistributionByDiceNumberAndDiceSide(int numberDice, int diceSides, int totalRolls, List<DistributionRecord> distributionRecord) {}
