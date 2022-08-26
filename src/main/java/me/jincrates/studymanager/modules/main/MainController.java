package me.jincrates.studymanager.modules.main;

import lombok.RequiredArgsConstructor;
import me.jincrates.studymanager.modules.account.AccountRepository;
import me.jincrates.studymanager.modules.account.CurrentAccount;
import me.jincrates.studymanager.modules.account.Account;
import me.jincrates.studymanager.modules.event.EnrollmentRepository;
import me.jincrates.studymanager.modules.study.Study;
import me.jincrates.studymanager.modules.study.StudyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final StudyRepository studyRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final AccountRepository accountRepository;

    @GetMapping("/")
    public String home(@CurrentAccount Account account, Model model) {
        if (account != null) {
            //계정정보
            Account accountLoaded = accountRepository.findAccountWithTagsAndZonesById(account.getId());
            model.addAttribute(accountLoaded);
            //모임 목록 - 자기가 참가신청했고 수락된 리스트
            model.addAttribute("enrollmentList", enrollmentRepository.findByAccountAndAcceptedOrderByEnrolledAtDesc(accountLoaded, true));
            //주요 활동 지역의 관심주제 스터디 목록
            model.addAttribute("studyList", studyRepository.findByAccountWithTagAndZone(accountLoaded.getTags(), accountLoaded.getZones()));
            //신규 스터디 목록
            model.addAttribute("studyListNew", studyRepository.findFirst6ByPublishedAndClosedOrderByPublishedAtDesc(true, false));
            //관리중인 스터디 목록
            model.addAttribute("studyManagerOf",
                    studyRepository.findFirst5ByManagersContainingAndClosedOrderByPublishedAtDesc(accountLoaded, false));
            //참여중인 스터디 목록
            model.addAttribute("studyMemberOf",
                    studyRepository.findFirst5ByMembersContainingAndClosedOrderByPublishedAtDesc(accountLoaded, false));
            return "index-after-login";
        }

        model.addAttribute("studyList", studyRepository.findFirst9ByPublishedAndClosedOrderByPublishedAtDesc(true, false));
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/search/study")  //TODO size, page, sort
    public String searchStudy(@CurrentAccount Account account, String keyword, Model model,
                              @PageableDefault(size = 9, sort = "publishedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        if (account != null) {
            model.addAttribute(account);
        }
        Page<Study> studyPage = studyRepository.findByKeyword(keyword, pageable);

        model.addAttribute("studyPage", studyPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("sortProperty",
                pageable.getSort().toString().contains("publishedAt") ? "publishedAt" : "memberCount");
        return "search";
    }
}
