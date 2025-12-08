package com.etfinder.mvc.simulation.service;

import com.etfinder.mvc.simulation.dto.SimulationRequest;
import com.etfinder.mvc.simulation.dto.SimulationResponse;

public interface SimulationService {

	//수익률 시뮬레이션 
	SimulationResponse simulate(Long userId, SimulationRequest request);
}
