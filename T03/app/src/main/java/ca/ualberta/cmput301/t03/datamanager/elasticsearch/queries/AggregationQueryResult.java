package ca.ualberta.cmput301.t03.datamanager.elasticsearch.queries;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by rishi on 15-11-28.
 */
public class AggregationQueryResult {

    private Aggregations aggregations;

    public Aggregations getAggregations() {
        return aggregations;
    }

    public void setAggregations(Aggregations aggregations) {
        this.aggregations = aggregations;
    }

    public static class Aggregations {

        @SerializedName(Query.AGGREGATION_KEY)
        private AggregationGroup group;

        public AggregationGroup getGroup() {
            return group;
        }

        public void setGroup(AggregationGroup group) {
            this.group = group;
        }

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
