package com.example.llesson1;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.llesson1.pillReminder.data.MedicineRepository;
import com.example.llesson1.pillReminder.data.source.MedicinesLocalDataSource;


public class Injection {

    public static MedicineRepository provideMedicineRepository(@NonNull Context context) {
        return MedicineRepository.getInstance(MedicinesLocalDataSource.getInstance(context));
    }
}
