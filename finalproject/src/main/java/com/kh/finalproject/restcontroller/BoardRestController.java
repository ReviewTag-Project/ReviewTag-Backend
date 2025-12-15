package com.kh.finalproject.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.finalproject.dao.BoardDao;
import com.kh.finalproject.dto.BoardDto;

@CrossOrigin
@RestController
@RequestMapping("/board")
public class BoardRestController {

	@Autowired
	private BoardDao boardDao;
	
	// 게시글 등록
	@PostMapping("/")
	public void insert(
			@RequestBody BoardDto boardDto
			) {
		boardDao.insert(boardDto);
	}
	
	//전체 조회
	@GetMapping("/")
	public List<BoardDto> selectList(){
		return boardDao.selectList();
	}
		
	//상세 조회
	@GetMapping("/{boardNo}")
	public BoardDto selectOne(@PathVariable int boardNo) {
		return boardDao.selectOne(boardNo);
	}
	
	// 컨텐츠별 조회
	@GetMapping("/contentsId/{contentsId}")
	public List<BoardDto> selesctListByContents(@PathVariable long contentsId){
		return boardDao.selesctListByContents(contentsId);
	}
	// 컨텐츠별 5개 조회
	@GetMapping("/contentsId/{contentsId}/5")
	public List<BoardDto> selesctListBy5Contents(@PathVariable long contentsId){
		return boardDao.selesctListBy5Contents(contentsId);
	}
	
	
	// 게시글 삭제
	@DeleteMapping("/{boardNo}")
	public void delete(@PathVariable int boardNo) {
		boardDao.delete(boardNo);
	}
	
}
