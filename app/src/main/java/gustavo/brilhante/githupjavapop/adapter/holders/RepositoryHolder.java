package gustavo.brilhante.githupjavapop.adapter.holders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import gustavo.brilhante.githupjavapop.R;
import gustavo.brilhante.githupjavapop.models.Repository;

/**
 * Created by Gustavo on 18/05/17.
 */

public class RepositoryHolder extends RecyclerView.ViewHolder{

    TextView repositoryNameTextView, descriptionTextView, forkTextView, starTextView, userNameTextView, userSecondNameTextView;
    ImageView forkImageView, starImageView, userNameImageView;

    Context context;

    public boolean firstBind = true;

    public RepositoryHolder(View itemView, Context context) {
        super(itemView);
        this.context = context;
        repositoryNameTextView = (TextView) itemView.findViewById(R.id.repositoryNameTextView);
        descriptionTextView = (TextView) itemView.findViewById(R.id.descriptionTextView);
        forkTextView = (TextView) itemView.findViewById(R.id.forkTextView);
        starTextView = (TextView) itemView.findViewById(R.id.starTextView);
        userNameTextView = (TextView) itemView.findViewById(R.id.userNameTextView);
        userSecondNameTextView = (TextView) itemView.findViewById(R.id.userSecondNameTextView);

        userNameImageView = (ImageView) itemView.findViewById(R.id.userNameImageView);
    }



    public void bind(Repository repository){
        repositoryNameTextView.setText(repository.getName());
        descriptionTextView.setText(repository.getDescription());
        forkTextView.setText(""+repository.getForks_count());
        starTextView.setText(""+repository.getScore());
        userNameTextView.setText(repository.getOwner().getLogin());
        userSecondNameTextView.setText(""+repository.getOwner().getId());

        Picasso.with(context)
                .load(repository.getOwner().getAvatar_url())
                .into(userNameImageView);
    }


    public static RepositoryHolder build(ViewGroup parent){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_adapter_repository, parent, false);
        RepositoryHolder holder = new RepositoryHolder(view, parent.getContext());
        return holder;
    }
}
