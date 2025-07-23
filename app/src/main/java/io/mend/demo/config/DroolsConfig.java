package io.mend.demo.config;

import org.drools.decisiontable.ExternalSpreadsheetCompiler;
import org.drools.decisiontable.InputType;
import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
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
    private final KieServices kieServices = KieServices.get();

    public DroolsConfig(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Bean
    public KieContainer kieContainer() throws IOException {
        var kieFileSystem = kieServices.newKieFileSystem();

        // Get the decision table CSV file and template file
        var resourcePathCsv = resourceLoader.getResource("classpath:rules/albatoreRouting.csv");
        var resourcePathDrt = resourceLoader.getResource("classpath:rules/albatoreRouting.drt");

        // Compile the template with the CSV data
        var compiler = new ExternalSpreadsheetCompiler();
        var compiled = compiler.compile(
                resourcePathCsv.getInputStream(), resourcePathDrt.getInputStream(),
                InputType.CSV, 2, 1);

        // Write the compiled rules to the KieFileSystem
        kieFileSystem.write("src/main/resources/io/mend/demo/rules/routing/albatoreRouting.drl", compiled);

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
        return kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
    }

    @Bean
    public KieBase kieBase() throws IOException {
        KieBaseConfiguration kieBaseConfig = kieServices.newKieBaseConfiguration();
        kieBaseConfig.setOption(EventProcessingOption.STREAM);
        return kieContainer().newKieBase(kieBaseConfig);
    }
}
