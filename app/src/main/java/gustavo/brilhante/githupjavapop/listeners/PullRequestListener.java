package gustavo.brilhante.githupjavapop.listeners;

import gustavo.brilhante.githupjavapop.models.PullRequest;

/**
 * Created by Gustavo on 21/05/17.
 */

public interface PullRequestListener {
    void onPullRequestClick(PullRequest pullRequest);
}
