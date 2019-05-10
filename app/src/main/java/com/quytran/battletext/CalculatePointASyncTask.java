package com.quytran.battletext;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CalculatePointASyncTask extends AsyncTask<String, Integer, Void> {

    Activity contextParent;
    static int pPoints=0;         //player points
    static int bPoints=0;         //bot points

    public CalculatePointASyncTask(Activity contextParent) {
        this.contextParent = contextParent;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Hàm này sẽ chạy đầu tiên khi AsyncTask này được gọi
        //Ở đây mình sẽ thông báo quá trình load bắt đâu "Start"

        Toast.makeText(contextParent, "Start", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Void doInBackground(String... params) {
        int playerMessageLength =params[0].length();
        int botMessageLength =params[1].length();

        pPoints=pPoints+playerMessageLength;
        bPoints=bPoints+botMessageLength;

        publishProgress(pPoints,bPoints);
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        int playerPoints = values[0];
        int botPoints = values[1];

        TextView textViewPlayerPoints = (TextView) contextParent.findViewById(R.id.playerPoints);
        TextView textViewBotPoints = (TextView) contextParent.findViewById(R.id.botPoints);
        //Set points
        textViewPlayerPoints.setText(String.valueOf(playerPoints));
        textViewBotPoints.setText(String.valueOf(botPoints));

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //Hàm này được thực hiện khi tiến trình kết thúc
        //Ở đây mình thông báo là đã "Finshed" để người dùng biết
        Toast.makeText(contextParent, "Okie, Finished", Toast.LENGTH_SHORT).show();
    }
}
