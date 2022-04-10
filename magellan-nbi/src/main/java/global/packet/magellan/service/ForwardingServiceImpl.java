package global.packet.magellan.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * 20210926
 * Stats
 * - 2 3610 MacMini on 1g - 2 vCores sat, 65/sec
 * @author michaelobrien
 *
 */
@Service
public class ForwardingServiceImpl implements ForwardingService {

	private String dnsFrom;
	private String dnsTo;
	private String delay;
	private String portTo;
	private String portFrom;
	private AtomicLong counter = new AtomicLong(1);
	private HttpClient httpClient = HttpClient.newBuilder().build();
	//private static boolean stopForwarding = false;
	private Thread thread;
	private AtomicBoolean running = new AtomicBoolean(false);
	private AtomicBoolean stopped = new AtomicBoolean(true);	

//  private static final String URL_RETURN = "http://host.docker.internal:8888";
//  private static final String URL_RETURN = "http://host.docker.internal:";//8080/nbi/forward/packet";
	private static final String URL_POSTFIX = "/nbi/forward/packet";

	private Runnable runnable = () -> { sendMessage(); };
  
  
	@Override
	public String reset() {
		String ret = "";
		if(running.get()) {
			running.set(false);
			stopped.set(true);
			System.out.println("Stopping thread");
			//thread.interrupt();
		} else {
			running.set(true);
			stopped.set(false);
			System.out.println("resuming thread on next request");
		}
		return ret;
	}

	@Override
	public String forward(String dnsFrom, String dnsTo, String from, String to, String delay) {
		try { 
			Thread.sleep(Long.parseLong(delay));
			} catch (Exception e) {};
			
		portFrom = from;
		portTo = to;
		this.delay = delay;
		this.dnsFrom = dnsFrom;
		this.dnsTo = dnsTo;
		//Runnable aRunnable = runnable;
		// 1 thread at a time
		//if(!stopped.get())
			thread = new Thread(runnable);
			thread.start();	
			//running.set(true);
			//stopped.set(false);		
		return Long.toString(counter.addAndGet(1));
	}

	private void sendMessage() {
		if(!stopped.get()) {
			String url = "http://" +
				dnsTo + 
				":" + portTo + URL_POSTFIX + 
				"?dnsFrom=" + dnsTo + 
				"&dnsTo=" + dnsFrom + 
				"&from=" + portTo + 
				"&to=" + portFrom + 
				"&delay=" + delay;
			System.out.println("Request: " + url);
			HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(url))
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
		} else {
			System.out.println("Skipping reflection");
		}
	}
}
