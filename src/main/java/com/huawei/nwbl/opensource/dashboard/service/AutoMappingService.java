package com.huawei.nwbl.opensource.dashboard.service;

import com.huawei.nwbl.opensource.dashboard.domain.Company;
import com.huawei.nwbl.opensource.dashboard.domain.CompanyEmailDomain;
import com.huawei.nwbl.opensource.dashboard.domain.CompanyEmailDomainRepository;
import com.huawei.nwbl.opensource.dashboard.domain.CompanyRepository;
import com.huawei.nwbl.opensource.dashboard.domain.GerritAccount;
import com.huawei.nwbl.opensource.dashboard.domain.GerritAccountRepository;
import com.huawei.nwbl.opensource.dashboard.domain.GerritChange;
import com.huawei.nwbl.opensource.dashboard.domain.GerritChangeRepository;
import com.huawei.nwbl.opensource.dashboard.domain.JiraAccount;
import com.huawei.nwbl.opensource.dashboard.domain.JiraAccountRepository;
import com.huawei.nwbl.opensource.dashboard.domain.OnosMember;
import com.huawei.nwbl.opensource.dashboard.domain.OnosMemberRepository;
import com.huawei.nwbl.opensource.dashboard.domain.OnosProject;
import com.huawei.nwbl.opensource.dashboard.domain.OnosProjectPath;
import com.huawei.nwbl.opensource.dashboard.domain.OnosProjectPathRepository;
import com.huawei.nwbl.opensource.dashboard.domain.OnosProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sojan on 22/11/16.
 */
@Service
public class AutoMappingService {

    @Autowired
    GerritAccountRepository gerritAccountRepository;

    @Autowired
    GerritChangeRepository gerritChangeRepository;

    @Autowired
    JiraAccountRepository jiraAccountRepository;

    @Autowired
    OnosMemberRepository onosMemberRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    CompanyEmailDomainRepository companyEmailDomainRepository;

    @Autowired
    OnosProjectRepository onosProjectRepository;

    @Autowired
    OnosProjectPathRepository onosProjectPathRepository;


    public void remap() {
//        List<GerritChange> gerritChanges = gerritChangeRepository.findAll();
//        for(GerritChange gerritChange : gerritChanges) {
//            GerritAccount gerritAccount = gerritChange.getAccount();
//            if(!gerritAccount.isInUse()) {
//                gerritAccount.setInUse(true);
//                gerritAccountRepository.save(gerritAccount);
//            }
//        }
//
//        List<GerritAccount> gerritAccounts = gerritAccountRepository.findAll();
//        List<JiraAccount> jiraAccounts = jiraAccountRepository.findAll();
//        for (GerritAccount gerritAccount : gerritAccounts) {
//            if (gerritAccount.isInUse()) {
//                OnosMember onosMember = onosMemberRepository.findByEmail(gerritAccount.getEmail());
//                if (onosMember == null) {
//                    onosMember = new OnosMember();
//                }
//                onosMember.setEmail(gerritAccount.getEmail());
//                onosMember.setCompany(gerritAccount.getCompany());
//                onosMember.setName(gerritAccount.getName());
//                onosMemberRepository.save(onosMember);
//            }
//        }
//
//        for (GerritAccount gerritAccount : gerritAccounts) {
//            gerritAccount.setOnosMember(onosMemberRepository.findByEmail(gerritAccount.getEmail()));
//            gerritAccountRepository.save(gerritAccount);
//        }
//
//        for (JiraAccount jiraAccount : jiraAccounts) {
//            OnosMember onosMember = onosMemberRepository.findByEmail(jiraAccount.getEmail());
//            if (onosMember == null) {
//                onosMember = new OnosMember();
//                String domain = jiraAccount.getEmail().split("@")[1];
//                CompanyEmailDomain emailDomain = companyEmailDomainRepository.findByDomain(domain);
//                if (emailDomain == null) {
//                    System.out.println("#######################" + jiraAccount.getEmail());
//                } else {
//                    onosMember.setCompany(emailDomain.getCompany());
//                }
//                onosMember.setName(jiraAccount.getName());
//                onosMember.setEmail(jiraAccount.getEmail());
//                onosMemberRepository.save(onosMember);
//            } else {
//                if(onosMember.getCompany() == null) {
//
//                    String domain = jiraAccount.getEmail().split("@")[1];
//                    CompanyEmailDomain emailDomain = companyEmailDomainRepository.findByDomain(domain);
//                    if (emailDomain == null) {
//                        System.out.println("#######################" + jiraAccount.getEmail());
//                    } else {
//                        onosMember.setCompany(emailDomain.getCompany());
//                    }
//                    onosMemberRepository.save(onosMember);
//                }
//            }
//        }
//
//
//        for (JiraAccount jiraAccount : jiraAccounts) {
//            jiraAccount.setOnosMember(onosMemberRepository.findByEmail(jiraAccount.getEmail()));
//            jiraAccountRepository.save(jiraAccount);
//        }
//
//        for (OnosMember onosMember : onosMemberRepository.findAll()) {
//            Company company = onosMember.getCompany();
//            List<OnosMember> onosMembers = company.getOnosMembers();
//            if (onosMembers == null) {
//                onosMembers = new ArrayList<>();
//            }
//            onosMembers.add(onosMember);
//            companyRepository.save(company);
//        }
        List<String> folderPatterns = new ArrayList<>();
        folderPatterns.add("(/(sfc)/)");
        folderPatterns.add("(/onosproject/([^/]+)/)");
        folderPatterns.add("(^(core)/)");
        folderPatterns.add("(^(incubator)/)");
        folderPatterns.add("(^(tools)/)");
        folderPatterns.add("(^(web)/)");
        folderPatterns.add("(^(cli)/)");
        folderPatterns.add("(^apps/([^/]+)/)");
        folderPatterns.add("(^protocols/([^/]+)/)");
        folderPatterns.add("(^providers/([^/]+)/)");
        folderPatterns.add("(^drivers/([^/]+)/)");
        folderPatterns.add("(^utils/([^/]+)/)");
        folderPatterns.add("(^(TestON)/)");

        for (GerritChange gerritChange : gerritChangeRepository.findAll()) {
            String filePath = gerritChange.getFirstFilePath();
            boolean found = false;

            for (String folderPattern: folderPatterns) {
                Pattern pattern = Pattern.compile(folderPattern);
                Matcher matcher = pattern.matcher(filePath);
                if (matcher.find()) {
                    String recognizedFolder = matcher.group(1);
                    String recognizedProject = matcher.group(2);
//                    System.out.println(recognizedFolder + " - " + recognizedProject);

                    OnosProject onosProject = onosProjectRepository.findByName(recognizedProject);
                    if (onosProject == null) {
                        onosProject = new OnosProject();
                        onosProject.setName(recognizedProject);
                        onosProjectRepository.save(onosProject);
                    }

                    gerritChange.setOnosProject(onosProject);
                    gerritChangeRepository.save(gerritChange);

                    found = true;
                    break;
                }
            }
            if(!found) {
//                    System.out.println("### Unrecognized folder " + filePath + gerritChange.getStatus());

                    OnosProject onosProject = onosProjectRepository.findByName("others");
                    if (onosProject == null) {
                        onosProject = new OnosProject();
                        onosProject.setName("others");
                        onosProjectRepository.save(onosProject);
                    }

                    gerritChange.setOnosProject(onosProject);
                    gerritChangeRepository.save(gerritChange);
            }
        }
    }
}
