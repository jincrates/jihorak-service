package me.jincrates.studymanager.modules.account;

import me.jincrates.studymanager.infra.AbstractContainerBaseTest;
import me.jincrates.studymanager.infra.MockMvcTest;
import me.jincrates.studymanager.infra.mail.EmailMessage;
import me.jincrates.studymanager.infra.mail.EmailService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockMvcTest
class AccountControllerTest extends AbstractContainerBaseTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private AccountRepository accountRepository;
    @MockBean EmailService emailService;

    @DisplayName("인증 메일 확인 - 입력값 오류")
    @Test
    void checkEmailTokenFailTest() throws Exception {
        mockMvc.perform(get("/check-email-token")
                .param("token", "adfasdf")
                .param("email", "email@email.com"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"))
                .andExpect(view().name("account/checked-email"))
                .andExpect(unauthenticated());
    }

    @DisplayName("인증 메일 확인 - 입력값 정상")
    @Test
    void checkEmailTokenTest() throws Exception {
        Account account = Account.builder()
                .email("jincrates@email.com")
                .password("12345678")
                .nickname("jincrates")
                .build();
        Account newAccount = accountRepository.save(account);
        newAccount.generateEmailCheckToken();

        mockMvc.perform(get("/check-email-token")
                        .param("token", newAccount.getEmailCheckToken())
                        .param("email", newAccount.getEmail()))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("error"))
                .andExpect(model().attributeExists("nickname"))
                .andExpect(model().attributeExists("numberOfUser"))
                .andExpect(view().name("account/checked-email"))
                .andExpect(authenticated());
    }

    // 매번 있는지 확인할 수 없으니 테스트 코드로 확인하기 위함
    @DisplayName("회원가입 화면 보이는지 테스트")
    @Test
    void signUpFormTest() throws Exception {
        mockMvc.perform(get("/sign-up"))
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up"))
                .andExpect(model().attributeExists("signUpForm"))
                .andExpect(unauthenticated());
    }

    @DisplayName("회원가입 처리 - 입력값 오류")
    @Test
    void signUpSubmitFailTest() throws Exception {
        mockMvc.perform(post("/sign-up")
                .param("nickname", "jincates")
                .param("email", "email..")
                .param("password", "123")
                .with(csrf()))  //이거 안넣으면 테스트 깨진다.
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up"))
                .andExpect(unauthenticated());
    }

    @DisplayName("회원가입 처리 - 입력값 정상")
    @Test
    void signUpSubmitTest() throws Exception {
        mockMvc.perform(post("/sign-up")
                .param("nickname", "jincrates")
                .param("email", "jincrates@email.com")
                .param("password", "12345678")
                .with(csrf()))  //이거 안넣으면 테스트 깨진다.
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"))
                .andExpect(authenticated().withUsername("jincrates"));

        Account account = accountRepository.findByEmail("jincrates@email.com");
        assertNotNull(account);
        //System.out.println(account.getPassword());
        assertNotEquals(account.getPassword(), "12345678");
        assertNotNull(account.getEmailCheckToken());

        //assertTrue(accountRepository.existsByEmail("jincrates@email.com"));  //위와 같은 테스트
        then(emailService).should().send(any(EmailMessage.class));  //메일 발송 확인
    }
}
