package com.example.llesson1.pillReminder.report;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.llesson1.R;
import com.example.llesson1.pillReminder.data.History;

import java.util.ArrayList;
import java.util.List;



public class MonthlyReportFragment extends Fragment implements MonthlyReportContract.View {

    private RecyclerView rvHistoryList;

    private ProgressBar progressLoader;

    private ImageView noMedIcon;

    private TextView noMedText;

    private View noMedView;

    private TextView filteringLabel;

    private LinearLayout tasksLL;

    private HistoryAdapter mHistoryAdapter;

    private MonthlyReportContract.Presenter presenter;

    MonthlyReportFragment(View itemView){
        super(itemView);
        bindViews(itemView);
    }

    private void bindViews (View root){
        rvHistoryList = (RecyclerView) root.findViewById(R.id.rv_history_list);
        progressLoader = (ProgressBar) root.findViewById(R.id.progressLoader);
        noMedIcon = (ImageView) root.findViewById(R.id.noMedIcon);
        noMedText = (TextView) root.findViewById(R.id.noMedText);
        noMedView = (View) root.findViewById(R.id.no_med_view);
        filteringLabel = (TextView) root.findViewById(R.id.filteringLabel);
        tasksLL = (LinearLayout) root.findViewById(R.id.tasksLL);
    }

    public static com.example.llesson1.pillReminder.report.MonthlyReportFragment newInstance() {
        Bundle args = new Bundle();
        com.example.llesson1.pillReminder.report.MonthlyReportFragment fragment = new com.example.llesson1.pillReminder.report.MonthlyReportFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHistoryAdapter = new HistoryAdapter(new ArrayList<History>());
        setHasOptionsMenu(true);
    }

    private void setAdapter() {
        rvHistoryList.setAdapter(mHistoryAdapter);
        rvHistoryList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvHistoryList.setHasFixedSize(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        setAdapter();
        return view;
    }

    @Override
    public void onResume() {
            super.onResume();
            presenter.start();
    }

    @Override
    public void setPresenter(MonthlyReportContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        if (getView() == null) {
            return;
        }
        progressLoader.setVisibility(active ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showHistoryList(List<History> historyList) {
        mHistoryAdapter.replaceData(historyList);
        tasksLL.setVisibility(View.VISIBLE);
        noMedView.setVisibility(View.GONE);
    }

    @Override
    public void showLoadingError() {

    }

    @Override
    public void showNoHistory() {
        showNoHistoryView(
                getString(R.string.no_history),
                R.drawable.ic_my_health
        );
    }

    @Override
    public void showTakenFilterLabel() {
        filteringLabel.setText(R.string.taken_label);
    }

    @Override
    public void showIgnoredFilterLabel() {
        filteringLabel.setText(R.string.ignore_label);
    }

    @Override
    public void showAllFilterLabel() {
        filteringLabel.setText(R.string.all_label);
    }

    @Override
    public void showNoTakenHistory() {
        showNoHistoryView(
                getString(R.string.no_taken_med_history),
                R.drawable.ic_my_health
        );
    }

    @Override
    public void showNoIgnoredHistory() {
        showNoHistoryView(
                getString(R.string.no_ignored_history),
                R.drawable.ic_my_health
        );
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showFilteringPopUpMenu() {
        PopupMenu popup = new PopupMenu(getContext(), getActivity().findViewById(R.id.menu_filter));
        popup.getMenuInflater().inflate(R.menu.filter_history, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.all:
                        presenter.setFiltering(FilterType.ALL_MEDICINES);
                        break;
                    case R.id.taken:
                        presenter.setFiltering(FilterType.TAKEN_MEDICINES);
                        break;
                    case R.id.ignored:
                        presenter.setFiltering(FilterType.IGNORED_MEDICINES);
                        break;
                }
                presenter.loadHistory(true);
                return true;
            }
        });
        popup.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.history_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_filter:
                showFilteringPopUpMenu();
                break;
        }
        return true;
    }

    private void showNoHistoryView(String mainText, int iconRes) {
        tasksLL.setVisibility(View.GONE);
        noMedView.setVisibility(View.VISIBLE);

        noMedText.setText(mainText);
        noMedIcon.setImageDrawable(getResources().getDrawable(iconRes));
    }
}
