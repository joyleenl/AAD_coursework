package com.example.llesson1.pillReminder.addMeds;


import com.example.llesson1.pillReminder.BasePresenter;
import com.example.llesson1.pillReminder.BaseView;
import com.example.llesson1.pillReminder.data.MedicineAlarm;
import com.example.llesson1.pillReminder.data.Pills;

import java.util.List;


public interface AddMedicineContract {

    interface View extends BaseView<Presenter> {

        void showEmptyMedicineError();

        void showMedicineList();

        boolean isActive();

    }

    interface  Presenter extends BasePresenter {


        void saveMedicine(MedicineAlarm alarm, Pills pills);


        boolean isDataMissing();

        boolean isMedicineExits(String pillName);

        long addPills(Pills pills);

        Pills getPillsByName(String pillName);

        List<MedicineAlarm> getMedicineByPillName(String pillName);

        List<Long> tempIds();

        void deleteMedicineAlarm(long alarmId);

    }
}
