package com.huawei.nwbl.opensource.dashboard.domain;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by sojan on 19/11/16.
 */
public interface JiraTicketRepository extends JpaRepository<JiraTicket, Long> {
}
