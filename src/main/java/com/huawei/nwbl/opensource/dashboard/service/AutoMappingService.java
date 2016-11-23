package com.huawei.nwbl.opensource.dashboard.service;

import com.huawei.nwbl.opensource.dashboard.domain.GerritAccount;
import com.huawei.nwbl.opensource.dashboard.domain.GerritAccountRepository;
import com.huawei.nwbl.opensource.dashboard.domain.GerritChange;
import com.huawei.nwbl.opensource.dashboard.domain.GerritChangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by sojan on 22/11/16.
 */
@Service
public class AutoMappingService {

    @Autowired
    GerritAccountRepository gerritAccountRepository;

    @Autowired
    GerritChangeRepository gerritChangeRepository;

    public void remap() {
        List<GerritChange> gerritChanges = gerritChangeRepository.findAll();
        for(GerritChange gerritChange : gerritChanges) {
            GerritAccount gerritAccount = gerritChange.getAccount();
            if(!gerritAccount.isInUse()) {
                gerritAccount.setInUse(true);
                gerritAccountRepository.save(gerritAccount);
            }
        }
    }
}
