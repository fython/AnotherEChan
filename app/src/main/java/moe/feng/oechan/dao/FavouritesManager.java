package moe.feng.oechan.dao;

import android.content.Context;

import java.io.IOException;
import java.util.List;

import moe.feng.oechan.model.PageListResult;
import moe.feng.oechan.support.FileUtils;
import moe.feng.oechan.support.GsonUtils;
import moe.feng.oechan.ui.callback.FavouritesCallback;

public class FavouritesManager implements FavouritesCallback {

	private MyArray list;
	private Context context;

	private static final String FILE_NAME = "fav.json";

	private static FavouritesManager sInstance;

	public static FavouritesManager getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new FavouritesManager(context);
			sInstance.reload();
		}
		return sInstance;
	}

	private FavouritesManager(Context context) {
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

	public void set(int index, PageListResult.Item item) {
		list.data.set(index, item);
	}

	public void remove(PageListResult.Item item) {
		if (contains(item)) {
			remove(indexOf(item));
		}
	}

	public PageListResult.Item remove(int index) {
		return list.data.remove(index);
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

	public void save() {
		try {
			FileUtils.saveStringToFile(context, FILE_NAME, GsonUtils.toJson(list));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public boolean isFavourite(PageListResult.Item item) {
		return contains(item);
	}

	@Override
	public void addFavourite(PageListResult.Item item) {
		add(item);
	}

	@Override
	public void removeFavourite(PageListResult.Item item) {
		remove(item);
	}

	private class MyArray {

		private List<PageListResult.Item> data;

	}

}
