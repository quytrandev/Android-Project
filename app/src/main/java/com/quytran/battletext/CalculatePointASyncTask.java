package com.quytran.battletext;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.TextView;

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
        //Toast.makeText(contextParent, "Start", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Void doInBackground(String... params) {

       int playerMessageLength =params[0].length();    //lấy giá trị được truyền từ MainActivity sang
       int botMessageLength =params[1].length();
       pPoints=pPoints+playerMessageLength;            //cộng dồn điểm cho player và bot
       bPoints=bPoints+botMessageLength;

       publishProgress(pPoints,bPoints,playerMessageLength,botMessageLength);               //truyền giá trị xuống onProgressUpdate sau khi xử lý

        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        int playerPoints = values[0];       //lấy giá trị từ doInBackground và gán cho playerPoints,botPoints
        int botPoints = values[1];
        int playerMessageLength=values[2];
        int botMessageLength=values[3];

        //
        TextView textViewPlayerPoints = (TextView) contextParent.findViewById(R.id.playerPoints);
        TextView textViewBotPoints = (TextView) contextParent.findViewById(R.id.botPoints);
        //Set points cho 2 textviews
        textViewPlayerPoints.setText(String.valueOf(playerPoints));
        textViewBotPoints.setText(String.valueOf(botPoints));

        AlertDialog alertDialog = new AlertDialog.Builder(contextParent).create();

        if(playerPoints>=30){
            alertDialog.setTitle("Congratulations");
            alertDialog.setMessage("YOU WON!");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
                });
                 // Set the Icon for the Dialog
                alertDialog.setIcon(R.drawable.battletext_icon1);
                alertDialog.show();
        }
        if(botPoints>=30){
            alertDialog.setTitle("Oh no...");
            alertDialog.setMessage("YOU LOST!");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            // Set the Icon for the Dialog
            alertDialog.setIcon(R.drawable.battletext_icon1);
            alertDialog.show();
        }

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        //Toast.makeText(contextParent, "Finished calculating", Toast.LENGTH_SHORT).show();
    }
}
