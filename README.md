# magellan
Magellan - world packet navigation

# Swagger API landing page
```
http://localhost:8080/nbi/swagger-ui.html
```
# Docker runs
```
source
docker run -d -p 8888:8080 --name magellan obrienlabs/magellan-nbi:0.0.3-arm
```

## API
start forwarding thread http://127.0.0.1:8888/nbi/forward/packet?dnsFrom=host.docker.internal&dnsTo=host.docker.internal&from=8889&to=8888&delay=1000

start/stop thread http://127.0.0.1:8888/nbi/forward/reset

- source URL
- target URL
- message
- timestamps
  - node
  - timestamp

# Example Google Cloud Run

HTTPS support is added via Google Cloud Run

source swagger: https://traffic-generation-source-vyua7q27tq-nn.a.run.app/nbi/swagger-ui.html#/application-service-controller/getHealthUsingGET_1
source: https://traffic-generation-source-vyua7q27tq-nn.a.run.app/nbi/api

target: https://traffic-generation-target-vyua7q27tq-pd.a.run.app/nbi/api

Traffic gen API: 

https://traffic-generation-source-vyua7q27tq-nn.a.run.app/nbi/swagger-ui.html#/forwarding-controller/getPacketUsingGET

source

https://traffic-generation-source-vyua7q27tq-nn.a.run.app/nbi/forward/packet?delay=1000&dnsFrom=traffic-generation-source-vyua7q27tq-nn.a.run.app&dnsTo=traffic-generation-target-vyua7q27tq-pd.a.run.app&from=80&to=80

curl -X GET "https://traffic-generation-source-vyua7q27tq-nn.a.run.app/nbi/forward/packet?delay=1000&dnsFrom=traffic-generation-source-vyua7q27tq-nn.a.run.app&dnsTo=traffic-generation-target-vyua7q27tq-pd.a.run.app&from=80&to=80" -H "accept: */*"

target

https://traffic-generation-target-vyua7q27tq-pd.a.run.app/nbi/forward/packet?delay=1000&dnsFrom=traffic-generation-target-vyua7q27tq-pd.a.run.app&dnsTo=traffic-generation-source-vyua7q27tq-nn.a.run.app&from=80&to=80


## Traffic Generation

2 copies of the traffic gen containers are currently running on cloud run until I move them over to the GKE cluster.
The health check doubles as the target REST endpoint for now.
Note: HTTPS termination is on the cloud run gateway - if we don't terminate on the API Gateway and/or the LB fronting the GKE cluster then I'll add a self signed to the app - for now we use the Google cert for https.

CICD
Currently there is a CSR trigger on cloud build to run the maven build and build/push the container into artifact registry.  The cloud run builds are static but can be switched to trigger.  I expect we can add a trigger on the cloud deploy job once I have moved the container to GKE.

To view - open swagger or use direct curls or the browser

https://traffic-generation-source-vyua7q27tq-nn.a.run.app/nbi/swagger-ui.html#/forwarding-controller/getTrafficUsingGET

enter for example 120k 1ms traffic https://traffic-generation-source-vyua7q27tq-nn.a.run.app/nbi/forward/traffic?delay=1&dns=traffic-generation-target-vyua7q27tq-pd.a.run.app&iterations=120000&to=80 

view the metrics on both source and target. 

These requests have no real load on the backend (db marshall/unmarshall as part of orm) therefore 1k requests/sec only scales a smaller than normal 1g/1vCore container up to 3 instances.  

Once we get backend load scaling should occur with over 1k req/sec traffic

https://console.cloud.google.com/run/detail/northamerica-northeast1/traffic-generation-source/metrics

https://console.cloud.google.com/run/detail/northamerica-northeast2/traffic-generation-target/metrics


# local development testing in Google cloud shell
```
curl -X GET "http://127.0.0.1:8080/nbi/forward/traffic?dns=127.0.0.1&to=8080&delay=1000&iterations=20"

curl -X GET  "http://127.0.0.1:8080/nbi/forward/reset"
```

# GCP Default Docker Container
```
gcloud compute instances create-with-container instance-20240227-002215 --project=cuda-old --zone=us-central1-a --machine-type=e2-medium --network-interface=address=34.69.213.211,network-tier=PREMIUM,stack-type=IPV4_ONLY,subnet=default --maintenance-policy=MIGRATE --provisioning-model=STANDARD --service-account=196717963363-compute@developer.gserviceaccount.com --scopes=https://www.googleapis.com/auth/cloud-platform --image=projects/cos-cloud/global/images/cos-stable-109-17800-147-15 --boot-disk-size=10GB --boot-disk-type=pd-balanced --boot-disk-device-name=instance-20240227-002215 --container-image=obrienlabs/magellan-nbi:0.0.3-ia64 --container-restart-policy=always --no-shielded-secure-boot --shielded-vtpm --shielded-integrity-monitoring --labels=goog-ec-src=vm_add-gcloud,container-vm=cos-stable-109-17800-147-15

Just need to expose the ports
michael@instance-20240227-002215 ~ $ docker ps -a
CONTAINER ID   IMAGE                                COMMAND                  CREATED             STATUS             PORTS     NAMES
d56ed6dbcdde   obrienlabs/magellan-nbi:0.0.3-ia64   "java -Djava.securit…"   About an hour ago   Up About an hour             klt-instance-20240227-002215-mdvq
michael@instance-20240227-002215 ~ $ docker exec -it d56ed6dbcdde bash
root@instance-20240227-002215:/# curl http://127.0.0.1:8080/nbi/api
{"id":1,"content":"PASS remoteAddr: 127.0.0.1 localAddr: 127.0.0.1 remoteHost: 127.0.0.1 serverName: 127.0.0.1"}root@instance-20240227-002215:/# 


  
