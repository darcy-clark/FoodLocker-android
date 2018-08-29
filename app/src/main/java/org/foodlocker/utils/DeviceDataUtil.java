package org.foodlocker.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;

import org.foodlocker.structs.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class DeviceDataUtil {

    public static void registerAccount(Context context, String user) {
//        String accountFileStr = "accountInfo.txt";
//        String msg = "1";
//        File accountFile = new File(context.getFilesDir(), accountFileStr);
//
//        try {
//            if (!accountFile.exists()) {
//                accountFile.createNewFile();
//            }
//            FileOutputStream fileOutputStream = context.openFileOutput(accountFileStr, Context.MODE_PRIVATE);
//            fileOutputStream.write(msg.getBytes());
//            fileOutputStream.close();
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences userPreferences = context.getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userPreferences.edit();
        editor.putString("account", user);
        editor.apply();
    }
}
