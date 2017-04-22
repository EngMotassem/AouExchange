package apps.aouexchange.StudentActivity.loginreg;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

import apps.aouexchange.R;
import apps.aouexchange.StudentActivity.FireChatHelper.ChatHelper;
import apps.aouexchange.StudentActivity.StudentActivity;
import apps.aouexchange.StudentActivity.adapters.UsersChatAdapter;
import apps.aouexchange.StudentActivity.models.User;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Register extends AppCompatActivity {
    @BindView(R.id.edit_text_display_name)
    EditText mUserFirstNameRegister;
    @BindView(R.id.edit_text_email_register) EditText mUserEmailRegister;
    @BindView(R.id.edit_text_password_register) EditText mUserPassWordRegister;
    @BindView(R.id.stepartmnet)
    RadioGroup mUserDepartment;


    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private AlertDialog dialog;
    private String MY_PREFS_NAME="MyPref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        bindButterKnife();
        setAuthInstance();
        setDatabaseInstance();
    }
    private void bindButterKnife() {

        ButterKnife.bind(this);
    }

    private void setAuthInstance() {


        mAuth = FirebaseAuth.getInstance();
    }

    private void setDatabaseInstance() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @OnClick(R.id.btn_register_user)
    public void registerUserClickListener(Button button) {

        onRegisterUser();
    }

    @OnClick(R.id.btn_cancel_register)
    public void cancelClickListener(Button button) {
        finish();
    }

    private void onRegisterUser() {
        if(getUserDisplayName().equals("") || getUserEmail().equals("") || getUserPassword().equals("")){
            showFieldsAreRequired();
        }else if(isIncorrectEmail(getUserEmail()) || isIncorrectPassword(getUserPassword())) {
            showIncorrectEmailPassword();
        }else {
            signUp(getUserEmail(), getUserPassword());
        }
    }

    private boolean isIncorrectEmail(String userEmail) {
        return !android.util.Patterns.EMAIL_ADDRESS.matcher(userEmail).matches();
    }

    private boolean isIncorrectPassword(String userPassword) {
        return !(userPassword.length() >= 6);
    }

    private void showIncorrectEmailPassword() {
        showAlertDialog(getString(R.string.error_incorrect_email_pass), true);
    }

    private void showFieldsAreRequired() {
        showAlertDialog(getString(R.string.error_fields_empty), true);
    }

    private void showAlertDialog(String message, boolean isCancelable){

        dialog = ChatHelper.buildAlertDialog(getString(R.string.login_error_title),message,isCancelable,Register.this);
        dialog.show();
    }

    private String getUserDisplayName() {
        return mUserFirstNameRegister.getText().toString().trim();
    }

    private String getUserEmail() {
        return mUserEmailRegister.getText().toString().trim();
    }

    private String getUserPassword() {
        return mUserPassWordRegister.getText().toString().trim();
    }

    private String getUserDepartment(){

        int selectedId = mUserDepartment.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) findViewById(selectedId);
        return radioButton.getText().toString().trim();
    }


    private void signUp(String email, String password) {

        showAlertDialog("Registering...",true);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                dismissAlertDialog();

                if(task.isSuccessful()){
                    onAuthSuccess(task.getResult().getUser());
                }else {
                    showAlertDialog(task.getException().getMessage(), true);
                }
            }
        });
    }

    private void dismissAlertDialog() {

        dialog.dismiss();
    }

    private void onAuthSuccess(FirebaseUser user) {
        createNewUser(user.getUid());
        goToMainActivity();
    }

    private void goToMainActivity() {
        Intent intent = new Intent(Register.this, StudentActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void createNewUser(String userId){
        User user = buildNewUser();
        Toast.makeText(getApplicationContext(),getUserDepartment(),Toast.LENGTH_LONG).show();

        savetoSharedPref();


        mDatabase.child("users").child(userId).setValue(user);
    }

    private void savetoSharedPref() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("depname", getUserDepartment());
        editor.commit();


    }

    private User buildNewUser() {
        return new User(
                getUserDisplayName(),
                getUserEmail(),
                UsersChatAdapter.ONLINE,
                ChatHelper.generateRandomAvatarForUser(),
                new Date().getTime(),
                getUserDepartment()
        );
    }

}