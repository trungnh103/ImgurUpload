package com.trung.imgurUpload.entity;

import java.io.Serializable;
import java.util.Set;

public class ImageUrls implements Serializable{
	private static final long serialVersionUID = 1L;
	Set<String> urls;

	public Set<String> getUrls() {
		return urls;
	}
}
