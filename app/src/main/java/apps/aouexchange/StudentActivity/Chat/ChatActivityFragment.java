package apps.aouexchange.StudentActivity.Chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import apps.aouexchange.R;
import apps.aouexchange.StudentActivity.adapters.UsersChatAdapter;
import apps.aouexchange.StudentActivity.models.User;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class ChatActivityFragment extends Fragment {



    private FirebaseUser mUser;


    private String mCurrentUserUid;
    private List<String>  mUsersKeyList;
    boolean anAdminUser;

    private FirebaseAuth mAuth;
    private DatabaseReference mUserRefDatabase,adminRef;
    private ChildEventListener mChildEventListener;
    private UsersChatAdapter mUsersChatAdapter;
    private String mUserDepartment="";
    private String MY_PREFS_NAME="MyPref";


    @BindView(R.id.recycler_view_users) RecyclerView mUsersRecyclerView;



    public ChatActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
              View rootView = inflater.inflate(R.layout.fragment_chat, container, false);

        mUsersKeyList = new ArrayList<String>();
        mUsersChatAdapter=new UsersChatAdapter(getActivity(),new ArrayList<User>());

        RecyclerView recy= (RecyclerView) rootView.findViewById(R.id.recycler_view_users);
        //mUsersChatAdapter = new UsersChatAdapter(getActivity(), new ArrayList<User>());
        recy.setLayoutManager(new LinearLayoutManager(getActivity()));
        recy.setHasFixedSize(true);
        recy.setAdapter(mUsersChatAdapter);


        allUOtherUsers();


        return rootView;

    }

    private void allUOtherUsers() {

        mChildEventListener = getChildEventListener();
        mUserRefDatabase = FirebaseDatabase.getInstance().getReference().child("users");


        mUserRefDatabase.orderByChild("mDepatrment").equalTo("it student").addChildEventListener(mChildEventListener);

        //mUserRefDatabase.limitToFirst(50).addChildEventListener(mChildEventListener);
    }

    private void setUserRecyclerView() {
        mUsersChatAdapter = new UsersChatAdapter(getActivity(), new ArrayList<User>());
        mUsersRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mUsersRecyclerView.setHasFixedSize(true);
        mUsersRecyclerView.setAdapter(mUsersChatAdapter);
    }

    private void setUsersKeyList() {
        mUsersKeyList = new ArrayList<String>();
    }




    private void bindButterKnife() {
        ButterKnife.bind(getActivity());
    }



    private void setUsersDatabase() {
        mUserRefDatabase = FirebaseDatabase.getInstance().getReference().child("users");
    }
    private void queryAllUsers() {
        mChildEventListener = getChildEventListener();

        mUserRefDatabase.orderByChild("mDepatrment").equalTo(getDepartment()).addChildEventListener(mChildEventListener);

    }

    private  String getDepartment(){

        String mDepartment="";
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);


        mDepartment = pref.getString("depname", "No name defined");



        Toast.makeText(getActivity(),mDepartment,Toast.LENGTH_SHORT).show();


        return mDepartment;

    }

    private ChildEventListener getChildEventListener() {
        return new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // mUsersChatAdapter = new UsersChatAdapter(this, new ArrayList<User>());

                mAuth = FirebaseAuth.getInstance();
                mCurrentUserUid=mAuth.getCurrentUser().getUid();
               //Toast.makeText(getActivity(),mCurrentUserUid,Toast.LENGTH_SHORT).show();


                if(dataSnapshot.exists()){

                    String userUid = dataSnapshot.getKey();

                    if(dataSnapshot.getKey().equals(mCurrentUserUid)){
                        User currentUser = dataSnapshot.getValue(User.class);
                        mUsersChatAdapter.setCurrentUserInfo(userUid, currentUser.getEmail(), currentUser.getCreatedAt());
                    }else {
                        User recipient = dataSnapshot.getValue(User.class);
                        recipient.setRecipientId(userUid);
                        mUsersKeyList.add(userUid);
                        mUsersChatAdapter.refill(recipient);
                        //Toast.makeText(getActivity(),recipient.getEmail(),Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.exists()) {
                    String userUid = dataSnapshot.getKey();
                    if(!userUid.equals(mCurrentUserUid)) {

                        User user = dataSnapshot.getValue(User.class);

                        int index = mUsersKeyList.indexOf(userUid);
                        if(index > -1) {
                            mUsersChatAdapter.changeUser(index, user);
                        }
                    }

                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }
    private void setUserData(FirebaseUser user) {

        mCurrentUserUid = user.getUid();


    }



}
