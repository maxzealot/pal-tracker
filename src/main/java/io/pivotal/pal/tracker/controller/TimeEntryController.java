package io.pivotal.pal.tracker.controller;

import io.pivotal.pal.tracker.entity.TimeEntry;
import io.pivotal.pal.tracker.repository.TimeEntryRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TimeEntryController {

    private  CounterService counter;
    private  GaugeService gauge;

    private TimeEntryRepository timeEntryRepository;

    public TimeEntryController(
            @Qualifier("jdbcTimeEntryRepository") TimeEntryRepository timeEntriesRepo,
            CounterService counter,
            GaugeService gauge
    ) {
        this.timeEntryRepository = timeEntriesRepo;
        this.counter = counter;
        this.gauge = gauge;
    }

    @PostMapping(path = "/time-entries", consumes = "application/json", produces = "application/json")
    public ResponseEntity create(@RequestBody TimeEntry timeEntryToCreate){
        TimeEntry created = this.timeEntryRepository.create(timeEntryToCreate);
        counter.increment("TimeEntry.created");
        gauge.submit("timeEntries.count", timeEntryRepository.list().size());

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/time-entries/{id}")
    public ResponseEntity read(@PathVariable  long id){

        TimeEntry entry = this.timeEntryRepository.find(id);

        if(entry == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found");
        }else{
            counter.increment("TimeEntry.read");
            return  ResponseEntity.ok(entry);
        }

    }

    @GetMapping("/time-entries")
    public ResponseEntity list(){
        counter.increment("TimeEntry.listed");
        return  ResponseEntity.status(HttpStatus.OK).body(this.timeEntryRepository.list());
    }

    @PutMapping(path = "/time-entries/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity update(@PathVariable  long id, @RequestBody TimeEntry entry){

        TimeEntry updated = this.timeEntryRepository.update(id, entry);

        if(updated == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found");
        }else{
            counter.increment("TimeEntry.updated");
            return  ResponseEntity.ok(updated);
        }
    }

    @DeleteMapping("/time-entries/{id}")
    public ResponseEntity delete(@PathVariable  long id){
        this.timeEntryRepository.delete(id);
        counter.increment("TimeEntry.deleted");
        gauge.submit("timeEntries.count", timeEntryRepository.list().size());

        return  ResponseEntity.status(HttpStatus.NO_CONTENT).body(id + "is deleted");
    }
}
