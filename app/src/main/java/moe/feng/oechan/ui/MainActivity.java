package moe.feng.oechan.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import moe.feng.oechan.R;
import moe.feng.oechan.api.HomeApi;
import moe.feng.oechan.dao.FavouritesManager;
import moe.feng.oechan.dao.HomePageKeeper;
import moe.feng.oechan.model.BaseMessage;
import moe.feng.oechan.model.PageListResult;
import moe.feng.oechan.ui.adapter.PageListAdapter;
import moe.feng.oechan.ui.callback.OnItemClickListener;
import moe.feng.oechan.ui.common.AbsActivity;

public class MainActivity extends AbsActivity
		implements SwipeRefreshLayout.OnRefreshListener, OnItemClickListener {

	private RecyclerView mListView;
	private SwipeRefreshLayout mRefreshLayout;

	private PageListAdapter mAdapter;

	private HomePageKeeper mPageKeeper;
	private FavouritesManager mFavManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

	    mPageKeeper = HomePageKeeper.getInstance(getApplicationContext());
	    mFavManager = FavouritesManager.getInstance(getApplicationContext());

	    mListView = $(R.id.recycler_view);
	    mRefreshLayout = $(R.id.refresh_layout);

	    setUpRecyclerView();
	    setUpRefreshLayout();

	    if (mPageKeeper.list() != null && mPageKeeper.size() > 0) {
		    mPageKeeper.buildFormattedText(this);
		    mAdapter.setData(mPageKeeper.list());
		    mAdapter.notifyDataSetChanged();
		    if (System.currentTimeMillis() - mPageKeeper.getUpdatedMiles() > 60 * 60 * 1000) {
			    onRefresh();
		    }
	     } else {
		    onRefresh();
	    }
    }

	private void setUpRefreshLayout() {
		mRefreshLayout.setColorSchemeResources(R.color.teal_a700);
		mRefreshLayout.setOnRefreshListener(this);
	}

	private void setUpRecyclerView() {
		mAdapter = new PageListAdapter();
		mAdapter.setFavouritesCallback(mFavManager);
		mAdapter.setOnItemClickListener(this);
		mListView.setHasFixedSize(false);
		mListView.setAdapter(mAdapter);
		mListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);
				checkLoadNextPage();
			}

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
				checkLoadNextPage();

			}

			private void checkLoadNextPage() {
				if (mRefreshLayout.isRefreshing()) return;
				if (mListView.getLayoutManager() instanceof LinearLayoutManager) {
					LinearLayoutManager lm = (LinearLayoutManager) mListView.getLayoutManager();
					if (lm.findLastVisibleItemPosition() >= mAdapter.getItemCount() - 2) {
						loadNextPage();
					}
				}
			}
		});
	}

	private void loadNextPage() {
		mRefreshLayout.setRefreshing(true);
		mPageKeeper.setNowPage(mPageKeeper.getNowPage() + 1);
		new GetPageTask().execute(mPageKeeper.getNowPage());
	}

	@Override
	protected void onStop() {
		super.onStop();
		mPageKeeper.save();
		mFavManager.save();
	}

	@Override
	public void onRefresh() {
		if (!mRefreshLayout.isRefreshing()) {
			mRefreshLayout.setRefreshing(true);
		}

		mPageKeeper.setNowPage(1);
		new GetPageTask().execute(1);
	}

	@Override
	public void onItemClick(RecyclerView.ViewHolder holder, int position, Object data) {
		if (data instanceof PageListResult.Item) {
			DetailsActivity.launch(this, (PageListResult.Item) data, position);
		}
	}

	private class GetPageTask extends AsyncTask<Integer, Void, BaseMessage<PageListResult>> {

		@Override
		protected BaseMessage<PageListResult> doInBackground(Integer... integers) {
			BaseMessage<PageListResult> result = HomeApi.getPage("zh-TW", integers[0] - 1);
			if (result.getCode() == BaseMessage.CODE_OKAY) {
				result.getData().buildFormattedText(MainActivity.this);
			}
			return result;
		}

		@Override
		protected void onPostExecute(BaseMessage<PageListResult> result) {
			if (isDestroyed() || isFinishing()) return;

			mRefreshLayout.setRefreshing(false);

			if (result.getCode() == BaseMessage.CODE_OKAY) {
				mPageKeeper.setUpdatedMiles(System.currentTimeMillis());
				if (mPageKeeper.getNowPage() == 1) {
					mPageKeeper.clear();
					mPageKeeper.addAll(result.getData().getList());
					mAdapter.setData(result.getData().getList());
				} else {
					mPageKeeper.addAll(result.getData().getList());
					mAdapter.addData(result.getData().getList());
				}
				mAdapter.notifyDataSetChanged();
			}
		}

	}

}