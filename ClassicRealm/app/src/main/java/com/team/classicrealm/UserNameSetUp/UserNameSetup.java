package com.team.classicrealm.UserNameSetUp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.team.classicrealm.GameUtility.Constants;
import com.team.classicrealm.GameUtility.Prompts;
import com.team.classicrealm.MainScreen.MainMenu;
import com.team.classicrealm.R;

public class UserNameSetup extends AppCompatActivity {
    Button setUserNameB;
    EditText userNameET;

    SharedPreferences sharedpreferences;

    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_name_setup);

        userNameET=findViewById(R.id.userNameEditText);
        setUserNameB=findViewById(R.id.setUserNameButton);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        hideSystemUI();

        database=FirebaseDatabase.getInstance();
        sharedpreferences = getSharedPreferences(Constants.PREF_USER, getApplication().MODE_PRIVATE);

        if(sharedpreferences.contains(Constants.SHARED_PREF_KEY_USER_NAME)){
            Intent i=new Intent(getApplicationContext(), MainMenu.class);
            i.putExtra(Constants.INTENT_KEY_USER_NAME,sharedpreferences.getString(Constants.SHARED_PREF_KEY_USER_NAME,Constants.DEFAULT_VALUE_USER_NAME));
            startActivity(i);
            finish();
        }


        final ValueEventListener[] e = new ValueEventListener[1];

        DatabaseReference usersRef=database.getReference().child(Constants.DATABASE_CHILD_USERS);

        setUserNameB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName=userNameET.getText().toString();
                String userCap=userName.toUpperCase();
                if(!userName.isBlank() && userName.length()>=Constants.USER_NAME_MIN_LENGTH && userName.length()<=Constants.USER_NAME_MAX_LENGTH){
                    e[0] =usersRef.child(userCap).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(!snapshot.exists()){
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString(Constants.SHARED_PREF_KEY_USER_NAME, userName);
                                editor.apply();
                                usersRef.child(userCap).removeEventListener(e[0]);
                                usersRef.child(userCap).setValue(true);
                                Intent i=new Intent(getApplicationContext(), MainMenu.class);
                                i.putExtra(Constants.INTENT_KEY_USER_NAME,userName);
                                startActivity(i);
                                finish();
                            }else{
                                Toast.makeText(UserNameSetup.this, Prompts.USERNAME_IN_USE, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(UserNameSetup.this, error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    Toast.makeText(UserNameSetup.this, Prompts.USERNAME_INVALID_LENGTH, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);

    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }
}