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
            "SYS1, VENUS, AGG1",
            "SYS2, MARS, AGG2",
            "SYS3, JUP, AGG2",
            "UNKNOWN, UNKNOWN, DEFAULT"
    })
    @DisplayName("Test routing with various inputs")
    void testRouting(String reportingSystem, String soName, String expectedRouting) {
        // Arrange
        
        // Act
        RoutingRequest request = new RoutingRequest(reportingSystem, soName);
        RoutingResponse response = routingService.processRoutingRequest(request);
        
        // Assert
        assertEquals(
                expectedRouting,
                response.getRouting(),
                "Routing for " + reportingSystem + ", " + soName + " should be " + expectedRouting);
    }
}