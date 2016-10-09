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

import com.huawei.nwbl.opensource.dashboard.domain.FolderRepository;
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

    @Autowired
    private FolderRepository folderRepository;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(DashboardApplication.class, args);
    }

    @Bean
    public Converter<String, Member> stringToMemberConverter() {
        return new Converter<String, Member>() {
            @Override
            public Member convert(String id) {
                return memberRepository.findOne(Long.valueOf(id));
            }
        };
    }

    @Bean
    public Converter<String, GerritAccount> stringToAccountConverter() {
        return new Converter<String, GerritAccount>() {
            @Override
            public GerritAccount convert(String id) {
                return gerritAccountRepository.findOne(Long.valueOf(id));
            }
        };
    }

    @Bean
    public Converter<String, Set<GerritAccount>> stringToAccountsConverter() {
        return new Converter<String, Set<GerritAccount>>() {
            @Override
            public Set<GerritAccount> convert(String accounts) {
                Set<GerritAccount> gerritAccounts = new HashSet<>();
                for (String id : accounts.trim().split("\n")) {
                    GerritAccount account = gerritAccountRepository.findOne(Long.valueOf(id.trim()));
                    if (account == null) {
                        return null;
                    }
                    gerritAccounts.add(account);
                }
                return gerritAccounts;
            }
        };
    }

    @Bean
    public Converter<Set<GerritAccount>, String> accountsToStringConverter() {
        return new Converter<Set<GerritAccount>, String>() {
            @Override
            public String convert(Set<GerritAccount> accounts) {
                List<String> accountsId = new ArrayList<>();
                for (GerritAccount account : accounts) {
                    accountsId.add(account.getId().toString());
                }
                return String.join("\n", accountsId);
            }
        };
    }

//    @Bean
//    public Converter<String, Set<Folder>> stingToFolderConverter() {
//        return new Converter<String, Set<Folder>>() {
//            @Override
//            public Set<Folder> convert(String folderNames) {
//                Set<Folder> folders = new HashSet<>();
//                for (String name : folderNames.split("\n")) {
//                    Folder folder = folderRepository.findByName(name.trim());
//                    if (folder == null) {
//                        folder = new Folder();
//                    }
//                    folder.setName(name.trim());
//                    folders.add(folder);
//                }
//                return folders;
//            }
//        };
//    }
//
//    @Bean
//    public Converter<Set<Folder>, String> folderToStringConverter() {
//        return new Converter<Set<Folder>, String>() {
//            @Override
//            public String convert(Set<Folder> folders) {
//                List<String> folderNames = new ArrayList<>();
//                for (Folder folder : folders) {
//                    folderNames.add(folder.getName());
//                }
//                return String.join("\n", folderNames);
//            }
//        };
//    }
}
