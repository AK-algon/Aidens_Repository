package algonquin.cst2335.kara0176;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class SecondActivity extends AppCompatActivity {

   ImageView profileImage; //= findViewById(R.id.imageView);

    ActivityResultLauncher<Intent> cameraResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();

                        File whereAmI = getFilesDir();
                        Bitmap thumbnail = data.getParcelableExtra("data");

                        FileOutputStream file = null;
                        try { file = openFileOutput("Picture.png", Context.MODE_PRIVATE);
                            thumbnail.compress(Bitmap.CompressFormat.PNG, 100, file);
                            file.flush();
                            file.close();
                        }
                        catch (FileNotFoundException e)
                        { e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        File file2 = new File( getFilesDir(), "Picture.png");
                        ImageView profileImage = findViewById(R.id.imageView);

                        if(file2.exists())
                        {Bitmap theImage = BitmapFactory.decodeFile("/data/data/algonquin.cst2335.kara0176/files/Picture.png");
                            // myImageView.setImageBitmap( Bitmap bmp );


                            profileImage.setImageBitmap(theImage);
                        }


                    }
                }
            });



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

        camButton.setOnClickListener( clk -> {

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


            cameraResult.launch(cameraIntent);

        } );
    }
}