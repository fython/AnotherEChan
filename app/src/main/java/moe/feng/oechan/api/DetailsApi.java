package moe.feng.oechan.api;

import android.annotation.SuppressLint;

import moe.feng.oechan.model.BaseMessage;
import moe.feng.oechan.model.DetailsResult;
import moe.feng.oechan.model.VideoUrl;
import moe.feng.oechan.support.HttpUtils;

@SuppressLint("DefaultLocale")
public class DetailsApi {

	private static final String GET_DETAILS_URL = "https://oneechan.moe/%1$s/api/detail?id=%2$d";
	private static final String GET_VIDEO_URL = "https://oneechan.moe/%1$s/api/watch?id=%2$d&set=%3$d";

	public static BaseMessage<DetailsResult> getDetails(String preferLang, int id) {
		BaseMessage<String> strMsg = HttpUtils.getString(
				String.format(GET_DETAILS_URL, preferLang, id), false
		);
		BaseMessage<DetailsResult> result = new BaseMessage<>();
		if (strMsg.getCode() == BaseMessage.CODE_OKAY) {
			result.setData(DetailsResult.fromJson(strMsg.getData()));
			result.setCode(BaseMessage.CODE_OKAY);
		} else {
			result.setCode(BaseMessage.CODE_ERROR);
		}
		return result;
	}

	public static BaseMessage<VideoUrl> getVideoUrl(String preferLang, int id, int set) {
		BaseMessage<String> strMsg = HttpUtils.getString(
				String.format(GET_VIDEO_URL, preferLang, id, set), false
		);
		BaseMessage<VideoUrl> result = new BaseMessage<>();
		if (strMsg.getCode() == BaseMessage.CODE_OKAY) {
			result.setData(VideoUrl.fromJson(strMsg.getData()));
			result.setCode(BaseMessage.CODE_OKAY);
		} else {
			result.setCode(BaseMessage.CODE_ERROR);
		}
		return result;
	}

}
