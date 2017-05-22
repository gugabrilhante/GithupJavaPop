package gustavo.brilhante.githupjavapop.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Gustavo on 19/05/17.
 */

public class CacheInfo extends RealmObject{

    @PrimaryKey
    int primaryKey = 1;

    String downloadedTime;
    int currentPage;


    public String getDownloadedTime() {
        return downloadedTime;
    }

    public void setDownloadedTime(String downloadedTime) {
        this.downloadedTime = downloadedTime;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
}
