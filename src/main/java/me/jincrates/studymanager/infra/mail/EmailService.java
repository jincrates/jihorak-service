package me.jincrates.studymanager.infra.mail;

public interface EmailService {

    void send(EmailMessage emailMessage);
}
