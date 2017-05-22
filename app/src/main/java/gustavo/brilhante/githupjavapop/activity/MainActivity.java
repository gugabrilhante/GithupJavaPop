package gustavo.brilhante.githupjavapop.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import gustavo.brilhante.githupjavapop.R;
import gustavo.brilhante.githupjavapop.adapter.RepositoryAdapter;
import gustavo.brilhante.githupjavapop.components.CustomActionBar;
import gustavo.brilhante.githupjavapop.constants.Config;
import gustavo.brilhante.githupjavapop.constants.Constants;
import gustavo.brilhante.githupjavapop.listeners.RepositoryListener;
import gustavo.brilhante.githupjavapop.models.CacheInfo;
import gustavo.brilhante.githupjavapop.models.Repository;
import gustavo.brilhante.githupjavapop.models.RepositoryResponse;
import gustavo.brilhante.githupjavapop.requests.MakeRequest;
import gustavo.brilhante.githupjavapop.utils.AnimUtils;
import gustavo.brilhante.githupjavapop.utils.DateUtils;
import gustavo.brilhante.githupjavapop.utils.LayoutUtils;
import io.realm.Realm;
import okhttp3.Response;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements ObservableScrollViewCallbacks, RepositoryListener {

    @ViewById
    LinearLayout rootView;

    @ViewById
    EditText searchEditText;

    @ViewById
    ObservableRecyclerView recyclerView;

    @ViewById
    LinearLayout listLayout, errorMessageLayout, loadingLayout;

    @ViewById
    RelativeLayout progressViewBottomLayout;

    @ViewById
    CircularProgressView progressView, progressViewBottom;

    @ViewById
    View circleProgresssFake;

    @ViewById
    CustomActionBar customActionBar;

    RepositoryAdapter adapter;

    RecyclerView.LayoutManager mLayoutManager;

    ArrayList<Repository> repositories;
    ArrayList<Repository> repositoriesDisplay;

    int currentPage;

    boolean isAdapterAttached = false;

    public boolean isLoadingSpinning = false;

    boolean isLoadinNextPage = false;

    Gson gson;

    int recyclerViewMaxScroll = 0;
    int recyclerViewExtendScroll = 0;
    int recyclerViewOffSet = 0;
    int progressBottomLayoutHeight = 0;

    Realm realm;

    @AfterViews
    public void afterViews(){
        getLayoutSizes();
        hideSoftKeyboard();

        if(customActionBar!=null){
            customActionBar.setTitle(getResources().getString(R.string.app_name));
        }

        realm = Realm.getDefaultInstance();

        gson = new Gson();

        progressViewBottom.stopAnimation();

        if(!isAdapterAttached) {

            List<Repository> realmList = realm.where(Repository.class).findAll();
            CacheInfo info = realm.where(CacheInfo.class).findFirst();
            boolean timeExpired;
            if(info!=null) {
                //cache de dados inspira no tempo definido pela variável CACHE_EXPIRE_TIME
                timeExpired = DateUtils.checkDifferenceTimeInMinutes(info.getDownloadedTime(), Config.CACHE_EXPIRE_TIME);
            }else{
                timeExpired = true;
            }
            if(realmList!=null && info!=null && realmList.size()>0 && !timeExpired ){
                currentPage = info.getCurrentPage();
                repositories = new ArrayList<Repository>(realmList);
                repositoriesDisplay = repositories;
                setRecyclerView();
            }else {
                setLoading(true, false);
                repositories = new ArrayList<>();
                currentPage = 1;
                downloadPage();
            }
        }

    }

    void setActionBarListeners(){

        customActionBar.searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               if(customActionBar.searchSwitch.isChecked())customActionBar.searchSwitch.setChecked(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        customActionBar.setActionBarClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.smoothScrollToPosition(0);
            }
        });

        customActionBar.setFadeInSwitchListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                adapter.setFadeInScroll(isChecked);
            }
        });
        customActionBar.setSearchSwitchListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(customActionBar.getSearchText().length()>0) {
                    if (isChecked) {
                        filterList(customActionBar.getSearchText());
                    } else {
                        customActionBar.setTitle(getResources().getString(R.string.ferramentas));
                        repositoriesDisplay = repositories;
                        setRecyclerView();
                    }
                }
            }
        });
    }


    void filterList(String text){
        repositoriesDisplay = new ArrayList<Repository>();
        for(int i=0; i<repositories.size(); i++){
            if(repositories.get(i).getName().toLowerCase().contains(text.toString().toLowerCase())){
                repositoriesDisplay.add(repositories.get(i));
            }
        }
        customActionBar.setTitle(getResources().getString(R.string.filtro)+": "+text);
        //adapter.notifyDataSetChanged();
        setRecyclerView();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getActionBar()!=null){
            getActionBar().hide();
        }
    }

    @UiThread
    void setRecyclerView(){
        adapter = new RepositoryAdapter(repositoriesDisplay, this);
        if(customActionBar.isSizesSetted)adapter.setHeaderHeight(customActionBar.shrinkedLayoutHeight);
        adapter.setListener(this);
        adapter.setFadeInScroll(customActionBar.fadeInSwitch.isChecked());
        recyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setScrollViewCallbacks(this);
        isAdapterAttached = true;

        setRecyclerViewMaxScroll();
        setActionBarListeners();
    }

    @UiThread
    void pushToRealm(Repository repository, CacheInfo info){
        realm.beginTransaction();
        realm.copyToRealm(repository);
        realm.copyToRealmOrUpdate(info);
        realm.commitTransaction();
    }


    @UiThread
    void cleanRealm(){
        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();
    }

    @Background(serial = "loadPage")
    void downloadPage(){
        String url = Constants.getRepositoryUrl(currentPage);

        try{
            Response response = MakeRequest.get(url, null);
            if(response.isSuccessful()){
                String resp = response.body().string();
                if(repositories==null)repositories = new ArrayList<Repository>();
                if(isAdapterAttached) {
                    CacheInfo info = new CacheInfo();
                    info.setDownloadedTime(DateUtils.getCurrentDateTime());
                    info.setCurrentPage(currentPage);
                    RepositoryResponse repositoryResponse = gson.fromJson(resp, RepositoryResponse.class);
                    ArrayList<Repository> list = new ArrayList<Repository>(repositoryResponse.getItems());
                    repositories.addAll(list);
                    for(int i=0; i<list.size(); i++)pushToRealm(list.get(i), info);
                    repositoriesDisplay = repositories;
                    finishProgressViewBottomLoading();
                }else{
                    cleanRealm();
                    CacheInfo info = new CacheInfo();
                    info.setDownloadedTime(DateUtils.getCurrentDateTime());
                    info.setCurrentPage(currentPage);
                    RepositoryResponse repositoryResponse = gson.fromJson(resp, RepositoryResponse.class);
                    repositories = new ArrayList<Repository>(repositoryResponse.getItems());
                    for(int i=0; i<repositories.size(); i++)pushToRealm(repositories.get(i), info);
                    repositoriesDisplay = repositories;
                    setRecyclerView();
                    setLoading(false, false);
                }
            }

        }
        catch (JsonParseException e) {
            if(isAdapterAttached){
                finishProgressViewBottomLoading();
            }else {
                setLoading(false, true);
            }
        } catch (IOException e) {
            e.printStackTrace();
            if(isAdapterAttached){
                finishProgressViewBottomLoading();
            }else {
                setLoading(false, true);
            }
        }
    }


    @Click(R.id.errorMessageTextView)
    void onErrorMessageClick(){
        setLoading(true, false);
        repositories = new ArrayList<>();
        currentPage = 1;
        downloadPage();
    }

    @UiThread
    void setLoading(boolean isLoading, boolean error) {
        if (isLoading && loadingLayout.getVisibility() == View.GONE) {
            isLoadingSpinning = isLoading;
            loadingLayout.setVisibility(View.VISIBLE);
            progressView.startAnimation();
            listLayout.setVisibility(View.GONE);
            errorMessageLayout.setVisibility(View.GONE);
            AnimUtils.fadeInView(loadingLayout);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        } else if (!isLoading && loadingLayout.getVisibility() == View.VISIBLE && !error) {
            isLoadingSpinning = isLoading;
            loadingLayout.setVisibility(View.GONE);
            progressView.stopAnimation();
            listLayout.setVisibility(View.VISIBLE);
            AnimUtils.fadeInView(listLayout);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else if (!isLoading && loadingLayout.getVisibility() == View.VISIBLE && error) {
            isLoadingSpinning = isLoading;
            loadingLayout.setVisibility(View.GONE);
            progressView.stopAnimation();
            errorMessageLayout.setVisibility(View.VISIBLE);
            AnimUtils.fadeInView(errorMessageLayout);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }



    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        if(customActionBar.isCollapsed && customActionBar.isSizesSetted && dragging){
            customActionBar.shrinkLayout(false);
        }

        setRecyclerViewMaxScroll();
        setRecyclerViewExtendScroll();
        setRecyclerViewOffSet();
        int scrollAt = recyclerViewOffSet+recyclerViewExtendScroll;
        int sum = (recyclerViewMaxScroll - (scrollAt) );

        if(!isLoadinNextPage) {
            int viewTranslation = AnimUtils.calculateBottomSwipeRefreshAnim(progressBottomLayoutHeight, recyclerViewMaxScroll, recyclerViewOffSet, recyclerViewExtendScroll);

            viewTranslation -= progressBottomLayoutHeight;

            float percentage = 0;
            float percentageFake = 0;

            //progressViewBottom.animate().alpha(percentage).setDuration(0);

            if (viewTranslation >= 0) {
                if (viewTranslation < progressBottomLayoutHeight) {
                    if (progressViewBottomLayout != null)
                        progressViewBottomLayout.animate().translationY(-viewTranslation).setDuration(0);
                    percentage = ((float) progressBottomLayoutHeight + (float) viewTranslation) / (float) progressBottomLayoutHeight;
                } else {
                    if (progressViewBottomLayout != null)
                        progressViewBottomLayout.animate().translationY(0).setDuration(0);
                    percentage = 1f;

                }

                if(!customActionBar.searchSwitch.isChecked()) {
                    loadNextPage();
                }else{
                    LayoutUtils.inflateBasicAlertDialog(getResources().getString(R.string.filtro_alerta_titule)
                            , getResources().getString(R.string.filtro_alerta_messagem)
                            ,this);
                }

            } else {
                if (viewTranslation > -progressBottomLayoutHeight) {
                    progressViewBottomLayout.animate().translationY(-viewTranslation).setDuration(0);
                    percentage = ((float) progressBottomLayoutHeight + (float) viewTranslation) / (float) progressBottomLayoutHeight;
                    if (percentage <= 0.4) percentageFake = 0f;
                    else percentageFake = percentage - 0.4f;
                    //progressViewBottom.stopAnimation();
                } else {
                    progressViewBottomLayout.animate().translationY(progressBottomLayoutHeight).setDuration(0);
                    percentage = 0f;
                    percentageFake = 0f;
                }
                //circleProgresssFake.setVisibility(View.VISIBLE);
                //progressViewBottom.setVisibility(View.INVISIBLE);
                circleProgresssFake.animate().alpha(percentageFake).setDuration(0);
                progressViewBottom.animate().alpha(0f).setDuration(0);
            }
        }
        //circleProgresssFake.animate().alpha(percentageFake).setDuration(0);
        //progressViewBottom.animate().alpha(percentage).setDuration(0);

    }

    @Background(serial = "loadPage")
    public void loadNextPage(){
        setProgressViewBottomLoading();
        currentPage++;
        downloadPage();
    }

    @UiThread
    public void setProgressViewBottomLoading(){
        isLoadinNextPage = true;
        //circleProgresssFake.setVisibility(View.INVISIBLE);
        //progressViewBottom.setVisibility(View.VISIBLE);
        progressViewBottom.setAlpha(circleProgresssFake.getAlpha());
        progressViewBottom.animate().alpha(1f).setDuration(200);
        circleProgresssFake.animate().alpha(0f).setDuration(200);
    }

    @UiThread
    public void finishProgressViewBottomLoading(){
        adapter.notifyDataSetChanged();
        progressViewBottom.animate().alpha(0f).setDuration(150);
        circleProgresssFake.animate().alpha(1f).setDuration(100);
        isLoadinNextPage = false;
        progressViewBottomLayout.animate().translationY(progressBottomLayoutHeight).setDuration(Config.MOVE_SWIPE_LOADER_TIME);
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }

    @Override
    public void onRepositoryClick(Repository repository) {
        RepositorySelectedActivity_.intent(this)
                .ownerLogin(repository.getOwner().getLogin())
                .repositoryName(repository.getName())
                .start();
    }

    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void setRecyclerViewMaxScroll(){
        recyclerViewMaxScroll = recyclerView.computeVerticalScrollRange();
    }

    public void setRecyclerViewExtendScroll(){
        recyclerViewExtendScroll = recyclerView.computeVerticalScrollExtent();
    }

    public void setRecyclerViewOffSet(){
        recyclerViewOffSet = recyclerView.computeVerticalScrollOffset();
    }

    private void getLayoutSizes() {

        customActionBar.getConfigLayout().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                if(customActionBar.collapsedLayoutHeight==0){
                    customActionBar.collapsedLayoutHeight = customActionBar.getConfigLayout().getHeight();
//                    if(customActionBar.shrinkedLayoutHeight!=0){
//                        customActionBar.isSizesSetted = true;
//                        customActionBar.shrinkLayout(true);
//                    }
                }
            }
        });

        customActionBar.getActionBarLayout().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                if(customActionBar.shrinkedLayoutHeight==0){
                    customActionBar.shrinkedLayoutHeight = customActionBar.getActionBarLayout().getHeight();
                    if(customActionBar.collapsedLayoutHeight==0){
                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        customActionBar.isSizesSetted = true;
                                        customActionBar.shrinkLayout(true);
                                        if(adapter!=null){
                                            adapter.setHeaderHeight(customActionBar.shrinkedLayoutHeight);
                                            //adapter.notifyDataSetChanged();
                                            setRecyclerView();
                                        }
                                    }
                                }, 300
                        );
                    }else{
                        customActionBar.isSizesSetted = true;
                        customActionBar.shrinkLayout(true);
                        if(adapter!=null){
                            adapter.setHeaderHeight(customActionBar.shrinkedLayoutHeight);
                            //adapter.notifyDataSetChanged();
                            setRecyclerView();
                        }
                    }

                }
            }
        });

        progressViewBottomLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                if(progressBottomLayoutHeight==0) {
                    progressBottomLayoutHeight = progressViewBottomLayout.getHeight();
                    progressViewBottomLayout.animate().translationY(progressBottomLayoutHeight).setDuration(0);
                }
            }
        });

    }

/*    private void shrinkActionBar(){
        if(customActionBar.collapsedLayoutHeight!=0 && customActionBar.shrinkedLayoutHeight!=0){

        }
    }*/


}
