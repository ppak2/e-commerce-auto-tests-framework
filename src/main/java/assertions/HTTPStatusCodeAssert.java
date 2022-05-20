package assertions;

import org.assertj.core.api.AbstractAssert;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class HTTPStatusCodeAssert extends AbstractAssert<HTTPStatusCodeAssert, String> {

    private String url;

    private HTTPStatusCodeAssert(String url){

        super(url, HTTPStatusCodeAssert.class);
        this.url = url;
    }

    public static HTTPStatusCodeAssert assertThat(String url) { return new HTTPStatusCodeAssert(url); }

    public static HTTPStatusCodeAssert assertThatResponse(String url) { return new HTTPStatusCodeAssert(url); }

    public HTTPStatusCodeAssert ofGETMethodIsOK(){

        isNotNull();

        if(getHTTPStatusCode(url,"GET") !=200){
            failWithMessage("Response status code for <%s> is NOT OK", hostValue(url));
        }
        return this;
    }

    public HTTPStatusCodeAssert ofPOSTMethodIsOK(){

        isNotNull();

        if(getHTTPStatusCode(url,"POST") !=200){
            failWithMessage("Response status code for <%s> is NOT OK", hostValue(url));
        }
        return this;
    }

    //main retrieving method
    private Integer getHTTPStatusCode(String url, String httpMethod){

        HttpURLConnection.setFollowRedirects(false);

        try {

            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod(httpMethod);
            return connection.getResponseCode();
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    //get host
    private String hostValue(String url){

        try{

            return new URL(url).getHost();
        }
        catch (MalformedURLException e){
            e.printStackTrace();
        }
        return "Empty host value";
    }
}
