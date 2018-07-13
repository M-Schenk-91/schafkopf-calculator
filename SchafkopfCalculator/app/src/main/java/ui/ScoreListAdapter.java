package ui;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.schenk.matthias.schafkopfcalculator.R;

import java.util.ArrayList;

import game.GameRound;
import ui.fragments.dialog.DeleteRoundDialogFragment;

/**
 * Created by Matze on 03.02.2018.
 */

public class ScoreListAdapter extends ArrayAdapter<GameRound> implements View.OnLongClickListener {

    private final FragmentManager frManager;
    private ArrayList<GameRound> data;
    Context context;
    private int lastPosition = -1;
    private float TEXT_SIZE_LABEL = 16;

    private static class Holder {
        LinearLayout layout;
        TextView lblPlayer1;
        TextView lblPlayer2;
        TextView lblPlayer3;
        TextView lblPlayer4;
        TextView lblDetails;
    }

    public ScoreListAdapter(ArrayList<GameRound> data, Context context, FragmentManager fragmentManager) {
        super(context, R.layout.list_item_score, data);
        this.data = data;
        this.context = context;
        this.frManager = fragmentManager;
    }


    @Override
    public boolean onLongClick(View v) {
        int position = (Integer) v.getTag();

        DeleteRoundDialogFragment frDeleteRound = new DeleteRoundDialogFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        frDeleteRound.setArguments(bundle);
        frDeleteRound.show(frManager, "detail");
        return true;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GameRound data = getItem(position);
        Holder holder;
        final View result;

        if (convertView == null) {
            holder = new Holder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.list_item_score, parent, false);

            holder.layout = (LinearLayout) convertView.findViewById(R.id.lay_list_item_main);
            holder.lblPlayer1 = (TextView) convertView.findViewById(R.id.list_item_lbl_score_player_1);
            holder.lblPlayer2 = (TextView) convertView.findViewById(R.id.list_item_lbl_score_player_2);
            holder.lblPlayer3 = (TextView) convertView.findViewById(R.id.list_item_lbl_score_player_3);
            holder.lblPlayer4 = (TextView) convertView.findViewById(R.id.list_item_lbl_score_player_4);
            holder.lblDetails = (TextView) convertView.findViewById(R.id.lbl_score_details);

            result = convertView;
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
            result = convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;


        ArrayList<TextView> lstLbls = new ArrayList<>();
        lstLbls.add(holder.lblPlayer1);
        lstLbls.add(holder.lblPlayer2);
        lstLbls.add(holder.lblPlayer3);
        lstLbls.add(holder.lblPlayer4);

        handleLongScoreStrings(lstLbls);
        boolean grayoutText = position != getCount() - 1;


        for (int i = 0; i < lstLbls.size(); i++) {
            TextView lbl = lstLbls.get(i);
            lbl.setTypeface(lbl.getTypeface(), Typeface.BOLD);
            lbl.setTextSize(TEXT_SIZE_LABEL);

            double score = data.getRoundScoreChangeForPlayer(i);
            score = UiUtils.formatScoreValue((int) score);
            String scoreString = UiUtils.getScoreString(score);
            lbl.setText(scoreString);

            //Highlight single solo winner
            boolean solo = data.getLstWinners()[i] && GameRound.getNumWinners(data.getLstWinners()) == 1;

            lbl.setTextColor(AppColors.getScoreTextColor(score, grayoutText, solo, false));
        }

        holder.lblDetails.setText(data.toString());
        holder.lblDetails.setTextColor(AppColors.getScoreTextColor(0, grayoutText, false, false));

        holder.layout.setOnLongClickListener(this);
        holder.layout.setTag(position);
        return convertView;
    }

    private void handleLongScoreStrings(ArrayList<TextView> lstLbls) {
        for (TextView lbl: lstLbls) {
            final TextView view = lbl;
            lbl.post(new Runnable() {
                @Override
                public void run() {
                    int lineCount = view.getLineCount();
                    if(lineCount > 1){
                        float pixelSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 1, getContext().getResources().getDisplayMetrics());
                        float newTextSize = view.getTextSize()/pixelSize;

                        if(newTextSize >= 4) newTextSize -= 4;

                        view.setTextSize(newTextSize);
                        view.setTypeface(view.getTypeface(), Typeface.NORMAL);
                        view.setGravity(Gravity.CENTER);
                    }
                }
            });
        }
    }


}
