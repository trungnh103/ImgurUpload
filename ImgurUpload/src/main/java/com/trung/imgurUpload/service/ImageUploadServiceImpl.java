package com.trung.imgurUpload.service;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trung.imgurUpload.dao.ImageUploadDao;
import com.trung.imgurUpload.entity.ImageUploadJob;
import com.trung.imgurUpload.entity.UploadResponse;
import com.trung.imgurUpload.entity.UploadedImgurLinks;

@Service
public class ImageUploadServiceImpl implements ImageUploadService {
	private static final Logger log = LoggerFactory.getLogger(ImageUploadServiceImpl.class);

	@Autowired
	ImageUploadDao repository;

	public UploadResponse upload(Set<String> imageUrls) throws IOException {
		log.info("Start uploading URLs: " + imageUrls);
		String jobId = UUID.randomUUID().toString();
		repository.createJob(jobId, imageUrls);
		UploadingJob job = new UploadingJob(imageUrls, jobId, repository);
		job.start();
		repository.updateJob(jobId, null, ImageUploadJob.IN_PROGRESS);
		return new UploadResponse(jobId);
	}

	public ImageUploadJob getJobStatus(String jobId) throws Exception {
		log.info("Start querying upload job status. ID: " + jobId);
		return repository.getJobStatus(jobId);
	}

	public UploadedImgurLinks getUploadedLinks() {
		log.info("Start querying Imgur links ");
		return repository.getUploadedLinks();
	}

}
