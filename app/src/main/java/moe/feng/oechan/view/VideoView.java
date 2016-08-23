package moe.feng.oechan.view;

import android.content.Context;
import android.util.AttributeSet;

public class VideoView extends io.vov.vitamio.widget.VideoView {

	public VideoView(Context context) {
		super(context);
	}

	public VideoView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public VideoView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean isInPlaybackState() {
		return super.isInPlaybackState();
	}

}
