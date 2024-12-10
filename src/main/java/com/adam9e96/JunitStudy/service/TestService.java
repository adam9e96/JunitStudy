package com.adam9e96.JunitStudy.service;

import com.adam9e96.JunitStudy.entity.Member;
import com.adam9e96.JunitStudy.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestService {

    @Autowired
    MemberRepository memberRepository;


    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }
}
