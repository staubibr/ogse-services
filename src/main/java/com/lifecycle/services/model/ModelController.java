package com.lifecycle.services.model;

import java.io.File;
import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lifecycle.components.metadata.Atomic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lifecycle.components.metadata.Entity;
import com.lifecycle.components.metadata.Model;
import com.lifecycle.components.rest.Controller;
import com.lifecycle.components.rest.FilesResponse;
import com.lifecycle.components.rest.RestResponse;

@RestController
public class ModelController extends Controller {
	
    private final ModelService mService;
    
    @Autowired
    public ModelController(ModelService mService) {
        this.mService = mService;
    }

	@GetMapping(path="/api/model/list", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<byte[]> get() throws Exception {
		return FilesResponse.build(this.mService.inventory.file);
	}

	@GetMapping(path="/api/model/list", produces = MediaType.TEXT_HTML_VALUE)
	public ModelAndView getList() throws Exception {
		ModelAndView mv = new ModelAndView();

		mv.addObject("entities", this.mService.inventory.entities);
		mv.setViewName("lifecycle/model-list");

		return mv;
	}

	@GetMapping(path="/api/model/{uuid}", produces=MediaType.APPLICATION_JSON_VALUE)
    public Entity getEntity(@PathVariable String uuid) throws Exception {
    	return this.mService.Read(uuid);
	}

	@GetMapping(path="/api/model/{uuid}", produces = MediaType.TEXT_HTML_VALUE)
	public ModelAndView getModel(@PathVariable(value="uuid") String uuid) throws Exception {
		ModelAndView mv = new ModelAndView();
		ObjectMapper om = new ObjectMapper();
		Model model = om.readValue(this.mService.ReadFile(uuid), Atomic.class);

		mv.addObject("model", model);
		mv.setViewName("lifecycle/model");

		return mv;
	}
	
	@GetMapping(path="/api/model/{uuid}/file", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<byte[]> getFile(@PathVariable String uuid) throws Exception {
    	File file = this.mService.ReadFile(uuid);
    	
    	return FilesResponse.build(file);
	}

	@PostMapping(path="/api/model", consumes={ MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE })
    public ObjectNode post(@RequestPart MultipartFile model,
						   @RequestParam String name,
						   @RequestParam(required = false) String description) throws Exception {
		return this.mService.Create(name, description, model).json();
    }

	@GetMapping(path="/api/model", produces = MediaType.TEXT_HTML_VALUE)
	public ModelAndView getPublish() throws Exception {
		ModelAndView mv = new ModelAndView();

		mv.setViewName("lifecycle/model-publish");

		return mv;
	}
	
	@PutMapping(path="/api/model/{uuid}")
    public ObjectNode put(@PathVariable String uuid, 
						  @RequestParam MultipartFile model,
						  @RequestParam String name,
						  @RequestParam(required = false) String description,
						  @RequestParam(required = false) Date created) throws Exception {
    	return this.mService.Update(uuid, name, description, created, model).json();
	}

	@DeleteMapping(path="/api/model/{uuid}")
	public RestResponse delete(@PathVariable String uuid) throws Exception {
		this.mService.Delete(uuid);

		return new RestResponse(HttpStatus.OK.value(), "Success");
	}
}