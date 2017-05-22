package gustavo.brilhante.githupjavapop.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import gustavo.brilhante.githupjavapop.R;
import gustavo.brilhante.githupjavapop.adapter.PullRequestAdapter;
import gustavo.brilhante.githupjavapop.components.CustomActionBar;
import gustavo.brilhante.githupjavapop.constants.Constants;
import gustavo.brilhante.githupjavapop.listeners.PullRequestListener;
import gustavo.brilhante.githupjavapop.models.PullRequest;
import gustavo.brilhante.githupjavapop.requests.MakeRequest;
import gustavo.brilhante.githupjavapop.utils.AnimUtils;
import okhttp3.Response;

@EActivity(R.layout.activity_repository_selected)
public class RepositorySelectedActivity extends AppCompatActivity implements ObservableScrollViewCallbacks, PullRequestListener {

    @ViewById
    LinearLayout rootView;

    @ViewById
    EditText searchEditText;

    @ViewById
    ObservableRecyclerView recyclerView;

    @ViewById
    CustomActionBar customActionBar;

    @ViewById
    LinearLayout listLayout, errorMessageLayout, loadingLayout;

    @ViewById
    CircularProgressView progressView;

    PullRequestAdapter adapter;

    RecyclerView.LayoutManager mLayoutManager;

    ArrayList<PullRequest> pullRequests;

    @Extra
    String ownerLogin, repositoryName;


    int currentPage;

    boolean isAdapterAttached = false;

    boolean isLoadingSpinning = false;

    Gson gson;

    @AfterViews
    public void afterViews(){

        initCustomActionBar();

        gson = new Gson();

        if(getActionBar()!=null){
            getActionBar().setTitle(repositoryName);
        }

        if(!isAdapterAttached) {
            setLoading(true, false);
            pullRequests = new ArrayList<>();
            downloadList();
        }

        /*rootView.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
                return false;
            }
        });*/

    }

    void initCustomActionBar(){
        customActionBar.setTitle(repositoryName);
        customActionBar.enableBackButton(this);
        customActionBar.disableToolBar();
        customActionBar.setTitleClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(recyclerView!=null && pullRequests.size()>0){
                    recyclerView.smoothScrollToPosition(0);
                }
            }
        });
    }

    @UiThread
    void setRecyclerView(){
        adapter = new PullRequestAdapter(pullRequests, this);
        adapter.setListener(this);
        recyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setScrollViewCallbacks(this);
        isAdapterAttached = true;
    }

    @Background
    void downloadList(){
        String url = Constants.getPullRequestsUrl(ownerLogin, repositoryName);

        try{
            Response response = MakeRequest.get(url, null);
            if(response.isSuccessful()){
                String resp = response.body().string();
                if(pullRequests==null)pullRequests = new ArrayList<PullRequest>();
                if(isAdapterAttached) {
                    Type listType = new TypeToken<ArrayList<PullRequest>>(){}.getType();
                    ArrayList<PullRequest> list = gson.fromJson(resp, listType);
                    pullRequests.addAll(list);
                }else{
                    Type listType = new TypeToken<ArrayList<PullRequest>>(){}.getType();
                    pullRequests = gson.fromJson(resp, listType);
                    setRecyclerView();
                }
                setLoading(false, false);
            }

        }
        catch (JsonParseException e) {

            setLoading(false, true);
        } catch (IOException e) {
            e.printStackTrace();
            setLoading(false, true);

        }
    }

    @Click(R.id.errorMessageTextView)
    void onErrorMessageClick(){
        setLoading(true, false);
        pullRequests = new ArrayList<>();
        downloadList();
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
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }


    @Override
    public void onPullRequestClick(PullRequest pullRequest) {
        if(pullRequest.getUrl()!=null) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pullRequest.getUrl()));
            startActivity(browserIntent);
        }
    }
}
