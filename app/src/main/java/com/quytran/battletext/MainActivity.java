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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText playerInput;
    RecyclerView recyclerView;
    List<ResponseMessage> responseMessageList;
    MessageAdapter messageAdapter;
    TextView playerPoints;

    AsyncTask asyncTask;
    JSONObject jsonObject;

    //vars
    String botWords;
    String playerWords;
    String lastCharacter;
    boolean isWordValid;
    String filename = "D:/words1.json";
    IMTester test;
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
        test = new IMTester();

        //
        playerInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(jsonObject.has(playerInput.getText().toString())){
                    isWordValid=true;
                }
                else {
                    isWordValid=false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        playerInput.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if(actionId == EditorInfo.IME_ACTION_SEND && isWordValid==true)
                {
                    //
                    playerWords=playerInput.getText().toString();
                    lastCharacter=playerWords.substring(playerWords.length()-1);
                    //botWords=playerInput.getText().toString();
                    botWords=lastCharacter;

                    //
                    if(jsonObject.has(playerWords)) {
                        ResponseMessage playerMessage =
                                new ResponseMessage(playerWords, true);
                        responseMessageList.add(playerMessage);
                        jsonObject.remove(playerWords);
                    }
                    else {

                    }

                    ResponseMessage botMessage =
                            new ResponseMessage(botWords,false);
                    responseMessageList.add(botMessage);
                    messageAdapter.notifyDataSetChanged();
                    if (!isLastVisible())
                        recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);

                    asyncTask=new CalculatePointASyncTask(MainActivity.this);
                    asyncTask.execute(new String[]{playerWords,botWords});
                }
                else{
                    Toast.makeText(getApplicationContext(), "Your word is incorect, try again u dumb shit", Toast.LENGTH_SHORT).show();

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
             jsonObject= new JSONObject(json);
            //String test=jsonObject.keys().toString();
        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private class IMTester {
        String[] mainList;
        String[][] containsCheck, prefixCheck, matchesCheck;
        Trie trie;
        //The constructor loads the file and initializes the trie
        public IMTester() {
            loadInfo();

            trie = new Trie();
            trie.loadKeys(new java.util.ArrayList<String>(Arrays.asList(mainList)));
            System.out.println("The trie should now contain " + mainList.length + " words.");
        }

        //Loading initializes and assigns values to all 4 arrays
        private void loadInfo() {
//        Scanner sc = null;
//        try {
//            sc = new Scanner(new File(filename));
//        } catch(FileNotFoundException e) {
//            System.out.println(e.getMessage());
//            System.exit(1);
//        }
            //Start by getting the Trie list
            try {


                if(jsonObject==null){
                    System.out.println("\nYour obj is null");
                }
                else{
                    System.out.println("\nYour obj is not null");

                }
                Iterator keysIterator = jsonObject.keys();
                List<String> keysList = new ArrayList<String>();
                while(keysIterator.hasNext()) {
                    String key = (String)keysIterator.next();
                    keysList.add(key);
                }
                mainList = keysList.toArray(new String[0]);   //mainList

            }
            catch (Exception ex){}

//        mainList = new String[Integer.parseInt(sc.nextLine())];
//        for(int i = 0; i<mainList.length; i++) mainList[i] = sc.nextLine();

            //Then, get the getPrefix check split up
            prefixCheck = new String[mainList.length][2];
            for(int i = 0; i<prefixCheck.length; i++) prefixCheck[i] = mainList[i].split(" ");


        }



        public void testPrefix() {
            System.out.println("\nTime to test the getPrefix() method! The correct answer is shown in parentheses. ");
            for(String[] row : prefixCheck)
                System.out.printf("The longest prefix of '%s' = %s (%s)%n", row[0], this.trie.getPrefix(row[0]), (row.length == 1 ? "" : row[1]));
        }



        //For the testMatches block, need to print row[1:]
        private String matchesList(String[] row) {
            String result = "";
            for(int i=1; i<row.length-1; i++) result+=row[i] + ", ";
            if(row.length>1) result+=row[row.length-1];
            return result;
        }



    }
    boolean isLastVisible() {
        LinearLayoutManager layoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
        int pos = layoutManager.findLastCompletelyVisibleItemPosition();
        int numItems = recyclerView.getAdapter().getItemCount();
        return (pos >= numItems);
    }
}
