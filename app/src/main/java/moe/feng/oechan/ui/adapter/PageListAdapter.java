package moe.feng.oechan.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import moe.feng.oechan.R;
import moe.feng.oechan.model.PageListResult;
import moe.feng.oechan.ui.callback.FavouritesCallback;

public class PageListAdapter extends RecyclerView.Adapter<PageListAdapter.ItemHolder> {

	private List<PageListResult.Item> data;

	private FavouritesCallback favouritesCallback;

	public PageListAdapter() {
		data = new ArrayList<>();
	}

	public void setData(List<PageListResult.Item> data) {
		this.data = data;
	}

	public void addData(Collection<? extends PageListResult.Item> data) {
		this.data.addAll(data);
	}

	public void setFavouritesCallback(FavouritesCallback callback) {
		this.favouritesCallback = callback;
	}

	@Override
	public PageListAdapter.ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new ItemHolder(
				LayoutInflater.from(parent.getContext())
						.inflate(R.layout.item_page_list, parent, false)
		);
	}

	@Override
	public void onBindViewHolder(PageListAdapter.ItemHolder holder, int position) {
		PageListResult.Item item = data.get(position);
		holder.titleText.setText(item.getName());
		holder.updatedAtText.setText(item.getFormattedUpdatedAt());
		if (favouritesCallback != null) {
			holder.checkBox.setChecked(favouritesCallback.isFavourite(item));
		}
	}

	@Override
	public int getItemCount() {
		return data.size();
	}

	class ItemHolder extends RecyclerView.ViewHolder {

		TextView titleText, updatedAtText;
		CheckBox checkBox;

		ItemHolder(View itemView) {
			super(itemView);

			titleText = (TextView) itemView.findViewById(R.id.tv_title);
			updatedAtText = (TextView) itemView.findViewById(R.id.tv_updated_at);
			checkBox = (CheckBox) itemView.findViewById(R.id.checkbox_fav);

			titleText.getPaint().setFakeBoldText(true);

			itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					PageListResult.Item item = data.get(getAdapterPosition());
				}
			});
			checkBox.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					if (favouritesCallback != null) {
						PageListResult.Item item = data.get(getAdapterPosition());
						if (!checkBox.isChecked()) {
							favouritesCallback.removeFavourite(item);
						} else {
							favouritesCallback.addFavourite(item);
						}
					}
				}
			});
		}

	}

}
