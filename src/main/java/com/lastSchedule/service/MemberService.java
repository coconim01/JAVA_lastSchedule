package com.lastSchedule.service;

import com.lastSchedule.dto.MemberSearchDto;
import com.lastSchedule.entity.Member;
import com.lastSchedule.repository.GroupRepository;
import com.lastSchedule.repository.MemberRepository;
import com.lastSchedule.repository.SchoolMapperInterface;
import com.lastSchedule.repository.SchoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.Optional;


@Service // for 비즈니스 로직 담당자
@Transactional
@RequiredArgsConstructor // final이나 @NotNull이 붙어 있는 변수에 생성자를 자동으로 만들어 줍니다.
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository ;
    private final SchoolMapperInterface schoolMapperInterface;
    private final GroupRepository groupRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email) ;
        if(member == null){ // 회원이 존재하지 않는 경우
            throw new UsernameNotFoundException(email) ;
        }
        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getId().toString())
                .build();
    }

    public Member saveMember(Member member){
        validateDuplicateMember(member) ;
        return memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByEmail(member.getEmail()) ;
        if(findMember != null){
            throw new IllegalStateException("이미 가입된 회원입니다.") ;
        }
    }

    public Page<Member> getAdminMemberPage(MemberSearchDto searchDto, Pageable pageable) {
        System.out.println("err GetAdminMemberPage ");

        return memberRepository.getAdminMemberPage(searchDto, pageable);
    }

    public List<String> searchSchoolName(String name) {
        return schoolMapperInterface.searchBySchoolName(name);
    }

    public List<Member> getAllMembers() {

        return memberRepository.findAll();
    }

    public Member getMemberById(Long memberId){
        return memberRepository.findById(memberId)
                .orElseThrow(EntityExistsException::new);
    }

    public Long getMemberIdByEmail(String email) {
        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            throw new IllegalStateException("존재하지 않는 이메일입니다");
        }
        return member.getId();
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public List<Member> findBySchoolCode(String schoolCode) {
        return memberRepository.findBySchoolCode(schoolCode);
    }

    public String getMemberNameByEmail(String email) {
        Member member = memberRepository.findByEmail(email);
        if(member != null){
            return member.getName();
        }
        return null;
    }
}
