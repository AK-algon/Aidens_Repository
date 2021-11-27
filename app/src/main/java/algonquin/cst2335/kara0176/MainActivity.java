package algonquin.cst2335.kara0176;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

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

        tv = findViewById(R.id.textView);
        cityText = findViewById(R.id.cityTextField);
        forecastBtn = findViewById(R.id.forcastButton);

        forecastBtn.setOnClickListener( clk -> {

            String cityName = cityText.getText().toString();

            AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Getting Forecast")
                    .setMessage("We're calling people in " + cityName
                    + " to look outside their windows and tell us what's the weather like over there.")
                    .setView(new ProgressBar(MainActivity.this))
                    .show();

            Executor newThread = Executors.newSingleThreadExecutor();



            newThread.execute( () -> { /* This runs in a separate thread */

            try {
               // String cityName = cityText.getText().toString();

                stringURL = "https://api.openweathermap.org/data/2.5/weather?q="
                        + URLEncoder.encode(cityName, "UTF-8")
                        + "&appid=7e943c97096a9784391a981c4d878b22&units=metric&mode=xml";

                URL url = new URL(stringURL);
                HttpURLConnection urlConnection = urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput( in  , "UTF-8");

                String current = null;  // mainObject.getDouble("temp");
                String min = null;      //mainObject.getDouble("temp_min");
                String max = null;      //mainObject.getDouble("temp_max");
                String humidity = null; //mainObject.getInt("humidity");
                String description = null;
                String iconName = null; //position0.getString("icon");

                while( xpp.next() != XmlPullParser.END_DOCUMENT )  {

                    switch( xpp.getEventType()) {

                        case XmlPullParser.START_TAG:
                            if (xpp.getName().equals("temperature")) {
                                current = xpp.getAttributeValue(null, "value");  //this gets the current temperature

                                min = xpp.getAttributeValue(null, "min"); //this gets the min temperature

                                max = xpp.getAttributeValue(null, "max"); //this gets the max temperature
                            }
                            else if (xpp.getName().equals("weather")) {
                                description = xpp.getAttributeValue(null, "value");  //this gets the weather description

                                iconName = xpp.getAttributeValue(null, "icon"); //this gets the icon name
                            }
                            else if (xpp.getName().equals("humidity")) {
                                humidity = xpp.getAttributeValue(null, "value");
                            }
                            break;
                        case XmlPullParser.END_TAG:

                            break;
                        case XmlPullParser.TEXT:

                            break;
                    }

                }


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

                String finalCurrent = current;
                String finalMin = min;
                String finalMax = max;
                String finalHumidity = humidity;
                String finalDescription = description;
                runOnUiThread( (  )  -> {
                    TextView tv = findViewById(R.id.temp);
                    tv.setText("The current temperature is " + finalCurrent);
                    tv.setVisibility(View.VISIBLE);

                    tv = findViewById(R.id.minTemp);
                    tv.setText("The min temperature is " + finalMin);
                    tv.setVisibility(View.VISIBLE);

                    tv = findViewById(R.id.maxTemp);
                    tv.setText("The max temperature is " + finalMax);
                    tv.setVisibility(View.VISIBLE);

                    tv = findViewById(R.id.humidity);
                    tv.setText("The humidity is " + finalHumidity);
                    tv.setVisibility(View.VISIBLE);

                    tv = findViewById(R.id.description);
                    tv.setText("Description: "+ finalDescription);
                    tv.setVisibility(View.VISIBLE);

                    ImageView iv = findViewById(R.id.icon);
                    iv.setImageBitmap(finalImage);
                    iv.setVisibility(View.VISIBLE);

                    dialog.hide();
                });
            } catch (IOException | XmlPullParserException ioe) {
                Log.e("connection error", ioe.getMessage());
            }


        } );





         //  String password = et.getText().toString();
          //  if (checkPasswordComplexity(password)) {
          //      tv.setText("Your password meets the requirements");
          //  }
          //  else {
             //   tv.setText("You shall not pass!");
         //   }
         });


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