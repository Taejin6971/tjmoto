package com.mysite.tjmoto.member;

import java.util.Optional;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

	private final MemberRepository memberRepository;

	public Member saveMember(Member member) {
		validateDuplicateMember(member);
		return memberRepository.save(member);
	}

	private void validateDuplicateMember(Member member) {
		Member findMember = memberRepository.findByEmail(member.getEmail());
		if (findMember != null) {
			throw new IllegalStateException("이미 가입된 회원입니다.");
		}
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		Member member = memberRepository.findByEmail(email);

		if (member == null) {
			throw new UsernameNotFoundException(email);
		}

        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
				.build();
	}

	public void modify(Member member, MemberModifyDto memberModifyDto, PasswordEncoder passwordEncoder) {

		member.setName(memberModifyDto.getName());
		member.setEmail(memberModifyDto.getEmail());
		String passowrd = passwordEncoder.encode(memberModifyDto.getPassword());
		member.setPassword(passowrd);
		member.setAddress(memberModifyDto.getAddress());

		this.memberRepository.save(member);
	}

	public Member getMember(Long id) {
		Optional<Member> member = memberRepository.findById(id);

		if (member.isPresent()) {
			return member.get();
		} else {
			return null;
		}
	}

	public void delete(Member member) {
		memberRepository.delete(member);
	}

}