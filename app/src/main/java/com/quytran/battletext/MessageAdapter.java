package com.quytran.battletext;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.CustomViewHolder> {

    class CustomViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public CustomViewHolder(View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.txtMsg);
        }
    }
    List<ResponseMessage> responseMessageList;
    Context context;

    public MessageAdapter(List<ResponseMessage> responseMessageList, Context context) {
        this.responseMessageList=responseMessageList;
        this.context=context;
    }

    @Override
    public int getItemViewType(int position) {
       if(responseMessageList.get(position).isPlayer())
       {
           return R.layout.player_bubble;
       }
       else return R.layout.bot_bubble;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CustomViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType,parent,false));
    }

    @Override
    public void onBindViewHolder(MessageAdapter.CustomViewHolder holder, int position) {
        holder.textView.setText(responseMessageList.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return responseMessageList.size();
    }
}
