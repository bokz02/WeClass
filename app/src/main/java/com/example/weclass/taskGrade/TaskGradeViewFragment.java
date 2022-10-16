package com.example.weclass.taskGrade;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.weclass.R;


public class TaskGradeViewFragment extends Fragment {

    TextView _taskType, _taskNumber, _gradingPeriod, _subjectId;
    View view;
    Bundle bundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_task_grade_view, container, false);

        initialize();
        getDataFromTaskGradeActivity();

        return view;
    }

    public void initialize(){
        _taskType = view.findViewById(R.id.simpleText);
        _taskNumber = view.findViewById(R.id.taskNumberGradeView);
        _gradingPeriod = view.findViewById(R.id.gradingPeriodGradeView);
        _subjectId = view.findViewById(R.id.subjectIdGradeViewFragment);
    }

    public void getDataFromTaskGradeActivity(){

        bundle = this.getArguments();
        if (getArguments() != null){
            String taskType = bundle.getString("TaskType");
            _taskType.setText(taskType);

            String taskNumber = bundle.getString("TaskNumber");
            _taskNumber.setText(taskNumber);

            String gradingPeriod = bundle.getString("GradingPeriod");
            _gradingPeriod.setText(gradingPeriod);

            String subjectId = bundle.getString("SubjectId");
            _subjectId.setText(subjectId);

        }
    }


}