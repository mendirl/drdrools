package io.mend.demo.rules.routing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoutingRequest {

	private String reportingSystem;
	private String soName;
	//	private String direction;

}
