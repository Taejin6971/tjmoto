package com.mysite.tjmoto.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.mysite.tjmoto.member.MemberService;

@EnableMethodSecurity
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	MemberService memberService;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {


		http
			.authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
					.requestMatchers(new AntPathRequestMatcher("/**")).permitAll()
					.requestMatchers(new AntPathRequestMatcher("/css/**\", \"/js/**\", \"/img/**")).permitAll()
					.requestMatchers(new AntPathRequestMatcher("\"/members/**\", \"/item/**\", \"/images/**\""))
					.permitAll().requestMatchers(new AntPathRequestMatcher("/admin/**")).hasRole("ADMIN"))

			.csrf((csrf) -> csrf
					.ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**")))

			.headers((headers) -> headers.addHeaderWriter(
					new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)))

			.formLogin((formLogin) -> formLogin
					.loginPage("/members/login")

					.usernameParameter("email")
					
					.failureUrl("/members/login/error")

					.defaultSuccessUrl("/"))

			.logout((logout) -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
					.logoutSuccessUrl("/").invalidateHttpSession(true))

		;

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}