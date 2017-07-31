package com.cj.simplecontacts.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.provider.CallLog;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cj.simplecontacts.R;
import com.cj.simplecontacts.enity.CallRecord;
import com.cj.simplecontacts.tool.TimeUtil;

import java.util.ArrayList;

/**
 * Created by chenjun on 17-7-27.
 */

public class CallRecordAdapter extends RecyclerView.Adapter<CallRecordAdapter.ViewHolder>{
    private ArrayList<CallRecord> list;
    private Context context;
    private boolean multiSim;
    private boolean isShowCheckBox = false;


    public CallRecordAdapter(ArrayList<CallRecord> list,Context context){
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.call_record_item, parent, false);
        CallRecordAdapter.ViewHolder vh = new CallRecordAdapter.ViewHolder(v);
        return vh;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        CallRecord callRecord = list.get(position);
        String accountId = callRecord.getAccountId();
        String number = callRecord.getNumber();
        String location = callRecord.getLocation();
        long date = callRecord.getDate();
        int type = callRecord.getType();
        String numAttr = callRecord.getNumAttr();
        String name = callRecord.getName();
        boolean checked = callRecord.isChecked();

//        Log.d("test","getType  type:"+type);
//        Log.d("test","getLocation  location:"+location);
//        Log.d("test","getAccountId  accountId:"+accountId);
//        Log.d("test","getName  name:"+name);
//        Log.d("test","isChecked  checked:"+checked);
        switch (type){
            case CallLog.Calls.INCOMING_TYPE:
                holder.photo.setImageResource(R.drawable.ic_call_log_list_outgoing_call);
                holder.name.setTextColor(Color.BLACK);
                break;
            case CallLog.Calls.OUTGOING_TYPE:
                holder.photo.setImageResource(R.drawable.ic_call_log_list_default_call);
                holder.name.setTextColor(Color.BLACK);
                break;
            case CallLog.Calls.MISSED_TYPE:
                holder.photo.setImageResource(R.drawable.ic_call_log_list_missed_call);
                holder.name.setTextColor(Color.RED);
                break;
            default:
                break;
        }

        holder.date.setText(TimeUtil.timeCompare(date));
        if(TextUtils.isEmpty(name)){
            holder.name.setText(number);
            if(TextUtils.isEmpty(numAttr)){
                holder.number.setText(location);
            }else{
                holder.number.setText(numAttr);
            }
        }else{
            holder.name.setText(name);
            if(TextUtils.isEmpty(numAttr)){
                holder.number.setText(location);
            }else{
                holder.number.setText(numAttr);
            }
        }
        if(!this.multiSim){
            holder.sim.setVisibility(View.GONE);
        }else{
            holder.sim.setVisibility(View.VISIBLE);
            if(accountId.equals("2")){
                holder.sim.setText("卡1");

                holder.sim.setBackground(context.getResources().getDrawable(R.drawable.sim1_text_bg));
            }else if(accountId.equals("3")){
                holder.sim.setText("卡2");
                holder.sim.setBackground(context.getResources().getDrawable(R.drawable.sim2_text_bg));
            }else{
                holder.sim.setText(accountId);
                holder.sim.setBackground(null);
            }
        }

        if(isShowCheckBox){
            holder.cb.setVisibility(View.VISIBLE);
            holder.arrow.setVisibility(View.GONE);
            if(checked){
                holder.cb.setChecked(true);
            }else {
                holder.cb.setChecked(false);
            }
        }else {
            holder.cb.setVisibility(View.GONE);
            holder.arrow.setVisibility(View.VISIBLE);
        }


        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,"点击打电话",Toast.LENGTH_SHORT).show();
                if(isShowCheckBox){
                    holder.cb.setChecked(!holder.cb.isChecked());
                    return;
                }
                if(listener != null){
                    listener.onItemClick(position);
                }
            }
        });

        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(listener != null){
                    listener.onLongClick(position,v);
                }
                isShowCheckBox = isShowCheckBox?isShowCheckBox:true;
                CallRecordAdapter.this.notifyDataSetChanged();
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setMultiSim(boolean multiSim) {
        this.multiSim = multiSim;
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {
        public TextView date;
        public TextView sim;
        public TextView name;
        public TextView number;
        public ImageView photo;
        public ImageView arrow;
        public CheckBox cb;
        public android.view.View view;
        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            date = (TextView) itemView.findViewById(R.id.call_date);
            sim = (TextView) itemView.findViewById(R.id.call_sim);
            name = (TextView) itemView.findViewById(R.id.call_name);
            number = (TextView) itemView.findViewById(R.id.call_number);
            photo = (ImageView) itemView.findViewById(R.id.call_log_iv);
            cb = (CheckBox) itemView.findViewById(R.id.call_log_cb);
            arrow = (ImageView) itemView.findViewById(R.id.call_log_arrow);

        }
    }

    private CallRecordAdapter.ReclerViewItemListener listener;
    public  void setReclerViewItemListener(CallRecordAdapter.ReclerViewItemListener listener){
        this.listener = listener;
    }

    public interface ReclerViewItemListener{
        void onItemClick(int position);
        void onLongClick(int position,View v);
        void onItemChecked(int position,View v);
    }
}