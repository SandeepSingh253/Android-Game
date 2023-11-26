package com.team.classicrealm.ScoreBoard;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.team.classicrealm.R;

import java.util.ArrayList;

public class ScoreBoardAdapter extends RecyclerView.Adapter<ScoreBoardAdapter.ViewHolder> {

    private ArrayList<Scores>  localDataSet;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView userName;
        private final TextView score;
        private final ImageView trophy;

        private final CardView rootLayout;
        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            userName = (TextView) view.findViewById(R.id.gamesScreenAdapterUserName);
            score = (TextView) view.findViewById(R.id.gamesScreenAdapterScoreTV);
            trophy = (ImageView) view.findViewById(R.id.gamesScreenAdapterTrophy);
            rootLayout = (CardView) view.findViewById(R.id.scoreBoardRowRootLayout);
        }

        public TextView getUserNameTV() {
            return userName;
        }
        public TextView getScoreTV() {
            return score;
        }
        public ImageView getTrophyIV() {
            return trophy;
        }
        public CardView getRootLayout() {
            return rootLayout;
        }
    }

    /**
     * Initialize the dataset of the Adapter
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView
     */
    public ScoreBoardAdapter(ArrayList<Scores> dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.scoreboard_adapter_row_layout, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        if(position==0) {
            viewHolder.getTrophyIV().setVisibility(View.VISIBLE);
        }else{
            viewHolder.getTrophyIV().setVisibility(View.INVISIBLE);
        }
        if(position%2==0){
            viewHolder.getRootLayout().setCardBackgroundColor(Color.rgb(211,211,211));
        }else{
            viewHolder.getRootLayout().setCardBackgroundColor(Color.rgb(255,255,255));
        }
        viewHolder.getUserNameTV().setText(localDataSet.get(position).getuName());
        viewHolder.getScoreTV().setText("Score: "+localDataSet.get(position).getScore());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
