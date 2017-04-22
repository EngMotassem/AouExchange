package apps.aouexchange.StudentActivity.admin;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import apps.aouexchange.R;
import apps.aouexchange.StudentActivity.adapters.NotifiAdapter;
import apps.aouexchange.StudentActivity.models.NotificationMessages;
import butterknife.BindView;
import butterknife.ButterKnife;


public class notificationlist extends Fragment {
    @BindView(R.id.progress_bar_not)
    ProgressBar mProgressBarForUsers;
    @BindView(R.id.recycler_view_notif)
    RecyclerView mUsersRecyclerView;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mUserRefDatabase;
    private ChildEventListener mChildEventListener;
    private NotifiAdapter mNotifyAdapter;
    private ArrayList<String> mNotifyKeyList;
    private RecyclerView recyclerView;



    public notificationlist() {

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (container != null) {
            container.removeAllViews();
        }

        View rootview=inflater.inflate(R.layout.fragment_notificationlist, container, false);

        recyclerView= (RecyclerView) rootview.findViewById(R.id.recycler_view_notif);

        setUsersDatabase();
        queryAllNotify();
        setUserRecyclerView();


        return  rootview;
    }


    private void updateRecyclerView() {

        mNotifyAdapter = new NotifiAdapter(getActivity(), new ArrayList<NotificationMessages>(),this);
        mUsersRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mUsersRecyclerView.setHasFixedSize(true);
        mUsersRecyclerView.setAdapter(mNotifyAdapter);
        mNotifyAdapter.notifyDataSetChanged();
    }

    private void bindButterKnife() {
        ButterKnife.bind(getActivity());
    }

    private void setAuthInstance() {
        mAuth = FirebaseAuth.getInstance();
    }

    private void setUsersDatabase() {
        mUserRefDatabase = FirebaseDatabase.getInstance().getReference().child("notification");
    }
    private void setUserRecyclerView() {
        mNotifyAdapter = new NotifiAdapter(getActivity(), new ArrayList<NotificationMessages>(),this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mNotifyAdapter);
    }

    private void queryAllNotify() {
        mChildEventListener = getChildEventListener();

        // mUserRefDatabase.orderByChild("depatrment").equalTo(getDepartment());
        mUserRefDatabase.limitToFirst(50).addChildEventListener(mChildEventListener);


    }

    private ChildEventListener getChildEventListener() {
        return new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if(dataSnapshot.exists()){

                    String notifyUi = dataSnapshot.getKey();

                    NotificationMessages currentNot=new NotificationMessages();

                    currentNot = dataSnapshot.getValue(NotificationMessages.class);

                    mNotifyAdapter.refill(currentNot);

                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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


}



