package apps.aouexchange.StudentActivity.admin;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import apps.aouexchange.R;
import apps.aouexchange.StudentActivity.models.StudentActivities;

/**
 * A placeholder fragment containing a simple view.
 */
public class ActivityDetailsFragment extends Fragment {

    private TextView user_sender,message_title,message_body;

    private String notifyiD;



    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private Button activateB,deleteBt;

    public ActivityDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (container != null) {
            container.removeAllViewsInLayout();
        }


        View  rootview=inflater.inflate(R.layout.fragment_activity_details, container, false);


        message_title= (TextView) rootview.findViewById(R.id.student_act_title);
        message_body=(TextView) rootview.findViewById(R.id.student_body);

        notifyiD=getArguments().getString("notifyId");
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child("activities").child(notifyiD);


        activateB=(Button)rootview.findViewById(R.id.activivateBt);
        deleteBt=(Button)rootview.findViewById(R.id.DeleteBt);


        String ab=   getActivity().getClass().getSimpleName();

        if (!ab.equals("AdminActivity")){

          activateB.setVisibility(View.GONE);
            deleteBt.setVisibility(View.GONE);

        }


        activateB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });

        deleteBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                DatabaseReference notificationR=firebaseDatabase.getReference().child("activities").child(notifyiD);
                notificationR.removeValue();
                Intent intent=new Intent(getActivity(),AdminActivity.class);
                startActivity(intent);
            }
        });



        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                StudentActivities notificationMessage=dataSnapshot.getValue(StudentActivities.class);
                message_title.setText(notificationMessage.getTitle());
                message_body.setText(notificationMessage.getBody());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





        return rootview;
    }

    private void updateData() {

        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference notificationR=firebaseDatabase.getReference().child("activities").child(notifyiD).child("activated");
        notificationR.setValue("yes");
        Intent intent=new Intent(getActivity(),AdminActivity.class);
        startActivity(intent);
    }
}
