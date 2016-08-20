package moe.feng.oechan.dao;

import android.content.Context;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import moe.feng.oechan.model.PageListResult;
import moe.feng.oechan.support.FileUtils;
import moe.feng.oechan.support.GsonUtils;

public class HomePageKeeper {

	private MyArray list;
	private Context context;

	private static final String FILE_NAME = "home.json";

	private static HomePageKeeper sInstance;

	public static HomePageKeeper getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new HomePageKeeper(context);
			sInstance.reload();
		}
		return sInstance;
	}

	private HomePageKeeper(Context context) {
		this.context = context;
	}

	public void reload() {
		String json;
		try {
			json = FileUtils.readStringFromFile(context, FILE_NAME);
		} catch (IOException e) {
			e.printStackTrace();
			json = "{\"data\":[]}";
		}

		list = GsonUtils.fromJson(json, MyArray.class);
	}

	public PageListResult.Item get(int index) {
		return list.data.get(index);
	}

	public void add(PageListResult.Item item) {
		list.data.add(item);
	}

	public void addAll(Collection<? extends PageListResult.Item> newList) {
		list.data.addAll(newList);
	}

	public void set(int index, PageListResult.Item item) {
		list.data.set(index, item);
	}

	public void clear() {
		list.data.clear();
	}

	public int size() {
		return list.data.size();
	}

	public int indexOf(int id) {
		for (int i = 0; i < size(); i++) {
			if (get(i).getId() == id) return i;
		}
		return -1;
	}

	public int indexOf(PageListResult.Item item) {
		return indexOf(item.getId());
	}

	public boolean contains(int id) {
		return indexOf(id) != -1;
	}

	public boolean contains(PageListResult.Item item) {
		return contains(item.getId());
	}

	public List<PageListResult.Item> list() {
		return list.data;
	}

	public int getNowPage() {
		return list.nowPage;
	}

	public void setNowPage(int nowPage) {
		list.nowPage = nowPage;
	}

	public long getUpdatedMiles() {
		return list.updateTime;
	}

	public void setUpdatedMiles(long updatedMiles) {
		list.updateTime = updatedMiles;
	}

	public void buildFormattedText(Context context) {
		for (PageListResult.Item item : list.data) {
			item.buildFormattedUpdatedAt(context);
		}
	}

	public void save() {
		try {
			FileUtils.saveStringToFile(context, FILE_NAME, GsonUtils.toJson(list));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private class MyArray {

		private List<PageListResult.Item> data;
		private int nowPage = 1;
		private long updateTime;

	}

}
