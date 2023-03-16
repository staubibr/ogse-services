package com.ogse.services.workspace;

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

import java.util.Date;
import java.util.List;

@RestController
public class WorkspaceControllerHtml extends Controller {

    private final WorkspaceService wService;

    @Autowired
    public WorkspaceControllerHtml(WorkspaceService vService) {
		this.wService = vService;
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
}