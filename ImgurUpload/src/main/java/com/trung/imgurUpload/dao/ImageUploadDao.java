package com.trung.imgurUpload.dao;

import java.util.Date;
import java.util.Set;

import com.trung.imgurUpload.entity.ImageUploadJob;
import com.trung.imgurUpload.entity.UploadedImgurLinks;

/**
 * @author Trung
 * Handle CRUD operations on H2Database
 */
public interface ImageUploadDao {
	// Insert a new record into image_upload_job table
	void createJob(String jobId, Set<String> imageUrls);

	// Update table image_upload_job
	void updateJob(String jobId, Date finished, String status);

	// Update table image_url
	void updateImageUrl(String jobId, String imageUrl, String status, String imgurLink);

	// Get upload job status 
	ImageUploadJob getJobStatus(String jobId) throws Exception;

	// Get Imgur links of successfully uploaded image URLs
	UploadedImgurLinks getUploadedLinks();
}
