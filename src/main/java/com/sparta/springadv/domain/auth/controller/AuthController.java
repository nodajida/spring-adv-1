package com.sparta.springadv.domain.auth.controller;

import com.sparta.springadv.domain.auth.dto.SigninRequest;
import com.sparta.springadv.domain.auth.dto.SigninResponse;
import com.sparta.springadv.domain.auth.dto.SignupRequest;
import com.sparta.springadv.domain.auth.dto.SignupResponse;
import com.sparta.springadv.domain.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    //회원가입: signup
    //회원가입은 새로운 사용자가 프로그램에 등록하는 과정이에요. 이 부분에서 사용자는 이름, 이메일, 비밀번호 같은 정보를 보내면,
    // 프로그램은 그 정보를 받아서 회원으로 등록해요.

    //@PostMapping("/auth/signup"): /auth/signup이라는 주소로 회원가입 요청을 보낼 수 있어요. 프로그램은 이 주소로 받은 데이터를 처리해요.
    //@RequestBody SignupRequest signupRequest: 사용자가 보내는 회원가입 정보(이름, 이메일, 비밀번호 등)를 signupRequest라는 곳에 담아줘요.
    //authService.signup(signupRequest): 프로그램은 authService라는 곳에서 회원가입을 처리해요. signupRequest에 담긴 정보를
    // 사용해서 새로운 사용자를 등록하고, 결과를 돌려줘요.
    @PostMapping("/auth/signup")
    public SignupResponse signup(@Valid @RequestBody SignupRequest signupRequest) {
        return authService.signup(signupRequest);
    }
    //로그인: signin
    //로그인은 이미 회원가입한 사용자가 이메일과 비밀번호를 입력해서 프로그램에 들어가는 과정이에요.

    //@PostMapping("/auth/signin"): /auth/signin이라는 주소로 로그인 요청을 보내요. 프로그램은 이 주소로 로그인 데이터를 받아요.
    //@RequestBody SigninRequest signinRequest: 사용자가 보내는 로그인 정보(이메일과 비밀번호)를 signinRequest라는 곳에 담아요.
    //authService.signin(signinRequest): 프로그램은 authService라는 곳에서 로그인 처리를 해요. 로그인에 성공하면 사용자가 프로그램에
    // 들어갈 수 있게 해줘요.
    @PostMapping("/auth/signin")
    public SigninResponse signin(@Valid @RequestBody SigninRequest signinRequest) {
        return authService.signin(signinRequest);
    }
}
//요약하면
//회원가입: 사용자가 이름, 이메일, 비밀번호를 보내면, 프로그램이 그 사람을 새로운 회원으로 등록해요.
//로그인: 이미 가입한 사용자가 이메일과 비밀번호를 보내면, 프로그램은 로그인을 시켜줘요.
//이 코드는 사용자들이 회원으로 가입하고 로그인할 수 있게 도와주는 역할을 해요!