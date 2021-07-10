package com.sungbin.stompclient_java.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sungbin.stompclient_java.MainActivity;
import com.sungbin.stompclient_java.databinding.ItemMyBinding;
import com.sungbin.stompclient_java.databinding.ItemOtherBinding;
import com.sungbin.stompclient_java.vo.MessageVO;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = MessageAdapter.class.getSimpleName();

    private ArrayList<MessageVO> mList = new ArrayList<>();
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == MainActivity.MY){
            return new MyViewHolder(ItemMyBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else if (viewType == MainActivity.OTHER) {
            return new OtherViewHolder(ItemOtherBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }else{
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MessageVO messageVO = mList.get(position);

        if (holder instanceof MyViewHolder) {
            ((MyViewHolder) holder).bind(messageVO);
        } else if (holder instanceof OtherViewHolder) {
            ((OtherViewHolder) holder).bind(messageVO);
        }
    }

    public void add(MessageVO messageVO){
        this.mList.add(messageVO);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).getType();           // 메세지 객체 타입 비교 후 홀더 구분
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder{
        private ItemMyBinding binding;
        public MyViewHolder(ItemMyBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        void bind(MessageVO messageVO){
            binding.myMessagge.setText(messageVO.getContent());
            binding.myTime.setText(messageVO.getTime());
        }
    }

    private class OtherViewHolder extends RecyclerView.ViewHolder{
        private ItemOtherBinding binding;
        public OtherViewHolder(ItemOtherBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        void bind(MessageVO messageVO){
            binding.otherMessage.setText(messageVO.getContent());
            binding.otherName.setText(messageVO.getName());
            binding.otherTime.setText(messageVO.getTime());
        }
    }
}
