package me.jincrates.studymanager.modules.event;

import lombok.RequiredArgsConstructor;
import me.jincrates.studymanager.modules.account.Account;
import me.jincrates.studymanager.modules.account.UserAccount;
import me.jincrates.studymanager.modules.notification.NotificationRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class NotificationInterceptor implements HandlerInterceptor {

    private final NotificationRepository notificationRepository;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //인증 정보가 있는 요청인지
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //리다이렉트 뷰가 아니고, 인증정보가 있으며, pricipal이 UserAcount여야 함
        if (modelAndView != null && !isRedirectView(modelAndView)
                && authentication != null && authentication.getPrincipal() instanceof UserAccount) {
            Account account = ((UserAccount)authentication.getPrincipal()).getAccount();
            long count = notificationRepository.countByAccountAndChecked(account, false);
            modelAndView.addObject("hasNotification", count > 0);
        }
    }

    private boolean isRedirectView(ModelAndView modelAndView) {
        return modelAndView.getViewName().startsWith("redirect:") || modelAndView.getView() instanceof RedirectView;
    }
}
