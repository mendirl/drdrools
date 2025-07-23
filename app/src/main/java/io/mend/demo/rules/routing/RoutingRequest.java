package io.mend.demo.rules.routing;

public class RoutingRequest {

	private String reportingSystem;
	private String soName;

	public RoutingRequest() {
	}

	public RoutingRequest(String reportingSystem, String soName) {
		this.reportingSystem = reportingSystem;
		this.soName = soName;
	}

	public String getReportingSystem() {
		return reportingSystem;
	}

	public void setReportingSystem(String reportingSystem) {
		this.reportingSystem = reportingSystem;
	}

	public String getSoName() {
		return soName;
	}

	public void setSoName(String soName) {
		this.soName = soName;
	}
}
