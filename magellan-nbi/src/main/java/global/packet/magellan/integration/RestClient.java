package global.packet.magellan.integration;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.logging.Logger;

import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.web.client.RestTemplate;

import global.packet.magellan.model.jpa.Record;

public class RestClient {
	
	static Logger logger = Logger.getLogger(RestClient.class.getName());
	
	// my last rollerblade before breaking my shoulder in 2021 after forgetting my flat feet inserts after 30y
    private static final String URL_CREATE_RECORD =
            "http://biometric.elasticbeanstalk.com/FrontController?action=activeid";
    
	public void post() {
        GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
        ctx.load("classpath:META-INF/spring-rest-client.xml");
        ctx.refresh();
        
        //Record record = null;
        RestTemplate restTemplate = ctx.getBean("restTemplate", RestTemplate.class);
        long time = System.currentTimeMillis();
        Record record = restTemplate.getForObject(URL_CREATE_RECORD, Record.class);
        long end = System.currentTimeMillis() - time;
        logger.info(end + "ms Record: " + record.getSendSeq());
        ctx.close();
	}

	public void httpClient() {
		HttpClient httpClient = HttpClient.newBuilder().build();

		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(URL_CREATE_RECORD))
				.GET()
				.build();

		try {
			HttpResponse<String> response =
				httpClient.send(request, BodyHandlers.ofString());
			String body = response.body();
			logger.info("Response: " + body);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}
	}
	
	public static void main(String[] args) {

		RestClient client = new RestClient();
		//client.post();
		client.httpClient();
	}

}
