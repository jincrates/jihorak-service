package me.jincrates.studymanager.modules.study.event;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.jincrates.studymanager.modules.study.Study;
import org.springframework.context.ApplicationEvent;

@Getter
@RequiredArgsConstructor
public class StudyCreatedEvent {

    private final Study study;
}
