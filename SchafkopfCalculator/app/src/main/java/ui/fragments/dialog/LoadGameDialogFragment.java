package ui.fragments.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.schenk.matthias.schafkopfcalculator.R;

import java.util.ArrayList;

import io.IOManager;
import ui.activities.MainActivity;

/**
 * Created by Matthias on 11.02.2018.
 */

public class LoadGameDialogFragment extends DialogFragment {

    private String fileToLoad = "";
    Activity activity = null;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        activity = getActivity();

        AlertDialog.Builder builderFirst = new AlertDialog.Builder(activity);
        builderFirst.setTitle(R.string.choose_game_to_load);

        ArrayList<String> savedGames = new IOManager(activity).getSavedGameNames();
        final LoadingAdapter arrayAdapter = new LoadingAdapter(activity, savedGames);

        builderFirst.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderFirst.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                fileToLoad = arrayAdapter.getItem(which);

                AlertDialog.Builder builderSecond = new AlertDialog.Builder(getActivity());
                builderSecond.setTitle(R.string.are_u_sure);
                builderSecond.setMessage(R.string.override_game);

                builderSecond.setPositiveButton(R.string.load, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loadSelectedGame();
                        dialog.dismiss();
                    }
                });

                builderSecond.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builderSecond.show();
            }
        });

        return builderFirst.create();
    }

    private void loadSelectedGame() {
        IOManager mgr = new IOManager(activity.getApplicationContext());
        mgr.addGameListener((MainActivity) activity);
        mgr.loadGame(fileToLoad);
    }

    private class LoadingAdapter extends ArrayAdapter<String> {
        public LoadingAdapter(Context context, ArrayList<String> data) {
            super(context, 0, data);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            String dataItem = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_load, parent, false);
            }

            TextView lblFileToLoad = (TextView) convertView.findViewById(R.id.lbl_list_load);
            lblFileToLoad.setText(dataItem);
            return convertView;
        }
    }
}
