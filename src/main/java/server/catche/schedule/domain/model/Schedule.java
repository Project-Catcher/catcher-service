package server.catche.schedule.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "schedule")
public class Schedule {

    @Id
    @Column(name = "schedule_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private String thumbnail;
    private LocalDate startDate;
    private LocalDate endDate;

    public Schedule(String title, String content, String thumbnail, LocalDate startDate, LocalDate endDate) {
        this.title = title;
        this.content = content;
        this.thumbnail = thumbnail;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
