package com.trung.imgurUpload.service;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Date;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.PathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.trung.imgurUpload.dao.ImageUploadDao;
import com.trung.imgurUpload.entity.ImageUploadJob;
import com.trung.imgurUpload.entity.UploadedUrls;

/**
 * @author Trung
 * 
 *         Download the content from image URLs and upload to Imgur.
 */
public class UploadingJob implements Runnable {

	private static final Logger log = LoggerFactory.getLogger(UploadingJob.class);

	ImageUploadDao repository;
	Set<String> urls;
	final String ROOT_URI = "https://api.imgur.com/3/image";
	final String clientId = "197c78d5b5e42c6";
	private Thread job;
	private String jobId;

	public UploadingJob(Set<String> urls, String jobId, ImageUploadDao repository) {
		this.urls = urls;
		this.jobId = jobId;
		this.repository = repository;
	}

	public void run() {
		try {
			for (String imageUrl : urls) {

				// Download the image from URL
				URL url = new URL(imageUrl);
				InputStream is = url.openStream();
				String temporaryFile = "temp.png";
				OutputStream os = new FileOutputStream(temporaryFile);
				byte[] b = new byte[2048];
				int length;
				while ((length = is.read(b)) != -1) {
					os.write(b, 0, length);
				}
				is.close();
				os.close();

				// Upload to Imgur using its API
				uploadToImgur(temporaryFile, imageUrl);
			}
			repository.updateJob(jobId, new Date(), ImageUploadJob.COMPLETE);
		} catch (Exception e) {
			log.error("Job " + jobId + " hasn't finished due to " + e);
		}
	}

	/**
	 * Upload image file to Imgur using its API with OAuth client ID
	 * 
	 * @param file
	 *            temporary file downloaded from image URL
	 * @param url
	 *            image URL to be uploaded to Imgur
	 */
	private void uploadToImgur(String file, String url) {
		String uploadStatus = UploadedUrls.UPLOAD_FAILED;
		String imgurLink = url;
		try {
			RestTemplate rt = new RestTemplate();
			rt.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
			rt.getMessageConverters().add(new StringHttpMessageConverter());
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", "Client-ID " + clientId);
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			PathResource pathResource = new PathResource(file);
			MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
			bodyMap.add("image", pathResource);
			HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(bodyMap, headers);
			ResponseEntity<String> response = rt.exchange(ROOT_URI, HttpMethod.POST, request, String.class);
			String responseBody = response.getBody();
			log.info("From Imgur: " + responseBody);
			JsonObject jsonObject = (JsonObject) new JsonParser().parse(responseBody);
			String status = jsonObject.get("status").getAsString();

			// On success, get Imgur link of the uploaded image and change
			// status -> complete.
			if (status.equals("200")) {
				JsonObject data = (JsonObject) jsonObject.get("data");
				imgurLink = data.get("link").getAsString();
				uploadStatus = UploadedUrls.UPLOAD_COMPLETE;
			}
		} catch (Exception e) {
			log.error("Uploading image " + url + " is not complete due to " + e);
		} finally {
			repository.updateImageUrl(jobId, url, uploadStatus, imgurLink);
			log.info("Image " + url + " uploaded. Status: " + uploadStatus);
		}

	}

	public void start() {
		if (job == null) {
			job = new Thread(this, jobId);
			job.start();
		}
		log.info("Upload job started.");
	}
}
