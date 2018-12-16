/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.pulsar.sql.presto;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.airlift.configuration.Config;
import org.apache.pulsar.client.admin.PulsarAdmin;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.shade.org.apache.bookkeeper.stats.NullStatsProvider;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PulsarConnectorConfig implements AutoCloseable {

    private String brokerServiceUrl = "http://localhost:8080";
    private String zookeeperUri = "localhost:2181";
    private int entryReadBatchSize = 100;
    private int targetNumSplits = 2;
    private int maxSplitMessageQueueSize = 10000;
    private int maxSplitEntryQueueSize = 1000;
    private String statsProvider = NullStatsProvider.class.getName();
    private Map<String, String> statsProviderConfigs = new HashMap<>();
    private PulsarAdmin pulsarAdmin;

    @NotNull
    public String getBrokerServiceUrl() {
        return brokerServiceUrl;
    }

    @Config("pulsar.broker-service-url")
    public PulsarConnectorConfig setBrokerServiceUrl(String brokerServiceUrl) {
        this.brokerServiceUrl = brokerServiceUrl;
        return this;
    }

    @NotNull
    public String getZookeeperUri() {
        return this.zookeeperUri;
    }

    @Config("pulsar.zookeeper-uri")
    public PulsarConnectorConfig setZookeeperUri(String zookeeperUri) {
        this.zookeeperUri = zookeeperUri;
        return this;
    }

    @NotNull
    public int getMaxEntryReadBatchSize() {
        return this.entryReadBatchSize;
    }

    @Config("pulsar.max-entry-read-batch-size")
    public PulsarConnectorConfig setMaxEntryReadBatchSize(int batchSize) {
        this.entryReadBatchSize = batchSize;
        return this;
    }

    @NotNull
    public int getTargetNumSplits() {
        return this.targetNumSplits;
    }

    @Config("pulsar.target-num-splits")
    public PulsarConnectorConfig setTargetNumSplits(int targetNumSplits) {
        this.targetNumSplits = targetNumSplits;
        return this;
    }

    @NotNull
    public int getMaxSplitMessageQueueSize() {
        return this.maxSplitMessageQueueSize;
    }

    @Config("pulsar.max-split-message-queue-size")
    public PulsarConnectorConfig setMaxSplitMessageQueueSize(int maxSplitMessageQueueSize) {
        this.maxSplitMessageQueueSize = maxSplitMessageQueueSize;
        return this;
    }

    @NotNull
    public int getMaxSplitEntryQueueSize() {
        return this.maxSplitEntryQueueSize;
    }

    @Config("pulsar.max-split-entry-queue-size")
    public PulsarConnectorConfig setMaxSplitEntryQueueSize(int maxSplitEntryQueueSize) {
        this.maxSplitEntryQueueSize = maxSplitEntryQueueSize;
        return this;
    }

    @NotNull
    public String getStatsProvider() {
        return statsProvider;
    }

    @Config("pulsar.stats-provider")
    public PulsarConnectorConfig setStatsProvider(String statsProvider) {
        this.statsProvider = statsProvider;
        return this;
    }

    @NotNull
    public Map<String, String> getStatsProviderConfigs() {
        return statsProviderConfigs;
    }

    @Config("pulsar.stats-provider-configs")
    public PulsarConnectorConfig setStatsProviderConfigs(String statsProviderConfigs) throws IOException {
        this.statsProviderConfigs = new ObjectMapper().readValue(statsProviderConfigs, Map.class);
        return this;
    }

    @NotNull
    public PulsarAdmin getPulsarAdmin() throws PulsarClientException {
        if (this.pulsarAdmin == null) {
            this.pulsarAdmin = PulsarAdmin.builder().serviceHttpUrl(getBrokerServiceUrl()).build();
        }
        return this.pulsarAdmin;
    }

    @Override
    public void close() throws Exception {
        this.pulsarAdmin.close();
    }

    @Override
    public String toString() {
        return "PulsarConnectorConfig{" +
                "brokerServiceUrl='" + brokerServiceUrl + '\'' +
                '}';
    }
}
