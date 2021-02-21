package com.example.llesson1.pillReminder.medicine;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.llesson1.R;
import com.example.llesson1.pillReminder.addMeds.AddMedicineActivity;
import com.example.llesson1.pillReminder.data.MedicineAlarm;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;



public class MedicineFragment extends Fragment implements MedicineContract.View, com.example.llesson1.pillReminder.medicine.MedicineAdapter.OnItemClickListener {

    private RecyclerView rvMedList;

    private ImageView noMedIcon;

    private TextView noMedText;

    private TextView addMedNow;

    private View noMedView;

    private ProgressBar progressLoader;


    private MedicineContract.Presenter presenter;

    private com.example.llesson1.pillReminder.medicine.MedicineAdapter medicineAdapter;

    private View.OnClickListener addMedOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            addMedicine();
        }
    };

   

    public static MedicineFragment newInstance() {
        Bundle args = new Bundle();
        MedicineFragment fragment = new MedicineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addMedNow = new TextView(getContext());
        medicineAdapter = new MedicineAdapter(new ArrayList<>(0));

        addMedNow.setOnClickListener(addMedOnClickListener);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medicine, container, false);
        bindViews(view);
        setAdapter();
        return view;
    }

    private void bindViews (View root) {
        rvMedList = (RecyclerView) root.findViewById(R.id.medicine_list);
        noMedIcon = (ImageView) root.findViewById(R.id.noMedIcon);
        noMedText = (TextView) root.findViewById(R.id.noMedText);
        addMedNow = (TextView) root.findViewById(R.id.add_med_now);
        noMedView = (View) root.findViewById(R.id.no_med_view);
        progressLoader = (ProgressBar) root.findViewById(R.id.progressLoader);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FloatingActionButton fab = Objects.requireNonNull(getActivity()).findViewById(R.id.fab_add_task);
        fab.setImageResource(R.drawable.ic_baseline_add_24);
        fab.setOnClickListener(v -> presenter.addNewMedicine());
    }

    private void setAdapter() {
        rvMedList.setAdapter(medicineAdapter);
        rvMedList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMedList.setHasFixedSize(true);
        medicineAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        presenter.onStart(day);
    }

    @Override
    public void setPresenter(MedicineContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showLoadingIndicator(boolean active) {
        if (getView() == null) {
            return;
        }
        progressLoader.setVisibility(active ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showMedicineList(List<MedicineAlarm> medicineAlarmList) {
        medicineAdapter.replaceData(medicineAlarmList);
        rvMedList.setVisibility(View.VISIBLE);
        noMedView.setVisibility(View.GONE);
    }

    @Override
    public void showAddMedicine() {
        Intent intent = new Intent(getContext(), AddMedicineActivity.class);
        startActivityForResult(intent, AddMedicineActivity.REQUEST_ADD_TASK);
    }


    @Override
    public void showMedicineDetails(long taskId, String medName) {
        Intent intent = new Intent(getContext(), AddMedicineActivity.class);
        intent.putExtra(AddMedicineActivity.EXTRA_TASK_ID, taskId);
        intent.putExtra(AddMedicineActivity.EXTRA_TASK_NAME, medName);
        startActivity(intent);
    }


    @Override
    public void showLoadingMedicineError() {
        showMessage(getString(R.string.loading_tasks_error));
    }

    @Override
    public void showNoMedicine() {
        showNoTasksViews(
                getResources().getString(R.string.no_medicine_added)
        );
    }

    @Override
    public void showSuccessfullySavedMessage() {
        showMessage(getString(R.string.successfully_saved_me_message));
    }

    @Override
    public void showMedicineDeletedSuccessfully() {
        showMessage(getString(R.string.successfully_deleted_message));
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        presenter.onStart(day);
    }

    private void showMessage(String message) {
        if (getView() != null)
            Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void addMedicine() {
        showAddMedicine();
    }

    private void showNoTasksViews(String mainText) {
        rvMedList.setVisibility(View.GONE);
        noMedView.setVisibility(View.VISIBLE);
        noMedText.setText(mainText);
        noMedIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_my_health
        ));
        addMedNow.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        presenter.result(requestCode, resultCode);
    }

    @Override
    public void onMedicineDeleteClicked(MedicineAlarm medicineAlarm) {
        presenter.deleteMedicineAlarm(medicineAlarm, getActivity());
    }
}

