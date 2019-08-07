/*
 * Copyright (C) 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.framework.cloud.apollo.refresh;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

/**
 * Apollo Config Refresh Records
 *
 * @author <a href="mailto:jiashuai.xie01@gmail.com">xiejiashuai</a>
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

