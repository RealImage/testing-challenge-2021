package com.qube.api.tests.features;
import com.intuit.karate.KarateOptions;
import com.intuit.karate.Results;
import com.intuit.karate.Runner;
import static org.junit.Assert.*;
import org.junit.Test;

@KarateOptions(tags = {"~@ignore"})
public class ParallelTestRunner {

    @Test
    // Method to run the features in Parallel
    public void testParallel() {
        Results results = Runner.parallel(getClass(), 5, "target/surefire-reports");
        assertTrue(results.getErrorMessages(), results.getFailCount() == 0);
    }

}