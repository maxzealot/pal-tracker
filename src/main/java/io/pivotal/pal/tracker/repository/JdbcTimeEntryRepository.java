package io.pivotal.pal.tracker.repository;

import io.pivotal.pal.tracker.entity.TimeEntry;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

@Component("jdbcTimeEntryRepository")
public class JdbcTimeEntryRepository implements TimeEntryRepository {

    private DataSource dataSource;

    private org.springframework.jdbc.core.JdbcTemplate JdbcTemplate;

    public JdbcTimeEntryRepository(DataSource dataSource) {
        this.dataSource =dataSource;
        this.JdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public TimeEntry create(TimeEntry entry) {
        KeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        this.JdbcTemplate.update(con -> {
            PreparedStatement statement =  con.prepareStatement("insert time_entries (project_id, user_id, date, hours) values (?,?,?,?)",RETURN_GENERATED_KEYS);
            statement.setLong(1, entry.getProjectId());
            statement.setLong(2, entry.getUserId());
            statement.setDate(3, Date.valueOf(entry.getDate()));
            statement.setInt(4, entry.getHours());
            return statement;
        }, generatedKeyHolder);
        return find(generatedKeyHolder.getKey().longValue());
    }

    @Override
    public TimeEntry find(long id) {
        TimeEntry entry = (TimeEntry) this.JdbcTemplate.query("select id, project_id, user_id, date, hours from time_entries where id=?",
                new Object[]{id},
                extractor);
        return entry;
    }

    private final RowMapper<TimeEntry> mapper = (rs, rowNum) -> new TimeEntry(
            rs.getLong("id"),
            rs.getLong("project_id"),
            rs.getLong("user_id"),
            rs.getDate("date").toLocalDate(),
            rs.getInt("hours")
    );

    private final ResultSetExtractor<TimeEntry> extractor =
            (rs) -> rs.next() ? mapper.mapRow(rs, 1) : null;

    @Override
    public List<TimeEntry> list() {
        return this.JdbcTemplate.query("select * from time_entries", mapper);

    }

    @Override
    public TimeEntry update(long id, TimeEntry entry) {
        this.JdbcTemplate.update("update time_entries set id = ?, project_id = ?, user_id = ?, date = ?, hours = ?", id, entry.getProjectId(), entry.getUserId(), Date.valueOf(entry.getDate()), entry.getHours());
        return find(id);
    }

    @Override
    public void delete(long id) {
        JdbcTemplate.update("delete from time_entries where id = ?", id);

    }
}
