package com.ogse.services.workflow;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ogse.components.io.Folder;
import com.ogse.components.metadata.Entity;
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
public class WorkflowControllerHtml extends Controller {

    private final WorkflowService wService;

    @Autowired
    public WorkflowControllerHtml(WorkflowService wService) {
        this.wService = wService;
    }

	@GetMapping(path="/api/workflow/list", produces = MediaType.TEXT_HTML_VALUE)
	public ModelAndView getList() throws Exception {
		ModelAndView mv = new ModelAndView();

		mv.addObject("entities", this.wService.inventory.entities);
		mv.setViewName("workflow/workflow-list");

		return mv;
	}

	@GetMapping(path="/api/workflow", produces = MediaType.TEXT_HTML_VALUE)
	public ModelAndView postHtml() throws Exception {
		ModelAndView mv = new ModelAndView();

		mv.setViewName("workflow/workflow-publish");

		return mv;
	}

	@GetMapping(path="/api/workflow/{uuid}/execute", produces = MediaType.TEXT_HTML_VALUE)
	public ModelAndView executeHtml(@PathVariable(value="uuid") String uuid) throws Exception {
		ModelAndView mv = new ModelAndView();

		mv.addObject("uuid", uuid);
		mv.setViewName("workflow/workflow-execute");

		return mv;
	}
}