package gustavo.brilhante.githupjavapop.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import gustavo.brilhante.githupjavapop.R;


/**
 * Created by USUARIO on 03/08/2016.
 */
public class InflateCustomDialog extends AlertDialog.Builder {

    public final static int ACTION_DIALOG_DISMISS = 1;
    public final static int ACTION_DIALOG_DELEGATE = 2;

            TextView titleTextView, messageTextView;
            Button option01, option02, option03, option04;
            Context context;
            Activity activity;
            AlertDialog dialog;


    public InflateCustomDialog(Context context) {
            super(context);

            this.context = context;
            this.activity = (Activity)context;
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.layout_dialog_custom_options, null);

            this.setView(layout);
            /*if(MainActivity.isSmallScreen) {
            Utils.setTodosOsTextos((ViewGroup) layout, MainActivity.textViewSize, MainActivity.editTextHeight);
            }*/

            titleTextView = (TextView) layout.findViewById(R.id.dialogcustom_title_TextView);
            messageTextView = (TextView) layout.findViewById(R.id.dialogcustom_message_TextView);
            option01 = (Button) layout.findViewById(R.id.dialogcustom_dialog_option01);
            option01.setVisibility(View.GONE);
            option02 = (Button) layout.findViewById(R.id.dialogcustom_dialog_option02);
            option02.setVisibility(View.GONE);
            option03 = (Button) layout.findViewById(R.id.dialogcustom_dialog_option03);
            option03.setVisibility(View.GONE);
            option04 = (Button) layout.findViewById(R.id.dialogcustom_dialog_option04);
            option04.setVisibility(View.GONE);

            }


    public void setTitle(String text){
            titleTextView.setVisibility(View.VISIBLE);
            titleTextView.setText(text);
            }
    public void setMessage(String text){
            messageTextView.setVisibility(View.VISIBLE);
            messageTextView.setText(text);
            }

    public void setOption01BtnText(String text){
            option01.setVisibility(View.VISIBLE);
            option01.setText(text);
            }

    public void setOption02BtnText(String text){
            option02.setVisibility(View.VISIBLE);
            option02.setText(text);
            }

    public void setOption03BtnText(String text){
            option03.setVisibility(View.VISIBLE);
            option03.setText(text);
            }

    public void setOption04BtnText(String text){
        option04.setVisibility(View.VISIBLE);
        option04.setText(text);
    }


    public void setOption01Click(View.OnClickListener listener){
            option01.setOnClickListener(listener);
            }

    public void setOption01Click(int action){
            setActionButtonDialog(option01, action);
            }

    public void setOption02Click(View.OnClickListener listener){
            option02.setOnClickListener(listener);
            }

    public void setOption02Click(int action){
            setActionButtonDialog(option02, action);
            }

    public void setOption03Click(View.OnClickListener listener){
            option03.setOnClickListener(listener);
            }

    public void setOption03Click(int action){
            setActionButtonDialog(option03, action);
            }

    public void setOption04Click(View.OnClickListener listener){
        option04.setOnClickListener(listener);
    }

    public void setOption04Click(int action){
        setActionButtonDialog(option04, action);
    }

    public void setActionButtonDialog(Button btn, int action){

        if(action==ACTION_DIALOG_DISMISS){
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }else if(action==ACTION_DIALOG_DELEGATE){
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }

    }

    public void dismiss(){
            dialog.dismiss();
            }

    public void inflateDialog(){
            dialog = this.create();
            dialog.show();
            }
}
