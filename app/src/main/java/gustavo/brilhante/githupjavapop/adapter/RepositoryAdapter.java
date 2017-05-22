package gustavo.brilhante.githupjavapop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import gustavo.brilhante.githupjavapop.adapter.holders.EmptyHolder;
import gustavo.brilhante.githupjavapop.adapter.holders.HeaderHolder;
import gustavo.brilhante.githupjavapop.adapter.holders.RepositoryHolder;
import gustavo.brilhante.githupjavapop.constants.Config;
import gustavo.brilhante.githupjavapop.constants.Constants;
import gustavo.brilhante.githupjavapop.listeners.RepositoryListener;
import gustavo.brilhante.githupjavapop.models.Repository;
import gustavo.brilhante.githupjavapop.utils.AnimUtils;
import gustavo.brilhante.githupjavapop.utils.LayoutUtils;

/**
 * Created by Gustavo on 18/05/17.
 */

public class RepositoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    ArrayList<Repository> data;
    Context context;

    boolean fadeInScroll = false;

    RepositoryListener listener;

    int headerHeight;
    boolean isHeaderSetted = false;

    public RepositoryAdapter(ArrayList<Repository> data, Context context) {
        this.data = data;
        this.context = context;
    }

    public void setListener(RepositoryListener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==Constants.HEADER_VIEW){
            return HeaderHolder.build(LayoutUtils.createHeader(context, headerHeight));
        }
        else if(viewType==Constants.REPOSITORY_HOLDER){
            return RepositoryHolder.build(parent);
        }else{
            return EmptyHolder.build(parent);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder.getItemViewType()==Constants.REPOSITORY_HOLDER) {
            RepositoryHolder viewHolder = (RepositoryHolder) holder;
            if(!isHeaderSetted)viewHolder.bind(data.get(position));
            else viewHolder.bind(data.get(position-1));
            if(viewHolder.firstBind && fadeInScroll){
                AnimUtils.fadeInViewWithTime(viewHolder.itemView, Config.FADE_SCROLL_TIME);
                //viewHolder.firstBind = false;
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        if(isHeaderSetted && position>0 && position<data.size()+1) {
                            listener.onRepositoryClick(data.get(position-1));
                        }else if(!isHeaderSetted && position<data.size()){
                            listener.onRepositoryClick(data.get(position));
                        }
                    }
                }
            });
        }else{

        }
    }

    @Override
    public int getItemViewType(int position) {
        int pos = 0;
        if(isHeaderSetted) {
            pos = 1;
            if(position==0)return  Constants.HEADER_VIEW;
        }

        if (position < data.size()+pos) {
            return Constants.REPOSITORY_HOLDER;
        } else {
            return Constants.EMPTY_HOLDER;
        }

    }

    public void setHeaderHeight(int headerHeight) {
        this.isHeaderSetted = true;
        this.headerHeight = headerHeight;
    }

    @Override
    public int getItemCount() {
        if(isHeaderSetted) return data.size()+2;
        else return data.size() + 1;
    }

    public void setFadeInScroll(boolean fadeInScroll) {
        this.fadeInScroll = fadeInScroll;
    }
}
