package com.ogse.services.workflow;

import java.io.File;
import java.util.Date;

import com.ogse.components.io.Folder;
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
import com.ogse.components.metadata.Entity;
import com.ogse.components.rest.Controller;
import com.ogse.components.rest.FilesResponse;
import com.ogse.components.rest.RestResponse;

@RestController
public class WorkflowController extends Controller {
	
    private final WorkflowService wService;
    
    @Autowired
    public WorkflowController(WorkflowService wService) {
        this.wService = wService;
    }

	@GetMapping(path="/api/workflow/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<byte[]> get() throws Exception {
		return FilesResponse.build(this.wService.inventory.file);
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

	@PostMapping(path="/api/workflow", consumes={ MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE })
    public ObjectNode post(@RequestPart MultipartFile workflow,
						   @RequestParam String name,
						   @RequestParam(required = false) String description) throws Exception {
    	return this.wService.Create(name, description, workflow).json();
    }

	@PutMapping(path="/api/workflow/{uuid}")
	public ObjectNode put(@PathVariable String uuid,
						  @RequestParam MultipartFile workflow,
						  @RequestParam String name,
						  @RequestParam(required = false) String description,
						  @RequestParam(required = false) Date created) throws Exception {
		return this.wService.Update(uuid, name, description, created, workflow).json();
	}

	@DeleteMapping(path="/api/workflow/{uuid}")
    public RestResponse delete(@PathVariable String uuid) throws Exception {
    	this.wService.Delete(uuid);

		return new RestResponse(HttpStatus.OK.value(), "Success");
    }

	@PostMapping(path = "/api/workflow/{uuid}/execute", consumes={ MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<byte[]> execute(@PathVariable String uuid,
										  @RequestParam(required = false) String experiment) throws Exception {

		Folder output = this.wService.Execute(uuid, experiment);
		ResponseEntity<byte[]> response = FilesResponse.build("workflow_results.zip", output.zip());
		output.delete();

		return response;
	}
}