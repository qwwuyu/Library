package com.qwwuyu.example.configs;

/**
 * 常量
 */
public class Constant {
    /* ======================== http code ======================== */
    /** 处理请求成功 */
    public static final int HTTP_SUC = 1;
    /** 处理请求失败 */
    public static final int HTTP_ERR = -1;
    /** 网络请求成功,数据解析失败状态码 */
    public static final int HTTP_DATA_ERR = Integer.MIN_VALUE + 2;
    /** 网络请求失败无状态码,有友好提示语 */
    public static final int HTTP_NO_CODE = Integer.MIN_VALUE;
    /** 网络请求失败无状态码,无处理提示语 */
    public static final int HTTP_NO_MSG = Integer.MIN_VALUE + 1;

    /* ======================== SP Key ======================== */
    // 数字
    /** app版本号 */
    public static final String SP_APP_VERSION = "SP_APP_VERSION";
    /** 升级不再提示的新版本号 */
    public static final String SP_HINT_UPDATE = "SP_HINT_UPDATE";
    // boolean
    /** 第一次请求权限 */
    public static final String SP_FIRST_PERMIT = "SP_FIRST_PERMIT";
    /** 请求权限弹窗不再提示 */
    public static final String SP_HINT_REQUEST = "SP_HINT_REQUEST";
    /** 请求权限拒绝不再提示 */
    public static final String SP_HINT_DENIED = "SP_HINT_DENIED";
    // 字符串
    /** 用户信息 */
    public static final String SP_ACCOUNT = "SP_ACCOUNT";

    /* ======================== RequestCode ======================== */

    /* ======================== Intent Key ======================== */
    /** 数据Bean */
    public static final String INTENT_BEAN = "INTENT_BEAN";
    /** 枚举 */
    public static final String INTENT_ENUM = "INTENT_ENUM";
    /** int数字 */
    public static final String INTENT_INT = "INTENT_INT";
    /** int类型 */
    public static final String INTENT_INT_TYPE = "INTENT_INT_TYPE";
    /** int类型2 */
    public static final String INTENT_INT_TYPE2 = "INTENT_INT_TYPE2";
    /** 字符串 */
    public static final String INTENT_STRING = "INTENT_STRING";

    /* ======================== 本地和服务器常量 ======================== */

}
