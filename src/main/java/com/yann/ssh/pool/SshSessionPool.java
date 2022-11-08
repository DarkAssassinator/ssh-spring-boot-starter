/*
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

package com.yann.ssh.pool;

import com.yann.ssh.entity.SshSession;

import java.util.Map;

import org.apache.commons.pool2.impl.AbandonedConfig;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SSH Session Pool
 * @author Yann Ann
 * @date 2022/11/8 22:01
 */
public class SshSessionPool {

    private static final Logger logger = LoggerFactory.getLogger(SshSessionPool.class);

    private volatile GenericKeyedObjectPool<SshSession, SshSessionHolder> sessionPool = null;

    private GenericKeyedObjectPoolConfig<SshSessionHolder> poolConfig;

    private AbandonedConfig abandonedConfig;

    private SftpConfig sftpConfig;

    public SshSessionPool(GenericKeyedObjectPoolConfig<SshSessionHolder> poolConfig, AbandonedConfig abandonedConfig) {
        this.poolConfig = poolConfig;
        this.abandonedConfig = abandonedConfig;
    }

    public GenericKeyedObjectPool<SshSession, SshSessionHolder> getSessionPool() {
        if (sessionPool == null) {
            synchronized (SshSessionPool.class) {
                if (sessionPool == null) {
                    sessionPool =
                            new GenericKeyedObjectPool<>(new PoolSshSessionFactory(), poolConfig, abandonedConfig);
                }
            }
        }
        return sessionPool;
    }

    public SshSessionHolder getSessionHolder(SshSession sessionHost) throws Exception {
        logger.info("try to borrow a session:{}", sessionHost.toString());
        return getSessionPool().borrowObject(sessionHost);
    }

    public void returnSshSessionHolder(SshSession sessionHost, SshSessionHolder sessionHolder) {
        logger.info("return session:{}", sessionHost.toString());
        getSessionPool().returnObject(sessionHost, sessionHolder);
    }

    public void printPoolStatus() {
        Map<String, Integer> activeKeyMap = sessionPool.getNumActivePerKey();
        for (Map.Entry<String, Integer> key : activeKeyMap.entrySet()) {
            String hostName = key.getKey();
            logger.info("Session Pool Stat: Key :{}, Active session count: {}, Total: {}", hostName, key.getValue(),
                    sessionPool.getMaxTotalPerKey());
        }
        logger.info("Session Pool Stat: Active session count: {}, Idle session : {}, Wait session: {} , Total: {}",
                sessionPool.getNumActive(), sessionPool.getNumIdle(), sessionPool.getNumWaiters(),
                sessionPool.getMaxTotal());
    }

    public void setPoolConfig(GenericKeyedObjectPoolConfig<SshSessionHolder> poolConfig) {
        this.poolConfig = poolConfig;
    }

    public void setAbandonedConfig(AbandonedConfig abandonedConfig) {
        this.abandonedConfig = abandonedConfig;
    }

    public SftpConfig getSftpConfig() {
        return sftpConfig;
    }

    public void setSftpConfig(SftpConfig sftpConfig) {
        this.sftpConfig = sftpConfig;
    }

}
