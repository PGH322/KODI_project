package controller;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import dto.MemberDTO;
import dto.OtpDTO;
import jakarta.servlet.http.HttpSession;
import service.EmailService;
import service.MemberService;

@Controller
@RequestMapping("/api")
public class MemberController {

	/**
	 * POST 이메일 발송(/api/email) DATA: 회원가입할 이메일
	 * POST 이메일 검증(/api/verify) DATA: 회원가입자가 입력한 OTP
	 * POST 회원가입 처리(/api/register) DATA: 회원가입자 이메일, 패스워드, 이름, 국적
	 * POST 로그인 처리(/api/login) DATA: 유저 이메일, 패스워드
	 * POST 로그아웃 처리(/api/logout) DATA: X
	 * 
	 * GET 회원가입 페이지 요청(/api/register)
	 * GET 로그인 페이지 요청(/api/login)
	 */
	@Autowired
	@Qualifier("memberservice")
	private MemberService memberService;

	@Autowired
	@Qualifier("emailservice")
	private EmailService emailService;

	/**
	 * 회원가입 페이지 요청
	 * 
	 * @return
	 */
	@GetMapping("/register")
	public ModelAndView registerMember() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("Register");
		return mv;
	}

	/**
	 * 로그인 페이지 요청
	 * 
	 * @return
	 */
	@GetMapping("/login")
	public ModelAndView loginMember() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("Login");
		return mv;
	}

	/**
	 * 이메일 전송
	 * DATA: 회원가입할 이메일
	 * 
	 * @return
	 */
	@PostMapping("/email")
	@ResponseBody
	public ResponseEntity<String> sendEmail(@RequestBody MemberDTO memberDTO, HttpSession session) {
		MemberDTO existingMember = memberService.findMemberByEmail(memberDTO.getEmail());
		// 중복 유저 확인
		if (existingMember != null) {
			return new ResponseEntity<>("중복된 유저가 있습니다", HttpStatus.BAD_REQUEST);
		}

		// OTP 생성
		Random r = new Random();
		String otp = String.format("%06d", r.nextInt(100000));

		// OTP 비교를 위해 세션에 바운딩
		session.setAttribute("otp", otp);

		// 이메일 발송
		String subject = "Email Verfication";
		String body = "Your verification OPT is " + otp;
		emailService.sendEmail(memberDTO.getEmail(), subject, body);
		return new ResponseEntity<>("인증코드를 발송했습니다, 이메일을 확인해 주세요", HttpStatus.OK);
	}

	/**
	 * 이메일 검증
	 * DATA: 유저가 입력한 OTP번호
	 * 
	 * @return
	 */
	@PostMapping("/verify")
	@ResponseBody
	public ResponseEntity<String> verifyEmail(@RequestBody OtpDTO otpDTO, HttpSession session) {

		// 세션에서 OTP 받아오기
		String sessionOtp = (String) session.getAttribute("otp");

		// 사용자가 입력한 OTP와 세션의 OTP 비교
		if (sessionOtp.equals(otpDTO.getOtp())) {
			session.removeAttribute("otp");
			return new ResponseEntity<>("이메일이 인증되었습니다", HttpStatus.OK);
		}
		return new ResponseEntity<>("OTP 번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
	}

	/**
	 * 회원가입 처리
	 * 
	 * @param memberDTO
	 * @param session
	 * @return
	 */
	@PostMapping("/register")
	public ResponseEntity<String> registerMember(@RequestBody MemberDTO memberDTO, HttpSession session) {
		MemberDTO findedMember = memberService.findMemberByEmail(memberDTO.getEmail());

		// 기존에 멤버가 없을 때 회원등록
		if (findedMember == null) {
			memberService.registerMember(memberDTO);
			return new ResponseEntity<>("회원등록이 완료되었습니다", HttpStatus.OK);
		}
		return new ResponseEntity<>("회원등록을 실패하였습니다", HttpStatus.BAD_REQUEST);
	}

	/**
	 * 로그인 처리
	 * 
	 * @param memberDTO
	 * @param session
	 * @return
	 */
	@PostMapping("/login")
	public ResponseEntity<String> loginMember(@RequestBody MemberDTO memberDTO,
			HttpSession session) {

		// 이메일로 회원여부 확인
		MemberDTO findedMember = memberService.findMemberByEmail(memberDTO.getEmail());
		if (findedMember == null) {
			// 회원이 없는 경우
			return new ResponseEntity<>("회원이 존재하지 않습니다", HttpStatus.BAD_REQUEST);
		}
		// 아이디가 존재하고, 비밀번호가 일치하는 경우
		if (memberDTO.getPw().equals(findedMember.getPw())) {
			// 멤버 아이디를 세션에 바운딩
			Integer memberIdxInteger = findedMember.getMemberIdx();
			String memberIdx = String.valueOf(memberIdxInteger);
			session.setAttribute("memberIdx", memberIdx);
			System.out.println(session.getAttribute("memberIdx"));
			return new ResponseEntity<>("로그인에 성공하였습니다", HttpStatus.OK);
		}
		return new ResponseEntity<>("비밀번호를 확인해 주세요", HttpStatus.BAD_REQUEST);
	}

	/**
	 * 로그아웃 처리
	 * 
	 * @return
	 */
	@PostMapping("/logout")
	public ResponseEntity<String> logoutMember(HttpSession session) {

		// 로그인이 아닐 시
		if (session.getAttribute("memberIdx") == null) {
			return new ResponseEntity<>("로그인된 정보가 없습니다", HttpStatus.BAD_REQUEST);
			// 로그인한 상태일 때
		} else {
			session.removeAttribute("memberIdx");
			return new ResponseEntity<>("로그아웃 되었습니다", HttpStatus.OK);
		}
	}
}
