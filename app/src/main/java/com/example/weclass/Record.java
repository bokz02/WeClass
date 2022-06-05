package com.example.weclass;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Record extends Fragment {

    TextView parentID, subjectCode;
    View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_record, container, false);

        initialize(); // INITIALIZE VIEWS
        getDataFromBottomNaviActivity(); // GET DATA FROM BOTTOM NAVI THE NEEDS to DISPLAY SPECIFIC DATA FROM EACH REPORTS

        return view;
    }

    // INITIALIZE VIEWS
    public void initialize(){
        parentID = view.findViewById(R.id.parentIDRecord);
        subjectCode = view.findViewById(R.id.subjectCodeRecords);
    }

    // GET DATA FROM BOTTOM NAVI THE NEEDS to DISPLAY SPECIFIC DATA FROM EACH SUBJECT
    public void getDataFromBottomNaviActivity() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            parentID.setText(bundle.getString("IDParent"));
            subjectCode.setText(bundle.getString("SubjectCode"));
        }

    }
}