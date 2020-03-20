package pl.edu.pjatk.pamo.bmicalculator;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.text.DecimalFormat;

import pl.edu.pjatk.pamo.bmicalculator.form.Bmi;
import pl.edu.pjatk.pamo.bmicalculator.model.Person;
import pl.edu.pjatk.pamo.bmicalculator.service.PersonHealthService;

public class PersonHealthInfo extends Fragment  {
    private static final String PERSON = "person";
    private OnPersonHealthInfoListener mListener;

    private Person person;
    private PersonHealthService personHealthService;

    private TextView bmi;
    private TextView calories;
    private TextView recomendedDish;

    public PersonHealthInfo() {
        personHealthService = new PersonHealthService();
    }

    public static PersonHealthInfo newInstancePersonHealthInfo(Person person) {
        PersonHealthInfo fragment = new PersonHealthInfo();
        Bundle args = new Bundle();
        args.putSerializable(PERSON, person);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            person = (Person) getArguments().getSerializable(PERSON);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person_health_info, container, false);
        initOput(view);
        setOutput(view,person);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPersonHealthInfoListener) {
            mListener = (OnPersonHealthInfoListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnPersonHealthInfoListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnPersonHealthInfoListener {
        // TODO: Update argument type and name
        void OnPersonHealthInfoFragmentInteraction(Uri uri);
    }

    private void initOput(View view){
        bmi = view.findViewById(R.id.bmi_output);
        calories = view.findViewById(R.id.metabolism_output);
        recomendedDish = view.findViewById(R.id.recommendations_output);
    }

    private void setOutput(View view, Person person) {
        DecimalFormat df2 = new DecimalFormat("#.##");
        Bmi resultBmi = personHealthService.calculateBmwForPerson(person);
        String bmiMessage =  resultBmi.getBmi() + ": " + resultBmi.getDescription();
        String caloriesMessage = df2.format(personHealthService.calculateCalForPerson(person)) + " Cal";

        bmi.setText( bmiMessage );
        calories.setText(caloriesMessage);
        recomendedDish.setText(personHealthService.getDishRecommendations(person));
    }

}
