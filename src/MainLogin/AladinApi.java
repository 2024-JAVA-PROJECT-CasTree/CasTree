package MainLogin;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AladinApi {
    @GET("ItemSearch.aspx")
    Call<AladinResponse> searchBooks(
            @Query("ttbkey") String ttbkey,
            @Query("Query") String query,
            @Query("QueryType") String queryType,
            @Query("MaxResults") int maxResults,
            @Query("start") int start,
            @Query("SearchTarget") String searchTarget,
            @Query("output") String output,
            @Query("Version") String version
    );
}
