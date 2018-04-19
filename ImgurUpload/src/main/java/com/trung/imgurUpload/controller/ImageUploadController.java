package com.trung.imgurUpload.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.trung.imgurUpload.entity.ImageUploadJob;
import com.trung.imgurUpload.entity.ImageUrls;
import com.trung.imgurUpload.entity.UploadResponse;
import com.trung.imgurUpload.entity.UploadedImgurLinks;
import com.trung.imgurUpload.service.ImageUploadService;

/**
 * @author Trung
 * Handle requests
 */
@RestController
public class ImageUploadController {
	private static final Logger log = LoggerFactory.getLogger(ImageUploadController.class);

	@Autowired
	ImageUploadService imageUploadService;

	/**
	 * Handle request POST /v1/images/upload
	 * @param imageUrls
	 *            an array of image URLs
	 * @return response body containing jobId 
	 */
	@RequestMapping(value = "/v1/images/upload", method = RequestMethod.POST)
	public @ResponseBody UploadResponse upload(@RequestBody ImageUrls imageUrls) throws IOException {
		log.info("POST /v1/images/upload. URLs: " + imageUrls);
		return imageUploadService.upload(imageUrls.getUrls());
	}

	/**
	 * Handle request GET /v1/images/upload/:jobId
	 * @param jobId
	 * @return upload job status 
	 */
	@RequestMapping("/v1/images/upload/{jobId}")
	public @ResponseBody ImageUploadJob getJobStatus(@PathVariable String jobId) throws Exception {
		log.info("GET /v1/images/upload/" + jobId);
		return imageUploadService.getJobStatus(jobId);
	}

	/**
	 * Handle request GET /v1/images
	 * @return list of all uploaded image links 
	 */
	@RequestMapping("/v1/images")
	public @ResponseBody UploadedImgurLinks getUploadedLinks() throws Exception {
		log.info("GET /v1/images");
		return imageUploadService.getUploadedLinks();
	}
}
