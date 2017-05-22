package gustavo.brilhante.githupjavapop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import gustavo.brilhante.githupjavapop.adapter.holders.PullRequestHolder;
import gustavo.brilhante.githupjavapop.listeners.PullRequestListener;
import gustavo.brilhante.githupjavapop.models.PullRequest;

/**
 * Created by Gustavo on 18/05/17.
 */

public class PullRequestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<PullRequest> data;
    Context context;
    PullRequestListener listener;

    public PullRequestAdapter(ArrayList<PullRequest> data, Context context) {
        this.data = data;
        this.context = context;
    }

    public void setListener(PullRequestListener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return PullRequestHolder.build(parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        PullRequestHolder viewHolder = (PullRequestHolder) holder;
        viewHolder.bind(data.get(position));
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null && position<data.size()) {
                        listener.onPullRequestClick(data.get(position));
                    }
                }
            });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
