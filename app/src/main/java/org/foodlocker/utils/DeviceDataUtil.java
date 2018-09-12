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

    /**
     * Retrieves the current user's username from {@link SharedPreferences} if logged in
     * @param context {@link Context} of caller's {@link android.app.Activity}
     * @return username {@link String}
     */
    public static String retrieveCurrentUser(Context context) {
        return context.getSharedPreferences("userDetails", Context.MODE_PRIVATE).getString("user", null);
    }

    /**
     * Retrieves the current volunteer's username from {@link SharedPreferences} if logged in
     * @param context context {@link Context} of caller's {@link android.app.Activity}
     * @return username {@link String}
     */
    public static String retrieveCurrentVolunteer(Context context) {
        return context.getSharedPreferences("userDetails", Context.MODE_PRIVATE).getString("volunteer", null);
    }
}
