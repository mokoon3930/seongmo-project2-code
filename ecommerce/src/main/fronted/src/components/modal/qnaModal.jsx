// src/components/modal/qnaModal.jsx
import { useState } from "react";
import axios from "axios";
import "../../css/qnaList.css";

function QnaModal({ onClose, locationId, qnaUserId }) {
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [loading, setLoading] = useState(false);
  const [isSecret, setIsSecret] = useState(false); // 체크박스

  const handleSubmit = async () => {
    if (!title.trim()) {
      alert("제목을 입력해 주세요.");
      return;
    }
    if (!content.trim()) {
      alert("내용을 입력해 주세요.");
      return;
    }
    if (!qnaUserId) {
      alert("로그인 정보가 없습니다.");
      return;
    }

    const dto = {
      qnaTitle: title,
      qnaContent: content,
      locationId: locationId,
      qnaUserId: qnaUserId,
      isSecret: isSecret ? "Y" : "N", // 🔥 여기 중요
    };

    try {
      setLoading(true);

      // 👉 이 URL은 스프링 컨트롤러 매핑에 맞게 수정
      await axios.post("/qna/register", dto, { withCredentials: true });

      alert("QnA가 등록되었습니다.");
      onClose(); // 모달 닫기
      // 나중에 여기서 부모에 콜백 넘겨서 QnaList 새로고침도 가능
    } catch (err) {
      console.error("QnA 등록 실패:", err);
      alert("QnA 등록 중 오류가 발생했습니다.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="modal-backdrop">
      <div className="modal-box">
        <h3>QnA 작성</h3>

        <input
          type="text"
          placeholder="제목"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
        />

        <textarea
          placeholder="내용"
          value={content}
          onChange={(e) => setContent(e.target.value)}
        />

        <label className="secret-checkbox">
          <input
            type="checkbox"
            checked={isSecret}
            onChange={(e) => setIsSecret(e.target.checked)}
          />
          비공개로 작성하기
        </label>

        <button onClick={handleSubmit} disabled={loading}>
          {loading ? "등록 중..." : "등록"}
        </button>
        <button onClick={onClose} disabled={loading}>
          닫기
        </button>
      </div>
    </div>
  );
}

export default QnaModal;
