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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
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
        parseJson();
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

    private class ReadJSONObject extends AsyncTask<String,Void,String> {


        @Override
        protected String doInBackground(String... strings) {
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }


    }
    public void parseJson(){
        String json;
        //int jsonLength=json.length();
        try
        {
            InputStream inputStream=getAssets().open("words1.json");

            int size =inputStream.available();
            byte[] buffer=new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            json = new String(buffer,"UTF-8");

            //JSONArray jsonArray=new JSONArray(json);
            JSONObject jsonObject= new JSONObject(json);
            String test=jsonObject.keys().toString();
        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    boolean isLastVisible() {
        LinearLayoutManager layoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
        int pos = layoutManager.findLastCompletelyVisibleItemPosition();
        int numItems = recyclerView.getAdapter().getItemCount();
        return (pos >= numItems);
    }
}
