package algonquin.cst2335.kara0176;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView mytext = findViewById(R.id.textview);
        Button btn = findViewById(R.id.mybutton);
        EditText myedit = findViewById(R.id.myedittext);
        Switch myswitch = findViewById(R.id.myswitch);
        CheckBox mychkbox = findViewById(R.id.mychkbox);
        RadioButton myradio = findViewById(R.id.myradio);
        ImageView myimage = findViewById(R.id.myimage);
        ImageButton imgbtn = findViewById( R.id.myimagebutton );

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String editString = myedit.getText().toString();
                mytext.setText("Your edit text has: " + editString);
                // btn.setOnClickListener(    ( vw ) -> {  mytext.setText("Your edit text has: " + editString);  }   );
            }
        });

       imgbtn.setOnClickListener( ( vw ) ->  {
           int height = imgbtn.getHeight();
           int width = imgbtn.getWidth();
           Context context = getApplicationContext();
           CharSequence text =  "The width = " + width + " and height = " + height;
           int duration = Toast.LENGTH_SHORT;
           Toast toast = Toast.makeText(context, text, duration);
           toast.show();
            });

        myswitch.setOnCheckedChangeListener( (vw,isChecked) -> {
            Context context = getApplicationContext();
            CharSequence text = "You clicked on the Switch and it is now: " + isChecked;
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } );

        mychkbox.setOnCheckedChangeListener( (vw,isChecked) -> {
            Context context = getApplicationContext();
            CharSequence text = "You checked the Box and it is now: " + isChecked;
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } );

        myradio.setOnCheckedChangeListener( (vw,isChecked) -> {
            Context context = getApplicationContext();
            CharSequence text = "You clicked the radio button and it is now: " + isChecked;
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } );

    }
}