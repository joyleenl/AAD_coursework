package com.example.llesson1.pillReminder.medicine;

import android.content.Context;

import com.example.llesson1.pillReminder.BasePresenter;
import com.example.llesson1.pillReminder.BaseView;
import com.example.llesson1.pillReminder.data.MedicineAlarm;

import java.util.List;


public interface MedicineContract {

    interface View extends BaseView<Presenter> {

        void showLoadingIndicator(boolean active);

        void showMedicineList(List<MedicineAlarm> medicineAlarmList);

        void showAddMedicine();

        void showMedicineDetails(long medId, String medName);

        void showLoadingMedicineError();

        void showNoMedicine();

        void showSuccessfullySavedMessage();

        void  showMedicineDeletedSuccessfully();

        boolean isActive();


    }

    interface Presenter extends BasePresenter {

        void onStart(int day);

        void reload(int day);

        void result(int requestCode, int resultCode);

        void loadMedicinesByDay(int day, boolean showIndicator);

        void deleteMedicineAlarm(MedicineAlarm medicineAlarm, Context context);

        void addNewMedicine();

    }
}
