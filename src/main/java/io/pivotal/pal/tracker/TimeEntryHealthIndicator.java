package io.pivotal.pal.tracker.actuator;

import io.pivotal.pal.tracker.repository.TimeEntryRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class TimeEntryHealthIndicator implements HealthIndicator {

    private TimeEntryRepository timeEntryRepo;

    public TimeEntryHealthIndicator(
            @Qualifier("jdbcTimeEntryRepository") TimeEntryRepository timeEntriesRepo
    ) {
        this.timeEntryRepo = timeEntryRepo;
    }

    @Override
    public Health health() {
        Health.Builder builder = new Health.Builder();

        if(timeEntryRepo.list().size() < 5) {
            builder.up();
        } else {
            builder.down();
        }

        return builder.build();
    }
}
