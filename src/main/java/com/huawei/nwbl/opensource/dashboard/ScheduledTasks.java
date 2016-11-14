package com.huawei.nwbl.opensource.dashboard;

import com.huawei.nwbl.opensource.dashboard.domain.GerritAccount;
import com.huawei.nwbl.opensource.dashboard.domain.GerritAccountRepository;
import com.huawei.nwbl.opensource.dashboard.domain.Member;
import com.huawei.nwbl.opensource.dashboard.domain.MemberRepository;
import com.huawei.nwbl.opensource.dashboard.service.GerritChangeListScraperService;
import com.huawei.nwbl.opensource.dashboard.web.ProjectController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class ScheduledTasks {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    private GerritChangeListScraperService gerritChangeListScraperService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private GerritAccountRepository gerritAccountRepository;

    @Autowired
    private ProjectController projectController;

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void autoUpdate() {
        log.info("Scheduled update started at {}", dateFormat.format(new Date()));

//        for (Member member : memberRepository.findAllByOrderByName()) {
//            for (GerritAccount account : member.getAccounts()) {
//                String searchTerm = String.format("owner:\"%s <%s>\"", account.getName(), account.getEmail());
//                gerritChangeListScraperService.scrape(searchTerm);
//            }
//        }

        for (GerritAccount account : gerritAccountRepository.findAll()) {
            String searchTerm = String.format("owner:\"%s <%s>\"", account.getName(), account.getEmail());
            gerritChangeListScraperService.scrape(searchTerm);
        }
        projectController.update();

        log.info("Scheduled update ended at {}", dateFormat.format(new Date()));

    }
}