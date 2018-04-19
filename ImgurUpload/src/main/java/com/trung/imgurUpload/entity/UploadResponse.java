package com.trung.imgurUpload.entity;

import java.io.Serializable;

public class UploadResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private String jobId;

	public UploadResponse(String jobId) {
		this.jobId = jobId;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

}
