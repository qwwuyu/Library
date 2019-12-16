package com.qwwuyu.lib.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.qwwuyu.lib.base.BaseActivity;
import com.qwwuyu.library.R;


/**
 * 统一Activity启动关闭工具类
 */
public class IntentUtils {
    public static void finish(BaseActivity activity) {
        if (activity.isTaskRoot()) {
            activity.finishSuper();
        } else {
            activity.finishSuper();
            activity.overridePendingTransition(R.anim.exit_enter, R.anim.exit_exit);
        }
    }

    public static void finishDefault(BaseActivity activity) {
        activity.finishSuper();
    }

    public static void goActivity(Context context, Intent intent) {
        goActivity(context, intent, -1, false, true);
    }

    public static void goActivityFinish(Context context, Intent intent) {
        goActivity(context, intent, -1, true, true);
    }

    public static void goActivityForResult(Context context, Intent intent, int requestCode) {
        goActivity(context, intent, requestCode, false, true);
    }

    public static void goActivityAnim(Context context, Intent intent) {
        goActivity(context, intent, -1, false, false);
    }

    public static void goActivityFinishAnim(Context context, Intent intent) {
        goActivity(context, intent, -1, true, false);
    }

    public static void goActivityForResultAnim(Context context, Intent intent, int requestCode) {
        goActivity(context, intent, requestCode, false, false);
    }

    private static void goActivity(Context context, Intent intent, int requestCode, boolean finish, boolean anim) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            activity.startActivityForResult(intent, requestCode);
            if (finish && context instanceof BaseActivity) {
                ((BaseActivity) context).finishSuper();
            } else if (finish) {
                activity.finish();
            }
            if (anim && finish) {
                activity.overridePendingTransition(R.anim.enter_enter, R.anim.enter_exit_finish);
            } else if (anim) {
                activity.overridePendingTransition(R.anim.enter_enter, R.anim.enter_exit);
            }
        }
    }
}
