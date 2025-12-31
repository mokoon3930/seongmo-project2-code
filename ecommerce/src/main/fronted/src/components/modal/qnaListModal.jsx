// src/components/modal/qnaListModal.jsx
import { useEffect, useState } from "react";
import axios from "axios";
import QnaAnswerModal from "./qnaAnswerModal";

function QnaListModal({ onClose, loc }) {
  const [qnaList, setQnaList] = useState([]);
  const [selectedQna, setSelectedQna] = useState(null); // 어떤 QnA를 클릭했는지
  const [isAnswerOpen, setIsAnswerOpen] = useState(false); // 답변 모달 열림 여부
  const [openAnswerId, setOpenAnswerId] = useState(null); // 아코디언 열린 qnaId

  const handleOpenAnswer = (qna) => {
    // ✅ 답변이 이미 있는 경우 → 아코디언 토글
    if (qna.answerContent && qna.answerContent.trim() !== "") {
      setOpenAnswerId((prev) => (prev === qna.qnaId ? null : qna.qnaId));
      return;
    }

    // ✅ 답변이 없는 경우 → 모달 오픈해서 작성
    setSelectedQna(qna);
    setIsAnswerOpen(true);
  };

  // ✅ 모달에서 답변 등록 후 리스트에 반영 + 아코디언 열기
  const handleAnswered = (updatedQna) => {
    setQnaList((prev) =>
      prev.map((q) =>
        q.qnaId === updatedQna.qnaId
          ? { ...q, answerContent: updatedQna.answerContent }
          : q
      )
    );
    setOpenAnswerId(updatedQna.qnaId); // 이 QnA의 답변을 바로 펼친 상태로
  };

  const handleCloseAnswer = () => {
    setIsAnswerOpen(false); // 모달 닫기
    setSelectedQna(null); // 선택 해제 (선택적)
  };

  const locationId = loc.locationId;

  useEffect(() => {
    if (!locationId) return;

    axios
      .get("/qnaSelect", {
        params: { locationId },
      })
      .then((res) => {
        console.log("QnA 모달 조회 결과:", res.data);
        setQnaList(res.data);
      })
      .catch((err) => {
        console.error("QnA 모달 조회 실패:", err);
      });
  }, [locationId]);

  useEffect(() => {
    if (qnaList.length === 0) return;

    const params = new URLSearchParams(window.location.search);
    const qnaIdParam = params.get("qnaId");
    if (!qnaIdParam) return;

    const targetId = Number(qnaIdParam);
    const exists = qnaList.some((q) => q.qnaId === targetId);
    if (!exists) return;

    console.log("🎯 initial qnaId 매칭됨, 자동으로 오픈:", targetId);
    setOpenAnswerId(targetId);

    // 선택한 QnA 카드로 스크롤
    setTimeout(() => {
      const el = document.getElementById(`qna-item-${targetId}`);
      if (el) {
        el.scrollIntoView({ behavior: "smooth", block: "center" });
      }
    }, 100);
  }, [qnaList]);

  const maskUsername = (name) => {
    if (!name) return "";
    if (name.length <= 2) {
      return name[0] + "*".repeat(name.length - 1);
    }
    const visible = name.slice(0, 2);
    const hiddenLength = name.length - 2;
    return visible + "*".repeat(hiddenLength);
  };

  return (
    // 🔥 배경 (검은 반투명)
    <div className="qna-modal-overlay" onClick={onClose}>
      {/* 🔥 모달 박스 (가운데 카드) */}
      <div
        className="qna-modal-container"
        onClick={(e) => e.stopPropagation()} // 안쪽 클릭 시 배경 클릭으로 안 닫히게
      >
        <div className="qna-modal-header">
          <h2>QnA 목록</h2>
          <button className="qna-modal-close-btn" onClick={onClose}>
            ✕
          </button>
        </div>

        {qnaList.length === 0 ? (
          <div className="qna-empty">등록된 QnA가 없습니다.</div>
        ) : (
          <div className="qna-modal-body">
            {qnaList.map((qna) => {
              const isSecret = qna.isSecret === "Y";

              const visibleTitle = qna.qnaTitle;

              const visibleContent = qna.qnaContent;

              const visibleUserName = maskUsername(qna.userName);

              const hasAnswer =
                qna.answerContent && qna.answerContent.trim() !== "";

              const status = qna.status;

              const isOpen = openAnswerId === qna.qnaId;

              return (
                <div
                  key={qna.qnaId}
                  className={`qna-item ${isSecret ? "secret" : ""}`} // 🔴 비밀글이면 secret 클래스 추가
                  onClick={() => handleOpenAnswer(qna)}
                >
                  <div className="qna-title-row">
                    <span className="qna-title-text">{visibleTitle}</span>

                    {/* 🔒 비밀글 배지 정도만 표시 */}
                    {isSecret && (
                      <span className="qna-secret-badge">비밀글</span>
                    )}

                    {/* ✅ 답변완료 배지 (답변이 있을 때만) */}
                    {status === "DONE" && (
                      <span className="qna-answer-badge">답변완료</span>
                    )}

                    {status === "WAIT" && (
                      <span className="qna-answer-wait-badge">답변대기중</span>
                    )}
                  </div>

                  <div className="qna-content">{visibleContent}</div>

                  <div className="qna-meta">
                    <span className="qna-writer">{visibleUserName}</span>
                    <span className="qna-date">{qna.qnaCreatedAt}</span>
                  </div>

                  {hasAnswer && isOpen && (
                    <div className="qna-answer-accordion">
                      <div className="qna-answer-accordion-label">
                        호스트 답변
                      </div>
                      <div className="qna-answer-accordion-text">
                        {qna.answerContent}
                      </div>
                      <span className="qna-date">{qna.answerCreatedAt}</span>
                    </div>
                  )}
                </div>
              );
            })}
          </div>
        )}
      </div>
      {isAnswerOpen && (
        <QnaAnswerModal
          qna={selectedQna} // 어떤 질문인지 전달 (qnaId, title, content 등)
          onClose={handleCloseAnswer} // 닫기 버튼에서 호출할 함수
          onAnswered={handleAnswered}
        />
      )}
    </div>
  );
}

export default QnaListModal;
