package dashboard.utils;

import dashboard.domain.GerritAccount;

import java.util.Set;

/**
 * Created by sojan on 5/10/16.
 */
public class WebUtils {

    public static String formatAccounts(Set<GerritAccount> accounts) {
        StringBuilder sb = new StringBuilder();
        for (GerritAccount account: accounts) {

            sb.append(account).append("\n");

        }
        return sb.toString();
    }


}
