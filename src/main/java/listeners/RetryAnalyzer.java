package listeners;

import lombok.extern.java.Log;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.testng.Reporter;

@Log
public class RetryAnalyzer implements IRetryAnalyzer {

    private int triesCount = 0;
    private static final int NUMBER_OF_TRIES = 30;

    @Override
    public boolean retry(ITestResult iTestResult) {

        log.warning("RETRYANALYZER IS CALLED");

        if(triesCount< NUMBER_OF_TRIES){

            triesCount++;
            return true;
        }
        return false;
    }
}
