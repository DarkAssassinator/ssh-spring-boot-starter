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

package com.yann.ssh.autoconfigure;

import com.yann.ssh.pool.SftpConfig;
import com.yann.ssh.pool.SshSessionAbandonedConfig;
import com.yann.ssh.pool.SshSessionHolder;
import com.yann.ssh.pool.SshSessionPool;
import com.yann.ssh.pool.SshSessionPoolConfig;
import com.yann.ssh.properties.SshProperties;

import org.apache.commons.pool2.impl.AbandonedConfig;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Yann Ann
 * @date 2022/11/7 21:14
 */
@Configuration
@EnableConfigurationProperties(SshProperties.class)
public class SshAutoConfiguration {

    @Bean
    public GenericKeyedObjectPoolConfig<SshSessionHolder> poolConfig(SshProperties properties) {
        SshSessionPoolConfig poolConfig = new SshSessionPoolConfig();
        BeanUtils.copyProperties(properties.getPool(), poolConfig);
        return poolConfig;
    }

    @Bean
    public AbandonedConfig abandonedConfig(SshProperties properties) {
        SshSessionAbandonedConfig abandonedConfig = new SshSessionAbandonedConfig();
        BeanUtils.copyProperties(properties.getPool(), abandonedConfig);
        return abandonedConfig;
    }

    @Bean
    public SftpConfig sftpConfig(SshProperties properties) {
        SftpConfig sftpConfig = new SftpConfig();
        BeanUtils.copyProperties(properties.getSftp(), sftpConfig);
        return sftpConfig;
    }

    @Bean
    public SshSessionPool sessionPool(GenericKeyedObjectPoolConfig<SshSessionHolder> poolConfig,
                                      AbandonedConfig abandonedConfig, SftpConfig sftpConfig) {
        return new SshSessionPool(poolConfig, abandonedConfig, sftpConfig);
    }

}
