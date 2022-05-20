package browserstack;

import io.qameta.allure.Attachment;
import io.vavr.Tuple2;
import java.util.*;


public class TestRun implements IBrowserStack {


    private static final Map<String, String> BUILDS = Collections.synchronizedMap(new LinkedHashMap<>());
    private static final Map<String, String> SESSIONS = Collections.synchronizedMap(new LinkedHashMap<>());


    public TestRun(){}

    public static synchronized void updateTestRunStatus(String buildName){

        if (BUILDS.containsKey(buildName)){
            updateBuildTestSessions(BUILDS.get(buildName));
        }
        else {
            BrowserStackHttpUtils.getBuildsFromJson()
                    .stream().map(Build::new)
                    .forEach(build -> {
                        String key = build.getName();
                        String value = build.getId();
                        BUILDS.put(key,value);
                    });
            updateBuildTestSessions(BUILDS.get(buildName));
        }
    }

    public static synchronized Tuple2<String, String> getTestSessionsUrls(String buildName, String testName){
        return BrowserStackHttpUtils.testSessionsUrls(BUILDS.get(buildName), SESSIONS.get(testName));
    }

    @Attachment(value = "Test session urls")
    @SuppressWarnings("unused")
    public static String getTestSessionUrls(Tuple2<String, String> urls){
        StringJoiner sj = new StringJoiner("\n");
        sj.add("Public url: "+urls._1);
        sj.add("Client url: "+urls._2);
        return sj.toString();
    }

    private static synchronized void updateBuildTestSessions(String buildID){

        BrowserStackHttpUtils.getBuildTestSessions(buildID)
                .stream().map(Session::new).forEach(testSession -> {
                    String key = testSession.getName();
                    String value = testSession.getId();
                    SESSIONS.putIfAbsent(key, value);
        });
    }

    @Override
    public Object getBuilds() {
        return null;
    }

}
