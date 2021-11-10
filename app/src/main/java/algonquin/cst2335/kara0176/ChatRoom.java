package algonquin.cst2335.kara0176;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ChatRoom extends AppCompatActivity {

    RecyclerView chatList;
    Button send, receive;
    EditText edit;
    MyChatAdapter adt;
    ArrayList<ChatMessage> messages = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatlayout);
        chatList = findViewById(R.id.myrecycler);
        send = findViewById(R.id.sendbutton);
        receive = findViewById(R.id.receivebutton);
        edit = findViewById(R.id.editTextTextPersonName);
        adt = new MyChatAdapter();
        chatList.setAdapter(adt);
        chatList.setLayoutManager(new LinearLayoutManager(this));
        edit.setText("");
        MyOpenHelper opener = new MyOpenHelper();

        send.setOnClickListener( click -> {
            String whatIsTyped = edit.getText().toString();
            Date timeNow = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
            String currentDateAndTime = sdf.format(timeNow);

            messages.add(new ChatMessage(whatIsTyped,1,currentDateAndTime));
            edit.setText("");

            adt.notifyItemInserted(messages.size() - 1);
        });

        receive.setOnClickListener( click -> {
            String whatIsTyped = edit.getText().toString();
            Date timeNow = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a", Locale.getDefault());
            String currentDateAndTime = sdf.format(timeNow);

            messages.add(new ChatMessage(whatIsTyped,2,currentDateAndTime));
            edit.setText("");

            adt.notifyItemInserted(messages.size() - 1);
        });

    }

    private class MyRowViews extends RecyclerView.ViewHolder {

        TextView messageText;
        TextView timeText;

        public MyRowViews(View itemView) {
            super(itemView);

            itemView.setOnClickListener(click -> {

                int position = getAbsoluteAdapterPosition();
                AlertDialog.Builder builder = new AlertDialog.Builder( ChatRoom.this );
                builder.setMessage("Do you want to delete the message: " + messageText.getText())
                    .setTitle("Question:")
                    .setPositiveButton("Yes", (Dialog, cl) -> {
                        ChatMessage removedMessage = messages.get(position);
                        messages.remove(position);
                        adt.notifyItemRemoved(position);
                        Snackbar.make(messageText, "You deleted message #" + position,Snackbar.LENGTH_LONG)
                            .setAction("UNDO",clk ->{
                                messages.add(position, removedMessage);
                                adt.notifyItemInserted(position);
                            })
                            .show();
                    })
                    .setNegativeButton("No", (Dialog, cl) -> {})
                    .create().show();
            });

            messageText = itemView.findViewById(R.id.message);
            timeText = itemView.findViewById(R.id.time);

        }
    }

    private class MyChatAdapter extends RecyclerView.Adapter<MyRowViews> {
        @Override
        public MyRowViews onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater inflater = getLayoutInflater();
            int layoutID;

            if (viewType==1) {
                layoutID = R.layout.sent_message;
            }
            else {layoutID = R.layout.receive_message;
            }

            View loadedRow = inflater.inflate(layoutID, parent, false);

            return new MyRowViews(loadedRow);

        }

        @Override
        public void onBindViewHolder(MyRowViews holder, int position) {
            holder.messageText.setText(messages.get(position).getMessage());
            holder.timeText.setText(messages.get(position).getTimeSent());
        }

        public int getItemViewType(int position){
            return messages.get(position).getSendOrReceive();
        }

        @Override
        public int getItemCount() {
            return messages.size();
        }}

    private class ChatMessage {
        String message;
        int sendOrReceive;
        String timeSent;

        public ChatMessage(String message, int sendOrReceive, String timeSent) {
            this.message = message;
            this.sendOrReceive = sendOrReceive;
            this.timeSent = timeSent;
        }

        public String getMessage() {
            return message;
        }

        public int getSendOrReceive() {
            return sendOrReceive;
        }

        public String getTimeSent() {
            return timeSent;
        }
    }



}



