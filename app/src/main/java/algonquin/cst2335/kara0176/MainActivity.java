package algonquin.cst2335.kara0176;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * This class is the main class for Aidens Android App
 * It will check if the user password is valid.
 * The user will enter a password...
 * Then will click on the LOGIN button...
 * The program gives feedback to user if successful or not.
 * @author Aiden Karam
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    /**
     * This variable holds the text on the screen
     */
    private TextView tv = null;

    /**
     * This variable is where user types in password
     */
    private EditText cityText = null;

    /**
     * This variable is the LOGIN button
     */
    private Button forecastBtn = null;
    private String stringURL;



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, myToolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.popout_menu);
        navigationView.setNavigationItemSelectedListener((item) -> {

            onOptionsItemSelected(item);
            drawer.closeDrawer(GravityCompat.START);
            return false;

        });

        tv = findViewById(R.id.textView);
        cityText = findViewById(R.id.cityTextField);
        forecastBtn = findViewById(R.id.forcastButton);

        forecastBtn.setOnClickListener(clk -> {

            String cityName = cityText.getText().toString();
            myToolbar.getMenu();
            myToolbar.getMenu().add(1, 5, 10, cityText.getText()).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
            runForecast(cityName);
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void runForecast(String cityName) {

            Executor newThread = Executors.newSingleThreadExecutor();

            newThread.execute( () -> { /* This runs in a separate thread */

                try {
              //  String cityName = cityText.getText().toString();

                stringURL = "https://api.openweathermap.org/data/2.5/weather?q="
                        + URLEncoder.encode(cityName, "UTF-8")
                        + "&appid=7e943c97096a9784391a981c4d878b22&units=metric";

                URL url = new URL(stringURL);
                HttpURLConnection urlConnection = urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                String text = (new BufferedReader(
                        new InputStreamReader(in, StandardCharsets.UTF_8)))
                        .lines()
                        .collect(Collectors.joining("\n"));

                JSONObject theDocument = new JSONObject( text ); //this converts the String to JSON Object.
              //  JSONObject coord = theDocument.getJSONObject( "coord" );

                JSONArray weatherArray = theDocument.getJSONArray ( "weather" );
                JSONObject position0 = weatherArray.getJSONObject( 0 );

                String description = position0.getString("description");
                String iconName = position0.getString("icon");
                int vis = theDocument.getInt("visibility");

                //System.out.println("*****************"+iconName);
                //String name = theDocument.getString( "name" );

                JSONObject mainObject = theDocument.getJSONObject( "main" );
                double current = mainObject.getDouble("temp");
                double min = mainObject.getDouble("temp_min");
                double max = mainObject.getDouble("temp_max");
                int humidity = mainObject.getInt("humidity");

                Bitmap image = null;

                File file = new File(getFilesDir(), iconName + ".png");
                if (file.exists()) {
                    image = BitmapFactory.decodeFile(getFilesDir() + "/" + iconName + ".png");
                }
                else {

                    URL imgUrl = new URL("https://openweathermap.org/img/w/" + iconName + ".png");
                    HttpURLConnection connection = (HttpURLConnection) imgUrl.openConnection();
                    connection.connect();
                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        image = BitmapFactory.decodeStream(connection.getInputStream());
                        image.compress(Bitmap.CompressFormat.PNG, 100, openFileOutput(iconName + ".png", Activity.MODE_PRIVATE));
                    }
                }
                Bitmap finalImage = image;

                runOnUiThread( (  )  -> {
                    TextView tv = findViewById(R.id.temp);
                    tv.setText("The current temperature is " + current);
                    tv.setVisibility(View.VISIBLE);

                    tv = findViewById(R.id.minTemp);
                    tv.setText("The min temperature is " + min);
                    tv.setVisibility(View.VISIBLE);

                    tv = findViewById(R.id.maxTemp);
                    tv.setText("The max temperature is " + max);
                    tv.setVisibility(View.VISIBLE);

                    tv = findViewById(R.id.humidity);
                    tv.setText("The humidity is " + humidity);
                    tv.setVisibility(View.VISIBLE);

                    tv = findViewById(R.id.description);
                    tv.setText("Description: "+ description);
                    tv.setVisibility(View.VISIBLE);

                    ImageView iv = findViewById(R.id.icon);
                    iv.setImageBitmap(finalImage);
                    iv.setVisibility(View.VISIBLE);

                });
            } catch (IOException | JSONException ioe) {
                Log.e("connection error", ioe.getMessage());
            }

        } );

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);

        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        float oldSize = 14;
        TextView currentTemp = findViewById(R.id.temp);
        TextView minTemp = findViewById(R.id.minTemp);
        TextView maxTemp = findViewById(R.id.maxTemp);
        TextView humidity = findViewById(R.id.humidity);
        TextView description = findViewById(R.id.description);
        ImageView iv = findViewById(R.id.icon);

        switch(item.getItemId()) {
            case R.id.hide_views:

                currentTemp.setVisibility(View.INVISIBLE);
                minTemp.setVisibility(View.INVISIBLE);
                maxTemp.setVisibility(View.INVISIBLE);
                humidity.setVisibility(View.INVISIBLE);
                description.setVisibility(View.INVISIBLE);
                iv.setVisibility(View.INVISIBLE);
                cityText.setText("");
                break;

            case R.id.id_increase:
                oldSize++;
                currentTemp.setTextSize(oldSize);
                minTemp.setTextSize(oldSize);
                maxTemp.setTextSize(oldSize);
                humidity.setTextSize(oldSize);
                description.setTextSize(oldSize);
                cityText.setTextSize(oldSize);
                break;

            case R.id.id_decrease:
                oldSize = Float.max(oldSize-1, 5);
                currentTemp.setTextSize(oldSize);
                minTemp.setTextSize(oldSize);
                maxTemp.setTextSize(oldSize);
                humidity.setTextSize(oldSize);
                description.setTextSize(oldSize);
                cityText.setTextSize(oldSize);
                break;

            case 5:
                String cityName = item.getTitle().toString();
                runForecast(cityName);

                break;

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This function should check if this string has an Upper Case letter,
     * a lower case letter,
     * a number,
     * and a special symbol (#$%^&*!@?).
     * If it is missing any of these 4 requirements,
     * then  a Toast message saying which requirement is missing.
     * @param pw The string object (password) that we are checking.
     * @return Returns true if Password passes all tests. False otherwise.
     */
    boolean checkPasswordComplexity(String pw) {
        boolean foundUpperCase, foundLowerCase, foundNumber, foundSpecial;
        foundUpperCase = foundLowerCase = foundNumber = foundSpecial = false;

        //start looping
        for (int i=0; i < pw.length(); i++) {
            char c = pw.charAt(i);
            if (Character.isDigit(c)) {
                foundNumber = true;
            }
            if (Character.isUpperCase(c)) {
                foundUpperCase = true;
            }
            if (Character.isLowerCase(c)) {
                foundLowerCase = true;
            }
            if (isSpecialCharacter(c)) {
                foundSpecial = true;
            }
        }
  //      return foundNumber && foundUpperCase && foundLowerCase && foundSpecial;


        if(!foundUpperCase) {
            Toast.makeText(getApplicationContext(),"Your password must have an uppercase character",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(!foundLowerCase) {
            Toast.makeText(getApplicationContext(),"Your password must have an lowercase character",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(!foundNumber) {
            Toast.makeText(getApplicationContext(),"Your password must have a number",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(!foundSpecial) {
            Toast.makeText(getApplicationContext(),"Your password must have an special character",Toast.LENGTH_LONG).show();
            return false;
        }
        else
            return true; //only get here if they're all true
    }


    /**
     * This function will check if a character is of any of the special characters
     * allowed for this password to be valid
     * @param c this is the character we are checking if it is special
     * @return Returns True if c is one of: #$%^&*!@?
     */
     boolean isSpecialCharacter(char c) {
        switch(c) {
            case '#':
            case '$':
            case '%':
            case '^':
            case '&':
            case '*':
            case '!':
            case '@':
            case '?':
                return true;
        }
        return false;
     }
}