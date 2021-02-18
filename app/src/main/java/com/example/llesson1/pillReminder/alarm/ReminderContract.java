package com.example.llesson1.pillReminder.alarm;


import com.example.llesson1.pillReminder.BasePresenter;
import com.example.llesson1.pillReminder.BaseView;
import com.example.llesson1.pillReminder.data.History;
import com.example.llesson1.pillReminder.data.MedicineAlarm;

public interface ReminderContract {

    interface View extends BaseView<Presenter> {

        void showMedicine(MedicineAlarm medicineAlarm);

        void showNoData();

        boolean isActive();

        void onFinish();

    }

    interface Presenter extends BasePresenter {

        void finishActivity();

        void onStart(long id);

        void loadMedicineById(long id);

        void addPillsToHistory(History history);

    }
}
