package org.foodlocker.utils;

import android.app.Activity;
import android.app.Notification;
import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import org.foodlocker.CreateAccount;
import org.foodlocker.LoginPage;
import org.foodlocker.NotificationService;
import org.foodlocker.OrderFirstPage;
import org.foodlocker.OrderSecondPage;
import org.foodlocker.OrdersListPage;
import org.foodlocker.structs.Box;
import org.foodlocker.structs.Order;
import org.foodlocker.structs.User;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class FirebaseUtil {

    static private FirebaseDatabase db = FirebaseDatabase.getInstance();

    // TODO: This is getting moved to a Firebase HTTPS Function
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
        createAccountActivity.onAccountCreation(newUser.getUsername(), actType);
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

    /**
     * Calls a Firebase Function that checks user credentials against the database. Retrieves a
     * Firebase token if login is good, otherwise reports bad credentials to the user.
     *
     * @param user the account credentials
     * @param loginPageActivity the {@link Activity} with the login callbacks
     */
    public void login(User user, final LoginPage loginPageActivity) {
        user.setPasshash(HashingUtil.sha256(user.getPasshash()));
        Map<String, String> userMap = new HashMap<>();
        userMap.put("username", user.getUsername());
        userMap.put("passhash", user.getPasshash());
        userMap.put("messagingToken", NotificationService.messagingToken());
        FirebaseFunctions.getInstance()
                .getHttpsCallable("login")
                .call(userMap)
                .addOnSuccessListener(new OnSuccessListener<HttpsCallableResult>() {
                    @Override
                    public void onSuccess(HttpsCallableResult result) {
                        Map<String, String> data = (Map) result.getData();
                        Log.d("CloudFunction", data.toString());
                        getFirebaseAuth(data, loginPageActivity);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loginPageActivity.onBadLogin();
                    }
                });
    }

    private void getFirebaseAuth(final Map<String, String> data, final LoginPage loginPageActivity) {
        FirebaseAuth.getInstance().signInWithCustomToken(data.get("token"))
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        loginPageActivity.onLogin(data.get("username"), data.get("type"));
                    }
                });
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

    public void retrieveOrders(final OrdersListPage ordersListPage) {
        DatabaseReference ordersRef = db.getReference("orders");
        ordersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String currentVolunteer = DeviceDataUtil.retrieveCurrentVolunteer(ordersListPage);
                Iterable<DataSnapshot> snapshots = dataSnapshot.getChildren();
                List<Order> openOrders = new ArrayList<>();
                List<Order> acceptedOrders = new ArrayList<>();
                for(DataSnapshot snap : snapshots) {
                    Order order = snap.getValue(Order.class);
                    if (order == null || order.getStatus() == null) {
                        continue;
                    }
                    if (order.getStatus().equals("open")) {
                        openOrders.add(order);
                    } else if (order.getVolunteer().equals(currentVolunteer) && order.getStatus().equals("accepted")) {
                        acceptedOrders.add(order);
                    }
                }
                List<Order> allOrders = new ArrayList<>(acceptedOrders);
                allOrders.addAll(openOrders);

                ordersListPage.populateOrderList(allOrders);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void createOrder(String box, List<String> dietRestrictions, OrderSecondPage caller) {
        pickLocker(box, dietRestrictions, caller);
    }

    private void createOrderCont(String box, String lockerNum, String lockerCombo,
                                 List<String> dietRestrictions, OrderSecondPage caller) {
        String user = DeviceDataUtil.retrieveCurrentUser(caller.getApplicationContext());
        long timestamp = new Date().getTime();
        Order order = new Order(box, lockerNum, lockerCombo, user, timestamp, dietRestrictions);

        String orderName = UUID.randomUUID().toString();
        DatabaseReference newOrderRef = db.getReference("orders").child(orderName);
        newOrderRef.setValue(order);

        caller.onOrderComplete(order);
    }

    private void pickLocker(final String box, final List<String> dietRestrictions, final OrderSecondPage caller) {
        final DatabaseReference lockerRef = db.getReference("lockers");
        Query freeLockersQuery = lockerRef.orderByChild("avail").equalTo(true);
        freeLockersQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // TODO: no available lockers CANNOT crash app
                Iterable<DataSnapshot> snapshots = dataSnapshot.getChildren();
                DataSnapshot lockerSnap = snapshots.iterator().next();
                String lockerNumber = lockerSnap.getKey();
                lockerRef.child(lockerNumber).child("avail").setValue(false);
                String lockerCombo = lockerSnap.child("combo").getValue(String.class);
                createOrderCont(box, lockerNumber, lockerCombo, dietRestrictions, caller);
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

    // TODO: This should all be moved to a Firebase Function, the app should not have write privs
    // TODO: before it's logged in
    class UserExistsChecker implements ValueEventListener {

        private User user;
        private Activity caller;
        // These are only needed for account creation
        private DatabaseReference userChildRef;
        private String actType;

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
            if (!exists) {
                createUserCont(user, userChildRef, (CreateAccount) caller, actType);
            } else {
                ((CreateAccount) caller).onDuplicateUsername();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    }
}
