package gustavo.brilhante.githupjavapop.adapter.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Gustavo on 22/05/17.
 */

public class HeaderHolder extends RecyclerView.ViewHolder {
    public HeaderHolder(View itemView) {
        super(itemView);
    }
    public void bind(){

    }

    public static HeaderHolder build(View view){
        HeaderHolder holder = new HeaderHolder(view);
        return holder;
    }
}
