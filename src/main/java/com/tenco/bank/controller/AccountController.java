package com.tenco.bank.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tenco.bank.dto.SaveDTO;
import com.tenco.bank.handler.exception.DataDeliveryException;
import com.tenco.bank.handler.exception.UnAuthorizedException;
import com.tenco.bank.repository.model.Account;
import com.tenco.bank.repository.model.User;
import com.tenco.bank.service.AccountService;

import jakarta.servlet.http.HttpSession;

@Controller // IoC 대상 (싱글톤으로 관리)
@RequestMapping("/account")
public class AccountController {

	// 계좌 생성 화면 요청  - DI 처리 (final -> 초기화 해줘야 됨 // final를 써주는게 좀 더 성능이 빠름)
	private final HttpSession session;
	private final AccountService accountService;
	
	//@Autowired
	public AccountController(AccountService accountservice,  HttpSession session) {
		this.session = session;
		this.accountService = accountservice;
	}
	
	/**
	 * 계좌 생성 페이지 요청
	 * 주소 설계: http://localhost:8080/account/save
	 * @return
	 */
	
	@GetMapping("/save")
	public String savePage() {
		
		// 1. 인증 검사가 필요(account 전체 필요함)
		User principal = (User) session.getAttribute("principal");
		if(principal == null) {
			throw new UnAuthorizedException("인증된 사용자가 아닙니다.", HttpStatus.UNAUTHORIZED);
		} 
		
		return "account/save";
	}
	
	/**
	 * 계좌 생성 기능 요청
	 * 주소 설계: http://localhost:8080/account/save
	 * @return : 추후 계좌 목록 페이지 이동 처리
	 */
	
	@PostMapping("/save")
	public String saveProc(SaveDTO dto) {
		// 1. form 데이터 추출 (파싱 전략)
		// 2. 인증 검사
		// 3. 유효성 검사
		// 4. 서비스 호출 (Service한테 위임 controller는 그냥 넘겨주기만 하면 됨)
		User principal = (User) session.getAttribute("principal"); // user 정보가 다 들어가 있음
		if(principal == null) {
			throw new UnAuthorizedException("인증된 사용자가 아닙니다.", HttpStatus.UNAUTHORIZED);
		}
		
		if(dto.getNumber() == null || dto.getNumber().isEmpty()) {
			throw new DataDeliveryException("계좌 번호를 입력하세요.", HttpStatus.BAD_REQUEST);
		}
		if(dto.getPassword() == null || dto.getPassword().isEmpty()) {
			throw new DataDeliveryException("계좌 비밀번호를 입력하세요.", HttpStatus.BAD_REQUEST);
		}
		// Balance은 long 타입이라 isEmpty가 없음
		if(dto.getBalance() == null || dto.getBalance() <= 0) {
			throw new DataDeliveryException("계좌 잔액을 입력하세요.", HttpStatus.BAD_REQUEST);
		}
		
		accountService.createAccount(dto, principal.getId());
		
		return "redirect:/index";
	}
	
	/**
	 * 계좌 목록 화면 요청
	 * 주소 설계: http://localhost:8080/account/list, .../
	 * @return list.jsp
	 */
	@GetMapping({"/list", "/"})
	public String listPage(Model model) {
		// 1. 인증검사
		User principal = (User) session.getAttribute("principal");
		if(principal == null) {
			throw new UnAuthorizedException("인증된 사용자가 아닙니다.", HttpStatus.UNAUTHORIZED);
		}
		
		// 2. 유효성 검사
		// 3. 서비스 호출
		List<Account> accountList = accountService.readAccountListByUserId(principal.getId());
		if(accountList.isEmpty()) {
			model.addAttribute("accountList", null);
		} else {
			model.addAttribute("accountList", accountList);
		}
		
		
		// JSP 데이터를 넣어 주는 방법 
		return "account/list";
	}
	

	
}
