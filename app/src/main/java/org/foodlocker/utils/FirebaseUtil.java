package org.foodlocker.utils;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.foodlocker.CreateAccount;
import org.foodlocker.structs.User;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FirebaseUtil {

    static private FirebaseDatabase db = FirebaseDatabase.getInstance();

    static public void createUser(User newUser, CreateAccount createAccountActivity) {
        DatabaseReference userChildRef = db.getReference("users").child(newUser.getUsername());

        UserExistsChecker listener = new UserExistsChecker(newUser, userChildRef, createAccountActivity, "create");
        userChildRef.addListenerForSingleValueEvent(listener);
    }

    static private void createUserCont(User newUser, DatabaseReference userChildRef, CreateAccount createAccountActivity) {
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

    static class UserExistsChecker implements ValueEventListener {

        private User newUser;
        private DatabaseReference userChildRef;
        private CreateAccount caller;
        private String accessType;

        UserExistsChecker(User newUser, DatabaseReference userChildRef, CreateAccount caller,
                          String accessType) {
            this.newUser = newUser;
            this.userChildRef = userChildRef;
            this.caller = caller;
            this.accessType = accessType;
        }

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            boolean exists = dataSnapshot.exists();
            if (!exists && accessType.equals("create")) {
                createUserCont(newUser, userChildRef, caller);
            } else if (exists && accessType.equals("create")) {
                caller.onDuplicateUsername();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }
}
