package me.jincrates.studymanager.modules.temp;

import lombok.RequiredArgsConstructor;
import me.jincrates.studymanager.modules.account.Account;
import me.jincrates.studymanager.modules.account.AccountRepository;
import me.jincrates.studymanager.modules.account.AccountService;
import me.jincrates.studymanager.modules.study.Study;
import me.jincrates.studymanager.modules.study.StudyService;
import me.jincrates.studymanager.modules.tag.Tag;
import me.jincrates.studymanager.modules.tag.TagRepository;
import me.jincrates.studymanager.modules.zone.Zone;
import me.jincrates.studymanager.modules.zone.ZoneRepository;
import net.bytebuddy.utility.RandomString;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TempDateService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountService accountService;
    private final StudyService studyService;
    private final TagRepository tagRepository;
    private final ZoneRepository zoneRepository;

    public void generateTempAccounts() {
        int DATA_COUNT = 30;
        for (int i = 0; i < DATA_COUNT; i++) {
            String randomValue = RandomString.make(5);  //랜덤한 String을 만들어준다.
            Account account = Account.builder()
                    .nickname("temp-" + randomValue)
                    .email("temp-" + randomValue + "@email.com")
                    .password(passwordEncoder.encode("1q2w3e4r!Q"))
                    .build();
            account.generateEmailCheckToken();  //토큰 발생
            account.completeSignUp();  //가입 완료

            accountRepository.save(account);
        }
    }

    public void generateTempStudies(Account account) {
        int DATA_COUNT = 30;
        for (int i = 0; i < DATA_COUNT; i++) {
            String randomValue = RandomString.make(5);  //랜덤한 String을 만들어준다.
            Study study = Study.builder()
                    .title("테스트 스터디 " + randomValue)
                    .path("test-" + randomValue)
                    .shortDescription("테스트용 스터디입니다.")
                    .fullDescription("test")
                    .tags(new HashSet<>())
                    .managers(new HashSet<>())
                    .build();
            study.publish();  //공개상태로
            Study newStudy = studyService.createNewStudy(study, account);
            Tag jpa = tagRepository.findByTitle("JPA");
            newStudy.getTags().add(jpa);
            //Zone seoul = zoneRepository.findByCityAndProvince("Seoul", "none");
            //newStudy.getZones().add(seoul);
        }
    }
}
