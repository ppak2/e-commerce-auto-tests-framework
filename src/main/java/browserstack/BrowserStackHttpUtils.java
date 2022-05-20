package browserstack;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.vavr.Tuple2;

import java.util.List;
import java.util.Map;

import static config.BrowserStackConfig.CONFIG;
import static io.restassured.RestAssured.given;

final class BrowserStackHttpUtils {

    private static int MAX_PARALLEL_SESSIONS = 2;
    private static final String QUERY_LIMIT = "?limit="+MAX_PARALLEL_SESSIONS;
    private static final String BASE_URI = "https://api.browserstack.com/automate";
    private static final String BUILDS_PATH = BASE_URI + "/builds.json";
    private static final String BUILD_TEST_SESSIONS = BASE_URI + "/builds/{build-id}.json";
    private static final String SESSION_PATH = BASE_URI + "/builds/{build-id}/sessions/{session-id}.json";
    private static final String SESSION_PATH2 = BASE_URI + "/sessions/{session-id}.json";
    private static final String SESSIONS_PATH = BASE_URI + "/builds/{build-id}/sessions.json";

    static {

        RestAssured.baseURI = BASE_URI;
        RestAssured.authentication = RestAssured.basic(CONFIG.userName(), CONFIG.key());
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

    }

    static List<Map<String, String>> getBuildsFromJson() {
        return given().get(BUILDS_PATH+QUERY_LIMIT).jsonPath().get("automation_build");
    }

    static List<Map<String, String>> getBuildTestSessions(String buildID){
        return given().pathParam("build-id", buildID)
                .when().get(BUILD_TEST_SESSIONS+QUERY_LIMIT).jsonPath()
                .get("build.sessions.automation_session");
    }

    static Tuple2<String, String> testSessionsUrls(String buildID, String testSessionID){
        Response response = given().pathParam("build-id",buildID)
                .pathParam("session-id",testSessionID)
                .when().get(SESSION_PATH);
        String publicUrl = response.jsonPath().getString("automation_session.public_url");
        String clientUrl = response.jsonPath().getString("automation_session.browser_url");
        return new Tuple2<>(publicUrl, clientUrl);
    }

    static List<Map<String, String>> getBuildSessions(String buildID) {
        return given().pathParam("build-id", buildID)
                .when().get(SESSIONS_PATH).jsonPath().get("automation_session");
    }

    static String test(){
        return given().get(BUILDS_PATH+QUERY_LIMIT).body().prettyPrint();
    }

}
