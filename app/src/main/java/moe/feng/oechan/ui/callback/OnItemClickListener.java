package moe.feng.oechan.ui.callback;

import android.support.v7.widget.RecyclerView;

public interface OnItemClickListener {

	void onItemClick(RecyclerView.ViewHolder holder, int position, Object data);

}
