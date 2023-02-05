package com.lifecycle.entities.workflow;

public class RelationSet {
	private String table;
	private String[] coupling;
	private String[] fields;

	public RelationSet() {}
	
	public String getTable() {
		return this.table;
	}

	public void setTable(String value) {
		this.table = value;
	}

	public String[] getCoupling() {
		return this.coupling;
	}

	public void setCoupling(String[] value) {
		this.coupling = value;
	}

	public String[] getFields() {
		return this.fields;
	}

	public void setFields(String[] value) {
		this.fields = value;
	}
}
