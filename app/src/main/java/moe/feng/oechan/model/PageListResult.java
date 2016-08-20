package moe.feng.oechan.model;

import android.annotation.SuppressLint;
import android.content.Context;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import moe.feng.oechan.support.DateTimeUtils;
import moe.feng.oechan.support.GsonUtils;

public class PageListResult {

	private int currentPage, maxPage, itemCount, maxCount;
	private List<PageListResult.Item> list;

	public int getCurrentPage() {
		return currentPage;
	}

	public int getMaxPage() {
		return maxPage;
	}

	public int getItemCount() {
		return itemCount;
	}

	public int getMaxCount() {
		return maxCount;
	}

	public List<Item> getList() {
		return list;
	}

	public String toJsonString() {
		return GsonUtils.toJson(this);
	}

	public void buildFormattedText(Context context) {
		for (Item item : list) {
			item.buildFormattedUpdatedAt(context);
		}
	}

	public static PageListResult fromJson(String json) {
		return GsonUtils.fromJson(json, PageListResult.class);
	}

	public class Item {

		private String name;
		@SerializedName("updated_At")
		private String updatedAt;
		private int id;

		private String formattedUpdatedAt;

		public String getName() {
			return name;
		}

		public String getUpdatedAt() {
			return updatedAt;
		}

		public String getFormattedUpdatedAt() {
			return formattedUpdatedAt;
		}

		@SuppressLint("SimpleDateFormat")
		public void buildFormattedUpdatedAt(Context context) {
			String time = updatedAt.substring(0, 19);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
			Calendar c = GregorianCalendar.getInstance();
			try {
				c.setTime(formatter.parse(time));
				c.setTimeZone(TimeZone.getDefault());
				formattedUpdatedAt = DateTimeUtils.formatTimeStampString(
						context,
						c.getTimeInMillis(),
						DateTimeUtils.FORMAT_TYPE_PERSONAL_FOOTPRINT
				);
			} catch (ParseException e) {
				e.printStackTrace();
				formattedUpdatedAt = time.replace("T", " ");
			}
		}

		public int getId() {
			return id;
		}

	}

}
