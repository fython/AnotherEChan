package moe.feng.oechan.ui.video;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.MediaController;
import moe.feng.oechan.R;
import moe.feng.oechan.model.VideoUrl;
import moe.feng.oechan.support.FullScreenHelper;
import moe.feng.oechan.ui.common.AbsActivity;
import moe.feng.oechan.view.VideoView;

public class PlayerActivity extends AbsActivity
		implements MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener {

	private String mTitle;
	private VideoUrl mVideoUrl;

	private VideoView mVideoView;
	private ProgressBar mProgressBar;

	private FullScreenHelper mFullScreenHelper;

	boolean isPlaying = false, isPrepared = true;

	private static final String EXTRA_TITLE = "extra_title", EXTRA_VIDEO_SRC = "extra_video_src";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player);

		mFullScreenHelper = new FullScreenHelper(this);
		mFullScreenHelper.setFullScreen(true);

		/** Get data from intent */
		Intent intent = getIntent();
		mTitle = intent.getStringExtra(EXTRA_TITLE);
		mVideoUrl = VideoUrl.fromJson(intent.getStringExtra(EXTRA_VIDEO_SRC));

		mVideoView = $(R.id.player_view);
		mProgressBar = $(R.id.progressBar);

		setUpMediaPlayer();

		mVideoView.setVideoURI(Uri.parse(mVideoUrl.getUrl(VideoUrl.QUALITY_ORIGINAL)));
	}

	private void setUpMediaPlayer() {
		MediaController mc = new MediaController(this);

		mVideoView.setMediaController(mc);
		mVideoView.setOnPreparedListener(this);
		mVideoView.setOnErrorListener(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		if (mVideoView.isPlaying()) {
			mVideoView.pause();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mVideoView.isInPlaybackState()) {
			mVideoView.resume();
		}
	}

	public static void launch(AbsActivity activity, String title, VideoUrl videoUrl) {
		Intent intent = new Intent(activity, PlayerActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(EXTRA_TITLE, title);
		intent.putExtra(EXTRA_VIDEO_SRC, videoUrl.toJsonString());
		activity.startActivity(intent);
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		mProgressBar.setVisibility(View.GONE);
		return false;
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		isPrepared = true;
		mVideoView.start();
		mProgressBar.setVisibility(View.GONE);
	}

}
