package com.lifecycle.services.workflow_v2;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lifecycle.entities.metadata.Entities;
import com.lifecycle.entities.metadata.Entity;
import com.lifecycle.components.io.ZipFile;
import com.lifecycle.components.rest.Controller;
import com.lifecycle.components.rest.FilesResponse;
import com.lifecycle.components.rest.RestResponse;
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
public class WorkflowV2Controller extends Controller {
	
    private final WorkflowV2Service wService;
    
    @Autowired
    public WorkflowV2Controller(WorkflowV2Service wService) {
        this.wService = wService;
    }

	@GetMapping(path="/api/workflow_v2/{uuid}", produces=MediaType.APPLICATION_JSON_VALUE)
    public Entity get(@PathVariable("uuid") String uuid) throws Exception {
    	return this.wService.Read(uuid);
	}

	@GetMapping(path="/api/workflow_v2/{uuid}/file", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<byte[]> getFile(@PathVariable("uuid") String uuid) throws Exception {
    	File file = this.wService.ReadFile(uuid);

    	return FilesResponse.build(file);
	}
	
	@GetMapping(path="/api/workflow_v2/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<byte[]> get() throws Exception {
    	File file = this.wService.List();

    	return FilesResponse.build(file);
	}
    
	@PostMapping(path="/api/workflow_v2", consumes={ MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE })
    public ObjectNode post(@RequestPart MultipartFile workflow,
						   @RequestParam(required = false) String name,
						   @RequestParam(required = false) String description) throws Exception {
    	return this.wService.Create(name, description, workflow).json();
    }

    @PostMapping(path = "/api/workflow_v2/{uuid}/execute", consumes={ MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<RestResponse> execute(@PathVariable String uuid,
    									  @RequestParam(required = false) String params) throws Exception {
    	this.wService.Execute(uuid, params);

		return this.handleSuccess();
    }
	
	@DeleteMapping(path="/api/workflow_v2/{uuid}")
    public ResponseEntity<RestResponse> delete(@PathVariable String uuid) throws Exception {
    	this.wService.Delete(uuid);
    	
    	return this.handleSuccess();
    }

	@PutMapping(path="/api/workflow_v2/{uuid}")
    public ObjectNode put(@PathVariable String uuid,
						  @RequestParam(required = false) MultipartFile workflow,
						  @RequestParam(required = false) String name,
						  @RequestParam(required = false) String description,
						  @RequestParam(required = false) Date created) throws Exception {
		return this.wService.Update(uuid, name, description, created, workflow).json();
	}






	/// HTML Endpoints
	@GetMapping(path="/api/workflow_v2", produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView postHtml() throws Exception {
		ModelAndView mv = new ModelAndView();

        mv.setViewName("lifecycle/workflow-publish");
        
        return mv;
	}
	
	@GetMapping(path="/api/workflow_v2/list", produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getList() throws Exception {
		ModelAndView mv = new ModelAndView();
		Entities<Entity> entities = this.wService.Entities();
		
        mv.addObject("entities", entities.entities);
        mv.setViewName("lifecycle/workflow-list");
        
        return mv;
	}
	
	@GetMapping(path="/api/workflow_v2/{uuid}/execute", produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView executeHtml(@PathVariable(value="uuid") String uuid) throws Exception {
		ModelAndView mv = new ModelAndView();

        mv.addObject("uuid", uuid);
        mv.setViewName("lifecycle/workflow-execute");
        
        return mv;
	}
}