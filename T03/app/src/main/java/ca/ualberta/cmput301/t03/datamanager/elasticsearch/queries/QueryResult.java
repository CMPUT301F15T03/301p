package ca.ualberta.cmput301.t03.datamanager.elasticsearch.queries;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by rishi on 15-11-28.
 */
public class QueryResult<T> {
    private Hits<T> hits;

    private Aggregations aggregations;

    public static class Hits<T> {

        private int total;

        @SerializedName("max_score")
        private double maxScore;

        private ArrayList<Hit<T>> hits;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public double getMaxScore() {
            return maxScore;
        }

        public void setMaxScore(double maxScore) {
            this.maxScore = maxScore;
        }

        public ArrayList<Hit<T>> getHits() {
            return hits;
        }

        public void setHits(ArrayList<Hit<T>> hits) {
            this.hits = hits;
        }

    }

    public static class Hit<T> {

        @SerializedName("_index")
        private String index;


        @SerializedName("_type")
        private String type;

        @SerializedName("_id")
        private String id;

        @SerializedName("_score")
        private Double score;

        @SerializedName("_source")
        private T source;

        public String getIndex() {
            return index;
        }

        public String getType() {
            return type;
        }

        public String getId() {
            return id;
        }

        public Double getScore() {
            return score;
        }

        public T getSource() {
            return source;
        }

        public void setIndex(String index) {
            this.index = index;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setScore(Double score) {
            this.score = score;
        }

        public void setSource(T source) {
            this.source = source;
        }

    }

    public static class Aggregations {

        @SerializedName(Query.AGGREGATION_KEY)
        private AggregationGroup group;
    }

    public static class AggregationGroup {
        private ArrayList<Bucket> buckets;

        public ArrayList<Bucket> getBuckets() {
            return buckets;
        }

        public void setBuckets(ArrayList<Bucket> buckets) {
            this.buckets = buckets;
        }

    }

    public static class Bucket {
        private String key;

        @SerializedName("doc_count")
        private int count;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}
