package global.packet.magellan.controller;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import global.packet.magellan.service.ForwardingService;

//import com.wordnik.swagger.annotations.ApiOperation;
//import com.wordnik.swagger.annotations.ApiResponse;
//import com.wordnik.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/forward")
public class ForwardingController {
	
	static Logger logger = Logger.getLogger(ForwardingController.class.getName());
	
	@Autowired
	ForwardingService forwardingService;
	
	//@GET
	//@Path("/health")
	//@Produces(MediaType.TEXT_HTML)
	//@RequestMapping("/test")
	/*@ApiOperation(value="health check", notes="health check for auto scaling")
	@ApiResponses (value= {
			@ApiResponse(code=200, message="OK - success"),
			@ApiResponse(code=400, message="Bad Request"),
			@ApiResponse(code=401, message="Unauthorized"),
			@ApiResponse(code=403, message="Forbidden"),
			@ApiResponse(code=404, message="NotFound"),
			@ApiResponse(code=409, message="Conflict"),
			@ApiResponse(code=500, message="Internal Server Error")
	})*/
	@GetMapping("/packet")
	@ResponseBody
	public String getPacket(
			@RequestParam String dnsFrom,
			@RequestParam String dnsTo,
			@RequestParam String from, 
			@RequestParam String to,
			@RequestParam String delay) {
		return forwardingService.forward(dnsFrom, dnsTo, from, to, delay).toString();
	}
	
	@GetMapping("/reset")
	@ResponseBody
	public String getReset() {
		return forwardingService.reset();
	}
	
		
	   // curl -X GET "http://127.0.0.1:8080/nbi/forward/traffic?dns=127.0.0.1&to=8080&delay=1000&iterations=20"
    //curl -X GET  "http://127.0.0.1:8080/nbi/forward/reset"
    @GetMapping("/traffic")
	@ResponseBody
	public String getTraffic(
			//@RequestParam String dnsFrom,
			@RequestParam String dns,
			//@RequestParam String from, 
			@RequestParam String to,
			@RequestParam String delay,
            @RequestParam String iterations) {
		return forwardingService.traffic(dns, to, delay, iterations).toString();
	}
		


}
