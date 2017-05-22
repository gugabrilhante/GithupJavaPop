package gustavo.brilhante.githupjavapop.utils;

import android.content.Context;
import android.view.View;
import android.widget.AbsListView;

import gustavo.brilhante.githupjavapop.dialogs.InflateCustomDialog;

/**
 * Created by Gustavo on 20/05/17.
 */

public class LayoutUtils {

    public static void inflateBasicAlertDialog(String title, String message, Context context){
        final InflateCustomDialog optionDialog = new InflateCustomDialog(context);
        if(title.length()>0)optionDialog.setTitle(title);
        if(message.length()>0)optionDialog.setMessage(message);

        optionDialog.setOption04BtnText("Ok");
        optionDialog.setOption04Click(InflateCustomDialog.ACTION_DIALOG_DISMISS);
        optionDialog.inflateDialog();
    }

    public static View createHeader(Context context, int height) {
        final View headerView = new View(context);
        headerView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, height));
        headerView.setMinimumHeight(height);
        return headerView;
    }

}
