package apps.aouexchange.StudentActivity.broadcast;

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
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class BroadcastActivityFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private EditText broadcastTitle,broadcastMessage;
    private  Button sendBr,cancelBt;

    private DatabaseReference mDatabaseref;
    private FirebaseUser user;
    private NotificationMessages notiMessage;
    private AlertDialog dialog;

    public BroadcastActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (container != null) {
            container.removeAllViewsInLayout();
        }



        View rootView=inflater.inflate(R.layout.fragment_broadcast, container, false);

        setUpDataBase();

        broadcastTitle= (EditText) rootView.findViewById(R.id.edit_text_display_name);
        broadcastMessage= (EditText) rootView.findViewById(R.id.messageText);

      sendBr=(Button) rootView.findViewById(R.id.btn_send_bro);
        sendBr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMessage();
                broadcastTitle.setText("");
                broadcastMessage.setText("");

            }
        });
        cancelBt=(Button)rootView.findViewById(R.id.btn_cancel_register);
        cancelBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                broadcastTitle.setText("");
                broadcastMessage.setText("");

            }
        });




        return rootView;
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
        notiMessage=new NotificationMessages();

        String newId=mDatabaseref.push().getKey();
        notiMessage.setId(newId);

        notiMessage.setUserName(getUserName());
        notiMessage.setTitle(broadcastTitle.getText().toString().trim());
        notiMessage.setBody(broadcastMessage.getText().toString().trim());

        mDatabaseref.child(newId).setValue(notiMessage);




    }

    private String getUserName() {
        mAuth=FirebaseAuth.getInstance();

        user=mAuth.getCurrentUser();
        return user.getEmail();
    }

    private void setUpDataBase() {
        mDatabase=FirebaseDatabase.getInstance();
        mDatabaseref=mDatabase.getReference().child("notification");
    }

    private void bindButterKnife() {
        ButterKnife.bind(getActivity());

    }




}

