package com.ogse.services.workspace;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ogse.components.io.Folder;
import com.ogse.components.metadata.Entity;
import com.ogse.components.rest.Controller;
import com.ogse.components.rest.FilesResponse;
import com.ogse.components.rest.RestResponse;
import com.ogse.services.workflow.WorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.util.Date;
import java.util.List;

@RestController
public class WorkspaceControllerHtml extends Controller {

	private final WorkspaceService wService;
	private final WorkflowService wfService;

    @Autowired
    public WorkspaceControllerHtml(WorkspaceService vService, WorkflowService wfService) {
		this.wService = vService;
		this.wfService = wfService;
    }

	@GetMapping(path="/api/workspace/list", produces = MediaType.TEXT_HTML_VALUE)
	public ModelAndView getList() throws Exception {
		ModelAndView mv = new ModelAndView();

		mv.addObject("entities", this.wService.inventory.entities);
		mv.setViewName("workspace/workspace-list");

		return mv;
	}

	/// HTML Endpoints
	@GetMapping(path="/api/workspace", produces = MediaType.TEXT_HTML_VALUE)
	public ModelAndView postHtml() {
		ModelAndView mv = new ModelAndView();

		mv.setViewName("workspace/workspace-publish");

		return mv;
	}

	@GetMapping(path="/api/workspace/{uuid}/execute", produces = MediaType.TEXT_HTML_VALUE)
	public ModelAndView executeHtml(@PathVariable(value="uuid") String uuid) throws Exception {
		ModelAndView mv = new ModelAndView();

		mv.addObject("workflows", this.wfService.inventory.entities);
		mv.addObject("uuid", uuid);
		mv.setViewName("workspace/workspace-execute");

		return mv;
	}
}