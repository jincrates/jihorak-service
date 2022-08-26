package me.jincrates.studymanager.modules.event;

import me.jincrates.studymanager.infra.AbstractContainerBaseTest;
import me.jincrates.studymanager.infra.MockMvcTest;
import me.jincrates.studymanager.modules.account.Account;
import me.jincrates.studymanager.modules.account.AccountFactory;
import me.jincrates.studymanager.modules.account.AccountRepository;
import me.jincrates.studymanager.modules.account.WithAccount;
import me.jincrates.studymanager.modules.study.Study;
import me.jincrates.studymanager.modules.study.StudyFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockMvcTest
class EventControllerTest extends AbstractContainerBaseTest {
    @Autowired MockMvc mockMvc;
    @Autowired StudyFactory studyFactory;
    @Autowired AccountFactory accountFactory;
    @Autowired EventService eventService;
    @Autowired EnrollmentRepository enrollmentRepository;
    @Autowired AccountRepository accountRepository;

    @Test
    @DisplayName("선착순 모임에 참가 신청 - 자동 수락")
    @WithAccount("jincrates")
    void newEnrollmentToFCFS_EventAccepted() throws Exception {
        Account jingyu = accountFactory.createAccount("jingyu");
        Study study = studyFactory.createStudy("test-study", jingyu);
        Event event = createEvent("test-event", EventType.FCFS, 2, study, jingyu);

        mockMvc.perform(post("/study/" + study.getPath() + "/events/" + event.getId() + "/enroll")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/study/" + study.getPath() + "/events/" + event.getId()));

        Account jincrates = accountRepository.findByNickname("jincrates");
        isAccepted(jincrates, event);
    }

    @Test
    @DisplayName("선착순 모임에 참가 신청 - 대기중 (이미 인원이 꽉참)")
    @WithAccount("jincrates")
    void newEnrollmentToFCFS_eventNotAccepted() throws Exception {
        Account jingyu = accountFactory.createAccount("jingyu");
        Study study = studyFactory.createStudy("test-study", jingyu);
        Event event = createEvent("test-event", EventType.FCFS, 2, study, jingyu);

        Account plato = accountFactory.createAccount("plato");
        Account aristotle = accountFactory.createAccount("aristotle");
        eventService.newEnrollment(event, plato);
        eventService.newEnrollment(event, aristotle);

        mockMvc.perform(post("/study/" + study.getPath() + "/events/" + event.getId() + "/enroll")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/study/" + study.getPath() + "/events/" + event.getId()));

        Account jincrates = accountRepository.findByNickname("jincrates");
        inNotAccepted(jincrates, event);
    }

    private void inNotAccepted(Account account, Event event) {
        assertFalse(enrollmentRepository.findByEventAndAccount(event, account).isAccepted());
    }

    private void isAccepted(Account account, Event event) {
        assertTrue(enrollmentRepository.findByEventAndAccount(event, account).isAccepted());
    }

    private Event createEvent(String eventTitle, EventType eventType, int limit, Study study, Account account) {
        Event event = new Event();
        event.setEventType(eventType);
        event.setLimitOfEnrollments(limit);
        event.setTitle(eventTitle);
        event.setCreatedDateTime(LocalDateTime.now());
        event.setEndEnrollmentDateTime(LocalDateTime.now().plusDays(1));
        event.setStartDateTime(LocalDateTime.now().plusDays(1).plusHours(5));
        event.setEndDateTime(LocalDateTime.now().plusDays(1).plusHours(7));
        return eventService.createEvent(event, study, account);
    }
}