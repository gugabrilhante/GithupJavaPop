package gustavo.brilhante.githupjavapop;

import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import gustavo.brilhante.githupjavapop.utils.DateUtils;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Gustavo on 20/05/17.
 */

public class DataUtilsTest {

    @Test
    public void cacheTest() throws Exception {

        Date savedTime1 = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Thread.sleep(200);
        boolean timeNotExpired = DateUtils.checkDifferenceTimeInMinutes(dateFormat.format(savedTime1), 1);
        assertFalse(timeNotExpired);

        Date savedTime2 = new Date();
        Thread.sleep(60000);
        boolean timeExpired = DateUtils.checkDifferenceTimeInMinutes(dateFormat.format(savedTime2), 1);
        assertTrue(timeExpired);


        Thread.sleep(200);
        timeExpired = DateUtils.checkDifferenceTimeInMinutes(dateFormat.format(savedTime2), 1);
        assertTrue(timeExpired);

    }
}
