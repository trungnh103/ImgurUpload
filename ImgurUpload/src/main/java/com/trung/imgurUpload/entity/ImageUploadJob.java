package com.trung.imgurUpload.entity;

import java.sql.Timestamp;
import java.util.Date;

public class ImageUploadJob {
	public static String IN_PROGRESS = "in-progress";
	public static String PENDING = "pending";
	public static String COMPLETE = "complete";
	
	private String jobId;
	private Timestamp created;
	private Timestamp finished;
	private String status;
	private UploadedUrls uploaded;

	public ImageUploadJob(String jobId) {
		this.jobId = jobId;
		created = new Timestamp(System.currentTimeMillis());
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public Date getFinished() {
		return finished;
	}

	public void setFinished(Timestamp finished) {
		this.finished = finished;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public UploadedUrls getUploaded() {
		return uploaded;
	}

	public void setUploaded(UploadedUrls uploaded) {
		this.uploaded = uploaded;
	}

}
