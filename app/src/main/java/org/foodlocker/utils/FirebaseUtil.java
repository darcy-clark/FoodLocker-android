package org.foodlocker.utils;

import android.app.Activity;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.foodlocker.CreateAccount;
import org.foodlocker.LoginPage;
import org.foodlocker.OrderFirstPage;
import org.foodlocker.structs.Box;
import org.foodlocker.structs.User;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class FirebaseUtil {

    static private FirebaseDatabase db = FirebaseDatabase.getInstance();

    public void createUser(User newUser, CreateAccount createAccountActivity) {
        DatabaseReference userChildRef = db.getReference("users").child(newUser.getUsername());

        UserExistsChecker listener = new UserExistsChecker(newUser, userChildRef, createAccountActivity, "create");
        userChildRef.addListenerForSingleValueEvent(listener);
    }

    private void createUserCont(User newUser, DatabaseReference userChildRef, CreateAccount createAccountActivity) {
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
        createAccountActivity.onAccountCreation();
    }

    public void login(User user, LoginPage loginPageActivity) {
        DatabaseReference userChildRef = db.getReference("users").child(user.getUsername());

        UserExistsChecker listener = new UserExistsChecker(user, loginPageActivity, "login");
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
            loginPageActivity.onLogin();
        } else {
            loginPageActivity.onBadLogin();
        }
    }

    public void retrieveBoxes(final OrderFirstPage orderFirstPage) {
        DatabaseReference boxesRef = db.getReference("boxes");
        boxesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapshots = dataSnapshot.getChildren();
                List<Box> boxes = new ArrayList<>();
                for(DataSnapshot snap : snapshots) {
                    Box box = snap.getValue(Box.class);
                    boxes.add(box);
                }
                orderFirstPage.populateBoxList(boxes);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    class UserExistsChecker implements ValueEventListener {

        private User user;
        private String accessType;
        private Activity caller;
        // This is only needed for account creation
        private DatabaseReference userChildRef;

        UserExistsChecker(User user, LoginPage caller, String accessType) {
            this.user = user;
            this.accessType = accessType;
            this.caller = caller;
        }

        UserExistsChecker(User user, DatabaseReference userChildRef, CreateAccount caller,
                          String accessType) {
            this.user = user;
            this.userChildRef = userChildRef;
            this.caller = caller;
            this.accessType = accessType;
        }

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            boolean exists = dataSnapshot.exists();
            if (!exists && accessType.equals("create")) {
                createUserCont(user, userChildRef, (CreateAccount) caller);
            } else if (exists && accessType.equals("create")) {
                ((CreateAccount) caller).onDuplicateUsername();
            } else if (!exists && accessType.equals("login")) {
                ((LoginPage) caller).onBadLogin();
            } else {
                loginCont(user, dataSnapshot, (LoginPage) caller);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }
}
