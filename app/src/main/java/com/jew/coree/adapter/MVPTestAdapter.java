package com.jew.coree.adapter;

import android.content.Context;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.corelibs.utils.ToastMgr;
import com.jew.coree.R;
import com.jew.coree.view.MVPTestActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pengzhu on 2019/5/6/006.
 */
public class MVPTestAdapter extends RecyclerView.Adapter<MVPTestAdapter.MVPTestHolder> {
    private Context context;
    private String[] data;
    private MVPTestActivity.CommonHandler commonHandler;

    public MVPTestAdapter(Context context, String[] data) {
        this.context = context;
        this.data = data;
    }

    public void setHandler(MVPTestActivity.CommonHandler handler){
        commonHandler = handler;
    }

    @NonNull
    @Override
    public MVPTestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //为了让item  match_parent起效
        return new MVPTestHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MVPTestHolder holder, int position) {
        if(TextUtils.isEmpty(data[position])){
            ToastMgr.show("有空值");
        }
        holder.tvTitle.setText(data[position]);
        holder.itemView.setTag(data[position]);
        holder.itemView.setOnClickListener(v -> {
            Message message = new Message();
            message.what = 0;
            message.arg1 = position;
            commonHandler.sendMessage(message);
        });
    }



    @Override
    public int getItemCount() {
        return data.length;
    }


    static class MVPTestHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_title)
        TextView tvTitle;

        public MVPTestHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
