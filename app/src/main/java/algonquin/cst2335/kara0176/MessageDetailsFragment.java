package algonquin.cst2335.kara0176;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

public class MessageDetailsFragment extends Fragment {

    MessageListFragment.ChatMessage chosenMessage;
    int chosenPosition;

    public MessageDetailsFragment(MessageListFragment.ChatMessage message, int position) {
        chosenMessage = message;
        chosenPosition = position;
    }

    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {

        View detailsView = inflater.inflate(R.layout.details_layout,container,false);

        TextView messageView = detailsView.findViewById(R.id.messageView);
        TextView sendView = detailsView.findViewById(R.id.sendView);
        TextView timeView = detailsView.findViewById(R.id.timeView);
        TextView idView = detailsView.findViewById(R.id.databaseIdView);

        messageView.setText("Message is: " + chosenMessage.getMessage());
        sendView.setText("Send or Receive? " + chosenMessage.getSendOrReceive());
        timeView.setText("Time sent: " + chosenMessage.getTimeSent());
        idView.setText("Database id is: " + chosenMessage.getId());

        Button closeButton = detailsView.findViewById(R.id.close);
        closeButton.setOnClickListener(closeClicked -> {

            getParentFragmentManager().beginTransaction().remove(this).commit();
        });

        Button deleteButton = detailsView.findViewById(R.id.delete);
        deleteButton.setOnClickListener(closeClicked -> {

            ChatRoom parentActivity = (ChatRoom)getContext();
            parentActivity.notifyMessageDeleted(chosenMessage, chosenPosition);
            getParentFragmentManager().beginTransaction().remove(this).commit();

        });

        return detailsView;
    }

}
