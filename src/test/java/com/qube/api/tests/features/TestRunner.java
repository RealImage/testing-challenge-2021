package com.qube.api.tests.features;

import com.intuit.karate.KarateOptions;
import com.intuit.karate.cucumber.CucumberRunner;
import com.intuit.karate.cucumber.KarateStats;
import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TimeZone;

import static org.junit.Assert.assertTrue;
// Using karate Options, We can add/remove any feature files and also specify the order in which the tests will be executed
@KarateOptions(tags = {"~@ignore"},
        features = {
                "src/test/java/com/qube/api/tests/features/testscenarios/upload.feature",
                "src/test/java/com/qube/api/tests/features/testscenarios/get-upload-details.feature",
                "src/test/java/com/qube/api/tests/features/testscenarios/get-all-files-from-account.feature",
                "src/test/java/com/qube/api/tests/features/testscenarios/delete-file.feature",
                "src/test/java/com/qube/api/tests/features/testscenarios/file-share.feature",
                "src/test/java/com/qube/api/tests/features/testscenarios/accept-shared-file.feature",
                "src/test/java/com/qube/api/tests/features/testscenarios/reject-shared-file.feature"
        })

public class TestRunner {
   // This Method is used to generate the Cucumber Report
    private void generateReport(String karateOutputPath) throws Exception {
        Collection<File> jsonFiles = FileUtils.listFiles(new File(karateOutputPath), new String[]{"json"}, true);
        List<String> jsonPaths = new ArrayList<String>(jsonFiles.size());
        for (File file : jsonFiles) {
            jsonPaths.add(file.getAbsolutePath());
        }
        Configuration config = new Configuration(new File("target"), "Rest-API Automation Suite");
        ReportBuilder reportBuilder = new ReportBuilder(jsonPaths, config);
        reportBuilder.generateReports();
    }

    @Test
    public void testAllFeatures() throws Exception {
        TimeZone.setDefault(TimeZone.getTimeZone("IST"));
        String karateOutputPath = "target/surefire-reports";
        KarateStats stats = CucumberRunner.parallel(getClass(), 1, karateOutputPath);
        generateReport(karateOutputPath);
        assertTrue("There are scenario failures", stats.getFailCount() == 0);
    }

}

