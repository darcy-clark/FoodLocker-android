package org.foodlocker;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class NotificationService extends FirebaseMessagingService {

    private static String messagingToken;

    public NotificationService() {
        super();
    }

    /**
     * Returns the current logged in user's messaging token for Firebase Messaging
     * @return messagingToken {@link String}
     */
    public static String messagingToken() {
        return messagingToken;
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
    }

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        messagingToken = token;
    }
}
