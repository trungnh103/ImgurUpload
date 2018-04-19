package com.trung.imgurUpload.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.trung.imgurUpload.entity.ImageUploadJob;
import com.trung.imgurUpload.entity.UploadedImgurLinks;
import com.trung.imgurUpload.entity.UploadedUrls;

@Repository
public class ImageUploadDaoImpl implements ImageUploadDao {
	private static final Logger log = LoggerFactory.getLogger(ImageUploadDaoImpl.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	public void createJob(String jobId, Set<String> imageUrls) {
		log.info("Start creating new upload job. ID: " + jobId + ", URLs: " + imageUrls);
		String query = "insert into image_upload_job (id, created, finished, status) values(?, ?, ?, ?)";
		log.info(query);
		ImageUploadJob job = new ImageUploadJob(jobId);
		jdbcTemplate.update(query, new Object[] { job.getJobId(), job.getCreated(), null, ImageUploadJob.PENDING });
		List<Object[]> urls = new ArrayList<Object[]>();
		for (String imageUrl : imageUrls) {
			urls.add(new Object[] { jobId, imageUrl, UploadedUrls.UPLOAD_PENDING });
		}
		jdbcTemplate.batchUpdate("insert into image_url (job_id, url, status) values(?, ?, ?)", urls);
	}

	public void updateJob(String jobId, Date finished, String status) {
		log.info("Start updating upload job status. ID: " + jobId + ", FINISHED: " + finished + ", STATUS: " + status);
		String query = "update image_upload_job set status = ?, finished = ?  where id = ?";
		log.info(query);
		jdbcTemplate.update(query, new Object[] { status, finished, jobId });
	}

	public void updateImageUrl(String jobId, String imageUrl, String status, String imgurLink) {
		log.info("Start updating upload status and Imgur link. JOBID: " + jobId + ", URL: " + imageUrl + ", STATUS: "
				+ status + ", IMGUR LINK: " + imgurLink);
		String query = "update image_url set status = ?, url = ?  where url = ? and job_id = ?";
		log.info(query);
		jdbcTemplate.update(query, new Object[] { status, imgurLink, imageUrl, jobId });
	}

	public ImageUploadJob getJobStatus(String jobId) throws Exception {
		log.info("Start querying for upload job status. ID: " + jobId);
		String query = "SELECT job.created created, job.finished finished, job.status jobStatus, url.url url, url.status urlStatus FROM image_upload_job job, image_url url WHERE job.id = url.job_id and job.id = ?";
		log.info(query);
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, jobId);
		ImageUploadJob job = null;
		UploadedUrls uploaded = new UploadedUrls();
		for (Map<String, Object> row : rows) {
			if (job == null) {
				job = new ImageUploadJob(jobId);
				job.setCreated((Timestamp) row.get("created"));
				job.setFinished((Timestamp) row.get("finished"));
				job.setStatus((String) row.get("jobStatus"));
			}
			String urlStatus = (String) row.get("urlStatus");
			String url = (String) row.get("url");
			uploaded.add(urlStatus, url);
		}
		job.setUploaded(uploaded);
		return job;
	}

	public UploadedImgurLinks getUploadedLinks() {
		log.info("Start querying for Imgur links");
		String query = "SELECT url FROM image_url WHERE status='complete'";
		log.info(query);
		List<String> urls = jdbcTemplate.queryForList(query, String.class);
		return new UploadedImgurLinks(urls);
	}
}
