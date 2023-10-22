package com.team.classicrealm.GamesScreen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.team.classicrealm.R;

import java.util.ArrayList;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.MyViewHolder> {

    private final ArrayList<GameModel> gamesData;

    private final RecycleViewInterface recycleViewInterface;
    private final Context context;

    public GameAdapter(Context context, ArrayList<GameModel> gamesData, RecycleViewInterface recycleViewInterface){
        this.gamesData=gamesData;
        this.context=context;
        this.recycleViewInterface = recycleViewInterface;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.games_adapter_row_layout,parent,false);
        return new MyViewHolder(view,recycleViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.icon.setImageResource(gamesData.get(position).getIconID());
    }

    @Override
    public int getItemCount() {
        return gamesData.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        public MyViewHolder(@NonNull View itemView,RecycleViewInterface recycleViewInterface) {
            super(itemView);
            icon=itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recycleViewInterface!=null){
                        int pos=getAdapterPosition();
                        if(pos!=RecyclerView.NO_POSITION){
                            recycleViewInterface.onClickItem(pos);
                        }
                    }
                }
            });
        }
    }
}
