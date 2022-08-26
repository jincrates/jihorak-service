package me.jincrates.studymanager.modules.event.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.jincrates.studymanager.modules.event.Enrollment;

@Getter
@RequiredArgsConstructor
public abstract class EnrollmentEvent {

    private final Enrollment enrollment;

    private final String message;
}
