package io.mend.demo.service;

import io.mend.demo.rules.routing.RoutingRequest;
import io.mend.demo.rules.routing.RoutingResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class RoutingServiceTest {

    @Autowired
    private RoutingService routingService;

    @Test
    @DisplayName("Test routing for SYS1, VENUS -> Should route to AGG1")
    public void testRoutingSys1Venus() {
        // Arrange
        String reportingSystem = "SYS1";
        String soName = "VENUS";
        String expectedRouting = "AGG1";
        
        // Act
        RoutingRequest request = new RoutingRequest(reportingSystem, soName);
        RoutingResponse response = routingService.processRoutingRequest(request);
        
        // Assert
        assertEquals(expectedRouting, response.getRouting(), 
                "Routing for " + reportingSystem + ", " + soName + " should be " + expectedRouting);
    }

    @Test
    @DisplayName("Test routing for SYS2, MARS -> Should route to AGG2")
    public void testRoutingSys2Mars() {
        // Arrange
        String reportingSystem = "SYS2";
        String soName = "MARS";
        String expectedRouting = "AGG2";
        
        // Act
        RoutingRequest request = new RoutingRequest(reportingSystem, soName);
        RoutingResponse response = routingService.processRoutingRequest(request);
        
        // Assert
        assertEquals(expectedRouting, response.getRouting(), 
                "Routing for " + reportingSystem + ", " + soName + " should be " + expectedRouting);
    }

    @Test
    @DisplayName("Test routing for SYS3, JUP -> Should route to AGG2")
    public void testRoutingSys3Jup() {
        // Arrange
        String reportingSystem = "SYS3";
        String soName = "JUP";
        String expectedRouting = "AGG2";
        
        // Act
        RoutingRequest request = new RoutingRequest(reportingSystem, soName);
        RoutingResponse response = routingService.processRoutingRequest(request);
        
        // Assert
        assertEquals(expectedRouting, response.getRouting(), 
                "Routing for " + reportingSystem + ", " + soName + " should be " + expectedRouting);
    }

    @Test
    @DisplayName("Test routing for unknown combination -> Should route to DEFAULT")
    public void testRoutingUnknown() {
        // Arrange
        String reportingSystem = "UNKNOWN";
        String soName = "UNKNOWN";
        String expectedRouting = "DEFAULT";
        
        // Act
        RoutingRequest request = new RoutingRequest(reportingSystem, soName);
        RoutingResponse response = routingService.processRoutingRequest(request);
        
        // Assert
        assertEquals(expectedRouting, response.getRouting(), 
                "Routing for unknown combination should be " + expectedRouting);
    }
}