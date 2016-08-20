package moe.feng.oechan.ui.common;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import moe.feng.oechan.R;

public class AbsActivity extends AppCompatActivity {

	private Toolbar mToolbar;

	@Override
	public void setContentView(@LayoutRes int layoutResId) {
		super.setContentView(layoutResId);
		try {
			mToolbar = (Toolbar) findViewById(R.id.toolbar);
			setSupportActionBar(mToolbar);
		} catch (NullPointerException e) {
			// Ignore NPE
		}
	}

	public Toolbar getToolbar() {
		return mToolbar;
	}

	public <T extends View> T $(@IdRes int id) {
		return (T) findViewById(id);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		int id = menuItem.getItemId();
		if (id == android.R.id.home) {
			onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(menuItem);
	}

}
