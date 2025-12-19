package com.kh.finalproject.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.finalproject.dao.QuizDao;
import com.kh.finalproject.dao.QuizLogDao;
import com.kh.finalproject.dto.QuizLogDto;
import com.kh.finalproject.error.NeedPermissionException;
import com.kh.finalproject.error.TargetNotfoundException;
import com.kh.finalproject.vo.QuizMyStatsVO;
import com.kh.finalproject.vo.RankVO;

@Service
public class QuizLogService {

    @Autowired private QuizLogDao quizLogDao;
    @Autowired private QuizDao quizDao;
    @Autowired private PointService pointService;
    
    @Transactional
    // 퀴즈 기록 등록
    public int submitQuizSession(List<QuizLogDto> logList, String memberId) {
        if(memberId == null) throw new NeedPermissionException("로그인이 필요합니다.");
        
        int correctCount = 0;
        
        for(QuizLogDto log : logList) {
            log.setQuizLogMemberId(memberId);
            quizLogDao.insert(log);
            quizDao.increaseSolveCount(log.getQuizLogQuizId());
            if("Y".equals(log.getQuizLogIsCorrect())) {
                correctCount++;
            }
        }

        int getPoint = 20; // 정답별 획득 포인트 배율
        int totalEarned = correctCount * getPoint;

        // [수정 포인트] 포인트 지급 및 사유 추가
        if (totalEarned > 0) {
            pointService.addPoint(
                memberId, 
                totalEarned, 
                "GET", 
                "영화 퀴즈 정답 보상 (" + correctCount + "개 정답)"
            );
        }
        
        return correctCount;
    }
    
    // 퀴즈 기록 상세 정보 조회 (오답노트)
    public QuizLogDto quizLogDetail(long quizLogId, String requesterId, String requesterLevel) {
        QuizLogDto log = quizLogDao.selectOne(quizLogId);
        if(log == null) throw new TargetNotfoundException("존재하지 않는 기록입니다.");
        if(requesterId == null) throw new NeedPermissionException("로그인이 필요합니다.");
        
        boolean isOwner = log.getQuizLogMemberId().equals(requesterId);
        boolean isAdmin = "관리자".equals(requesterLevel);
        
        if (!isOwner && !isAdmin) throw new NeedPermissionException();
        return log;
    }
    
    // 마이페이지용 목록 조회
    public List<QuizLogDto> myQuizLogList(String memberId){
        if(memberId == null) throw new NeedPermissionException("로그인이 필요합니다.");
        return quizLogDao.selectListByMember(memberId);
    }
    
    // 내 총 정답수 조회
    public int getMyScore(String memberId) {
        if(memberId == null) throw new NeedPermissionException("로그인이 필요합니다.");
        return quizLogDao.countCorrectAnswer(memberId);
    }
    
    // 통계 조회
    public QuizMyStatsVO getMyStats(int contentsId, String memberId) {
        return quizLogDao.getMyStats(contentsId, memberId);
    }
    
    // 랭킹 조회
    public List<RankVO> getRanking(int contentsId) {
        return quizLogDao.getRanking(contentsId);
    }
    
    // 관리자용 로그 조회
    public List<QuizLogDto> quizLogList(long quizLogQuizId, String memberLevel){
        if(!"관리자".equals(memberLevel)) throw new NeedPermissionException();
        return quizLogDao.selectList(quizLogQuizId);
    }
}




