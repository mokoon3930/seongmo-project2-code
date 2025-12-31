// src/components/NotificationModal.jsx
import styles from "../../css/notiModal.module.css";

function NotiModal({
  isOpen,
  notifications,
  onClose,
  onClickNotification,
  allDelete,
  onDeleteNotification,
  modalPosition,
}) {
  if (!isOpen) return null; // 안 열려 있으면 아무것도 렌더링 안 함

  const handleBackdropClick = () => {
    onClose();
  };

  const stopClose = (e) => {
    e.stopPropagation();
  };

  const handleDeleteClick = (e, notiId) => {
    // li의 onClick (onClickNotification) 안 타게 막기
    e.stopPropagation();

    // 부모로 삭제 요청 올려보내기
    onDeleteNotification(notiId);
  };

  return (
    <div className={styles.notiModalBackdrop} onClick={handleBackdropClick}>
      <div
        className={styles.notiModal}
        onClick={stopClose}
        style={{ top: modalPosition.top + 15, left: modalPosition.left - 160 }}
      >
        <div className={styles.notiModalHeader}>
          <span>알림</span>
          <button onClick={allDelete}>일괄 삭제</button>
          <button className={styles.notiCloseBtn} onClick={onClose}>
            ✕
          </button>
        </div>

        <div className={styles.notiModalBody}>
          {notifications.length === 0 ? (
            <div className={styles.notiEmpty}>알림이 없습니다.</div>
          ) : (
            <ul className={styles.notiList}>
              {notifications.map((n) => (
                <li
                  key={n.notiId}
                  className={`${styles.notiItem} ${
                    n.isRead === "N" ? styles.unread : ""
                  }`}
                  onClick={() => onClickNotification(n)}
                >
                  <div>
                    {n.isRead === "N" && (
                      <span
                        className={styles.notiNewTag}
                        style={{ color: "red" }}
                      >
                        new
                      </span>
                    )}
                    <div className={styles.notiMessage}>{n.message}</div>
                  </div>

                  <div>
                    <button
                      type="button"
                      onClick={(e) => handleDeleteClick(e, n.notiId)}
                      className={styles.notiDeleteButton}
                    >
                      x
                    </button>
                  </div>
                </li>
              ))}
            </ul>
          )}
        </div>
      </div>
    </div>
  );
}

export default NotiModal;
