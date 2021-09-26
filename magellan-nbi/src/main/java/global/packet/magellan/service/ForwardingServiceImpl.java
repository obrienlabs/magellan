package global.packet.magellan.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class ForwardingServiceImpl implements ForwardingService {

	private String delay;
	private String portTo;
	private String portFrom;
	private AtomicLong counter = new AtomicLong(1);

//  private static final String URL_RETURN = "http://host.docker.internal:8888";
  private static final String URL_RETURN = "http://host.docker.internal:";//8080/nbi/forward/packet";
  private static final String URL_POSTFIX = "/nbi/forward/packet";

  private Runnable runnable = () -> { sendMessage(); };
  
	@Override
	public String forward(String from, String to, String delay) {
		try { Thread.sleep(Long.parseLong(delay)); } catch (Exception e) {};
		portFrom = from;
		portTo = to;
		this.delay = delay;
		Runnable aRunnable = runnable;
		Thread thread = new Thread(aRunnable);
		thread.start();
		return Long.toString(counter.addAndGet(1));
	}

	private void sendMessage() {
		HttpClient httpClient = HttpClient.newBuilder().build();

		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(URL_RETURN + portFrom + URL_POSTFIX + 
						"?from=" + portTo + "&to=" + portFrom + "&delay=" + delay))
				.GET()
				.build();

		try {
			HttpResponse<String> response =
				httpClient.send(request, BodyHandlers.ofString());
			String body = response.body();
			System.out.println("Response: " + body);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}
	}
}
