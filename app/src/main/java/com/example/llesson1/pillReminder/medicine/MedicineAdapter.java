package com.example.llesson1.pillReminder.medicine;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.llesson1.R;
import com.example.llesson1.pillReminder.data.MedicineAlarm;

import java.util.List;



public class MedicineAdapter extends RecyclerView.Adapter<com.example.llesson1.pillReminder.medicine.MedicineAdapter.MedicineViewHolder> {

    private List<MedicineAlarm> medicineAlarmList;
    private OnItemClickListener onItemClickListener;

    public MedicineAdapter(List<MedicineAlarm> medicineAlarmList) {
        this.medicineAlarmList = medicineAlarmList;
    }

    public void replaceData(List<MedicineAlarm> medicineAlarmList) {
        this.medicineAlarmList = medicineAlarmList;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public MedicineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_medicine, parent, false);
        return new MedicineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineViewHolder holder, int position) {
        final MedicineAlarm medicineAlarm = medicineAlarmList.get(position);
        if (medicineAlarm == null) {
            return;
        }
        holder.tvMedTime.setText(medicineAlarm.getStringTime());
        holder.tvMedicineName.setText(medicineAlarm.getPillName());
        holder.tvDoseDetails.setText(medicineAlarm.getFormattedDose());
        holder.ivAlarmDelete.setVisibility(View.VISIBLE);
        holder.ivAlarmDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onMedicineDeleteClicked(medicineAlarm);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (medicineAlarmList != null && !medicineAlarmList.isEmpty()) ? medicineAlarmList.size() : 0;
    }

    static class MedicineViewHolder extends RecyclerView.ViewHolder {

        private TextView tvMedTime;
        private TextView tvMedicineName;
        private TextView tvDoseDetails;
        public ImageView ivMedicineAction;
        private ImageView ivAlarmDelete;

        MedicineViewHolder(View itemView) {
            super(itemView);
            bindViews (itemView);
        }
        private void bindViews (View root){
            tvMedTime =(TextView) root.findViewById(R.id.tv_med_time);
            tvMedicineName =(TextView) root.findViewById(R.id.tv_medicine_name);
            tvDoseDetails =(TextView) root.findViewById(R.id.tv_dose_details);
            ivMedicineAction = (ImageView) root.findViewById(R.id.iv_medicine_action);
            ivAlarmDelete  = (ImageView) root.findViewById(R.id.iv_alarm_delete);
        }
    }

    interface OnItemClickListener {
        void onMedicineDeleteClicked(MedicineAlarm medicineAlarm);
    }
}
