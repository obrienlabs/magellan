package global.packet.magellan;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

//import com.wordnik.swagger.annotations.ApiOperation;
//import com.wordnik.swagger.annotations.ApiResponse;
//import com.wordnik.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/forward")
public class ForwardingController {
	
	@Autowired
	ApplicationServiceLocal applicationService;
	
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
	@GetMapping("/forward")
	public String getHealth() {
		return applicationService.health().toString();
	}

}
