package moe.feng.oechan.ui.callback;

import moe.feng.oechan.model.PageListResult;

public interface FavouritesCallback {

	boolean isFavourite(PageListResult.Item item);
	void addFavourite(PageListResult.Item item);
	void removeFavourite(PageListResult.Item item);

}