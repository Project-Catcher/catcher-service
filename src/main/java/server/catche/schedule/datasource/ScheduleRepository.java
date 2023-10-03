package server.catche.schedule.datasource;


import org.springframework.data.jpa.repository.JpaRepository;
import server.catche.schedule.domain.model.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
