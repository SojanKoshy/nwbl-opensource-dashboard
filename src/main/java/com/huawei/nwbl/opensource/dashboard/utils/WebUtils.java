package com.huawei.nwbl.opensource.dashboard.utils;

import com.huawei.nwbl.opensource.dashboard.domain.Folder;
import com.huawei.nwbl.opensource.dashboard.domain.GerritAccount;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Web format utils for thymeleaf
 */
public class WebUtils {

    public static String formatAccounts(Set<GerritAccount> accounts) {

        List<String> accountList = new ArrayList<>();
        for (GerritAccount account : accounts) {
            accountList.add(account.toString());
        }
        Collections.sort(accountList);
        return String.join(",", accountList);
    }

    public static String formatFolders(Set<Folder> folders) {
        List<String> folderList = new ArrayList<>();
        for (Folder folder : folders) {
            folderList.add(folder.getName());
        }
        Collections.sort(folderList);
        return String.join(",", folderList);
    }

}
