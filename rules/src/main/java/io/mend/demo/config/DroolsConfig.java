package io.mend.demo.config;

import org.drools.decisiontable.ExternalSpreadsheetCompiler;
import org.drools.decisiontable.InputType;
import org.drools.decisiontable.SpreadsheetCompiler;
import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.runtime.KieContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;

/**
 * Custom Drools configuration for template-based rules.
 */
@Configuration
public class DroolsConfig {

	private final ResourceLoader resourceLoader;
	private final KieServices    kieServices = KieServices.get();

	public DroolsConfig(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	@Bean
	public KieContainer kieContainer() throws IOException {
		var kieFileSystem = kieServices.newKieFileSystem();

		var drlcsvCompiled = createFromDrlCsv();
		kieFileSystem.write("src/main/resources/io/mend/demo/rules/routing/appRouting.drl", drlcsvCompiled);

		// Build the KieModule
		KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
		kieBuilder.buildAll();

		// Check for errors
		Results results = kieBuilder.getResults();
		if (results.hasMessages(Message.Level.ERROR)) {
			System.out.println("Build Errors:\n" + results.toString());
			throw new RuntimeException("Build Errors:\n" + results.toString());
		} else {
			System.out.println("Rules successfully loaded");
		}

		// Create and return the KieContainer
		return kieServices.newKieContainer(kieServices.getRepository()
													  .getDefaultReleaseId());
	}

	private String createFromDrtCsv() throws IOException {
		// Get the decision table CSV file and template file
		var resourcePathDrt = resourceLoader.getResource("classpath:io/mend/demo/rules/routingalt/appRouting.drt");
		var resourcePathCsv = resourceLoader.getResource("classpath:io/mend/demo/rules/routingalt/appRouting.csv");
		var spreadsheetCompiler = new ExternalSpreadsheetCompiler();
		return spreadsheetCompiler.compile(
				resourcePathCsv.getInputStream(),
				resourcePathDrt.getInputStream(),
				InputType.CSV,
				2,
				1);
	}

	private String createFromDrlCsv() throws IOException {
		// Get the decision tablea and rule in DRL.CSV
		var resourcePathDrl = resourceLoader.getResource("classpath:io/mend/demo/rules/routing/appRouting.drl.csv");
		var spreadsheetCompiler = new SpreadsheetCompiler();
		return spreadsheetCompiler.compile(resourcePathDrl.getInputStream(), InputType.CSV);
	}

	@Bean
	public KieBase kieBase() throws IOException {
		KieBaseConfiguration kieBaseConfig = kieServices.newKieBaseConfiguration();
		kieBaseConfig.setOption(EventProcessingOption.STREAM);
		return kieContainer().newKieBase(kieBaseConfig);
	}

}
