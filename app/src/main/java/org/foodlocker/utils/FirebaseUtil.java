package org.foodlocker.utils;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.foodlocker.CreateAccount;
import org.foodlocker.LoginPage;
import org.foodlocker.OrderFirstPage;
import org.foodlocker.structs.Box;
import org.foodlocker.structs.User;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class FirebaseUtil {

    static private FirebaseDatabase db = FirebaseDatabase.getInstance();

    public void createUser(User newUser, CreateAccount createAccountActivity, String actType) {
        DatabaseReference userChildRef = db.getReference("users").child(newUser.getUsername());

        UserExistsChecker listener = new UserExistsChecker(newUser, createAccountActivity,
                userChildRef, actType);
        userChildRef.addListenerForSingleValueEvent(listener);
    }

    private void createUserCont(User newUser, DatabaseReference userChildRef,
                                CreateAccount createAccountActivity, String actType) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            return;
        }
        md.update(newUser.getPasshash().getBytes());
        byte[] bytes = md.digest();
        String hashedPass = String.format( "%064x", new BigInteger( 1, bytes ) );
        newUser.setPasshash(hashedPass);

        userChildRef.setValue(newUser);
        addActToTopic(actType);
        createAccountActivity.onAccountCreation(newUser.getUsername());
    }

    private void addActToTopic(final String actType) {
        FirebaseMessaging.getInstance().subscribeToTopic(actType)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    throw new RuntimeException("Failed to subscribe to topic \"" + actType + "\"");
                }
                Log.d("Subscribe", "Successful " + actType);
            }
        });
    }

    public void login(User user, LoginPage loginPageActivity) {
        DatabaseReference userChildRef = db.getReference("users").child(user.getUsername());

        UserExistsChecker listener = new UserExistsChecker(user, loginPageActivity);
        userChildRef.addListenerForSingleValueEvent(listener);
    }

    private void loginCont(User user, DataSnapshot snapshot, LoginPage loginPageActivity) {
        // TODO: Extract hasher into method
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            return;
        }
        md.update(user.getPasshash().getBytes());
        byte[] bytes = md.digest();
        String hashedPass = String.format( "%064x", new BigInteger( 1, bytes ) );

        if (hashedPass.equals(snapshot.child("passhash").getValue(String.class))) {
            addActToTopic(snapshot.child("type").getValue(String.class));
            loginPageActivity.onLogin(user.getUsername());
        } else {
            loginPageActivity.onBadLogin();
        }
    }

    public void retrieveBoxes(final OrderFirstPage orderFirstPage) {
        DatabaseReference boxesRef = db.getReference("boxes");
        boxesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapshots = dataSnapshot.getChildren();
                List<Box> boxes = new ArrayList<>();
                for(DataSnapshot snap : snapshots) {
                    Box box = snap.getValue(Box.class);
                    boxes.add(box);
                }
                orderFirstPage.populateBoxList(boxes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void unsubscribe() {
        try {
            FirebaseInstanceId.getInstance().deleteInstanceId();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    class UserExistsChecker implements ValueEventListener {

        private User user;
        private Activity caller;
        // These are only needed for account creation
        private DatabaseReference userChildRef;
        private String actType;

        /**
         * Constructor used for account login
         * @param user {@link User} object containing username and password
         * @param caller the caller {@link Activity} which contains the callback
         */
        UserExistsChecker(User user, Activity caller) {
            this(user, caller, null, null);
        }

        /**
         * Constructor used for account creation
         * @param user {@link User} object containing username and password
         * @param caller the caller {@link Activity} which contains the callback
         * @param userChildRef DB space to create the new user in
         * @param actType {@link String} representing whether the new account is a user or volunteer
         */
        UserExistsChecker(User user, Activity caller, DatabaseReference userChildRef, String actType) {
            this.user = user;
            this.userChildRef = userChildRef;
            this.caller = caller;
            this.actType = actType;
        }

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            boolean exists = dataSnapshot.exists();
            // TODO: Simplify
            if (!exists && actType != null) {
                createUserCont(user, userChildRef, (CreateAccount) caller, actType);
            } else if (exists && actType != null) {
                ((CreateAccount) caller).onDuplicateUsername();
            } else if (!exists) {
                ((LoginPage) caller).onBadLogin();
            } else {
                loginCont(user, dataSnapshot, (LoginPage) caller);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    }
}
