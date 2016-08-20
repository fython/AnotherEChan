package moe.feng.oechan.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.GridLayoutAnimationController;

public class GridRecyclerView extends RecyclerView {

	public GridRecyclerView(Context context) {
		super(context);
	}

	public GridRecyclerView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
	}

	public GridRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void attachLayoutAnimationParameters(View child, ViewGroup.LayoutParams params, int index, int count) {
		GridLayoutAnimationController.AnimationParameters animationParams = (GridLayoutAnimationController.AnimationParameters) params.layoutAnimationParameters;

		if (animationParams == null) {
			animationParams = new GridLayoutAnimationController.AnimationParameters();
			params.layoutAnimationParameters = animationParams;
		}

		int columns = ((GridLayoutManager) getLayoutManager()).getSpanCount();

		animationParams.count = count;
		animationParams.index = index;
		animationParams.columnsCount = columns;
		animationParams.rowsCount = count / columns;

		final int invertedIndex = count - 1 - index;
		animationParams.column = columns - 1 - (invertedIndex % columns);
		animationParams.row = animationParams.rowsCount - 1 - invertedIndex / columns;
	}

}
