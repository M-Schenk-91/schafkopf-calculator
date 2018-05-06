package ui.fragments.dialog;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.schenk.matthias.schafkopfcalculator.R;

import game.Game;
import game.GameController;
import io.IOManager;
import ui.activities.MainActivity;
import ui.UiUtils;

/**
 * Created by Matthias on 10.02.2018.
 */

public class SaveGameDialogFragment extends DialogFragment {

    private EditText edtFileName;

    public SaveGameDialogFragment(){}

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.fragment_dialog_save, null);
        builder.setView(dialogView);

        edtFileName = (EditText) dialogView.findViewById(R.id.edt_file_name_to_save);
        edtFileName.setText(UiUtils.getDateString());

        builder.setMessage(R.string.save_game)
                .setPositiveButton(R.string.confirm_save_game, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Game game = GameController.getActiveGame();
                        String dirName = edtFileName.getText().toString();
                        //TODO ProgressWorker
                        new IOManager(getActivity().getApplicationContext()).saveGame(game, dirName);
                        MainActivity.showToast("Spiel gespeichert");

                    }
                })

                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        return builder.create();
    }
}
