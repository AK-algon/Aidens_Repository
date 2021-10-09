package algonquin.cst2335.kara0176;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        TextView text = findViewById(R.id.textView3);
        EditText phone = findViewById(R.id.editTextPhone);

        Intent fromPrevious = getIntent();
        String emailAddress = fromPrevious.getStringExtra("EmailAddress");
        text.setText("Welcome back " + emailAddress);

        Button callButton = findViewById(R.id.button2);
        Button camButton = findViewById(R.id.button3);
        ImageView profileImage = findViewById(R.id.imageView);

        SharedPreferences prefs2 = getSharedPreferences("MyData2", Context.MODE_PRIVATE);
        //prefs.getString("VariableName", String defaultValue);
        String phoneNo = prefs2.getString("Phone", "");
        phone.setText(phoneNo);

        callButton.setOnClickListener( clk -> {
            Intent call = new Intent(Intent.ACTION_DIAL);
            String phoneNumber = phone.getText().toString();

            SharedPreferences.Editor editor = prefs2.edit();
            editor.putString("Phone", phoneNumber);
            editor.apply();

            call.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(call);

        } );

   /*     camButton.setOnClickListener( clk -> {

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        ActivityResultLauncher<Intent> cameraResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            Bitmap thumbnail = data.getParcelableExtra("data");
                            profileImage.setImageBitmap(thumbnail);
                        }
                    }
                });
            cameraResult.launch(cameraIntent);

        } );*/
    }
}