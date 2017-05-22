package gustavo.brilhante.githupjavapop.constants;

/**
 * Created by Gustavo on 18/05/17.
 */

public class Constants {

    public static int REPOSITORY_HOLDER = 1;
    public static int PULL_REQUEST_HOLDER = 2;
    public static int EMPTY_HOLDER = 3;
    public static int HEADER_VIEW = 4;

    public static String REPOSITORY_URL = "https://api.github.com/search/repositories?q=language:Java&sort=stars&page=PAGE_ARG";

    public static String getRepositoryUrl(int page){
        return REPOSITORY_URL.replace("PAGE_ARG", ""+page);
    }

    public static String PULL_REQUESTS_URL = "https://api.github.com/repos/<criador>/<repositório>/pulls";

    public static String getPullRequestsUrl(String ownerLogin, String repositoryName){
        return PULL_REQUESTS_URL.replace("<criador>",ownerLogin).replace("<repositório>", repositoryName);
    }
}
