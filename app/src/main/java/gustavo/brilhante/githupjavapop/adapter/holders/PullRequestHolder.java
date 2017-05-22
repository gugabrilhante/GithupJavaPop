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
import gustavo.brilhante.githupjavapop.models.PullRequest;

/**
 * Created by Gustavo on 18/05/17.
 */

public class PullRequestHolder extends RecyclerView.ViewHolder {

    TextView pullRequestNameTextView, descriptionTextView, userNameTextView, userSecondNameTextView;
    ImageView userNameImageView;

    Context context;

    public PullRequestHolder(View itemView, Context context) {
        super(itemView);
        this.context = context;
        pullRequestNameTextView = (TextView) itemView.findViewById(R.id.pullRequestNameTextView);
        descriptionTextView = (TextView) itemView.findViewById(R.id.descriptionTextView);
        userNameTextView = (TextView) itemView.findViewById(R.id.userNameTextView);
        userSecondNameTextView = (TextView) itemView.findViewById(R.id.userSecondNameTextView);

        userNameImageView = (ImageView) itemView.findViewById(R.id.userNameImageView);
    }

    public void bind(PullRequest pullRequest){
        pullRequestNameTextView.setText(pullRequest.getTitle());
        descriptionTextView.setText(pullRequest.getBody());
        userNameTextView.setText(pullRequest.getUser().getLogin());
        userSecondNameTextView.setText(""+pullRequest.getUser().getId());

        Picasso.with(context).load(pullRequest.getUser().getAvatar_url()).into(userNameImageView);
    }

    public static PullRequestHolder build(ViewGroup parent){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_adapter_pull_request, parent, false);
        PullRequestHolder holder = new PullRequestHolder(view, parent.getContext());
        return holder;
    }

}
