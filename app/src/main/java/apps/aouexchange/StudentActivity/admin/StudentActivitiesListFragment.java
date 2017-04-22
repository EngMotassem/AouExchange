package apps.aouexchange.StudentActivity.admin;

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

import apps.aouexchange.R;

import apps.aouexchange.StudentActivity.adapters.StduentActivitiesAdapter;

import apps.aouexchange.StudentActivity.models.StudentActivities;
import butterknife.BindView;

/**
 * A placeholder fragment containing a simple view.
 */
public class StudentActivitiesListFragment extends Fragment {


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mUserRefDatabase,adminRef;

    private FirebaseDatabase adminDatabase;
    private FirebaseUser user;
    private boolean anAdminUser;


    private ChildEventListener mChildEventListener;
    private StduentActivitiesAdapter mNotifyAdapter;
    private RecyclerView recyclerView;

    public StudentActivitiesListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (container != null) {
            container.removeAllViews();
        }

        //anAdminUser=true;
        View view=inflater.inflate(R.layout.fragment_student_activities_list, container, false);
        setUsersDatabase();

        queryAllStudentActivities();

        recyclerView= (RecyclerView) view.findViewById(R.id.rec_studentActivities_admin);


        setUserRecyclerView();



        return view;
    }

    private void setUsersDatabase() {
        mUserRefDatabase = FirebaseDatabase.getInstance().getReference().child("activities");
    }
    private void setUserRecyclerView() {
        mNotifyAdapter = new StduentActivitiesAdapter(getActivity(), new ArrayList<StudentActivities>(),this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mNotifyAdapter);
    }

    private void queryAllStudentActivities() {
        mChildEventListener = getChildEventListener();
       // getActivity().
      String ab=   getActivity().getClass().getSimpleName();

       // Toast.makeText(getActivity(),ab,Toast.LENGTH_LONG).show();


        if(ab.equals("AdminActivity") ){
            Toast.makeText(getActivity(), "admin", Toast.LENGTH_LONG).show();
            mUserRefDatabase.orderByChild("activated").equalTo("no").addChildEventListener(mChildEventListener);
        }
        else {
            mUserRefDatabase.orderByChild("activated").equalTo("yes").addChildEventListener(mChildEventListener);
            Toast.makeText(getActivity(), "not an admin", Toast.LENGTH_LONG).show();
        }



    }

    private ChildEventListener getChildEventListener() {
        return new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if(dataSnapshot.exists()){

                    String notifyUi = dataSnapshot.getKey();

                    StudentActivities currentNot=new StudentActivities();

                    currentNot = dataSnapshot.getValue(StudentActivities.class);

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

