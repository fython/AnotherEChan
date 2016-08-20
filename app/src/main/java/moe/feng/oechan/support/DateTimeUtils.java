package moe.feng.oechan.support;

import android.content.Context;
import android.content.res.Resources;
import android.text.format.Time;
import android.text.TextUtils;
import android.text.format.DateFormat;

import moe.feng.oechan.R;

/**
 *	日期时间工具类
 *  提取自 Flyme 辅助工具
 */
public class DateTimeUtils {
	/**
     * 普通列表：
     *  当天："时间"
     *  本周："周几"
     *  本年：“月/日”
     *  往年：“年/月/日”
     */
    public static final int FORMAT_TYPE_NORMAL = 0;

    /**
     * 短信详情列表：
     *  当天："时间"
     *  本周："周几 时间"
     *  本年：“月/日 时间”
     *  往年：“年/月/日”
     */
    public static final int FORMAT_TYPE_MMS = 1;
    
    /**
     * 邮件详情列表
     *  本年：“周几 月/日 时间”
     *  往年：“年/月/日”
     */
    public static final int FORMAT_TYPE_EMAIL = 2;
    
    /**
     * 录音机 备忘录列表
     *  当天：“时间”
     *  本年：“月/日 时间”
     *  往年：“年/月/日”
     */
    public static final int FORMAT_TYPE_RECORDER = 3;

    /**
     * 录音机 通话录音列表
     *  当天：“时间”
     *  本年：“月/日”
     *  往年：“年/月/日”
     */
    public static final int FORMAT_TYPE_RECORDER_PHONE = 4;
    
    /**
     * 通话记录 列表需求
     * 本年：“月/日 时间”
     * 往年：“年/月/日”
     */
    public static final int FORMAT_TYPE_CALL_LOGS = 5;
    
    /**
     * 个人足迹 朋友需求
     * 当天：（时差在1小时内）mm分钟前
     *      （时差在1小时外）mm小时前
     * 昨天：“昨天”
     * 本年：“月/日”
     * 往年：“年/月/日”
     */
    public static final int FORMAT_TYPE_PERSONAL_FOOTPRINT = 6;
    
    /**
     * 版本日期 应用中心需求
     * 本年：“月/日”
     * 往年：“年/月/日”
     */
    public static final int FORMAT_TYPE_APP_VERSIONS = 7;
    
    /**
     * 日历 桌面小工具
     * 本年：“月/日”
     * 往年：“年/月”
     */
    public static final int FORMAT_TYPE_CALENDAR_APPWIDGET = 8;
    
    /**
     * 联系人生日
     * “年/月/日”
     */
    public static final int FORMAT_TYPE_CONTACTS_BIRTHDAY_YMD = 9;
    
    /**
     * 联系人生日
     * “月/日”
     */
    public static final int FORMAT_TYPE_CONTACTS_BIRTHDAY_MD = 10;
    
    /**
     * 通话记录 列表需求
     * 本年：“月/日; 时间”
     * 往年：“年/月/日;时间”
     */
    public static final int FORMAT_TYPE_CALL_LOGS_NEW = 11;

    private static final int MILLISECONDS_OF_HOUR = 60 * 60 * 1000;

	/**
	 * @deprecated use
	 *             {@link #formatTimeStampString(Context, long, int, boolean)}
	 */
	public static String formatTimeStampString(Context context, long when,
			boolean fullFormat) {
		return formatTimeStampString(context, when, FORMAT_TYPE_NORMAL,
				fullFormat);
	}

	/**
	 * @deprecated use {@link #formatTimeStampString(Context, long, int)}
	 * @param type
	 *            日期类型
	 *            <ul>
	 *            <li>{@link #FORMAT_TYPE_NORMAL}</li>
	 *            <li>{@link #FORMAT_TYPE_MMS}</li>
	 *            <li>{@link #FORMAT_TYPE_EMAIL}</li>
	 *            <li>{@link #FORMAT_TYPE_RECORDER}</li>
	 *            </ul>
	 */
	public static String formatTimeStampString(Context context, long when,
			int type, boolean hasTime) {
		return formatTimeStampString(context, when, type);
	}

