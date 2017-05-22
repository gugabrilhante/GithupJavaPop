package gustavo.brilhante.githupjavapop;

import android.support.test.espresso.Espresso;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import gustavo.brilhante.githupjavapop.activity.MainActivity;
import gustavo.brilhante.githupjavapop.activity.MainActivity_;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by Gustavo on 20/05/17.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityInterfaceTest {



    //@ClassRule
    //public static DisableAnimationsRule disableAnimationsRule = new DisableAnimationsRule();

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule(MainActivity_.class, true, true);



    @Test
    public void testIsLoading() throws Exception {
        MainActivity_ activity = (MainActivity_) mActivityRule.getActivity();
        if(activity.isLoadingSpinning){
            Thread.sleep(20000);//para funcionar sempre deve ser colocado mesmo tempo de timeout
        }
    }

    @Test
    public void testWhenContentDownloaded() throws Exception {


        Thread.sleep(2000);//abre ferramentas
        onView( allOf(withId(R.id.rightButton), withParent(withParent(withParent(withParent(withId(R.id.customActionBar)))))) ).perform(click());
        Thread.sleep(3000);
        onView(withId(R.id.recyclerView)).perform(swipeUp());
        Thread.sleep(500);
        onView(withId(R.id.recyclerView)).perform(swipeUp());
        Thread.sleep(500);
        onView(withId(R.id.recyclerView)).perform(swipeUp());

        Thread.sleep(2000);//abre ferramentas
        onView( allOf(withId(R.id.rightButton), withParent(withParent(withParent(withParent(withId(R.id.customActionBar)))))) ).perform(click());
        Thread.sleep(1000);//abre ferramentas
        onView( allOf(withId(R.id.fadeInSwitch), withParent(withParent(withParent(withParent(withId(R.id.customActionBar)))))) ).perform(click());
        Thread.sleep(500);
        onView(withId(R.id.recyclerView)).perform(swipeUp());
        onView(withId(R.id.recyclerView)).perform(swipeUp());
        Thread.sleep(500);

        Thread.sleep(2000);//abre ferramentas
        onView( allOf(withId(R.id.rightButton), withParent(withParent(withParent(withParent(withId(R.id.customActionBar)))))) ).perform(click());
        Thread.sleep(1000);//abre ferramentas
        onView( allOf(withId(R.id.fadeInSwitch), withParent(withParent(withParent(withParent(withId(R.id.customActionBar)))))) ).perform(click());

        Thread.sleep(1000);
        onView( allOf(withId(R.id.searchEditText), withParent(withParent(withParent(withParent(withId(R.id.customActionBar)))))) ).perform(typeText("android"));
        Thread.sleep(1000);
        onView( allOf(withId(R.id.searchSwitch), withParent(withParent(withParent(withParent(withId(R.id.customActionBar)))))) ).perform(click());
        Espresso.closeSoftKeyboard();

        //onView(withId(R.id.recyclerView)).perform(swipeUp());
        //Thread.sleep(500);
        //onView(withId(R.id.recyclerView)).perform(swipeDown());
        //Thread.sleep(1000);
        onView(withId(R.id.recyclerView)).perform(swipeUp());
        onView(withId(R.id.recyclerView)).perform(swipeDown());
        Thread.sleep(500);

        Thread.sleep(2000);//abre ferramentas
        onView( allOf(withId(R.id.rightButton), withParent(withParent(withParent(withParent(withId(R.id.customActionBar)))))) ).perform(click());
        Thread.sleep(1000);
        onView( allOf(withId(R.id.searchSwitch), withParent(withParent(withParent(withParent(withId(R.id.customActionBar)))))) ).perform(click());

        onView(withId(R.id.recyclerView)).perform(swipeUp());
        onView(withId(R.id.recyclerView)).perform(swipeUp());
        onView(withId(R.id.recyclerView)).perform(swipeUp());
        onView(withId(R.id.recyclerView)).perform(swipeUp());
        Thread.sleep(500);
        onView(withId(R.id.recyclerView)).perform(click());
        Thread.sleep(10000);

        //onView( allOf(withId(R.id.rightButton), withParent(withId(R.id.customActionBar)) )).check(matches(isDisplayed()));

    }
}
