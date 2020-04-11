package cucumberOptions;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import resources.Utilities;

@RunWith(Cucumber.class)
@CucumberOptions(strict = true,
        features = "src/test/java/features",
        glue = {"stepDefinitions"},
        plugin = "json:target/jsonReports/cucumber-report.json",
        tags = {"@Test"}
        )

public class TestRunner extends Utilities{
	
	//Setting up the Test File
	
	@BeforeClass
	public static void setup() throws IOException {
		setupTestFile();
	}
}