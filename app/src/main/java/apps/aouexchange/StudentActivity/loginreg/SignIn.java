package apps.aouexchange.StudentActivity.loginreg;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import apps.aouexchange.R;
import apps.aouexchange.StudentActivity.FireChatHelper.ChatHelper;
import apps.aouexchange.StudentActivity.StudentActivity;
import apps.aouexchange.StudentActivity.adapters.UsersChatAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignIn extends AppCompatActivity {

    @BindView(R.id.edit_text_email_login)
    EditText mUserEmail;
    @BindView(R.id.edit_text_password_log_in) EditText mUserPassWord;

    private FirebaseAuth mAuth;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //hideActionBar();
        bindButterKnife();
        setAuthInstance();
    }

    private void hideActionBar() {

        this.getActionBar().hide();
    }

    private void bindButterKnife() {

        ButterKnife.bind(this);
    }

    private void setAuthInstance() {

        mAuth = FirebaseAuth.getInstance();
    }

    @OnClick(R.id.btn_login)
    public void logInClickListener(Button button) {
        onLogInUser();
    }

    @OnClick(R.id.btn_register)
    public void registerClickListener(Button button) {
        goToRegisterActivity();
    }

    private void onLogInUser() {
        if(getUserEmail().equals("") || getUserPassword().equals("")){
            showFieldsAreRequired();
        }else {
            logIn(getUserEmail(), getUserPassword());
        }
    }

    private void showFieldsAreRequired() {
        showAlertDialog("incorrect email",true);
    }

    private void logIn(final String email, String password) {

        showAlertDialog("Log In...",false);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                dismissAlertDialog();

                if(task.isSuccessful()){
                    setUserOnline();
                    goToMainActivity();
                }else {
                    showAlertDialog(task.getException().getMessage(),true);
                }
            }
        });
    }





    private void setUserOnline() {
        if(mAuth.getCurrentUser()!=null ) {
            String userId = mAuth.getCurrentUser().getUid();
            FirebaseDatabase.getInstance()
                    .getReference().
                    child("users").
                    child(userId).
                    child("connection").
                    setValue(UsersChatAdapter.ONLINE);
        }
    }

    private void goToMainActivity() {
        Intent intent = new Intent(SignIn.this, StudentActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void goToRegisterActivity() {
        Intent intent = new Intent(SignIn.this, Register.class);
        startActivity(intent);
    }

    private String getUserEmail() {

        return mUserEmail.getText().toString().trim();
    }

    private String getUserPassword() {

        return mUserPassWord.getText().toString().trim();
    }

    private void showAlertDialog(String message, boolean isCancelable){
        dialog = ChatHelper.buildAlertDialog("aou exchannge", message,isCancelable,SignIn.this);
        dialog.show();
    }

    private void dismissAlertDialog() {

        dialog.dismiss();
    }
}