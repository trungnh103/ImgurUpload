package com.trung.imgurUpload.entity;

import java.util.ArrayList;
import java.util.List;

public class UploadedUrls {
	public static String UPLOAD_PENDING = "pending";
	public static String UPLOAD_COMPLETE = "complete";
	public static String UPLOAD_FAILED = "failed";

	public List<String> pending = new ArrayList<String>();
	public List<String> complete = new ArrayList<String>();
	public List<String> failed = new ArrayList<String>();

	public void add(String status, String url) {
		if (status.equals(UPLOAD_PENDING))
			pending.add(url);
		if (status.equals(UPLOAD_COMPLETE))
			complete.add(url);
		if (status.equals(UPLOAD_FAILED))
			failed.add(url);
	}
}
