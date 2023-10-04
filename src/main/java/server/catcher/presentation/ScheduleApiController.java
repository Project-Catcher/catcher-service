package server.catcher.presentation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.catcher.core.command.Command;
import server.catcher.core.command.GetScheduleCommand;
import server.catcher.core.command.RegisterScheduleCommand;
import server.catcher.core.service.ScheduleService;
import server.catcher.core.dto.ScheduleReq;
import server.catcher.core.dto.ScheduleResp;
import server.catcher.core.command.ScheduleCommandExecutor;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedule")
public class ScheduleApiController {
    private final ScheduleService scheduleService;
    private final ScheduleCommandExecutor commandExecutor;

    @PostMapping("/register")
    public ResponseEntity<Object> registerSchedule(
            @RequestBody ScheduleReq.ScheduleRegisterDTO request
    ) {
        Command<Void> command = new RegisterScheduleCommand(scheduleService, request);
        commandExecutor.run(command);
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
