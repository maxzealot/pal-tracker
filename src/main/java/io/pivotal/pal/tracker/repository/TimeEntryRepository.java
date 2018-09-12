package io.pivotal.pal.tracker.repository;

import io.pivotal.pal.tracker.entity.TimeEntry;

import java.util.List;


public interface TimeEntryRepository {
    TimeEntry create(TimeEntry entry);

    TimeEntry find(long id);

    List<TimeEntry> list();

    TimeEntry update(long id, TimeEntry entry);

    void delete(long id);
}
