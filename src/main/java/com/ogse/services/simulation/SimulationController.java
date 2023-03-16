package com.ogse.services.simulation;

import com.ogse.components.io.Folder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ogse.components.rest.Controller;
import com.ogse.components.rest.FilesResponse;

@RestController
public class SimulationController extends Controller {

	@Value("${app.folders.scratch}")
	private String APP_FOLDERS_SCRATCH;
	
    private final SimulationService sService;
    
    @Autowired
    public SimulationController(SimulationService sService) {
        this.sService = sService;
    }
    
    @PostMapping("/api/simulation/execute")
    public ResponseEntity<byte[]> simulate(@RequestPart("scenario") MultipartFile scenario,
    								   	   @RequestParam(value = "iterations", required = false) Long n_iterations,
    								   	   @RequestParam(value = "duration", required = false) Double n_duration) 
    								   			   throws Exception {    	

        Folder output = this.sService.Execute(scenario, n_iterations, n_duration);
        byte[] zipped = output.zip();
        output.delete();

    	return FilesResponse.build("simulation_results.zip", zipped);
    }
}