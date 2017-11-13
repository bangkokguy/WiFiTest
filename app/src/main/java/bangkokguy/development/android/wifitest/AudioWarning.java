package bangkokguy.development.android.wifitest;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bangkokguy on 11/11/17.
 *
 */

class AudioWarning {
    private final static String TAG = AudioWarning.class.getSimpleName();

    final static int WIFI_DISCONNECTED = 1;
    final static int WIFI_CONNECTED = 2;
    final static int WIFI_DISABLED = 3;
    final static int WIFI_DISCONNECT_ERROR = 4;
    final static int WIFI_CONNECT_ERROR = 5;
    final static int WIFI_CONNECTING_PREFERRED_AP = 6;

    private int notifyId = 1;

    AudioWarning () {}

    Notification getNotification (Context context, int type) {
        return getNotification(context, type, "", "");
    }

    Notification getNotification (Context context, int type, CharSequence text) {
        return getNotification(context, type, text, "");
    }

    Notification getNotification (Context context, int type, CharSequence text, CharSequence explanation) {

        if (type < 0 || type > 6) {
            throw new NullPointerException("type parameter out of range");
        }

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification noti = null;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String channelId = "some_channel_id";
            CharSequence channelName = "Some Channel";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel notificationChannel = null;

            notificationChannel = new NotificationChannel(channelId, channelName, importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            nm.createNotificationChannel(notificationChannel);

            Log.d(TAG, "Uri-->"+resourceToUri(context, R.raw.chafing).toString());
            notificationChannel.setSound(getNotificationSoundUri(context, type), new AudioAttributes.Builder().build());

            //
            String groupId = "some_group_id";
            CharSequence groupName = "Some Group";
            nm.createNotificationChannelGroup(new NotificationChannelGroup(groupId, groupName));
            //
            List<NotificationChannelGroup> notificationChannelGroups = new ArrayList();
            notificationChannelGroups.add(new NotificationChannelGroup("group_one", "Group One"));
            notificationChannelGroups.add(new NotificationChannelGroup("group_two", "Group Two"));
            notificationChannelGroups.add(new NotificationChannelGroup("group_three", "Group Three"));

            nm.createNotificationChannelGroup(new NotificationChannelGroup("group_one", "Group One"));
            //
            noti = new Notification.Builder(context, channelId)
                    .setContentTitle(text)
                    .setContentText(explanation)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .build();
            //

        } else {
            noti = new Notification.Builder(context)
                    .setContentTitle(text)
                    .setContentText(explanation)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setSound(getNotificationSoundUri(context, type))
                    .build();
        }
        nm.notify(notifyId++, noti);
        return noti;
    }

    private static Uri getNotificationSoundUri (Context context, int type) {
        int resID = 0;
        switch (type) {
            case WIFI_DISCONNECTED:
                resID = R.raw.chafing;
                break;
            case WIFI_CONNECTED:
                resID = R.raw.gentle_alarm;
                break;
            case WIFI_DISABLED:
                resID = R.raw.just_like_magic;
                break;
            case WIFI_DISCONNECT_ERROR:
                resID = R.raw.solemn;
                break;
            case WIFI_CONNECT_ERROR:
                resID = R.raw.twirl;
                break;
            case WIFI_CONNECTING_PREFERRED_AP:
                resID = R.raw.wet;
                break;
        }
        return resourceToUri(context, resID);
    }

    private static Uri resourceToUri(Context context, int resID) {
        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                context.getResources().getResourcePackageName(resID) + '/' +
                context.getResources().getResourceTypeName(resID) + '/' +
                context.getResources().getResourceEntryName(resID) );
    }


}
