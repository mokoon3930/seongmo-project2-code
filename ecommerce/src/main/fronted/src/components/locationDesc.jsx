import { useEffect, useRef, useState } from "react";
import "../css/locationDesc.css";
// import "../../css/qna.css"; // 🔥 (중요) 페이징 및 후기 CSS가 들어있는 파일을 꼭 import 하게!
import { useUser } from "../context/userContext";
import Swal from "sweetalert2";
import { useNavigate } from "react-router-dom";
import Minimap from "./commons/minimap";
import LocationDescPhoto from "./locationDescPhoto";
import { useScrollContainer } from "../context/scrollContainerContext";
import QnaList from "./commons/qnaList";

import FavoritesIcon from "./commons/favoritesIcon";
import QnaModal from "./modal/qnaModal";
import axios from "axios";

// ✅ 페이지당 후기 개수 설정 (3개씩)
const REVIEWS_PER_PAGE = 3;

export default function LocationDesc(props) {
  const navigate = useNavigate();
  const { user } = useUser();
  const scrollContainerRef = useScrollContainer();
  const previewRef = useRef(null); // location_preview를 참조할 ref
  const { setIsScrolledPastPreviewState } = props;

  // --- 상태 관리 (State) ---
  const [isQnaModalOpen, setIsQnaModalOpen] = useState(false);
  const [reviews, setReviews] = useState([]);
  const [reviewPage, setReviewPage] = useState(1); // 후기 현재 페이지 번호

  // --- 모달 핸들러 ---
  const openQnaModal = () => setIsQnaModalOpen(true);
  const qnaModalClose = () => setIsQnaModalOpen(false);

  // 1. 페이지 진입 시 스크롤 맨 위로
  useEffect(() => {
    if (scrollContainerRef.current) {
      scrollContainerRef.current.scrollTop = 0;
    }
  }, [scrollContainerRef]);

  // 2. 리뷰 데이터 가져오기 (장소 ID가 있을 때만)
  useEffect(() => {
    if (props.loc?.locationId) {
      fetchLocationReviews();
    }
  }, [props.loc?.locationId]);

  const fetchLocationReviews = async () => {
    try {
      const response = await axios.get(
        `/api/review/location?locationId=${props.loc.locationId}`
      );
      setReviews(response.data);
      setReviewPage(1); // 데이터 바뀌면 1페이지로 초기화
    } catch (error) {
      console.error("리뷰 조회 실패:", error);
    }
  };

  // 3. 스크롤 감지 (Intersection Observer) - 헤더 Sticky 처리를 위함
  useEffect(() => {
    const target = previewRef.current;
    const scrollElement = scrollContainerRef.current;

    if (!target || !scrollElement) return;

    const options = {
      root: scrollElement,
      rootMargin: "0px",
      threshold: 0.0,
    };

    const observer = new IntersectionObserver(([entry]) => {
      // preview 영역이 화면 위로 사라졌는지 체크
      const isPast = !entry.isIntersecting && entry.boundingClientRect.top < 0;
      setIsScrolledPastPreviewState(isPast);
    }, options);

    observer.observe(target);

    return () => observer.unobserve(target);
  }, [setIsScrolledPastPreviewState, scrollContainerRef]);

  // 로딩 중일 때 방어
  if (!props.loc) {
    return <div>장소 정보를 불러오는 중입니다...</div>;
  }

  // 편의시설 데이터 가공
  const convenience = [
    { item: "주차시설", possible: props.loc.parking },
    { item: "급수설비", possible: props.loc.waterworks },
    { item: "조리시설", possible: props.loc.cookingAble },
    { item: "음향시설", possible: props.loc.audioAble },
    { item: "조명시설", possible: props.loc.lightingAble },
    { item: "에어컨", possible: props.loc.airConditioner },
    { item: "와이파이", possible: props.loc.wifi },
    { item: "엘리베이터", possible: props.loc.elevator },
    { item: "화장실", possible: props.loc.toilet },
    { item: "외벽광고", possible: props.loc.adAble },
  ];

  // 4. 예약하기 버튼 핸들러 (로그인 체크)
  const onClickMoveReservation = () => {
    if (!user) {
      Swal.fire({
        title: "로그인이 필요합니다.",
        text: "로그인하러 가시겠습니까?",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "네",
        cancelButtonText: "아니요",
      }).then((result) => {
        if (result.isConfirmed) {
          navigate("/account/login", {
            state: {
              from: `/reservation/${props.loc.rrId}`,
              locationData: props.loc,
            },
          });
        }
      });
      return;
    }
    navigate(`/reservation/${props.loc.rrId}`, { state: props.loc });
  };

  // 5. 게시글 차단하기 (관리자 전용)
  const blockPost = async () => {
    const result = await Swal.fire({
      title: "정말 해당 상품을 차단하시겠습니까?",
      text: "차단 시 상품이 홈페이지 노출되지 않습니다.",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#d33", // 위험하니까 빨간색
      cancelButtonColor: "#3085d6",
      confirmButtonText: "네, 차단합니다",
      cancelButtonText: "취소",
    });

    if (result.isConfirmed) {
      try {
        const response = await axios.post(
          `/blockPost/${props.loc.rrId}`, // URL 오타 수정됨 (blcok -> block)
          {}, // Body data (empty)
          { withCredentials: true }
        );

        if (response.data.success) {
          await Swal.fire(
            "차단되었습니다.",
            "게시글이 차단되어 지도에서 사라졌습니다.",
            "success"
          );
          navigate("/");
        } else {
          Swal.fire("실패", "차단 처리에 실패했습니다.", "error");
        }
      } catch (error) {
        console.error("차단 오류:", error);
        Swal.fire("오류", "서버 통신 중 문제가 발생했습니다.", "error");
      }
    }
  };

  // 6. 후기 페이징 계산 로직
  const indexOfLastReview = reviewPage * REVIEWS_PER_PAGE;
  const indexOfFirstReview = indexOfLastReview - REVIEWS_PER_PAGE;
  const currentReviews = reviews.slice(indexOfFirstReview, indexOfLastReview);
  const totalReviewPages = Math.ceil(reviews.length / REVIEWS_PER_PAGE);

  const paginateReviews = (pageNumber) => {
    if (pageNumber >= 1 && pageNumber <= totalReviewPages) {
      setReviewPage(pageNumber);
    }
  };

  return (
    <div className="location_container">
      {/* 📸 상단 사진 */}
      <LocationDescPhoto
        photo={props.loc.photo}
        thumbnail={props.loc.thumbnail}
      />

      {/* 📌 요약 정보 (Sticky Header 감지용 ref) */}
      <div className="location_preview" ref={previewRef}>
        <div className="location_preview_top">
          <div className="location_preivew_header">
            <h1>{props.loc.locationName}</h1>
            <div>
              <FavoritesIcon
                loc={props.loc}
                classnameContent="star_hover"
                usePurpose="locationDesc"
              />
            </div>
          </div>
          <p>{props.loc.description}</p>
          <p>대관료 : {props.loc.rentalFee}</p>
          <p>보증금 : {props.loc.deposit}</p>
          <p>
            임대 가능 기한 : {props.loc.rentStart} ~ {props.loc.rentEnd}
          </p>
        </div>
        <div className="location_preview_bot">
          <button className="orderBtn" onClick={onClickMoveReservation}>
            <span>예약하기</span>
          </button>

          {/* 관리자에게만 보이는 차단 버튼 */}
          {user?.role === "ROLE_ADMIN" && (
            <button
              className="orderBtn"
              onClick={blockPost}
              style={{ marginTop: "5px", backgroundColor: "#ef4444" }}
            >
              <span>게시글 차단하기</span>
            </button>
          )}
        </div>
      </div>

      {/* 📝 상세 정보 섹션 */}
      <div id="location_info" className="sections">
        {/* 편의시설 */}
        <h2>공간 정보</h2>
        <div>
          {convenience.map((item, idx) => (
            <label className="chk" key={idx}>
              <span>{item.item}</span>
              <input type="checkbox" checked={item.possible} disabled />
            </label>
          ))}
        </div>

        {/* 상세 설명 텍스트 */}
        <div className="info_details">{props.loc.descDetails}</div>

        {/* 주소 및 지도 */}
        <div>
          <h2>주소</h2>
          <h4>
            {props.loc.stAddress}, {props.loc.detailAddress}
          </h4>
          <Minimap
            address={props.loc.stAddress}
            height={"400px"}
            width={"60%"}
          />
        </div>

        {/* 💬 QnA 섹션 */}
        <div>
          <div className="qnaTitle">
            <h2>QnA</h2>
            {/* 펼쳐지는 플로팅 버튼 (로그인한 유저만) */}
            {user && (
              <button className="qna-btn-floating" onClick={openQnaModal}>
                <span className="qna-icon">+</span>
                <span className="qna-text">문의하기</span>
              </button>
            )}
          </div>
          <QnaList
            locationId={props.loc.locationId}
            currentUserId={user && user.userId}
            hostUserId={props.loc.userId}
          />
        </div>

        {/* QnA 모달 */}
        {isQnaModalOpen && (
          <QnaModal
            onClose={qnaModalClose}
            locationId={props.loc.locationId}
            qnaUserId={user && user.userId}
          />
        )}

        {/* ⭐ 후기 (Review) 섹션 */}
        <div style={{ marginTop: "50px" }}>
          <h2>후기 ({reviews.length})</h2>

          {reviews.length === 0 ? (
            <p style={{ color: "#6b7280", padding: "10px 0" }}>
              아직 작성된 후기가 없습니다.
            </p>
          ) : (
            <>
              {/* 🔥 스타일이 적용된 후기 리스트 컨테이너 */}
              <div className="review-container">
                {currentReviews.map((review) => (
                  <div key={review.reviewId} className="review-item">
                    {/* 후기 헤더: 별점 + 날짜 */}
                    <div className="review-header">
                      <span className="review-rating">
                        {"★".repeat(review.rating)}
                        <span className="empty-star">
                          {"★".repeat(5 - review.rating)}
                        </span>
                      </span>
                      <span className="review-date">
                        {new Date(review.createdDate).toLocaleDateString()}
                      </span>
                    </div>

                    {/* 후기 본문 */}
                    <div className="review-content">{review.content}</div>
                  </div>
                ))}
              </div>

              {/* 페이징 버튼 (3개 이상일 때만 표시) */}
              {totalReviewPages > 1 && (
                <div className="qna-pagination">
                  <button
                    onClick={() => paginateReviews(reviewPage - 1)}
                    disabled={reviewPage === 1}
                    className="qna-page-btn"
                  >
                    이전
                  </button>

                  {Array.from(
                    { length: totalReviewPages },
                    (_, idx) => idx + 1
                  ).map((page) => (
                    <button
                      key={page}
                      onClick={() => paginateReviews(page)}
                      className={
                        page === reviewPage
                          ? "qna-page-btn active"
                          : "qna-page-btn"
                      }
                    >
                      {page}
                    </button>
                  ))}

                  <button
                    onClick={() => paginateReviews(reviewPage + 1)}
                    disabled={reviewPage === totalReviewPages}
                    className="qna-page-btn"
                  >
                    다음
                  </button>
                </div>
              )}
            </>
          )}
        </div>
      </div>
    </div>
  );
}
