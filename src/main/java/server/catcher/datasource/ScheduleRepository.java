package server.catcher.datasource;


import org.springframework.data.jpa.repository.JpaRepository;
import server.catcher.domain.model.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
