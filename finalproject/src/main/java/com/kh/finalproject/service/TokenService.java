package com.kh.finalproject.service;

import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.finalproject.configuration.JwtProperties;
import com.kh.finalproject.dao.MemberTokenDao;
import com.kh.finalproject.dto.MemberDto;
import com.kh.finalproject.vo.TokenVO;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class TokenService {

	@Autowired
	private JwtProperties  jwtProperties;
	@Autowired
	private MemberTokenDao memberTokenDao;
	
	// AccessToken 생성
	public String generateAccessToken(MemberDto memberDto) {
		String keyStr = jwtProperties.getKeyStr();
		SecretKey key = Keys.hmacShaKeyFor(keyStr.getBytes(StandardCharsets.UTF_8));
	
		// 만료시간 설정
		Calendar calendar = Calendar.getInstance();
		Date now = calendar.getTime(); // 현재시각
		calendar.add(Calendar.MINUTE, jwtProperties.getExpiration());
		Date expire = calendar.getTime(); // 만료시각
		
		//Jwt 토큰 생성
		return Jwts.builder()
				.signWith(key)
				.expiration(expire)
				.issuedAt(now)
				.issuer(jwtProperties.getIssuer()) // 발행자
				.claim("loginId", memberDto.getMemberId())
				.claim("loginLevel", memberDto.getMemberLevel())
				//.claim("loginPoint", memberDto.getMemberPoint())
			.compact();
	}
	public String generateAccessToken(TokenVO tokenVO) {
		return generateAccessToken(MemberDto.builder()
				.memberId(tokenVO.getLoginId())
				.memberLevel(tokenVO.getLoginLevel())
				.build());
	}
	
	
	
	// RefreshToken 생성
	
	
	// Parse
	
	
	// 토큰 만료까지 남은시간을 구하는 기능
	
	
	// checkRefresh Token
	
	
}
