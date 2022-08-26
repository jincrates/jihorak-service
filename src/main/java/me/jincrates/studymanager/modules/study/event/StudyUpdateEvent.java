package me.jincrates.studymanager.modules.study.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.jincrates.studymanager.modules.study.Study;

@Getter
@RequiredArgsConstructor
public class StudyUpdateEvent {

    private final Study study;

    private final String message;
}
