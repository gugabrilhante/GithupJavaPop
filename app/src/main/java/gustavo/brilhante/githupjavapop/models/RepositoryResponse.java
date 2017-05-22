package gustavo.brilhante.githupjavapop.models;

import java.util.List;

/**
 * Created by Gustavo on 18/05/17.
 */

public class RepositoryResponse {
    int total_count;
    String incomplete_results;
    List<Repository> items;

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public String getIncomplete_results() {
        return incomplete_results;
    }

    public void setIncomplete_results(String incomplete_results) {
        this.incomplete_results = incomplete_results;
    }

    public List<Repository> getItems() {
        return items;
    }

    public void setItems(List<Repository> items) {
        this.items = items;
    }
}
