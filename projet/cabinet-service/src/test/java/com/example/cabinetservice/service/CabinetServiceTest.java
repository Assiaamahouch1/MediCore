package com.example.cabinetservice.service;

import com.example.cabinetservice.dto.CabinetResponse;
import com.example.cabinetservice.dto.SubscriptionRenewRequest;
import com.example.cabinetservice.dto.SubscriptionStatusResponse;
import com.example.cabinetservice.mapper.CabinetMapper;
import com.example.cabinetservice.model.Cabinet;
import com.example.cabinetservice.repository.CabinetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CabinetServiceTest {

    @Mock CabinetRepository repo;
    CabinetMapper mapper = Mappers.getMapper(CabinetMapper.class);

    CabinetService service;

    @BeforeEach
    void setUp() {
        service = new CabinetService(repo, mapper);
    }

    @Test
    void activate_shouldFail_whenExpirationNullOrPast() {
        // Constructor: (id, logo, nom, specialite, adresse, ville, tel, service_actif, date_expiration_service)
        Cabinet c = new Cabinet(1L, null, "Cab A", null, null, null, null, true, null);
        when(repo.findById(1L)).thenReturn(Optional.of(c));

        assertThatThrownBy(() -> service.activate(1L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Subscription expired or not set");

        // Expiration passée
        c.setDate_expiration_service(LocalDate.now().minusDays(1));
        assertThatThrownBy(() -> service.activate(1L))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void renew_shouldSetFutureExpirationAndActivate() {
        // Constructor: (id, logo, nom, specialite, adresse, ville, tel, service_actif, date_expiration_service)
        Cabinet c = new Cabinet(1L, null, "Cab A", null, null, null, null, false, LocalDate.now().minusDays(10));
        when(repo.findById(1L)).thenReturn(Optional.of(c));
        when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        CabinetResponse res = service.renew(1L, new SubscriptionRenewRequest(3));

        assertThat(res.service_actif()).isTrue();
        assertThat(res.date_expiration_service()).isAfter(LocalDate.now());
    }

    @Test
    void status_shouldReturnExpired() {
        // Constructor: (id, logo, nom, specialite, adresse, ville, tel, service_actif, date_expiration_service)
        Cabinet c = new Cabinet(1L, null, "Cab A", null, null, null, null, false, LocalDate.now().minusDays(1));
        when(repo.findById(1L)).thenReturn(Optional.of(c));

        SubscriptionStatusResponse status = service.status(1L);
        assertThat(status.expired()).isTrue();
        assertThat(status.active()).isFalse();
        assertThat(status.daysRemaining()).isZero();
    }
}
