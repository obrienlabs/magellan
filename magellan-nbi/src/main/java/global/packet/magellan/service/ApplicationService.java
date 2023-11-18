package global.packet.magellan.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import com.google.auth.oauth2.GoogleCredentials;
//https://github.com/googleapis/java-bigquery/blob/main/samples/snippets/src/main/java/com/example/bigquery/AuthSnippets.java#L51
//import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import com.google.cloud.bigquery.Dataset;
import com.google.cloud.discoveryengine.v1.SearchRequest;
import com.google.cloud.discoveryengine.v1.SearchResponse;
import com.google.cloud.discoveryengine.v1.SearchServiceClient;
import com.google.cloud.discoveryengine.v1.SearchServiceSettings;
import com.google.cloud.discoveryengine.v1.ServingConfigName;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;


@Service
public class ApplicationService implements ApplicationServiceLocal {
	
	public static String PROJECT_ID = "gen-ai-old";

	static Logger logger = Logger.getLogger(ApplicationService.class.getName());

	@Override
	public String health() {
		return "OK";
	}

	
	@Override
	public String event() {
		return "{key: 1, value: second}";
	}

	
	@Override
	public String gcpViaEnv() {
		String response = null;
		try {
			response = authenticateImplicitWithAdc(PROJECT_ID);
			//response = search(PROJECT_ID, );
		} catch (IOException io) {
			logger.info(io.getMessage());
		}
		return response;
	}
	
	
	
	
	//Performs a search on a given datastore. */
	
	 public String search(
	     String projectId,
	     String location,
	     String collectionId,
	     String dataStoreId,
	     String servingConfigId,
	     String searchQuery)
	     throws Exception {//IOException, ExecutionException {
	 
	 List<SearchResponse.SearchResult> searchResults = null;
	 InputStream credentialsStream = new FileInputStream("classpath:gcp-gen-ai-old-sa.json");//GOOGLE_APPLICATION_CREDENTIALS);
	 GoogleCredentials credentials = GoogleCredentials.fromStream(credentialsStream);
	   // For more information, refer to:
	   // https://cloud.google.com/generative-ai-app-builder/docs/locations#specify_a_multi-       region_for_your_data_store
	   String endpoint = (location.equals("global")) 
	       ? String.format("discoveryengine.googleapis.com:443", location) 
	       : String.format("%s-discoveryengine.googleapis.com:443", location);
	   System.out.println("Here is the endpoint" + endpoint);
	   //making change here
	   
	   
	    // TODO(developer): Replace these variables before running the sample.
	    // move inside the resource path - for jav/war deployment
	    //File credentialsPath = new File("~/keys/gcp-gen-ai-old-sa.json");
	    File credentialsPath = ResourceUtils.getFile("classpath:gcp-gen-ai-old-sa.json");

	    // Load credentials from JSON key file. If you can't set the GOOGLE_APPLICATION_CREDENTIALS
	    // environment variable, you can explicitly load the credentials file to construct the
	    // credentials.
	    //GoogleCredentials credentials;
	    //try (FileInputStream serviceAccountStream = new FileInputStream(credentialsPath)) {
	    //  credentials = ServiceAccountCredentials.fromStream(serviceAccountStream);
	   // }
	    
	    //InputStream credentialsStream = new FileInputStream(GOOGLE_APPLICATION_CREDENTIALS);
		// GoogleCredentials credentials = GoogleCredentials.fromStream(credentialsStream);
		 
	   SearchServiceSettings settings = SearchServiceSettings.newBuilder()
	       //     .setCredentials(credentials)
	       .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
	       .setEndpoint(endpoint)
	       .build();
	     
	   // Initialize client that will be used to send requests. This client only needs to be created
	   // once, and can be reused for multiple requests. After completing all of your requests, call
	   // the `searchServiceClient.close()` method on the client to safely
	   // clean up any remaining background resources.
	   try (SearchServiceClient searchServiceClient = SearchServiceClient.create(settings)) {
	     SearchRequest request =
	         SearchRequest.newBuilder()
	             .setServingConfig(
	                 ServingConfigName.formatProjectLocationCollectionDataStoreServingConfigName(
	                     projectId, location, collectionId, dataStoreId, servingConfigId))
	             .setQuery(searchQuery)
	             .setPageSize(10)
	             .build();
	     System.out.println("Sent the request here");
	     SearchResponse response = searchServiceClient.search(request).getPage().getResponse();
	     System.out.println("Got the response");
	     System.out.println(response);
	     searchResults = response.getResultsList();
	    
	   }
	   return "ok";// searchResponse.
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
	
	// https://github.com/obrienlabs/magellan/commit/52641990082c32b22f32137f8316382d88b55264
	// https://cloud.google.com/bigquery/docs/authentication/service-account-file
	// https://github.com/googleapis/java-bigquery/blob/main/samples/snippets/src/main/java/com/example/bigquery/AuthSnippets.java
	@Override
	public String gcpViaFile() {
		String response = null;
		try {
			response = authenticateFileWithAdc(PROJECT_ID);
			//response = explicit(PROJECT_ID);
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
		    /*
		     * credentials	ServiceAccountCredentials  (id=155)	
		     * clientEmail	"gen....n-ai-old.iam.gserviceaccount.com" (id=165)
		     * clientId	"1085...6390" (id=166)	
		     */

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
	    Storage storage = StorageOptions
	    		.newBuilder()
	    		.setCredentials(credentials)
	    		.setProjectId(project).build().getService();

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
