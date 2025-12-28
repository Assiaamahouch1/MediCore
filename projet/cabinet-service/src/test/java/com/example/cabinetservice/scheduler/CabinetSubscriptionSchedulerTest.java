package com.example.cabinetservice.scheduler;

import com.example.cabinetservice.service.CabinetService;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class CabinetSubscriptionSchedulerTest {

    @Test
    void scheduler_callsService() {
        CabinetService service = mock(CabinetService.class);
        when(service.deactivateExpired()).thenReturn(2);

        CabinetSubscriptionScheduler scheduler = new CabinetSubscriptionScheduler(service);
        scheduler.deactivateExpiredDaily();

        verify(service, times(1)).deactivateExpired();
    }
}