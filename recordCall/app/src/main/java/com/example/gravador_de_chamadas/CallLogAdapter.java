package com.example.gravador_de_chamadas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CallLogAdapter extends RecyclerView.Adapter<CallLogAdapter.CallLogViewHolder> {

    private List<CallLogItem> callLogList;

    public CallLogAdapter(List<CallLogItem> callLogList) {
        this.callLogList = callLogList;
    }

    @NonNull
    @Override
    public CallLogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.call_log_item, parent, false);
        return new CallLogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CallLogViewHolder holder, int position) {
        CallLogItem item = callLogList.get(position);
        holder.callNumber.setText("Number: " + item.getNumber());
        holder.callType.setText("Type: " + item.getType());
        holder.callDate.setText("Date: " + item.getDate());
        holder.callTime.setText("Time: " + item.getTime());
        holder.callDuration.setText("Duration: " + item.getDuration());
    }

    @Override
    public int getItemCount() {
        return callLogList.size();
    }

    static class CallLogViewHolder extends RecyclerView.ViewHolder {
        TextView callNumber, callType, callDate, callTime, callDuration;

        public CallLogViewHolder(@NonNull View itemView) {
            super(itemView);
            callNumber = itemView.findViewById(R.id.call_number);
            callType = itemView.findViewById(R.id.call_type);
            callDate = itemView.findViewById(R.id.call_date);
            callTime = itemView.findViewById(R.id.call_time);
            callDuration = itemView.findViewById(R.id.call_duration);
        }
    }
}
