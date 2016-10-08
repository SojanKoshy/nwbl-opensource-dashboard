/*
 * Copyright 2012-2013 the original author or authors.
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

package com.huawei.nwbl.opensource.dashboard;

import com.huawei.nwbl.opensource.dashboard.domain.GerritAccount;
import com.huawei.nwbl.opensource.dashboard.domain.GerritAccountRepository;
import com.huawei.nwbl.opensource.dashboard.domain.Member;
import com.huawei.nwbl.opensource.dashboard.domain.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootApplication
public class DashboardApplication {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private GerritAccountRepository gerritAccountRepository;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(DashboardApplication.class, args);
    }

    @Bean
    public Converter<String, Member> memberConverter() {
        return new Converter<String, Member>() {
            @Override
            public Member convert(String id) {
                return memberRepository.getOne(Long.valueOf(id));
            }
        };
    }

    @Bean
    public Converter<String, GerritAccount> accountConverter() {
        return new Converter<String, GerritAccount>() {
            @Override
            public GerritAccount convert(String id) {
                return gerritAccountRepository.getOne(Long.valueOf(id));
            }
        };
    }

    @Bean
    public Converter<String, Set<GerritAccount>> accountsConverter() {
        return new Converter<String, Set<GerritAccount>>() {
            @Override
            public Set<GerritAccount> convert(String accounts) {
                Set<GerritAccount> gerritAccounts = new HashSet<>();
                for (String id : accounts.split(",")) {
                    GerritAccount account = gerritAccountRepository.getOne(Long.valueOf(id));
                    if (account != null) {
                        gerritAccounts.add(account);
                    }
                }
                if (gerritAccounts.isEmpty()) {
                    return null;
                }
                return gerritAccounts;
            }
        };
    }

    @Bean
    public Converter<Set<GerritAccount>, String> accountsDisplayConverter() {
        return new Converter<Set<GerritAccount>, String>() {
            @Override
            public String convert(Set<GerritAccount> accounts) {
                List<String> accountsId = new ArrayList<>();
                for (GerritAccount account : accounts) {
                    accountsId.add(account.getId().toString());
                }
                return String.join(",", accountsId);
            }
        };
    }
}
