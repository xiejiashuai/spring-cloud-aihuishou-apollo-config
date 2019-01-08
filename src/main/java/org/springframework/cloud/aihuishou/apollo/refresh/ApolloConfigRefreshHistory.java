package org.springframework.cloud.aihuishou.apollo.refresh;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

/**
 * Apollo Config Refresh Records
 *
 * @author jiashuai.xie
 * @since 2019/1/8 15:08 1.0.0.RELEASE
 */
public class ApolloConfigRefreshHistory {

    private static final int MAX_SIZE = 20;

    private LinkedList<Record> records = new LinkedList<>();

    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public void add(String namespace, String md5) {
        records.addFirst(new Record(dateFormat.format(new Date()), namespace, md5));
        if (records.size() > MAX_SIZE) {
            records.removeLast();
        }
    }

    public LinkedList<Record> getRecords() {
        return records;
    }

}
class Record {

    private final String timestamp;

    private final String namespace;

    /**
     * apollo config md5
     */
    private final String md5;

    public Record(String timestamp, String namespace, String md5) {
        this.timestamp = timestamp;
        this.namespace = namespace;
        this.md5 = md5;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getNamespace() {
        return namespace;
    }

    public String getMd5() {
        return md5;
    }
}

