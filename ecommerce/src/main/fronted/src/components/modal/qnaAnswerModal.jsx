import { useState } from "react";
import axios from "axios";

function QnaAnswerModal({ qna, onClose, onAnswered }) {
  const [answerContent, setAnswerContent] = useState("");

  if (!qna) return null;

  const answerPush = async () => {
    // 빈값 체크
    if (!answerContent.trim()) {
      alert("답변 내용을 입력해 주세요.");
      return;
    }

    try {
      const sendLoad = {
        qnaId: qna.qnaId,
        answerContent: answerContent,
      };

      const res = await axios.post("/qnaAnswerInsert", sendLoad);
      console.log("답변 등록 성공:", res.data);

      alert("답변이 등록되었습니다.");

      // ✅ 부모에게 "이 QnA에 이런 답변이 생겼다" 알려주기
      if (onAnswered) {
        onAnswered({
          ...qna,
          answerContent: answerContent,
        });
      }

      onClose(); // 모달 닫기
      // 필요하면 여기서 부모에 "다시 조회해줘" 콜백을 넣어도 됨
    } catch (err) {
      console.error("답변 등록 실패:", err);
      alert("답변 등록 중 오류가 발생했습니다.");
    }
  };

  return (
    <div className="qna-answer-overlay" onClick={onClose}>
      <div
        className="qna-answer-container"
        onClick={(e) => e.stopPropagation()}
      >
        <button className="qna-answer-close" onClick={onClose}>
          ✕
        </button>

        <h3>{qna.qnaTitle}</h3>
        <p>{qna.qnaContent}</p>

        {/* 여기 밑에 답변 작성 폼 추가할 예정이면 이 안에 만들면 됨 */}
        <div className="qna-answer-form">
          <textarea
            className="qna-answer-input"
            placeholder="답변 입력"
            value={answerContent}
            onChange={(e) => setAnswerContent(e.target.value)}
          />
          <div className="submit-box">
            <button
              type="button"
              className="qna-answer-submit"
              onClick={answerPush}
            >
              답변 등록
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}

export default QnaAnswerModal;
