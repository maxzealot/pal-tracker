package io.pivotal.pal.tracker;

import org.springframework.stereotype.Component;

import java.util.List;


public interface TimeEntryRepository {
    TimeEntry create(TimeEntry entry);

    TimeEntry find(long id);

    List<TimeEntry> list();

    TimeEntry update(long id, TimeEntry entry);

    void delete(long id);
}
