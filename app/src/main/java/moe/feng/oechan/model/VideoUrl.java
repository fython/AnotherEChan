package moe.feng.oechan.model;

import moe.feng.oechan.support.GsonUtils;

public class VideoUrl {

	private String highQuality, lowQuality, mediumQuality, originalQuality;

	public static final int QUALITY_ORIGINAL = 0, QUALITY_LOW = 1, QUALITY_MEDIUM = 2, QUALITY_HIGH = 3;

	public String getUrl(int qualityField) {
		switch (qualityField) {
			case QUALITY_HIGH: return highQuality;
			case QUALITY_MEDIUM: return mediumQuality;
			case QUALITY_LOW: return lowQuality;
			case QUALITY_ORIGINAL: default: return originalQuality;
		}
	}

	public String toJsonString() {
		return GsonUtils.toJson(this);
	}

	public static VideoUrl fromJson(String json) {
		return GsonUtils.fromJson(json, VideoUrl.class);
	}

}
