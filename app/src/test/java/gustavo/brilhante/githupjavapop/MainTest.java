package gustavo.brilhante.githupjavapop;

import org.junit.Test;

import java.util.Random;

import gustavo.brilhante.githupjavapop.utils.AnimUtils;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Gustavo on 20/05/17.
 */

public class MainTest {

    //teste que garante o calculo da animação do swipeRefreshUp
    @Test
    public void swipeBottomRefresh() throws Exception {
        int progressBottomLayoutHeight = 1;
        int recyclerViewMaxScroll = 10;
        int recyclerViewOffSet = 3;
        int recyclerViewExtendScroll = 4;

        int resultMustBe = (progressBottomLayoutHeight - (recyclerViewMaxScroll - (recyclerViewOffSet + recyclerViewExtendScroll)));

        int viewTranslation = AnimUtils.calculateBottomSwipeRefreshAnim(progressBottomLayoutHeight, recyclerViewMaxScroll, recyclerViewOffSet, recyclerViewExtendScroll);

        assertEquals(resultMustBe, viewTranslation);


        progressBottomLayoutHeight = 100;
        recyclerViewMaxScroll = 10000;
        recyclerViewOffSet = 8500;
        recyclerViewExtendScroll = 300;

        resultMustBe = (progressBottomLayoutHeight - (recyclerViewMaxScroll - (recyclerViewOffSet + recyclerViewExtendScroll)));

        viewTranslation = AnimUtils.calculateBottomSwipeRefreshAnim(progressBottomLayoutHeight, recyclerViewMaxScroll, recyclerViewOffSet, recyclerViewExtendScroll);

        assertEquals(resultMustBe, viewTranslation);

        Random random = new Random();

        for(int i=0; i<5; i++) {//simula scroll aleatório


            boolean scrollDown = random.nextBoolean();
            int scrolledValue;
            if(scrollDown) scrolledValue = random.nextInt(recyclerViewMaxScroll - (recyclerViewOffSet + recyclerViewExtendScroll));
            else scrolledValue = -random.nextInt(recyclerViewOffSet);

            recyclerViewOffSet += scrolledValue;

            resultMustBe = (progressBottomLayoutHeight - (recyclerViewMaxScroll - (recyclerViewOffSet + recyclerViewExtendScroll)));

            viewTranslation = AnimUtils.calculateBottomSwipeRefreshAnim(progressBottomLayoutHeight, recyclerViewMaxScroll, recyclerViewOffSet, recyclerViewExtendScroll);

            assertEquals(resultMustBe, viewTranslation);
        }

    }
}
