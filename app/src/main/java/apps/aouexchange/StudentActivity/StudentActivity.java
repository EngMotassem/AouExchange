package apps.aouexchange.StudentActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import apps.aouexchange.R;
import apps.aouexchange.StudentActivity.Chat.ChatActivityFragment;
import apps.aouexchange.StudentActivity.adapters.UsersChatAdapter;
import apps.aouexchange.StudentActivity.addActivity.AddActivityFragment;
import apps.aouexchange.StudentActivity.admin.AdminActivity;
import apps.aouexchange.StudentActivity.admin.StudentActivitiesListFragment;
import apps.aouexchange.StudentActivity.broadcast.BroadcastActivityFragment;
import apps.aouexchange.StudentActivity.getData.getDataFragment;
import apps.aouexchange.StudentActivity.loginreg.SignIn;
import apps.aouexchange.StudentActivity.models.User;
import apps.aouexchange.StudentActivity.models.admin;
import butterknife.BindView;
import butterknife.ButterKnife;

public class StudentActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mUserRefDatabase,adminRef;
    private FirebaseDatabase adminDatabase;
    private String mCurrentUserUid;
    private UsersChatAdapter mUsersChatAdapter;
    private  FirebaseUser mUser;
    boolean anAdminUser;



    private TextView userNameTxt,userEmailTxt;

    private List<String> mUsersKeyList;

    private ChildEventListener mChildEventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mUsersChatAdapter = new UsersChatAdapter(this, new ArrayList<User>());

        bindButterKnife();
        setAuthInstance();
        isAdmin(mUser);
        setUsersDatabase();
        setUsersKeyList();
        setAuthListener();
        //setUserRecyclerView();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);

        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        View header=navigationView.getHeaderView(0);

        userNameTxt= (TextView) header.findViewById(R.id.userHeaderTxtV);
        userEmailTxt= (TextView) header.findViewById(R.id.userEmailHeV);



    }








    private void goToLogin() {
        Intent intent = new Intent(this, SignIn.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // LoginActivity is a New Task
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); // The old task when coming back to this activity should be cleared so we cannot come back to it.
        startActivity(intent);
    }





    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.student, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        displaySelectedScreen(id);
        return true;
    }


    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        android.support.v4.app.Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_camera:
                fragment = new BroadcastActivityFragment();
                break;
            case R.id.nav_gallery:
                fragment = new AddActivityFragment();
                break;
            case R.id.nav_slideshow:
                fragment = new StudentActivitiesListFragment();
                break;
            case R.id.nav_manage:
                fragment = new ChatActivityFragment();
                break;
            case R.id.nav_share:
                mAuth.signOut();
                break;

        }

        //replacing the fragment
        if (fragment != null) {
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAuth.signOut();
    }



    private  String getDepartment(){

        String mDepartment="";
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);


        mDepartment = pref.getString("depname", "No name defined");



      //  Toast.makeText(getApplicationContext(),mDepartment,Toast.LENGTH_SHORT).show();


        return mDepartment;

    }


    private void setUsersKeyList() {
        mUsersKeyList = new ArrayList<String>();
    }

    private void setAuthListener() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                // hideProgressBarForUsers();
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {

                    userNameTxt.setText(getDepartment());
                    userEmailTxt.setText(user.getEmail());
                    setUserData(user);
                    queryAllUsers();
                } else {
                    // User is signed out
                    goToLogin();
                }
            }
        };
    }


    private void bindButterKnife() {
        ButterKnife.bind(this);
    }

    private void setAuthInstance() {

        mAuth = FirebaseAuth.getInstance();


    }

    private void setUsersDatabase() {
        mUserRefDatabase = FirebaseDatabase.getInstance().getReference().child("users");
    }
    private void queryAllUsers() {
        mChildEventListener = getChildEventListener();

        //mUserRefDatabase.orderByChild("depatrment").equalTo(getDepartment());
        //mUserRefDatabase.limitToFirst(50).addChildEventListener(mChildEventListener);

        mUserRefDatabase.orderByChild("mDepatrment").equalTo(getDepartment()).addChildEventListener(mChildEventListener);

    }

    private ChildEventListener getChildEventListener() {
        return new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
               // mUsersChatAdapter = new UsersChatAdapter(this, new ArrayList<User>());


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


    private  boolean isAdmin(FirebaseUser user){


        user=mAuth.getCurrentUser();
        adminDatabase = FirebaseDatabase.getInstance();
        adminRef = adminDatabase.getReference().child("admin");

        final FirebaseUser finalUser = user;
        adminRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                admin madmin=new admin();
                madmin=dataSnapshot.getValue(admin.class);
                if(finalUser.getEmail().equals(madmin.getDisplay())){
                    anAdminUser =true;
                    Intent intent=new Intent(StudentActivity.this,AdminActivity.class);
                    startActivity(intent);

                    //Toast.makeText(getApplicationContext(),"admin",Toast.LENGTH_LONG).show();
                }
                else{
                    anAdminUser=false;

                  //  Toast.makeText(getApplicationContext(),madmin.getDisplay(),Toast.LENGTH_LONG).show();

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return anAdminUser;

    }


}
