package com.trung.imgurUpload.service;

import java.io.IOException;
import java.util.Set;

import com.trung.imgurUpload.entity.ImageUploadJob;
import com.trung.imgurUpload.entity.UploadResponse;
import com.trung.imgurUpload.entity.UploadedImgurLinks;

/**
 * @author Trung
 * Provide services of the API
 */
public interface ImageUploadService {
	// Upload image URLs to Imgur
	UploadResponse upload(Set<String> imageUrls) throws IOException;

	// Get upload job status 
	ImageUploadJob getJobStatus(String jobId) throws Exception;

	// Get Imgur links of successfully uploaded image URLs
	UploadedImgurLinks getUploadedLinks();
}
