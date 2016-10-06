package dashboard.utils;

import dashboard.domain.GerritAccount;

import java.util.Set;

/**
 * Web format utils for thymeleaf
 */
public class WebUtils {

    public static String formatAccounts(Set<GerritAccount> accounts) {
        StringBuilder sb = new StringBuilder();
        for (GerritAccount account : accounts) {

            sb.append(account).append("\n");

        }
        return sb.toString();
    }


}
