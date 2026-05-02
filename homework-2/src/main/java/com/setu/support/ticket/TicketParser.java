package com.setu.support.ticket;

import java.util.List;

interface TicketParser {
    List<CreateTicketRequest> parse(String content);
}
