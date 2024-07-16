package io.github.douglas.ms_orchestrator.service;

import io.github.douglas.ms_orchestrator.dto.event.Event;

public interface OrchestratorService {
    void startSaga(Event event);

    void finishSagaSuccess(Event event);

    void finishSagaFail(Event event);

    void continueSaga(Event event);
}
