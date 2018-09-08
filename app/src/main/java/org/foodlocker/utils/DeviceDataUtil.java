package org.foodlocker.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class DeviceDataUtil {

    /**
     * Saves the account to SharedPreferences, letting the app know in future that it's logged in,
     * and the user it's logged in to.
     * @param context {@link Context} of the {@link android.app.Activity} user is registering/signing
     *                               up from
     * @param user the username of the new login/sign up
     */
    public static void registerAccount(Context context, String user, String type) {
        SharedPreferences userPreferences = context.getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userPreferences.edit();
        editor.putString(type, user);
        editor.apply();
    }

    public static String retrieveCurrentUser(Context context) {
        return context.getSharedPreferences("userDetails", Context.MODE_PRIVATE).getString("user", null);
    }

    public static String retrieveCurrentVolunteer(Context context) {
        return context.getSharedPreferences("userDetails", Context.MODE_PRIVATE).getString("volunteer", null);
    }
}
