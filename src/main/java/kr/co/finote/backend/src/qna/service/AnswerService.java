package kr.co.finote.backend.src.qna.service;

import java.util.List;
import java.util.stream.Collectors;
import kr.co.finote.backend.src.qna.domain.Answer;
import kr.co.finote.backend.src.qna.domain.Question;
import kr.co.finote.backend.src.qna.dto.request.PostAnswerRequest;
import kr.co.finote.backend.src.qna.dto.response.AnswerListResponse;
import kr.co.finote.backend.src.qna.dto.response.AnswerResponse;
import kr.co.finote.backend.src.qna.dto.response.PostAnswerResponse;
import kr.co.finote.backend.src.qna.repository.AnswerRepository;
import kr.co.finote.backend.src.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final QuestionService questionService;
    private final AnswerRepository answerRepository;

    public PostAnswerResponse postAnswer(
            User writer, String authorNickname, String title, PostAnswerRequest request) {
        Question question = questionService.findByNicknameAndTitle(authorNickname, title);
        Answer answer = Answer.createAnswer(writer, question, request);

        Answer savedAnswer = answerRepository.save(answer);
        return PostAnswerResponse.of(savedAnswer);
    }

    public AnswerListResponse getAnswers(String nickname, String title) {
        Question question = questionService.findByNicknameAndTitle(nickname, title);

        List<Answer> answers = answerRepository.findAllWithQuestion(question);
        List<AnswerResponse> answerReponseList = toAnswerReponseList(answers);

        return AnswerListResponse.of(answerReponseList);
    }

    private List<AnswerResponse> toAnswerReponseList(List<Answer> answerList) {
        return answerList.stream().map(AnswerResponse::of).collect(Collectors.toList());
    }
}
