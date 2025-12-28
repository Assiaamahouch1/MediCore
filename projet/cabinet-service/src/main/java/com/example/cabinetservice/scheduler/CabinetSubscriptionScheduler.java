package com.example.cabinetservice.scheduler;

import com.example.cabinetservice.service.CabinetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CabinetSubscriptionScheduler {
    private static final Logger log = LoggerFactory.getLogger(CabinetSubscriptionScheduler.class);

    private final CabinetService service;

    public CabinetSubscriptionScheduler(CabinetService service) {
        this.service = service;
    }

    // Tous les jours à 02:00 (heure du serveur)
    @Scheduled(cron = "0 0 2 * * *")
    public void deactivateExpiredDaily() {
        int count = service.deactivateExpired();
        if (count > 0) {
            log.info("Deactivated {} expired cabinets", count);
        } else {
            log.debug("No expired cabinets to deactivate today");
        }
    }
}