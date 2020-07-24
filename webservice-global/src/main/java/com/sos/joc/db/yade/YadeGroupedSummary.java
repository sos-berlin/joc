package com.sos.joc.db.yade;

import com.sos.joc.Globals;

public class YadeGroupedSummary {

    private Long count = null;
    private String folder = null;

    public YadeGroupedSummary(Long count, String jobChain, String job) {
        this.count = count;
        if (jobChain != null) {
            this.folder = Globals.getParent(jobChain);
        } else if (job != null) {
            this.folder = Globals.getParent(job);
        }
    }

    public int getCount() {
        return count.intValue();
    }

    public String getFolder() {
        return folder;
    }

}
