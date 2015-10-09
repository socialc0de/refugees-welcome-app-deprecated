package de.pajowu.donate;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.melnykov.fab.FloatingActionButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfileFragment extends Fragment implements View.OnClickListener {
    private EditText name, emailPersonal, emailWork, phonePersonal, phoneWork;
    private ImageView imageView;
    private Button editButton;
    private FloatingActionButton finishedButton;


    public EditProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        // Initializing Variables

        // EditTexts
        //TODO Load current profile data into EditText as hints
        name = (EditText) view.findViewById(R.id.editProfileName);
        emailPersonal = (EditText) view.findViewById(R.id.editProfileEmailPersonal);
        emailWork = (EditText) view.findViewById(R.id.editProfileEmailWork);
        phonePersonal = (EditText) view.findViewById(R.id.editProfilePhonePersonal);
        phoneWork = (EditText) view.findViewById(R.id.editProfilePhoneWork);

        // ImageView
        // TODO Load ProfileImage inside
        imageView = (ImageView) view.findViewById(R.id.editProfileImage);

        // FinishedButton
        finishedButton = (FloatingActionButton) view.findViewById(R.id.editProfileFinished);
        finishedButton.setOnClickListener(this);

        // EditImage Button
        editButton = (Button) view.findViewById(R.id.editProfileEditButton);
        editButton.setOnClickListener(this);
        editButton.setAlpha(.3f);

        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.editProfileFinished:
                //TODO Sync EditTextData with Server
                break;
            case R.id.editProfileEditButton:
                //TODO Open ImageChooser
                break;
        }
    }
}
