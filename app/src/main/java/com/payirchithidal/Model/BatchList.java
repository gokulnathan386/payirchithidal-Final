package com.payirchithidal.Model;

public class BatchList {

    private String batch_name;
    private String batch_type;
    private String start_time;
    private String end_time;
    private String batchId;



    public BatchList(String batch_name, String batch_type, String start_time, String end_time, String batch_id) {
        this.batch_name=batch_name;
        this.batch_type=batch_type;
        this.start_time=start_time;
        this.end_time=end_time;
        this.batchId=batch_id;
    }

    public String getbatch_name() {
        return batch_name;
    }

    public String getbatch_type() {
        return batch_type;
    }

    public String getstart_time() {
        return start_time;
    }

    public String getend_time() {
        return end_time;
    }

    public String getBatchId() {
        return batchId;
    }

}
