package gustavo.brilhante.githupjavapop.components;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import gustavo.brilhante.githupjavapop.R;
import gustavo.brilhante.githupjavapop.constants.Config;
import gustavo.brilhante.githupjavapop.utils.ResizeAnimation;

/**
 * Created by Gustavo on 19/05/17.
 */

@EViewGroup(R.layout.custom_action_bar_layout)
public class CustomActionBar extends RelativeLayout{

    RelativeLayout currentInstance;

    @ViewById
    RelativeLayout actionBarLayout, backButtonTouchArea, rightButtonTouchArea;

    @ViewById
    TextView titleTextView;

    @ViewById
    ImageButton rightButton, backButton;

    @ViewById
    LinearLayout configLayout;

    @ViewById
    public EditText searchEditText;

    @ViewById
    public SwitchCompat fadeInSwitch, searchSwitch;

    public int collapsedLayoutHeight = 0, shrinkedLayoutHeight = 0;

    public boolean isCollapsed = true, isAnimating = false;

    public boolean isSizesSetted = false;

    boolean backClickable = false;

    boolean isToolBarEnabled = true;

    Activity activity;

    public CustomActionBar(Context context) {
        super(context);
        currentInstance = this;
    }

    public CustomActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        currentInstance = this;
    }

    public CustomActionBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        currentInstance = this;
    }

    void initViews(){


        currentInstance = this;
    }

    @Click(R.id.rightButton)
    void rightButtonClick(){
        collapseLayout();
    }

    @Click(R.id.rightButtonTouchArea)
    void rightButtonTouchArea(){
        rightButtonClick();
    }

    public void enableBackButton(Activity activity){
        backButton.setVisibility(View.VISIBLE);
        backClickable = true;
        this.activity = activity;
        backButton.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                return backAnimation(event);
            }
        });
        backButtonTouchArea.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                return backAnimation(event);
            }
        });
    }

    public boolean backAnimation(MotionEvent event){
        if(backClickable) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Animation shakeOutAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.shake_in);
                    backButton.startAnimation(shakeOutAnimation);
                    return true;
                case MotionEvent.ACTION_UP:
                    Animation shakeInAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.shake_out);
                    shakeInAnimation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            if(activity!=null) {
                                activity.finish();
                            }
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    backButton.startAnimation(shakeInAnimation);


                    return true;
            }
        }
        return false;
    }


    public void disableToolBar(){
        isToolBarEnabled = false;
        rightButton.setVisibility(View.GONE);
        rightButtonTouchArea.setVisibility(View.GONE);
        configLayout.setVisibility(View.GONE);
    }

    public void collapseLayout(){
        searchEditText.setVisibility(View.VISIBLE);
        if(isSizesSetted && !isCollapsed && !isAnimating){
            isCollapsed = true;
            ResizeAnimation animationTopLayout = new ResizeAnimation(this, 0,0, shrinkedLayoutHeight, collapsedLayoutHeight);
            animationTopLayout.setFillEnabled(true);
            animationTopLayout.setDuration(Config.RESIZE_DURATION);
            animationTopLayout.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    actionBarLayout.setVisibility(View.VISIBLE);
                    actionBarLayout.setAlpha(1f);
                    actionBarLayout.animate().alpha(0f).setDuration(200);
                    configLayout.setAlpha(0f);
                    configLayout.setVisibility(View.VISIBLE);
                    configLayout.animate().alpha(1f).setDuration(Config.RESIZE_DURATION);

                    isAnimating = true;
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    actionBarLayout.setVisibility(View.GONE);
                    isAnimating = false;

                    currentInstance.clearAnimation();

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            this.startAnimation(animationTopLayout);
        }
    }

    public void shrinkLayout(boolean start){
        final int shrinkTime;
        shrinkTime = Config.RESIZE_DURATION;
        if(isSizesSetted && isCollapsed && !isAnimating){
            isCollapsed = false;
            ResizeAnimation animationTopLayout = new ResizeAnimation(this, 0,0, collapsedLayoutHeight, shrinkedLayoutHeight);
            animationTopLayout.setFillEnabled(true);
            animationTopLayout.setDuration(shrinkTime);
            animationTopLayout.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    actionBarLayout.setVisibility(View.VISIBLE);
                    actionBarLayout.setAlpha(0f);
                    configLayout.setAlpha(1f);
                    configLayout.setVisibility(View.VISIBLE);
                    configLayout.animate().alpha(0f).setDuration(shrinkTime);

                    isAnimating = true;
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    actionBarLayout.animate().alpha(1f).setDuration(shrinkTime);
                    configLayout.setVisibility(View.GONE);
                    isAnimating = false;
                    currentInstance.clearAnimation();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            this.startAnimation(animationTopLayout);
        }
    }

    public void setActionBarClickListener(OnClickListener listener){
        actionBarLayout.setOnClickListener(listener);
        actionBarLayout.setClickable(true);
        titleTextView.setOnClickListener(listener);
        titleTextView.setClickable(true);
    }

    public void setTitleClickListener(OnClickListener listener){
        titleTextView.setOnClickListener(listener);
        titleTextView.setClickable(true);
    }

    public void setFadeInSwitchListener(CompoundButton.OnCheckedChangeListener listener){
        fadeInSwitch.setOnCheckedChangeListener(listener);
    }

    public void setSearchSwitchListener(CompoundButton.OnCheckedChangeListener listener){
        searchSwitch.setOnCheckedChangeListener(listener);
    }

    public void setTitle(String title){
        titleTextView.setText(title);
    }

    public RelativeLayout getActionBarLayout() {
        return actionBarLayout;
    }

    public LinearLayout getConfigLayout() {
        return configLayout;
    }

    public String getSearchText(){
        return searchEditText.getText().toString();
    }

}
