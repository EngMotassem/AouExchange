package apps.aouexchange.StudentActivity.addActivity;

import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import apps.aouexchange.R;
import apps.aouexchange.StudentActivity.FireChatHelper.ChatHelper;
import apps.aouexchange.StudentActivity.models.NotificationMessages;
import apps.aouexchange.StudentActivity.models.StudentActivities;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddActivityFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private EditText broadcastTitle,broadcastMessage;
    private Button addButton;

    private DatabaseReference mDatabaseref;
    private FirebaseUser user;
    private StudentActivities studentActivities;
    private AlertDialog dialog;

    public AddActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViewsInLayout();
        }


        View view=inflater.inflate(R.layout.fragment_add, container, false);
        setUpDataBase();

        broadcastTitle= (EditText) view.findViewById(R.id.activity_title);
        broadcastMessage= (EditText) view.findViewById(R.id.activity_details);
        addButton= (Button) view.findViewById(R.id.addBurron);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMessage();
            }
        });




        return view;
    }

    private void showAlertDialog(String message, boolean isCancelable){

        dialog = ChatHelper.buildAlertDialog(getString(R.string.login_error_title),message,isCancelable,getActivity());
        dialog.show();
    }

    private void dismissAlertDialog() {
        dialog.dismiss();
    }

    private String getUserUId() {
        mAuth=FirebaseAuth.getInstance();

        user=mAuth.getCurrentUser();
        return user.getUid();
    }


    private void addMessage() {


        if(broadcastTitle.getText().toString().trim().equals("")||broadcastMessage.getText().toString().trim().equals("")){

            showAlertDialog("please enter required information ",true);
        }
        studentActivities=new StudentActivities();

        String newId=mDatabaseref.push().getKey();
        studentActivities.setId(newId);

        studentActivities.setUserName(getUserName());
        studentActivities.setTitle(broadcastTitle.getText().toString().trim());
        studentActivities.setBody(broadcastMessage.getText().toString().trim());
        studentActivities.setActivated("no");

        mDatabaseref.child(newId).setValue(studentActivities);

        broadcastTitle.setText(" ");
        broadcastMessage.setText(" ");




    }

    private String getUserName() {
        mAuth=FirebaseAuth.getInstance();

        user=mAuth.getCurrentUser();
        return user.getEmail();
    }

    private void setUpDataBase() {
        mDatabase=FirebaseDatabase.getInstance();
        mDatabaseref=mDatabase.getReference().child("activities");
    }


}
