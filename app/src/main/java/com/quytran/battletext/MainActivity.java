package com.quytran.battletext;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
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
    TextView playerPoints;

    AsyncTask asyncTask;

    //vars
    String botWords;
    String playerWords;
    String lastCharacter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        playerInput=findViewById(R.id.playerInput);
        recyclerView=findViewById(R.id.conversation);
        playerPoints=findViewById(R.id.playerPoints);
        //
        responseMessageList = new ArrayList<>();
        messageAdapter=new MessageAdapter(responseMessageList,this);
        //
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(messageAdapter);

        //

        playerInput.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if(actionId == EditorInfo.IME_ACTION_SEND)
                {
                    //
                    playerWords=playerInput.getText().toString();
                    lastCharacter=playerWords.substring(playerWords.length()-1);
                    //botWords=playerInput.getText().toString();
                    botWords=lastCharacter;

                    //
                    ResponseMessage playerMessage =
                            new ResponseMessage(playerWords,true);
                    responseMessageList.add(playerMessage);
                    ResponseMessage botMessage =
                            new ResponseMessage(botWords,false);
                    responseMessageList.add(botMessage);
                    messageAdapter.notifyDataSetChanged();
                    if (!isLastVisible())
                        recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
                    asyncTask=new CalculatePointASyncTask(MainActivity.this);
                    asyncTask.execute(new String[]{playerWords,botWords});
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
