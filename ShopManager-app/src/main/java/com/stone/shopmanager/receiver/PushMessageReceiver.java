package com.stone.shopmanager.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.stone.shopmanager.R;
import com.stone.shopmanager.application.BaseApplication;
import com.stone.shopmanager.old.HomeActivity;

import cn.bmob.push.PushConstants;

/**
 * Created by stone on 15/3/31.
 */
public class PushMessageReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(PushConstants.ACTION_MESSAGE)) {
            notifyPushMessage("小菜管家", intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING));
        }
    }

    private void notifyPushMessage(String title, String content) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(BaseApplication.getAppContext())
                        .setSmallIcon(R.drawable.ic_app)
                        .setContentTitle(title)
                        .setContentText(content);
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(BaseApplication.getAppContext(), HomeActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(BaseApplication.getAppContext());
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(HomeActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) BaseApplication.getAppContext().getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(0, mBuilder.build());
    }
}
