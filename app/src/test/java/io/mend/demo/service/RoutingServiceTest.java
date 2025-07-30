package io.mend.demo.service;

import io.mend.demo.rules.routing.RoutingRequest;
import io.mend.demo.rules.routing.RoutingResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class RoutingServiceTest {

	@Autowired
	private RoutingService routingService;

	@ParameterizedTest
	@CsvSource({
			"SYS1, VENUS, S, AGG1",
			"SYS2, MARS, S, AGG2",
			"SYS3, JUP, S, AGG2",
			"DEFAULT, DEFAULT, DEFAULT, DEFAULT"
	})
	@DisplayName("Test routing with various inputs")
	void testRouting(String reportingSystem, String soName, String direction, String expectedRouting) {
		// Arrange

		// Act
		RoutingRequest request = new RoutingRequest(reportingSystem, soName, direction);
		RoutingResponse response = routingService.processRoutingRequest(request);

		// Assert
		RoutingResponse responseExpected = new RoutingResponse(expectedRouting, direction);
		assertEquals(responseExpected, response);
	}
}