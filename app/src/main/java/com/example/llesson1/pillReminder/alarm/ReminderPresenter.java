package com.example.llesson1.pillReminder.alarm;

import androidx.annotation.NonNull;

import com.example.llesson1.pillReminder.data.History;
import com.example.llesson1.pillReminder.data.MedicineAlarm;
import com.example.llesson1.pillReminder.data.MedicineDataSource;
import com.example.llesson1.pillReminder.data.MedicineRepository;


public class ReminderPresenter implements com.example.llesson1.pillReminder.alarm.ReminderContract.Presenter {

    private final MedicineRepository mMedicineRepository;

    private final com.example.llesson1.pillReminder.alarm.ReminderContract.View mReminderView;

    ReminderPresenter(@NonNull MedicineRepository medicineRepository, @NonNull com.example.llesson1.pillReminder.alarm.ReminderContract.View reminderView) {
        this.mMedicineRepository = medicineRepository;
        this.mReminderView = reminderView;

        mReminderView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void finishActivity() {
        mReminderView.onFinish();
    }

    @Override
    public void onStart(long id) {
        loadMedicineById(id);
    }

    @Override
    public void loadMedicineById(long id) {
        loadMedicine(id);
    }


    private void loadMedicine(long id) {
        mMedicineRepository.getMedicineAlarmById(id, new MedicineDataSource.GetTaskCallback() {
            @Override
            public void onTaskLoaded(MedicineAlarm medicineAlarm) {
                if (!mReminderView.isActive()) {
                    return;
                }
                if (medicineAlarm == null) {
                    return;
                }
                mReminderView.showMedicine(medicineAlarm);
            }

            @Override
            public void onDataNotAvailable() {
                mReminderView.showNoData();
            }
        });
    }

    @Override
    public void addPillsToHistory(History history) {
        mMedicineRepository.saveToHistory(history);
    }
}
