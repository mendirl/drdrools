package io.mend.demo.service;

import io.mend.demo.rules.routing.RoutingRequest;
import io.mend.demo.rules.routing.RoutingResponse;
import org.kie.api.runtime.KieRuntimeBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service for processing routing requests using Drools rules.
 * This service uses the KieContainer provided by our custom DroolsConfig.
 */
@Service
public class RoutingService {

	private final KieRuntimeBuilder kieRuntimeBuilder;

	public RoutingService(KieRuntimeBuilder kieRuntimeBuilder) {
		this.kieRuntimeBuilder = kieRuntimeBuilder;
	}

    /**
     * Process a routing request using Drools rules
     *
     * @param request The routing request containing reportingSystem and soName
     * @return The routing response with the determined routing
     */
    public RoutingResponse processRoutingRequest(RoutingRequest request) {
        // Create a new KieSession for this request
        try (var kieSession = kieRuntimeBuilder.newKieSession()){
            // Insert the request into the session
            kieSession.insert(request);

            // Fire all rules (limit to 1 rule execution)
            kieSession.fireAllRules(1);

            // Collect all RoutingResponse objects from the session
           var responses = new ArrayList<RoutingResponse>();
            kieSession.getObjects(obj -> obj instanceof RoutingResponse)
                    .forEach(obj -> responses.add((RoutingResponse) obj));

            // Get the first response if available
            if (!responses.isEmpty()) {
                return responses.getFirst();
            }

            // Create a default response if no rule matched
            return new RoutingResponse("DEFAULT", "D_DEFAULT_F");
        }
    }

}
