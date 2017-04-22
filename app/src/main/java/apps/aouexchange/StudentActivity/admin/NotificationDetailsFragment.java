package apps.aouexchange.StudentActivity.admin;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import apps.aouexchange.R;
import apps.aouexchange.StudentActivity.adapters.NotifiAdapter;
import apps.aouexchange.StudentActivity.models.NotificationMessages;
import butterknife.BindView;

/**
 * A placeholder fragment containing a simple view.
 */
public class NotificationDetailsFragment extends Fragment {


    private  TextView user_sender,message_title,message_body;

    private String notifyiD;



    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private Button activateB,deleteBt;

    public NotificationDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        if (container != null) {
            container.removeAllViewsInLayout();
        }



        View rootview =inflater.inflate(R.layout.fragment_notification_details, container, false);

        user_sender= (TextView) rootview.findViewById(R.id.user_sender_no_text);
        message_title= (TextView) rootview.findViewById(R.id.user_message_title);
        message_body=(TextView) rootview.findViewById(R.id.messageBody);

        activateB=(Button)rootview.findViewById(R.id.activivateBt);

        activateB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postData();
            }
        });

        deleteBt=(Button)rootview.findViewById(R.id.DeleteBt);
        deleteBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                DatabaseReference notificationR=firebaseDatabase.getReference().child("notification").child(notifyiD);
                notificationR.removeValue();
                Intent intent=new Intent(getActivity(),AdminActivity.class);
                startActivity(intent);
            }
        });






        notifyiD=getArguments().getString("notifyId");
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child("notification").child(notifyiD);


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                NotificationMessages notificationMessage=dataSnapshot.getValue(NotificationMessages.class);
                message_title.setText(notificationMessage.getTitle());
                message_body.setText(notificationMessage.getBody());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        user_sender.setText(notifyiD);
        //Toast.makeText(getActivity(),notifyiD,Toast.LENGTH_LONG).show();



        return rootview;
    }


    private void postData() {

        String title = message_title.getText().toString();

        String message = message_body.getText().toString();

        String url = "http://mo-course.com/push/1.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        Toast.makeText(getActivity(),"data added",Toast.LENGTH_LONG).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String title = message_title.getText().toString();

                String message = message_body.getText().toString();
                Map<String,String> map = new HashMap<String, String>();
                map.put("title",title);
                map.put("message",message);
                map.put("push_type","topic");
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }




}
