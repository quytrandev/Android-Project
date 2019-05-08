package com.quytran.battletext;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText playerInput;
    RecyclerView recyclerView;
    List<ResponseMessage> responseMessageList;
    MessageAdapter messageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        playerInput=findViewById(R.id.playerInput);
        recyclerView=findViewById(R.id.conversation);

        responseMessageList = new ArrayList<>();
        messageAdapter=new MessageAdapter(responseMessageList,this);
        //
        //final LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
        //layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //layoutManager.setReverseLayout(false);
        //recyclerView.setLayoutManager(layoutManager);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(messageAdapter);

        playerInput.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if(actionId == EditorInfo.IME_ACTION_SEND)
                {
                    ResponseMessage playerMessage =
                            new ResponseMessage(playerInput.getText().toString(),true);
                    responseMessageList.add(playerMessage);
                    ResponseMessage botMessage =
                            new ResponseMessage(playerInput.getText().toString(),false);
                    responseMessageList.add(botMessage);
                    messageAdapter.notifyDataSetChanged();
                    if (!isLastVisible())
                        recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
                }
                return false;
            }
        });
    }
    boolean isLastVisible() {
        LinearLayoutManager layoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
        int pos = layoutManager.findLastCompletelyVisibleItemPosition();
        int numItems = recyclerView.getAdapter().getItemCount();
        return (pos >= numItems);
    }
}
