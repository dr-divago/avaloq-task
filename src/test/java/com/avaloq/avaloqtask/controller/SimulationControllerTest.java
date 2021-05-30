package com.avaloq.avaloqtask.controller;


import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
class SimulationControllerTest {

	@Autowired
	private MockMvc mvc;


	@Test
	@Order(1)
	void when_getSimulationAndRolls_with_no_simulation_then_204() throws Exception {
		mvc.perform(
				MockMvcRequestBuilders.get("/getSimulationsAndRolls"))
				.andExpect(status().is2xxSuccessful());
	}

	@Test
	@Order(2)
	void when_getDistribution_with_no_simulation_then_204() throws Exception {
		mvc.perform(
				MockMvcRequestBuilders.get("/getDistribution")
				.param("numberDice", "3")
				.param("diceSides", "6"))
				.andExpect(status().is2xxSuccessful());
	}

	@Test
	@Order(3)
	void when_simulation_with_parameters_then_ok() throws Exception {
    mvc.perform(
            MockMvcRequestBuilders.get("/simulation")
                .param("diceSides", "6")
                .param("totalRolls", "1")
                .param("numberDice", "3"))
        .andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].sumRolls", greaterThan(3)))
				.andExpect(jsonPath("$[0].sumRolls", lessThan(18)))
				.andExpect(jsonPath("$[0].frequency", equalTo(1)));
	}

	@Test
	@Order(4)
	void when_getSimulationAndRolls_with_one_simulation_then_200() throws Exception {

		mvc.perform(
				MockMvcRequestBuilders.get("/getSimulationsAndRolls"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].numberDice", is(3)))
				.andExpect(jsonPath("$[0].diceSides", is(6)))
				.andExpect(jsonPath("$[0].totalRolls", is(1)));
	}

	@Test
	@Order(5)
	void when_getDistribution_with_two_simulation_then_200() throws Exception {

		mvc.perform(
				MockMvcRequestBuilders.get("/simulation")
						.param("diceSides", "6")
						.param("totalRolls", "1")
						.param("numberDice", "4"));

		mvc.perform(
				MockMvcRequestBuilders.get("/getDistribution")
						.param("numberDice", "3")
						.param("diceSides", "6"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.numberDice", is(3)))
				.andExpect(jsonPath("$.diceSides", is(6)))
				.andExpect(jsonPath("$.totalRolls", is(1)))
				.andExpect(jsonPath("$.distributionRecord[0].distribution", is(100.0)));
	}

	@Test
	@Order(6)
	void when_getSimulationAndRolls_with_two_simulation_then_200() throws Exception {

		mvc.perform(
				MockMvcRequestBuilders.get("/simulation")
						.param("diceSides", "6")
						.param("totalRolls", "1")
						.param("numberDice", "3"));

		mvc.perform(
				MockMvcRequestBuilders.get("/getSimulationsAndRolls"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].numberDice", is(3)))
				.andExpect(jsonPath("$[0].diceSides", is(6)))
				.andExpect(jsonPath("$[0].totalRolls", is(2)));
	}

	@Test
	void when_simulation_with_wrong_diceSides_parameter_then_error() throws Exception {
		mvc.perform(
				MockMvcRequestBuilders.get("/simulation")
						.param("diceSides", "3")
						.param("totalRolls", "1")
						.param("numberDice", "3"))
				.andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
	}

	@Test
	void when_simulation_with_wrong_totalRolls_parameter_then_error() throws Exception {
		mvc.perform(
				MockMvcRequestBuilders.get("/simulation")
						.param("diceSides", "4")
						.param("totalRolls", "0")
						.param("numberDice", "3"))
				.andDo(print())
				.andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
	}

	@Test
	void when_simulation_with_wrong_numberDice_parameter_then_error() throws Exception {
		mvc.perform(
				MockMvcRequestBuilders.get("/simulation")
						.param("diceSides", "4")
						.param("totalRolls", "1")
						.param("numberDice", "0"))
				.andDo(print())
				.andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
	}
}
