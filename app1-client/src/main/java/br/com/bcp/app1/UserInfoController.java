package br.com.bcp.app1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;

@Controller
public class UserInfoController {

	@Value("${resourceserver.api.url.userinfo}")
    private String apiUrl;

    @Autowired
    private WebClient webClient;

	@GetMapping("/user")
	public String displayHomePage(Model model, @AuthenticationPrincipal OAuth2User principal) {
		
		if (principal != null) {
			String name = principal.getAttribute("preferred_username");
			model.addAttribute("name",name);
		}

		String result = this.webClient.get()
			.uri(apiUrl)
			.retrieve()
			.bodyToMono(String.class)
			.block();
		model.addAttribute("result", result);
		
		return "user";
		
	}
	
	
}
