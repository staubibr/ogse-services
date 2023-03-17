package com.ogse.services.workspace;

import java.io.File;
import java.util.Date;
import java.util.List;

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
public class WorkspaceController extends Controller {
	
    private final WorkspaceService wService;
    
    @Autowired
    public WorkspaceController(WorkspaceService vService) {
		this.wService = vService;
    }

	@GetMapping(path="/api/workspace/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<byte[]> get() throws Exception {
		return FilesResponse.build(this.wService.inventory.file);
	}

	@GetMapping(path="/api/workspace/{uuid}", produces=MediaType.APPLICATION_JSON_VALUE)
    public Entity getEntity(@PathVariable String uuid) throws Exception {
    	return this.wService.Read(uuid);
	}

	@GetMapping(path="/api/workspace/{uuid}/file", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<byte[]> getFile(@PathVariable String uuid) throws Exception {
		byte[] zipped = this.wService.ReadFiles(uuid);

		return FilesResponse.build("workspace.zip", zipped);
	}

	@GetMapping(path="/api/workspace/{uuid}/file/{name}", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<byte[]> getFile(@PathVariable String uuid,
										  @PathVariable String name) throws Exception {
		File file = this.wService.GetFile(uuid, name);

		return FilesResponse.build(file);
	}

	@PostMapping(path="/api/workspace", consumes={ MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE })
    public ObjectNode post(@RequestParam String name,
						   @RequestParam(required = false) String description) throws Exception {
    	return this.wService.Create(name, description).json();
    }

	@PutMapping(path="/api/workspace/{uuid}")
	public ObjectNode put(@PathVariable String uuid,
						  @RequestPart List<MultipartFile> files,
						  @RequestParam String name,
						  @RequestParam(required = false) String description,
						  @RequestParam(required = false) Date created) throws Exception {
		return this.wService.Update(uuid, name, description, created, files).json();
	}

	@DeleteMapping(path="/api/workspace/{uuid}")
    public RestResponse delete(@PathVariable String uuid) throws Exception {
    	this.wService.Delete(uuid);

		return new RestResponse(HttpStatus.OK.value(), "Success");
    }

	@PostMapping("/api/workspace/{workspace_uuid}/workflow/{workflow_uuid}/execute")
	public ObjectNode workflow_execute(@PathVariable String workspace_uuid,
							   @PathVariable String workflow_uuid,
							   @RequestParam(required = false) String experiment) throws Exception {

		this.wService.ExecuteWorkflow(workspace_uuid, workflow_uuid, experiment);

		return this.wService.inventory.Get(workspace_uuid).json();
	}

    @PostMapping("/api/workspace/{workspace_uuid}/simulation/execute")
    public ObjectNode simulation_execute(@PathVariable String workspace_uuid,
                                         @RequestParam(value = "iterations", required = false) Long n_iterations,
                                         @RequestParam(value = "duration", required = false) Double n_duration) throws Exception {

        this.wService.ExecuteSimulation(workspace_uuid, n_iterations, n_duration);

        return this.wService.inventory.Get(workspace_uuid).json();
    }
    @PostMapping("/api/workspace/{workspace_uuid}/visualization")
    public ObjectNode visualization(@PathVariable String workspace_uuid,
                                    @RequestPart MultipartFile file) throws Exception {

        this.wService.UploadVisualization(workspace_uuid, file);

        return this.wService.inventory.Get(workspace_uuid).json();
    }
}