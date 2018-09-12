package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TimeEntryController {

    @Autowired
    private TimeEntryRepository timeEntryRepository;

    public TimeEntryController(TimeEntryRepository timeEntryRepository) {
        this.timeEntryRepository = timeEntryRepository;
    }

    @PostMapping(path = "/time-entries", consumes = "application/json", produces = "application/json")
    public ResponseEntity create(@RequestBody TimeEntry timeEntryToCreate){
        TimeEntry created = this.timeEntryRepository.create(timeEntryToCreate);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/time-entries/{id}")
    public ResponseEntity read(@PathVariable  long id){

        TimeEntry entry = this.timeEntryRepository.find(id);

        if(entry == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found");
        }else{
            return  ResponseEntity.ok(entry);
        }

    }

    @GetMapping("/time-entries")
    public ResponseEntity list(){
        return  ResponseEntity.status(HttpStatus.OK).body(this.timeEntryRepository.list());
    }

    @PutMapping(path = "/time-entries/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity update(@PathVariable  long id, @RequestBody TimeEntry entry){

        TimeEntry updated = this.timeEntryRepository.update(id, entry);

        if(updated == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found");
        }else{
            return  ResponseEntity.ok(updated);
        }
    }

    @DeleteMapping("/time-entries/{id}")
    public ResponseEntity delete(@PathVariable  long id){
        this.timeEntryRepository.delete(id);
        return  ResponseEntity.status(HttpStatus.NO_CONTENT).body(id + "is deleted");
    }
}
