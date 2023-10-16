package global.packet.magellan.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import java.io.IOException;

@Service
public class ApplicationService implements ApplicationServiceLocal {

	@Override
	public String health() {
		return "OK";
	}

	@Override
	public String gcp() {
		try {
			authenticateImplicitWithAdc("gen-ai-old");
		} catch (IOException io) {
			System.out.println(io.getMessage());
		}
		return "gcp";
	}
	
	// https://cloud.google.com/docs/authentication/client-libraries#java
	private void authenticateImplicitWithAdc(String project) throws IOException {

		    // *NOTE*: Replace the client created below with the client required for your application.
		    // Note that the credentials are not specified when constructing the client.
		    // Hence, the client library will look for credentials using ADC.
		    //
		    // Initialize client that will be used to send requests. This client only needs to be created
		    // once, and can be reused for multiple requests.
		    Storage storage = StorageOptions.newBuilder().setProjectId(project).build().getService();

		    System.out.println("Buckets:");
		    Page<Bucket> buckets = storage.list();
		    for (Bucket bucket : buckets.iterateAll()) {
		      System.out.println(bucket.toString());
		    }
		    System.out.println("Listed all storage buckets.");
		  }
	
	  
	@Override
	public String forward() {
		// TODO Auto-generated method stub
		return "OK";
	}

}
