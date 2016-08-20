package moe.feng.oechan.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import moe.feng.oechan.support.GsonUtils;

public class DetailsResult {

	private String name;
	private int id;
	private List<DetailsResult.Episode> list;

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	public List<Episode> getList() {
		return list;
	}

	public String toJsonString() {
		return GsonUtils.toJson(this);
	}

	public static DetailsResult fromJson(String json) {
		return GsonUtils.fromJson(json, DetailsResult.class);
	}

	public class Episode {

		private int set, clickCount;
		private String fileThumb;
		@SerializedName("created_At")
		private String createdAt;

		public int getNumber() {
			return set;
		}

		public int getClickCount() {
			return clickCount;
		}

		public String getPreviewUrl() {
			return fileThumb;
		}

		public String getCreatedAt() {
			return createdAt;
		}

	}

}
