package com.example.llesson1.cadangan;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.llesson1.R;
import com.example.llesson1.pillReminder.data.MedicineAlarm;
import com.example.llesson1.pillReminder.medicine.MedicineActivity;
import com.example.llesson1.pillReminder.medicine.MedicineAdapter;
import com.example.llesson1.pillReminder.medicine.MedicineContract;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BlankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MedicineFragment extends Fragment implements MedicineContract.View, MedicineAdapter.OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final RecyclerView ARG_PARAM1 = rvMedList ;
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private MedicineContract.Presenter presenter;
    private MedicineAdapter medicineAdapter;

    public MedicineFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MedicineFragment newInstance(String param1, String param2) {
        MedicineFragment fragment = new MedicineFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        medicineAdapter = new  MedicineAdapter(new ArrayList<>(0));
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank, container, false);
    }

    @Override
    public void onMedicineDeleteClicked(MedicineAlarm medicineAlarm) {

    }

    @Override
    public void showLoadingIndicator(boolean active) {

    }

    @Override
    public void showMedicineList(List<MedicineAlarm> medicineAlarmList) {

    }

    @Override
    public void showAddMedicine() {

    }

    @Override
    public void showMedicineDetails(long medId, String medName) {

    }

    @Override
    public void showLoadingMedicineError() {

    }

    @Override
    public void showNoMedicine() {

    }

    @Override
    public void showSuccessfullySavedMessage() {

    }

    @Override
    public void showMedicineDeletedSuccessfully() {

    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void setPresenter(MedicineContract.Presenter presenter) {

    }
}