package global.packet.magellan.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import java.io.IOException;
import java.util.logging.Logger;

//https://github.com/googleapis/java-bigquery/blob/main/samples/snippets/src/main/java/com/example/bigquery/AuthSnippets.java#L51
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import com.google.cloud.bigquery.Dataset;
import java.io.File;
import java.io.FileInputStream;


@Service
public class ApplicationService implements ApplicationServiceLocal {
	
	public static String PROJECT_ID = "gen-ai-old";

	static Logger logger = Logger.getLogger(ApplicationService.class.getName());
	@Override
	public String health() {
		return "OK";
	}

	@Override
	public String gcpViaEnv() {
		String response = null;
		try {
			response = authenticateImplicitWithAdc(PROJECT_ID);
		} catch (IOException io) {
			logger.info(io.getMessage());
		}
		return response;
	}
	
	
	// https://cloud.google.com/docs/authentication/client-libraries#java
	/**
	 * Requries that the environment variable is set
	 * export GOOGLE_APPLICATION_CREDENTIALS="~/keys/gcp-gen-ai-old-sa.json"
	 * 
	 * @param project
	 * @throws IOException
	 */
	private String authenticateImplicitWithAdc(String project) throws IOException {
		String lastBucket = "none";

		    // *NOTE*: Replace the client created below with the client required for your application.
		    // Note that the credentials are not specified when constructing the client.
		    // Hence, the client library will look for credentials using ADC.
		    //
		    // Initialize client that will be used to send requests. This client only needs to be created
		    // once, and can be reused for multiple requests.
		    Storage storage = StorageOptions.newBuilder().setProjectId(project).build().getService();

		    logger.info("Buckets:");
		    Page<Bucket> buckets = storage.list();
		    for (Bucket bucket : buckets.iterateAll()) {
		    	logger.info(bucket.toString());
		      lastBucket = bucket.toString();
		    }
		    logger.info("Listed all storage buckets.");
		    return lastBucket;
	}
	
	
	// https://cloud.google.com/bigquery/docs/authentication/service-account-file
	// https://github.com/googleapis/java-bigquery/blob/main/samples/snippets/src/main/java/com/example/bigquery/AuthSnippets.java
	@Override
	public String gcpViaFile() {
		String response = null;
		try {
			//response = authenticateFileWithAdc(PROJECT_ID);
			response = explicit(PROJECT_ID);
		} catch (IOException io) {
			logger.info(io.getMessage());
		}
		return response;
	}
	
	  public String explicit(String project) throws IOException {
		  String response = "none";
		    // TODO(developer): Replace these variables before running the sample.
		    // move inside the resource path - for jav/war deployment
		    //File credentialsPath = new File("~/keys/gcp-gen-ai-old-sa.json");
		    File credentialsPath = ResourceUtils.getFile("classpath:gcp-gen-ai-old-sa.json");


		    // Load credentials from JSON key file. If you can't set the GOOGLE_APPLICATION_CREDENTIALS
		    // environment variable, you can explicitly load the credentials file to construct the
		    // credentials.
		    GoogleCredentials credentials;
		    try (FileInputStream serviceAccountStream = new FileInputStream(credentialsPath)) {
		      credentials = ServiceAccountCredentials.fromStream(serviceAccountStream);
		    }

		    // Instantiate a client.
		    BigQuery bigquery =
		        BigQueryOptions.newBuilder()
		            .setCredentials(credentials)
		            .setProjectId(project)
		            .build()
		            .getService();

		    // Use the client.
		    System.out.println("Datasets:");
		    for (Dataset dataset : bigquery.listDatasets().iterateAll()) {
		      System.out.printf("%s%n", dataset.getDatasetId().getDataset());
		    }
		    return response;
		  }
	
	private String authenticateFileWithAdc(String project) throws IOException {

		String lastBucket = "none";

	    // *NOTE*: Replace the client created below with the client required for your application.
	    // Note that the credentials are not specified when constructing the client.
	    // Hence, the client library will look for credentials using ADC.
	    //
	    // Initialize client that will be used to send requests. This client only needs to be created
	    // once, and can be reused for multiple requests.
	    Storage storage = StorageOptions.newBuilder().setProjectId(project).build().getService();

	    logger.info("Buckets:");
	    Page<Bucket> buckets = storage.list();
	    for (Bucket bucket : buckets.iterateAll()) {
	    	logger.info(bucket.toString());
	      lastBucket = bucket.toString();
	    }
	    logger.info("Listed all storage buckets.");
	    return lastBucket;
	}
	  
	/*
	@Override
	public String forward() {
		// TODO Auto-generated method stub
		return "OK";
	}*/

}
