package com.ogse.services.simulation;

import com.ogse.components.io.Folder;
import com.ogse.components.rest.Controller;
import com.ogse.components.rest.FilesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class SimulationControllerHtml extends Controller {

	@Value("${app.folders.scratch}")
	private String APP_FOLDERS_SCRATCH;

    private final SimulationService sService;

    @Autowired
    public SimulationControllerHtml(SimulationService sService) {
        this.sService = sService;
    }

	@GetMapping(path="/api/simulation/execute", produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView simulateHtml() {
		ModelAndView mv = new ModelAndView();

        mv.setViewName("simulation/simulate");

        return mv;
	}
}