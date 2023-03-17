package com.ogse.components.workspace;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ogse.components.io.Folder;

import java.io.IOException;
import java.util.Date;

public class Workspace {
    public enum Step {
        NEW, WORKFLOW, SIMULATION, VISUALIZATION;

        @JsonValue
        public int toValue() {
            return ordinal();
        }
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date date = new Date();
    private Step step = Step.NEW;
    private String workflow;
    private Double duration;
    private Long iteration;


    private boolean has_visualization;

    public Date getDate() { return date; }
    public void setDate(Date value) { date = value; }

    public Step getStep() { return step; }
    public void setStep(Step value) { step = value; }

    public String getWorkflow() { return workflow; }
    public void setWorkflow(String value) { workflow = value; }

    public Double getDuration() { return duration; }
    public void setDuration(Double value) { duration = value; }

    public Long getIteration() { return iteration; }
    public void setIteration(Long value) { iteration = value; }

    public boolean getHas_visualization() { return has_visualization; }
    public void setHas_visualization(boolean has_visualization) { this.has_visualization = has_visualization; }

    public Workspace() { }

    public Workspace(Step step) {
        this.step = step;
    }

    public String to_string() throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();

        return om.writeValueAsString(this);
    }

    public Workspace update(Step step) {
        this.date = new Date();
        this.step = step;

        return this;
    }

    public Workspace update(Step step, String workflow) {
        this.workflow = workflow;

        return this.update(step);
    }

    public Workspace update(Step step, Long iteration, Double duration) {
        this.duration = duration;
        this.iteration = iteration;

        return this.update(step);
    }

    public Workspace update(boolean has_visualization) {
        this.has_visualization = has_visualization;

        return this.update(this.step);
    }

    public void write(Folder folder) throws IOException {
        folder.write("workspace.json", this);
    }


}
