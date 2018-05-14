package ui.fragments.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.schenk.matthias.schafkopfcalculator.R;

public class CustomValueInputFragment extends Fragment {

    private AddNewRoundDialogFragment addNewRoundDialogListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_custom_value_input, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void setAddNewRoundDialogListener(AddNewRoundDialogFragment addNewRoundDialogListener) {
        this.addNewRoundDialogListener = addNewRoundDialogListener;
    }
}
