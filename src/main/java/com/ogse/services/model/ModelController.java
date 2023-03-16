package com.ogse.services.model;

import java.io.File;
import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ogse.components.metadata.Atomic;
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
import com.ogse.components.metadata.Model;
import com.ogse.components.rest.Controller;
import com.ogse.components.rest.FilesResponse;
import com.ogse.components.rest.RestResponse;

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

	@GetMapping(path="/api/model/{uuid}", produces=MediaType.APPLICATION_JSON_VALUE)
    public Entity getEntity(@PathVariable String uuid) throws Exception {
    	return this.mService.Read(uuid);
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