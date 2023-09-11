package com.mysite.tjmoto.member;

import java.security.Principal;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;
	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;

	@GetMapping(value = "/new")
	public String memberForm(Model model) {
		model.addAttribute("memberFormDto", new MemberFormDto());
		return "member/memberForm";
	}

	// 회원가입
	@PostMapping(value = "/new")
	public String newMember(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model) {

		if (bindingResult.hasErrors()) {
			return "member/memberForm";
		}

		if (!memberFormDto.getPassword().equals(memberFormDto.getPassword2())) {
			bindingResult.rejectValue("password2", "passwordInCorrect", "2개의 패스워드가 일치하지 않습니다.");
			return "member/memberForm";
		}

		try {
			if (memberFormDto.getEmail().equals("admin@aaa.com")) {
				Member member = Member.createMember(memberFormDto, passwordEncoder);
				memberService.saveMember(member);
			} else {
				Member member = Member.createMember1(memberFormDto, passwordEncoder);
				memberService.saveMember(member);
			}
		} catch (IllegalStateException e) {
			model.addAttribute("errorMessage", e.getMessage());
			return "member/memberForm";
		}

		return "redirect:/";
	}

	@GetMapping(value = "/login")
	public String loginMember() {
		return "/member/memberLoginForm";
	}

	@GetMapping(value = "/login/error")
	public String loginError(Model model) {
		model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
		return "/member/memberLoginForm";
	}

	@GetMapping("/membermodify")
	public String modifyFrom(MemberModifyDto memberModifyDto, Principal principal) {

		String name = principal.getName();
		Member member = memberRepository.findByEmail(name);

		memberModifyDto.setId(member.getId());
		memberModifyDto.setName(member.getName());
		memberModifyDto.setEmail(member.getEmail());
		memberModifyDto.setPassword(member.getPassword());
		memberModifyDto.setAddress(member.getAddress());

		return "/member/membermodify";
	}

	// 회원수정
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/membermodify")
	public String modifyFrom(@Valid MemberModifyDto memberModifyDto, BindingResult bindingResult, Principal principal,
			String email, Model model) {

		Member member = memberRepository.findByEmail(email);
		String pass = member.getPassword();

		if (!passwordEncoder.matches(memberModifyDto.getPassword(), pass)) {

			model.addAttribute("errorMessage", "비밀번호가 일치하지 않습니다. ");

			return "/member/membermodify";
		}

		if (bindingResult.hasErrors()) {
			return "/member/membermodify";
		}

		memberService.modify(member, memberModifyDto, passwordEncoder);

		return "redirect:/";
	}

	// 회원탈퇴
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{id}")
	public String memberDelete(Principal principal, @PathVariable("id") Long id) {
		Member member = memberService.getMember(id);
		memberService.delete(member);
		return "redirect:/";
	}

}