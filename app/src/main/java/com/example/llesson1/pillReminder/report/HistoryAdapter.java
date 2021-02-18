package com.example.llesson1.pillReminder.report;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.llesson1.R;
import com.example.llesson1.pillReminder.data.History;

import java.util.List;


public class HistoryAdapter extends RecyclerView.Adapter<com.example.llesson1.pillReminder.report.HistoryAdapter.HistoryViewHolder> {


    private List<History> mHistoryList;

    HistoryAdapter(List<History> historyList) {
        setList(historyList);
    }

    void replaceData(List<History> tasks) {
        setList(tasks);
        notifyDataSetChanged();
    }

    private void setList(List<History> historyList) {
        this.mHistoryList = historyList;
    }


    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        History history = mHistoryList.get(position);
        if (history == null) {
            return;
        }
        holder.tvMedDate.setText(history.getFormattedDate());
        setMedicineAction(holder, history.getAction());
        holder.tvMedicineName.setText(history.getPillName());
        holder.tvDoseDetails.setText(history.getFormattedDose());

    }

    private void setMedicineAction(HistoryViewHolder holder, int action) {
        switch (action) {
            case 0:
            default:
                holder.ivMedicineAction.setVisibility(View.GONE);
                break;
            case 1:
                holder.ivMedicineAction.setVisibility(View.VISIBLE);
                holder.ivMedicineAction.setImageResource(R.drawable.image_reminder_taken);
                break;
            case 2:
                holder.ivMedicineAction.setImageResource(R.drawable.image_reminder_not_taken);
                holder.ivMedicineAction.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return (mHistoryList != null && !mHistoryList.isEmpty()) ? mHistoryList.size() : 0;
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {

        private TextView tvMedDate;
        private TextView tvMedicineName;
        private TextView tvDoseDetails;
        private ImageView ivMedicineAction;

        HistoryViewHolder(View itemView) {
            super(itemView);
            bindViews(itemView);
        }

        private void bindViews(View root) {
            tvMedDate = (TextView) root.findViewById(R.id.tv_med_date);
            tvMedicineName = (TextView) root.findViewById(R.id.tv_medicine_name);
            tvDoseDetails = (TextView) root.findViewById(R.id.tv_dose_details);
            ivMedicineAction = (ImageView) root.findViewById(R.id.iv_medicine_action);
        }
    }
}
