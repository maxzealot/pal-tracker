package io.pivotal.pal.tracker;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class InMemoryTimeEntryRepository implements TimeEntryRepository {


    private Map<Long, TimeEntry> mem = new HashMap<>();

    private AtomicLong idGen = new AtomicLong(1);

    @Override
    public TimeEntry create(TimeEntry entry) {
        long id = idGen.getAndAdd(1);
        TimeEntry created = new TimeEntry(id , entry.getProjectId(),entry.getUserId(),entry.getDate(), entry.getHours());
        mem.put(id, created);

        return created;
    }

    @Override
    public TimeEntry find(long id) {

        return mem.get(id);
    }

    @Override
    public List<TimeEntry> list() {
        return new ArrayList<>(mem.values());
    }

    @Override
    public TimeEntry update(long id, TimeEntry entry) {
        TimeEntry old = mem.get(id);

        if (old ==null){
            return  null;
        }else{
            old.setProjectId(entry.getProjectId());
            old.setUserId(entry.getUserId());
            old.setDate(entry.getDate());
            old.setHours(entry.getHours());
            return old;
        }

    }

    @Override
    public void delete(long id) {
        mem.remove(id);
    }
}
