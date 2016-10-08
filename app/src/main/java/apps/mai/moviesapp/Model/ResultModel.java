package apps.mai.moviesapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mai_ on 08-Oct-16.
 */

public class ResultModel {

        @SerializedName("page")
        @Expose
        private Integer page;
        @SerializedName("results")
        @Expose
        private List<Movie> movies = new ArrayList<Movie>();
        @SerializedName("total_results")
        @Expose
        private Integer totalResults;
        @SerializedName("total_pages")
        @Expose
        private Integer totalPages;

        /**
         * @return The page
         */
        public Integer getPage() {
            return page;
        }

        /**
         * @param page The page
         */
        public void setPage(Integer page) {
            this.page = page;
        }

        /**
         * @return The results
         */
        public List<Movie> getResults() {
            return movies;
        }

        /**
         * @param movies The results
         */
        public void setResults(List<Movie> movies) {
            this.movies = movies;
        }

        /**
         * @return The totalResults
         */
        public Integer getTotalResults() {
            return totalResults;
        }

        /**
         * @param totalResults The total_results
         */
        public void setTotalResults(Integer totalResults) {
            this.totalResults = totalResults;
        }

        /**
         * @return The totalPages
         */
        public Integer getTotalPages() {
            return totalPages;
        }

        /**
         * @param totalPages The total_pages
         */
        public void setTotalPages(Integer totalPages) {
            this.totalPages = totalPages;
        }


}