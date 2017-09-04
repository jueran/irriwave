package com.irrigate.constant;

/**
 * Created by xinyuanzhong on 2017/7/14.
 */

public final class EventFlags {

    private EventFlags() {
    }

    // 个人资料更新成功，发出方：UserSingleton
    public static final String UPDATE_USER_MESSAGE = "UPDATE_USER_MESSAGE";

    // 更新词库的eventBus
    public static final String UPDATE_LEXICON = "UPDATE_LEXICON";

    // 全部通关后检查完最后一关的单词
    public static final String CHECK_WORDS_ACTIVITY_SECTIONS_ALL_OVER =
            "CHECK_WORDS_ACTIVITY_SECTIONS_ALL_OVER";

    /**
     * 发出方：退出登录如路
     */
    public static final class LoginStateBroadcast {
        public final static String LOGIN_SUCCESS = "login_success";
        public final static String LOGOUT = "LOGOUT";
    }

    public static final class RestReviewWordsAmount {
        public int amount;
    }
}