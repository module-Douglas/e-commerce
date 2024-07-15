package io.github.douglas.ms_orchestrator.service;

import io.github.douglas.ms_orchestrator.dto.event.Event;

public interface OrchestratorService {
    void startSaga(Event event);
}
