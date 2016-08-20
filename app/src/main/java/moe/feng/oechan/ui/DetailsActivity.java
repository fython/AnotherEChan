package moe.feng.oechan.ui;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import moe.feng.oechan.R;
import moe.feng.oechan.api.DetailsApi;
import moe.feng.oechan.model.BaseMessage;
import moe.feng.oechan.model.DetailsResult;
import moe.feng.oechan.model.PageListResult;
import moe.feng.oechan.support.GsonUtils;
import moe.feng.oechan.ui.adapter.DetailsGridAdapter;
import moe.feng.oechan.ui.common.AbsActivity;

public class DetailsActivity extends AbsActivity implements SwipeRefreshLayout.OnRefreshListener {

	private PageListResult.Item data;

	private RecyclerView mListView;
	private SwipeRefreshLayout mRefreshLayout;

	private GridLayoutManager mLayoutManager;
	private DetailsGridAdapter mAdapter;

	private static final String EXTRA_DATA_JSON = "extra_data_json", EXTRA_POSITION = "extra_position";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);

		/** Get data from intent */
		Intent intent = getIntent();
		data = GsonUtils.fromJson(intent.getStringExtra(EXTRA_DATA_JSON), PageListResult.Item.class);

		setTitle(data.getName());
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		ActivityManager.TaskDescription desc = new ActivityManager.TaskDescription(data.getName());
		setTaskDescription(desc);

		mListView = $(R.id.recycler_view);
		mRefreshLayout = $(R.id.refresh_layout);

		setUpRecyclerView();
		setUpRefreshLayout();

		onRefresh();
	}

	private void setUpRefreshLayout() {
		mRefreshLayout.setColorSchemeResources(R.color.teal_a700);
		mRefreshLayout.setOnRefreshListener(this);
	}

	private void setUpRecyclerView() {
		mAdapter = new DetailsGridAdapter();
		mLayoutManager = new GridLayoutManager(this, 2);
		mListView.setHasFixedSize(true);
		mListView.setLayoutManager(mLayoutManager);
		mListView.setAdapter(mAdapter);
	}

	public static void launch(AbsActivity activity, PageListResult.Item data, int position) {
		Intent intent = new Intent(activity, DetailsActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
		intent.putExtra(EXTRA_DATA_JSON, GsonUtils.toJson(data));
		intent.putExtra(EXTRA_POSITION, position);
		activity.startActivity(intent);
	}

	@Override
	public void onRefresh() {
		if (!mRefreshLayout.isRefreshing()) {
			mRefreshLayout.setRefreshing(true);
		}

		new GetDetailsTask().execute();
	}

	private class GetDetailsTask extends AsyncTask<Void, Void, BaseMessage<DetailsResult>> {

		@Override
		protected BaseMessage<DetailsResult> doInBackground(Void... voids) {
			return DetailsApi.getDetails("zh-TW", data.getId());
		}

		@Override
		protected void onPostExecute(BaseMessage<DetailsResult> result) {
			if (isFinishing() || isDestroyed()) return;
			mRefreshLayout.setRefreshing(false);
			if (result.getCode() == BaseMessage.CODE_OKAY) {
				mAdapter.setData(result.getData().getList());
				mAdapter.notifyDataSetChanged();
			}
		}

	}

}
