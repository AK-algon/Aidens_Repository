package algonquin.cst2335.kara0176;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.w( "MainActivity", "In onCreate() - Loading Widgets" );

        Button loginButton = findViewById(R.id.button2);
        EditText emailEditText = findViewById(R.id.editTextTextEmailAddress);

        Intent nextPage = new Intent( MainActivity.this, SecondActivity.class);

        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        //prefs.getString("VariableName", String defaultValue);
        String emailAddress = prefs.getString("LoginName", "");
        emailEditText.setText(emailAddress);

        loginButton.setOnClickListener( clk -> {


            String emailAddress2 = emailEditText.getText().toString();

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("LoginName", emailAddress2);
            editor.apply();

            nextPage.putExtra( "EmailAddress", emailEditText.getText().toString() );
            startActivity(nextPage);

        } );






    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.w( "MainActivity", "The application is now visible on screen.");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.w( "MainActivity", "The application is now responding to user input");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w( "MainActivity", "The application no longer responds to user input");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.w( "MainActivity", "The application is no longer visible.");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.w( "MainActivity", "Any memory used by the application is freed.");
    }
}