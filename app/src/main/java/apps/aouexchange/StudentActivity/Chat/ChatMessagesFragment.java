package apps.aouexchange.StudentActivity.Chat;

import android.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import apps.aouexchange.R;
import apps.aouexchange.StudentActivity.FireChatHelper.ExtraIntent;
import apps.aouexchange.StudentActivity.adapters.MessageChatAdapter;
import apps.aouexchange.StudentActivity.models.ChatMessage;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class ChatMessagesFragment extends Fragment {


   private Button send;
    private EditText messageEdit ;
    private View view;
    private ViewGroup viewGroup;

    private  RecyclerView recy;


    private String mRecipientId;
    private String mCurrentUserId;
    private MessageChatAdapter messageChatAdapter;
    private DatabaseReference messageChatDatabase;
    private ChildEventListener messageChatListener;

    public ChatMessagesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_chat_messages, container, false);

      if (container != null) {
            container.removeAllViewsInLayout();
        }




        setDatabaseInstance();
        setUsersId();


        messageChatAdapter = new MessageChatAdapter(new ArrayList<ChatMessage>());
         recy= (RecyclerView) rootView.findViewById(R.id.recycler_view_chat);
        recy.setLayoutManager(new LinearLayoutManager(getActivity()));
        recy.setHasFixedSize(true);
        recy.setAdapter(messageChatAdapter);

        messageEdit= (EditText) rootView.findViewById(R.id.edit_text_message);
        send= (Button) rootView.findViewById(R.id.btn_send_message);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),mCurrentUserId,Toast.LENGTH_SHORT).show();

                String senderMessage = messageEdit.getText().toString().trim();

                if(!senderMessage.isEmpty()){

                    ChatMessage newMessage = new ChatMessage(senderMessage,mCurrentUserId,mRecipientId);
                    messageChatDatabase.push().setValue(newMessage);

                    messageEdit.setText("");
                }

            }
        });
      //  getActivity().setActionBar(YOUR_TITLE);

        return rootView;

    }
    private void bindButterKnife() {
        ButterKnife.bind(getActivity());
    }
    private void setDatabaseInstance() {
        String chatRef = getActivity().getIntent().getStringExtra(ExtraIntent.EXTRA_CHAT_REF);
        messageChatDatabase = FirebaseDatabase.getInstance().getReference().child(chatRef);
    }

    private void setUsersId() {
        mRecipientId = getActivity().getIntent().getStringExtra(ExtraIntent.EXTRA_RECIPIENT_ID);
        mCurrentUserId = getActivity().getIntent().getStringExtra(ExtraIntent.EXTRA_CURRENT_USER_ID);
    }

    private void setChatRecyclerView() {

/*

        mChatRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mChatRecyclerView.setHasFixedSize(true);
        messageChatAdapter = new MessageChatAdapter(new ArrayList<ChatMessage>());
        mChatRecyclerView.setAdapter(messageChatAdapter);
        */
    }

    @Override
    public void onStart() {



        super.onStart();


        messageChatListener = messageChatDatabase.limitToFirst(20).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildKey) {




                if(dataSnapshot.exists()){
                    ChatMessage newMessage = dataSnapshot.getValue(ChatMessage.class);
                    if(newMessage.getSender().equals(mCurrentUserId)){
                        newMessage.setRecipientOrSenderStatus(MessageChatAdapter.SENDER);
                    }else{
                        newMessage.setRecipientOrSenderStatus(MessageChatAdapter.RECIPIENT);
                    }
                    messageChatAdapter.refillAdapter(newMessage);
                   recy.scrollToPosition(messageChatAdapter.getItemCount()-1);
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
        });

    }






    @Override
    public void onStop() {
        super.onStop();

        if(messageChatListener != null) {
            messageChatDatabase.removeEventListener(messageChatListener);
        }
        messageChatAdapter.cleanUp();

    }






}