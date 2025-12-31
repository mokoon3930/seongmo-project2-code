import { useEffect, useState } from "react";
import axios from "axios";
import "../../css/qna.css";
import Loading from "../commons/spinner";
import styles from "../../css/myPage.module.css";

const ITEMS_PER_PAGE = 8;

export function MyWriteQnA({ focusQnaId }) {
  const [loading, setLoading] = useState(true);
  const [qnaList, setQnaList] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [openQnaId, setOpenQnaId] = useState(null);

  // const maskUsername = (name) => {
  //   if (!name) return "";
  //   if (name.length <= 2) {
  //     return name[0] + "*".repeat(name.length - 1);
  //   }
  //   const visible = name.slice(0, 2);
  //   const hiddenLength = name.length - 2;
  //   return visible + "*".repeat(hiddenLength);
  // };

  // ✅ 내 QnA 목록 불러오기
  useEffect(() => {
    console.log("🔁 MyWriteQnA 마운트, focusQnaId =", focusQnaId);

    axios
      .get("/api/my-qna-list", { withCredentials: true })
      .then((res) => {
        console.log("📥 내 QnA 조회 결과:", res.data);
        setLoading(false);
        setQnaList(res.data);
      })
      .catch((err) => {
        setLoading(false);
        console.error("❌ 내 QnA 조회 실패:", err);
      });
  }, [focusQnaId]); // 굳이 안 넣어도 되지만 디버깅용으로 넣어도 OK

  const totalItems = qnaList.length;
  const totalPages = Math.ceil(totalItems / ITEMS_PER_PAGE);

  const indexOfLastItem = currentPage * ITEMS_PER_PAGE;
  const indexOfFirstItem = indexOfLastItem - ITEMS_PER_PAGE;
  const currentQnaList = qnaList.slice(indexOfFirstItem, indexOfLastItem);

  const paginate = (pageNumber) => {
    if (pageNumber >= 1 && pageNumber <= totalPages) {
      setCurrentPage(pageNumber);
      setOpenQnaId(null);
    }
  };

  const toggleQna = (qnaId) => {
    setOpenQnaId((prev) => (prev === qnaId ? null : qnaId));
  };

  // 🔥 핵심: 특정 qnaId(focusQnaId)를 자동으로 찾아서 페이지/열림 세팅
  useEffect(() => {
    console.log("👀 focus useEffect 실행", {
      focusQnaId,
      qnaLength: qnaList.length,
    });

    if (!focusQnaId) {
      console.log("➡ focusQnaId 없음, 자동 포커스 스킵");
      return; // 쿼리에 qnaId가 없으면 패스
    }
    if (qnaList.length === 0) {
      console.log("➡ qnaList 아직 비어있음, 대기");
      return; // 아직 로딩 전이면 패스
    }

    const targetIndex = qnaList.findIndex(
      (qna) => Number(qna.qnaId) === Number(focusQnaId)
    );

    console.log("🎯 찾은 targetIndex =", targetIndex);

    if (targetIndex === -1) {
      console.log("⚠ 해당 qnaId를 qnaList에서 찾지 못함");
      return;
    }

    const targetPage = Math.floor(targetIndex / ITEMS_PER_PAGE) + 1;

    console.log("👉 targetPage =", targetPage);

    setCurrentPage(targetPage);
    setOpenQnaId(String(focusQnaId)); // 해당 QnA를 열린 상태로
  }, [focusQnaId, qnaList]);

  if (loading)
    return (
      <div>
        <Loading />
      </div>
    );

  return (
    <div className={styles.reviewContainer}>
      <h2 className={styles.contentContainerH2}>내 QnA 목록</h2>

      {totalItems === 0 ? (
        // 🔹 QnA가 하나도 없을 때
        <div className="qna-empty">현재 작성하신 QnA가 없습니다.</div>
      ) : (
        // 🔹 QnA가 1개 이상 있을 때
        <>
          {currentQnaList.map((qna) => {
            const qnaKey = `${qna.qnaId}`;
            const isSecret = qna.isSecret === "Y";

            const canView = true;

            const visibleTitle = canView ? qna.qnaTitle : "🔒 비공개 글입니다.";
            const visibleContent = canView
              ? qna.qnaContent
              : "작성자와 호스트만 내용을 볼 수 있습니다.";

            const visibleUserName = qna.userName;

            const hasAnswer =
              qna.answerContent && qna.answerContent.trim() !== "";

            const isOpen = openQnaId === qnaKey;
            const isAnswered = qna.status; // "WAIT" / "DONE"

            return (
              <div
                key={qna.qnaId}
                className={`qna-item ${isSecret ? "secret" : ""} ${
                  isOpen ? "open" : ""
                }`}
              >
                <div
                  className="qna-title-row"
                  onClick={() => toggleQna(qnaKey)}
                >
                  <span className="qna-title-text">{visibleTitle}</span>
                  <span className="qna-title-text">
                    {"장소 : " + qna.locationName}
                  </span>
                  {isSecret ? (
                    <span className="qna-secret-badge">비공개</span>
                  ) : (
                    <span className="qna-none-badge"></span>
                  )}
                  {isAnswered === "DONE" && (
                    <span className="qna-answer-badge">답변완료</span>
                  )}
                  {isAnswered === "WAIT" && (
                    <span className="qna-answer-wait-badge">답변대기중</span>
                  )}
                </div>

                {isOpen && (
                  <>
                    <div className="qna-content">{visibleContent}</div>

                    <div className="qna-meta">
                      <span className="qna-writer">{visibleUserName}</span>
                      <span className="qna-date">{qna.qnaCreatedAt}</span>
                    </div>
                  </>
                )}

                {hasAnswer && isOpen && canView && (
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

          {totalPages > 1 && (
            <div className="qna-pagination">
              <button
                onClick={() => paginate(currentPage - 1)}
                disabled={currentPage === 1}
                className="qna-page-btn"
              >
                이전
              </button>

              {Array.from({ length: totalPages }, (_, idx) => idx + 1).map(
                (page) => (
                  <button
                    key={page}
                    onClick={() => paginate(page)}
                    className={
                      page === currentPage
                        ? "qna-page-btn active"
                        : "qna-page-btn"
                    }
                  >
                    {page}
                  </button>
                )
              )}

              <button
                onClick={() => paginate(currentPage + 1)}
                disabled={currentPage === totalPages}
                className="qna-page-btn"
              >
                다음
              </button>
            </div>
          )}
        </>
      )}
    </div>
  );
}

export default MyWriteQnA;
