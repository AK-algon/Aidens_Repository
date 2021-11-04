package algonquin.cst2335.kara0176;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
    private EditText et = null;

    /**
     * This variable is the LOGIN button
     */
    private Button btn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         tv = findViewById(R.id.textView);
         et = findViewById(R.id.editText);
         btn = findViewById(R.id.button);

        btn.setOnClickListener( clk -> {

            String password = et.getText().toString();
            if (checkPasswordComplexity(password)) {
                tv.setText("Your password meets the requirements");
            }
            else {
                tv.setText("You shall not pass!");
            }
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