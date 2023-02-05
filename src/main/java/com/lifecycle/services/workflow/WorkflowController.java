package com.lifecycle.services.workflow;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.lifecycle.entities.metadata.Entities;
import com.lifecycle.entities.metadata.Entity;
import com.lifecycle.components.io.ZipFile;
import com.lifecycle.components.rest.Controller;
import com.lifecycle.components.rest.FilesResponse;
import com.lifecycle.components.rest.RestResponse;

@RestController
public class WorkflowController extends Controller {
	
    private final WorkflowService wService;
    
    @Autowired
    public WorkflowController(WorkflowService wService) {
        this.wService = wService;
    }

	@GetMapping(path="/api/workflow/{uuid}", produces=MediaType.APPLICATION_JSON_VALUE)
    public Entity get(@PathVariable("uuid") String uuid) throws Exception {
    	return this.wService.Read(uuid);
	}

	@GetMapping(path="/api/workflow/{uuid}/file", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<byte[]> getFile(@PathVariable("uuid") String uuid) throws Exception {
    	File file = this.wService.ReadFile(uuid);

    	return FilesResponse.build(file);
	}
	
	@GetMapping(path="/api/workflow/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<byte[]> get() throws Exception {
    	File file = this.wService.List();

    	return FilesResponse.build(file);
	}
    
	@PostMapping(path="/api/workflow", consumes={ MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE })
    public ObjectNode post(@RequestPart MultipartFile workflow,
    					   @RequestPart List<MultipartFile> data,
						   @RequestParam(required = false) String name,
						   @RequestParam(required = false) String description) throws Exception {
    	return this.wService.Create(name, description, workflow, data).json();
    }

	@PostMapping(path = "/api/workflow/{uuid}/execute", consumes={ MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<byte[]> execute(@PathVariable String uuid,
										  @RequestParam(required = false) String params) throws Exception {
		byte[] content = this.wService.Execute(uuid, params);

		return FilesResponse.build("scenario.json", content);
	}

	@DeleteMapping(path="/api/workflow/{uuid}")
    public ResponseEntity<RestResponse> delete(@PathVariable String uuid) throws Exception {
    	this.wService.Delete(uuid);
    	
    	return this.handleSuccess();
    }

	@PutMapping(path="/api/workflow/{uuid}")
    public ObjectNode put(@PathVariable String uuid,
						  @RequestParam(required = false) MultipartFile workflow,
						  @RequestParam(required = false) List<MultipartFile> data,
						  @RequestParam(required = false) String name,
						  @RequestParam(required = false) String description,
						  @RequestParam(required = false) Date created) throws Exception {
		return this.wService.Update(uuid, name, description, created, workflow, data).json();
	}

	/// HTML Endpoints
	@GetMapping(path="/api/workflow", produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView postHtml() throws Exception {
		ModelAndView mv = new ModelAndView();

        mv.setViewName("lifecycle/workflow-publish");
        
        return mv;
	}
	
	@GetMapping(path="/api/workflow/list", produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getList() throws Exception {
		ModelAndView mv = new ModelAndView();
		Entities<Entity> entities = this.wService.Entities();
		
        mv.addObject("entities", entities.entities);
        mv.setViewName("lifecycle/workflow-list");
        
        return mv;
	}
	
	@GetMapping(path="/api/workflow/{uuid}/execute", produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView executeHtml(@PathVariable(value="uuid") String uuid) throws Exception {
		ModelAndView mv = new ModelAndView();

        mv.addObject("uuid", uuid);
        mv.setViewName("lifecycle/workflow-execute");
        
        return mv;
	}
}