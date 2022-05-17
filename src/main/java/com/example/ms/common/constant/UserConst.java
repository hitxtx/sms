package com.example.ms.common.constant;

/**
 * 用户常量
 */
public class UserConst {

    /**
     * Account default failed count
     */
    public static final int DEFAULT_FAILED_COUNT = 0;

    /**
     * Input your password error count
     */
    public static final int MAX_FAILED_COUNT = 3;

    /**
     * When your failed over MAX_FAILED_COUNT, your account will be locked for a while (24h)
     */
    public static final long LOCK_TIME_DURATION = 24 * 60 * 60 * 1000;
}
