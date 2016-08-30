package com.sos.joc.job.post;

import java.util.HashMap;
import java.util.Map;

public class JobConfigurationBody {

    private String jobschedulerId;
    private String job;
    private JobConfigurationBody.Mime mime = JobConfigurationBody.Mime.fromValue("xml");

    public String getJobschedulerId() {
        return jobschedulerId;
    }

    public void setJobschedulerId(String jobschedulerId) {
        this.jobschedulerId = jobschedulerId;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public JobConfigurationBody.Mime getMime() {
        return mime;
    }

    public void setMime(JobConfigurationBody.Mime mime) {
        this.mime = mime;
    }

    public enum Mime {

        html("html"), xml("xml");
        private final String value;
        private final static Map<String, JobConfigurationBody.Mime> CONSTANTS = new HashMap<String, JobConfigurationBody.Mime>();

        static {
            for (JobConfigurationBody.Mime c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private Mime(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public static JobConfigurationBody.Mime fromValue(String value) {
            JobConfigurationBody.Mime constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