	private static Time NowTimeLast;
	private static Time ThenTimeLast;
	private static long NowMillisLast = 0;
	private static int FormatTypeLast = -1;
	private static String FormatResultLast = null;

	/**
	 * @param context 
	 * @param when
	 *            毫秒时间数
	 * @param type
	 *            the type of date time {@link #FORMAT_TYPE_NORMAL} <br/>
	 *            {@link #FORMAT_TYPE_MMS} <br/>
	 *            {@link #FORMAT_TYPE_EMAIL} <br/>
	 *            {@link #FORMAT_TYPE_RECORDER} <br/>
	 *            {@link #FORMAT_TYPE_RECORDER_PHONE} <br/>
	 *            {@link #FORMAT_TYPE_CALL_LOGS} <br/>
	 * @return 日期字符串
	 */
	public static String formatTimeStampString(Context context, long when,
			int type) {
		Time then = new Time();
		then.set(when);
		Time now = null;
		Long nowmillis = System.currentTimeMillis();
		boolean is24 = DateFormat.is24HourFormat(context);

		boolean sameType = (type == FormatTypeLast);
		FormatTypeLast = type;

		boolean sameNowDay = false;
		if (NowTimeLast == null) {
			sameNowDay = false;
		} else {
			if (nowmillis >= NowMillisLast
					&& nowmillis < (NowMillisLast + 24 * 60 * 60 * 1000)) {
				sameNowDay = true;
			} else {
				sameNowDay = false;
			}
		}
		if (!sameNowDay) {
			now = new Time();
			now.set(nowmillis);
			NowTimeLast = now;
			NowMillisLast = nowmillis
					- (now.hour * 60 * 60 * 1000 + now.minute * 60 * 1000 + now.second * 1000);
		} else {
			now = NowTimeLast;
		}

		boolean sameWhenDay = false;
		if (ThenTimeLast != null) {
			sameWhenDay = (ThenTimeLast.year == then.year && ThenTimeLast.yearDay == then.yearDay);
		}

		boolean sameWhenMonth = false;
		if (ThenTimeLast != null) {
			sameWhenMonth = (ThenTimeLast.year == then.year && ThenTimeLast.month == then.month);
		}

		ThenTimeLast = then;
		int weekStart = now.yearDay - now.weekDay;
		boolean isBeforeYear = then.year <= now.year;
		boolean isThisYear = (now.year == then.year)
				&& (then.yearDay <= now.yearDay);
		boolean isToday = (isThisYear && (then.yearDay == now.yearDay));
		boolean isYesterday = (isThisYear && (then.yearDay == now.yearDay - 1));
		boolean isThisWeek = (isThisYear && (then.yearDay >= weekStart && then.yearDay < now.yearDay));
		Resources resources = context.getResources();
		String currentTime = "";
		String currentDay = "";
		switch (type) {
		case FORMAT_TYPE_NORMAL:
			if (isToday) {
				if (is24) {
					return then.format(resources
							.getString(R.string.mc_pattern_hour_minute));
				} else {
					return then.format(resources
							.getString(R.string.mc_pattern_hour_minute_12));
				}
			} else if (isThisWeek) {
				if (sameWhenDay && sameType
						&& !TextUtils.isEmpty(FormatResultLast)) {
					return FormatResultLast;
				}
				FormatResultLast = then.format(resources
						.getString(R.string.mc_pattern_week));
				return FormatResultLast;
			} else if (isThisYear) {
				if (sameWhenDay && sameType
						&& !TextUtils.isEmpty(FormatResultLast)) {
					return FormatResultLast;
				}
				FormatResultLast = then.format(resources
						.getString(R.string.mc_pattern_month_day));
				return FormatResultLast;
			} else if (isBeforeYear) {// past
				if (sameWhenDay && sameType
						&& !TextUtils.isEmpty(FormatResultLast)) {
					return FormatResultLast;
				}
				FormatResultLast = then.format(resources
						.getString(R.string.mc_pattern_year_month_day));
				return FormatResultLast;
			} else {

				if (sameWhenDay && sameType
						&& !TextUtils.isEmpty(FormatResultLast)) {
					return FormatResultLast;
				}
				FormatResultLast = then.format(resources
						.getString(R.string.mc_pattern_year_month_day));
				return FormatResultLast;
			}
		case FORMAT_TYPE_MMS:
			if (isToday) {
				if (is24) {
					return then.format(resources
							.getString(R.string.mc_pattern_hour_minute));
				} else {
					return then.format(resources
							.getString(R.string.mc_pattern_hour_minute_12));
				}
			} else if (isThisWeek) {
				if (is24) {
					return then.format(resources
							.getString(R.string.mc_pattern_week_hour_minute));
				} else {
					return then
							.format(resources
									.getString(R.string.mc_pattern_week_hour_minute_12));
				}
			} else if (isThisYear) {
				if (is24) {
					return then
							.format(resources
									.getString(R.string.mc_pattern_month_day_hour_minute));
				} else {
					return then
							.format(resources
									.getString(R.string.mc_pattern_month_day_hour_minute_12));
				}
			} else if (isBeforeYear) {
				if (sameWhenDay && sameType
						&& !TextUtils.isEmpty(FormatResultLast)) {
					return FormatResultLast;
				}
				FormatResultLast = then.format(resources
						.getString(R.string.mc_pattern_year_month_day));
				return FormatResultLast;
			} else {
				if (is24) {
					return then
							.format(resources
									.getString(R.string.mc_pattern_year_month_day_hour_minute));
				} else {
					return then
							.format(resources
									.getString(R.string.mc_pattern_year_month_day_hour_minute_12));
				}
			}
		case FORMAT_TYPE_EMAIL:
			if (isThisYear) {
				if (is24) {
					return then
							.format(resources
									.getString(R.string.mc_pattern_week_month_day_hour_minute));
				} else {
					return then
							.format(resources
									.getString(R.string.mc_pattern_week_month_day_hour_minute_12));
				}
			} else if (isBeforeYear) {
				if (sameWhenDay && sameType
						&& !TextUtils.isEmpty(FormatResultLast)) {
					return FormatResultLast;
				}
				FormatResultLast = then.format(resources
						.getString(R.string.mc_pattern_year_month_day));
				return FormatResultLast;
			} else {
				return then
						.format(resources
								.getString(R.string.mc_pattern_year_month_day_hour_minute));
			}
		case FORMAT_TYPE_RECORDER:
			if (isToday) {
				if (is24) {
					return then.format(resources
							.getString(R.string.mc_pattern_hour_minute));
				} else {
					return then.format(resources
							.getString(R.string.mc_pattern_hour_minute_12));
				}
			} else if (isThisYear) {
				if (is24) {
					return then
							.format(resources
									.getString(R.string.mc_pattern_month_day_hour_minute));
				} else {
					return then
							.format(resources
									.getString(R.string.mc_pattern_month_day_hour_minute_12));
				}
			} else if (isBeforeYear) {
				if (sameWhenDay && sameType
						&& !TextUtils.isEmpty(FormatResultLast)) {
					return FormatResultLast;
				}
				FormatResultLast = then.format(resources
						.getString(R.string.mc_pattern_year_month_day));
				return FormatResultLast;
			} else {
				if (is24) {
					return then
							.format(resources
									.getString(R.string.mc_pattern_year_month_day_hour_minute));
				} else {
					return then
							.format(resources
									.getString(R.string.mc_pattern_year_month_day_hour_minute_12));
				}
			}
		case FORMAT_TYPE_RECORDER_PHONE:
			if (isToday) {
				if (is24) {
					return then.format(resources
							.getString(R.string.mc_pattern_hour_minute));
				} else {
					return then.format(resources
							.getString(R.string.mc_pattern_hour_minute_12));
				}
			} else if (isThisYear) {
				if (sameWhenDay && sameType
						&& !TextUtils.isEmpty(FormatResultLast)) {
					return FormatResultLast;
				}
				FormatResultLast = then.format(resources
						.getString(R.string.mc_pattern_month_day));
				return FormatResultLast;
			} else if (isBeforeYear) {
				if (sameWhenDay && sameType
						&& !TextUtils.isEmpty(FormatResultLast)) {
					return FormatResultLast;
				}
				FormatResultLast = then.format(resources
						.getString(R.string.mc_pattern_year_month_day));
				return FormatResultLast;
			} else {
				if (sameWhenDay && sameType
						&& !TextUtils.isEmpty(FormatResultLast)) {
					return FormatResultLast;
				}
				FormatResultLast = then.format(resources
						.getString(R.string.mc_pattern_year_month_day));
				return FormatResultLast;
			}
		case FORMAT_TYPE_CALL_LOGS:
			if (isThisYear) {
				if (is24) {
					return then
							.format(resources
									.getString(R.string.mc_pattern_month_day_hour_minute));
				} else {
					return then
							.format(resources
									.getString(R.string.mc_pattern_month_day_hour_minute_12));
				}
			} else if (isBeforeYear) {
				if (sameWhenDay && sameType
						&& !TextUtils.isEmpty(FormatResultLast)) {
					return FormatResultLast;
				}
				FormatResultLast = then.format(resources
						.getString(R.string.mc_pattern_year_month_day));
				return FormatResultLast;
			} else {
				if (sameWhenDay && sameType
						&& !TextUtils.isEmpty(FormatResultLast)) {
					return FormatResultLast;
				}
				if (is24) {
					FormatResultLast = then
							.format(resources
									.getString(R.string.mc_pattern_year_month_day_hour_minute));
				} else {
					FormatResultLast = then
							.format(resources
									.getString(R.string.mc_pattern_year_month_day_hour_minute_12));
				}
				return FormatResultLast;
			}
		case FORMAT_TYPE_PERSONAL_FOOTPRINT:
			if (isToday) {
				long offsetMilliSecounds = nowmillis - when;
				int returnValue;
				if (offsetMilliSecounds >= MILLISECONDS_OF_HOUR) {
					returnValue = (int) offsetMilliSecounds / 60 / 60 / 1000;
					if (returnValue == 1) {
						return resources
								.getString(R.string.mc_pattern_a_hour_before);
					} else {
						return resources.getString(
								R.string.mc_pattern_hour_before).replace(",",
								String.valueOf(returnValue));
					}
				} else {
					returnValue = (int) offsetMilliSecounds / 60 / 1000;
					if (returnValue <= 1) {
						return resources
								.getString(R.string.mc_pattern_a_minute_before);
					} else {
						return resources.getString(
								R.string.mc_pattern_minute_before).replace(",",
								String.valueOf(returnValue));
					}
				}
			} else if (isYesterday) {
				return resources.getString(R.string.mc_pattern_yesterday) + " " + then.format("%H:%M");
			} else if (isThisYear) {
				if (sameWhenDay && sameType
						&& !TextUtils.isEmpty(FormatResultLast)) {
					return FormatResultLast;
				}
				FormatResultLast = then.format(resources
						.getString(R.string.mc_pattern_month_day) + " %H:%M");
				return FormatResultLast;
			} else if (isBeforeYear) {
				if (sameWhenDay && sameType
						&& !TextUtils.isEmpty(FormatResultLast)) {
					return FormatResultLast;
				}
				FormatResultLast = then.format(resources
						.getString(R.string.mc_pattern_year_month_day));
				return FormatResultLast;
			} else {
				if (sameWhenDay && sameType
						&& !TextUtils.isEmpty(FormatResultLast)) {
					return FormatResultLast;
				}
				FormatResultLast = then.format(resources
						.getString(R.string.mc_pattern_year_month_day));
				return FormatResultLast;
			}
		case FORMAT_TYPE_APP_VERSIONS:
			if (sameWhenDay && sameType && !TextUtils.isEmpty(FormatResultLast)) {
				return FormatResultLast;
			}
			if (isThisYear) {
				FormatResultLast = then.format(resources
						.getString(R.string.mc_pattern_month_day));
				return FormatResultLast;
			} else {
				FormatResultLast = then.format(resources
						.getString(R.string.mc_pattern_year_month_day));
				return FormatResultLast;
			}
		case FORMAT_TYPE_CALENDAR_APPWIDGET:
			if (now.year == then.year) {
				if (sameWhenDay && sameType
						&& !TextUtils.isEmpty(FormatResultLast)) {
					return FormatResultLast;
				}
				FormatResultLast = then.format(resources
						.getString(R.string.mc_pattern_month_day));
				return FormatResultLast;
			} else {
				if (sameWhenMonth && sameType
						&& !TextUtils.isEmpty(FormatResultLast)) {
					return FormatResultLast;
				}
				FormatResultLast = then.format(resources
						.getString(R.string.mc_pattern_year_month));
				return FormatResultLast;
			}
		case FORMAT_TYPE_CONTACTS_BIRTHDAY_YMD:
			if (sameWhenDay && sameType && !TextUtils.isEmpty(FormatResultLast)) {
				return FormatResultLast;
			}
			FormatResultLast = then.format(resources
					.getString(R.string.mc_pattern_year_month_day));
			return FormatResultLast;
		case FORMAT_TYPE_CONTACTS_BIRTHDAY_MD:
			if (sameWhenDay && sameType && !TextUtils.isEmpty(FormatResultLast)) {
				return FormatResultLast;
			}
			FormatResultLast = then.format(resources
					.getString(R.string.mc_pattern_month_day));
			return FormatResultLast;
		case FORMAT_TYPE_CALL_LOGS_NEW:
			if (is24) {
				currentTime = then.format(resources
						.getString(R.string.mc_pattern_hour_minute));
			} else {
				currentTime = then.format(resources
						.getString(R.string.mc_pattern_hour_minute_12));
			}
			if (isToday) {
				currentDay = resources.getString(R.string.mc_pattern_today);
			} else if (isThisWeek) {
				if (sameWhenDay && sameType
						&& !TextUtils.isEmpty(FormatResultLast)) {
					currentDay = FormatResultLast;
				}
				FormatResultLast = then.format(resources
						.getString(R.string.mc_pattern_week));
				currentDay = FormatResultLast;
			} else if (isThisYear) {
				if (sameWhenDay && sameType
						&& !TextUtils.isEmpty(FormatResultLast)) {
					currentDay = FormatResultLast;
				}
				FormatResultLast = then.format(resources
						.getString(R.string.mc_pattern_month_day));
				currentDay = FormatResultLast;
			} else if (isBeforeYear) {
				if (sameWhenDay && sameType
						&& !TextUtils.isEmpty(FormatResultLast)) {
					currentDay = FormatResultLast;
				}
				FormatResultLast = then.format(resources
						.getString(R.string.mc_pattern_year_month_day));
				currentDay = FormatResultLast;
			} else {
				if (sameWhenDay && sameType
						&& !TextUtils.isEmpty(FormatResultLast)) {
					currentDay = FormatResultLast;
				}
				FormatResultLast = then.format(resources
						.getString(R.string.mc_pattern_year_month_day));
				currentDay = FormatResultLast;
			}
			return currentDay + ";" + currentTime;
		}
		return null;
	}
}
