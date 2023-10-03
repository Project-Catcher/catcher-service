package server.catche.schedule.presentation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.catche.schedule.application.command.Command;
import server.catche.schedule.application.command.GetScheduleCommand;
import server.catche.schedule.application.command.RegisterScheduleCommand;
import server.catche.schedule.application.service.ScheduleService;
import server.catche.schedule.presentation.dto.ScheduleReq;
import server.catche.schedule.presentation.dto.ScheduleResp;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedule")
public class ScheduleApiController {
    private final ScheduleService scheduleService;

    @PostMapping("/register")
    public ResponseEntity<Object> registerSchedule(
            @RequestBody ScheduleReq.ScheduleRegisterDTO request
    ) {
        Command<Void> command = new RegisterScheduleCommand(scheduleService, request);
        command.execute();
        return ResponseEntity.ok(null);
    }

    @GetMapping("/{scheduleId}")
    public ResponseEntity<ScheduleResp.ScheduleDTO> getSchedule(
            @PathVariable Long scheduleId
    ) {
        Command<ScheduleResp.ScheduleDTO> command = new GetScheduleCommand(scheduleService, scheduleId);
        ScheduleResp.ScheduleDTO response = command.execute();
        return ResponseEntity.ok(response);
    }
}
