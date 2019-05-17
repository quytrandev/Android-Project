package com.quytran.battletext;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
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
    TextView playerPoints;

    MessageAdapter messageAdapter;          //khai báo class MessageAdapter
    List<ResponseMessage> responseMessageList;          //khai báo danh sách
    AsyncTask asyncTask;        //khai báo Async Task để thực hiện việc tính điểm
    JSONObject jsonObject;      //khai báo JSON object để load dữ liệu từ file .json

    //vars
    String botWords;            //từ của bot
    String playerWords;         //từ của player
    String lastCharacter;       //lấy ký tự cuối
    boolean isWordValid;        //check xem từ có tồn tại
    BotResponse botResponse;    //class thực hiện việc lấy từ dành cho bot
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //lấy id
        playerInput=findViewById(R.id.playerInput);
        recyclerView=findViewById(R.id.conversation);
        playerPoints=findViewById(R.id.playerPoints);

        //khởi tạo ResponseMessage list và Message Adapter
        responseMessageList = new ArrayList<>();
        //truyền message list cho MessageAdapter để tự động điều phối tin nhắn
        messageAdapter=new MessageAdapter(responseMessageList,this);
        //set layout và adapter cho Recycler View
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(messageAdapter);
        parseJson();        //load dữ liệu từ json file
        botResponse = new BotResponse();        //khởi tạo class BotResponse
        //sự kiện text change cho edit text

        playerInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //check trong json object để xem từ có tồn tại hay không
                if(jsonObject.has(playerInput.getText().toString())){
                    isWordValid=true;       //từ tồn tại
                }
                else {
                    isWordValid=false;      //không tồn tại
                }
                try{
                    if(playerInput.getText().toString().length()<1){
                    playerInput.append(lastCharacter);
                }
                }
                catch (Exception ex)
                {

                }



            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //sự kiện action listener
        playerInput.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                //check xem sự kiện có phải là 'SEND' và từ player nhập vào có đúng hay không
                if(actionId == EditorInfo.IME_ACTION_SEND && isWordValid==true )
                {
                    //
                    playerWords=playerInput.getText().toString();   //lấy từ của player
                    //lấy ký tự cuối của playerWords
                    //VD: playerWords= "apple" => lastCharacter = "e"
                    lastCharacter=playerWords.substring(playerWords.length()-1);

                    //
                    if(jsonObject.has(playerWords)) {
                        //khởi tạo model và truyền giá trị cho model đó
                        ResponseMessage playerMessage =
                                new ResponseMessage(playerWords, true);

                        responseMessageList.add(playerMessage); //thêm message vào list
                        //sau khi thêm xong sẽ remove từ đó khỏi danh sách từ
                        //tránh việc từ được sử dụng 2 lần
                        jsonObject.remove(playerWords);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Your word is incorect, try again", Toast.LENGTH_SHORT).show();
                    }
                    botResponse.getBotWord();           //lấy từ được trả về từ class BotResponse
                    //khởi tạo model và truyền giá trị cho model đó
                    ResponseMessage botMessage =
                            new ResponseMessage(botWords,false);
                    responseMessageList.add(botMessage); //thêm message vào list
                    lastCharacter= botWords.substring(botWords.length()-1);
                    //sau khi thêm xong sẽ remove từ đó khỏi danh sách từ
                    //tránh việc từ được sử dụng 2 lần
                    jsonObject.remove(botWords);
                    //thông báo đến messageAdapter dữ liệu được thay đổi
                    messageAdapter.notifyDataSetChanged();

                    if (!isLastVisible()) {
                        //scroll tới tin nhắn gần nhất
                        recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
                    }

                    //gọi async task để tính điểm cho player & bot
                    asyncTask=new CalculatePointASyncTask(MainActivity.this);
                    //truyền 2 giá trị là playerWords và botWords đến async task để đo số ký tự và tính điểm
                    asyncTask.execute(new String[]{playerWords,botWords});

                    playerInput.setText("");
//                    playerInput.append(lastCharacter);
                }
                else{

                }

                return false;
            }
        });


    }

//    public void reloadMain(View view) {
//asyncTask=new CalculatePointASyncTask(MainActivity.this);
//        Intent intent = getIntent();
//        finish();
//        startActivity(intent);
//
//
//    }


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
        String json;        //chuỗi json
        try
        {
            //đọc file từ asset
            InputStream inputStream=getAssets().open("english.json");

            //lấy size cho buffer
            int size =inputStream.available();
            byte[] buffer=new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            //đọc file và load chuỗi json
            json = new String(buffer,"UTF-8");

            //khởi tạo json object với chuỗi json
             jsonObject= new JSONObject(json);
        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private class BotResponse {
        String[] mainList;          //khai báo danh sách từ
        Trie trie;                  //khai báo Trie (prefix tree)
        ArrayList<String> botWordsList; //danh sách từ của bot
        //The constructor loads the file and initializes the trie
        public BotResponse() {
            loadInfo();         //load từ

            trie = new Trie();  //khởi tạo Trie
            botWordsList = new ArrayList<String>(Arrays.asList(mainList));      //khởi tạo array list
            trie.loadKeys(botWordsList);        //load giá trị từ array list vào trie
            //ArrayList<String> list =trie.getAllPrefixMatches("c");

        }
        public void getBotWord() {
            //getAllPrefixMatches sẽ trả về một mảng các từ bắt đầu bằng 'lastCharacter'
            //VD: lastCharacter = "th" => trie.getAllPrefixMatches(lastCharacter) = ["than", "their", "they", "the",...]
            //Lấy phần tử đầu tiên tìm thấy để làm từ của bot
            botWords = trie.getAllPrefixMatches(lastCharacter).get(0);         //VD botWords ="than"
            //xóa từ khỏi danh sách tránh việc sử dụng một từ 2 lần
            botWordsList.remove(botWords);
            //khởi tạo một cây Trie mới và load lại danh sách mới (danh sách này không còn botWords)
            trie = new Trie();
            trie.loadKeys(botWordsList);            //VD: botWordsList =["their", "they", "the",...]
        }
        //Loading initializes and assigns values to all 4 arrays
        private void loadInfo() {
            try{
                //load dữ liệu là key của json object. Key ở đây sẽ là các từ (words)
                //khởi tạo vòng lặp và lặp cho đến cuối để load dữ liệu vào danh sách
                Iterator keysIterator = jsonObject.keys();
                List<String> keysList = new ArrayList<String>();
                while(keysIterator.hasNext()) {
                    String key = (String)keysIterator.next();
                    keysList.add(key);
                }
                mainList = keysList.toArray(new String[0]);   //chuyển list sang array list
            }
            catch (Exception ex){}
        }
    }
    boolean isLastVisible() {
        //scroll màn hình đến bottom
        LinearLayoutManager layoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
        int pos = layoutManager.findLastCompletelyVisibleItemPosition();
        int numItems = recyclerView.getAdapter().getItemCount();
        return (pos >= numItems);
    }
}
