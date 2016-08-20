package moe.feng.oechan.api;

import android.annotation.SuppressLint;

import moe.feng.oechan.model.BaseMessage;
import moe.feng.oechan.model.PageListResult;
import moe.feng.oechan.support.HttpUtils;

@SuppressLint("DefaultLocale")
public class HomeApi {

	private static final String GET_PAGE_URL = "https://oneechan.moe/%1$s/api/list?page=%2$d";

	public static BaseMessage<PageListResult> getPage(String preferLang, int page) {
		BaseMessage<String> strMsg = HttpUtils.getString(
				String.format(GET_PAGE_URL, preferLang, page), false
		);
		BaseMessage<PageListResult> result = new BaseMessage<>();
		if (strMsg.getCode() == BaseMessage.CODE_OKAY) {
			result.setData(PageListResult.fromJson(strMsg.getData()));
			result.setCode(BaseMessage.CODE_OKAY);
		} else {
			result.setCode(BaseMessage.CODE_ERROR);
		}
		return result;
	}

}
