package com.ogse.services.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ogse.components.metadata.Atomic;
import com.ogse.components.metadata.Entity;
import com.ogse.components.metadata.Model;
import com.ogse.components.rest.Controller;
import com.ogse.components.rest.FilesResponse;
import com.ogse.components.rest.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.util.Date;

@RestController
public class ModelControllerHtml extends Controller {

    private final ModelService mService;

    @Autowired
    public ModelControllerHtml(ModelService mService) {
        this.mService = mService;
    }

	@GetMapping(path="/api/model/list", produces = MediaType.TEXT_HTML_VALUE)
	public ModelAndView getList() throws Exception {
		ModelAndView mv = new ModelAndView();

		mv.addObject("entities", this.mService.inventory.entities);
		mv.setViewName("model/model-list");

		return mv;
	}

	@GetMapping(path="/api/model/{uuid}", produces = MediaType.TEXT_HTML_VALUE)
	public ModelAndView getModel(@PathVariable(value="uuid") String uuid) throws Exception {
		ModelAndView mv = new ModelAndView();
		ObjectMapper om = new ObjectMapper();
		Model model = om.readValue(this.mService.ReadFile(uuid), Atomic.class);

		mv.addObject("model", model);
		mv.setViewName("model/model");

		return mv;
	}

	@GetMapping(path="/api/model", produces = MediaType.TEXT_HTML_VALUE)
	public ModelAndView getPublish() throws Exception {
		ModelAndView mv = new ModelAndView();

		mv.setViewName("model/model-publish");

		return mv;
	}
}