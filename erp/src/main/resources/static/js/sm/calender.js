// calender.js

document.addEventListener("DOMContentLoaded", function () {
  console.log("calender.js loaded");

  var calendarEl = document.getElementById("calendar");

  if (!calendarEl) {
    console.error("#calendar 요소를 찾을 수 없습니다.");
    return;
  }

  var calendar = new FullCalendar.Calendar(calendarEl, {
    initialView: "dayGridMonth",
    locale: "ko",
    headerToolbar: {
      left: "prev,next today",
      center: "title",
      right: "dayGridMonth,timeGridWeek,timeGridDay",
    },

    eventClick: function (info) {
      info.jsEvent.preventDefault(); // 기본 동작(링크 이동 등) 막기

      var event = info.event;
      console.log("클릭한 이벤트:", event);

      var modal = document.getElementById("rentModal");
      var titleEl = document.getElementById("rentModalTitle");
      var bodyEl = document.getElementById("rentModalBody");

      if (!modal || !titleEl || !bodyEl) {
        console.error("모달 요소를 찾을 수 없습니다.");
        return;
      }

      // 제목: 건물 / 공간 이름
      titleEl.textContent = event.title;

      // extendedProps에서 추가 정보 가져오기 (없으면 빈 객체)
      var props = event.extendedProps || {};

      // 내용 구성 (필요 없는 줄은 지워도 됨)
      bodyEl.innerHTML = `
            <div class="userDesc">
                  <div class="landlordDesc">
                    <div id="landlord">임대인</div>
                    <div>ID: ${props.landlordId}</div>
                    <div>이름: ${props.landlordName}</div>
                    <div>번호: ${props.landlordPhone}</div>
                    <div>이메일: ${props.landlordEmail}</div>
                  </div>
                  <div class="tenantDesc">
                    <div id="tenant">임차인</div>
                    <div>ID: ${props.tenantId}</div>
                    <div>이름: ${props.tenantName}</div>
                    <div>번호: ${props.tenantPhone}</div>
                    <div>이메일: ${props.tenantEmail}</div>
                  </div>
                </div>
                <hr/>
                <p><b>이용 일수</b> : ${props.bookingDays}일</p>
                <p><b>기본 대여료</b> : ${props.bookingFee?.toLocaleString() || "-"}원</p>
                <p><b>운송비</b> : ${props.transportFee?.toLocaleString() || "-"}원</p>
                <p><b>아이템 대여료</b> : ${props.itemFee?.toLocaleString() || "-"}원</p>

            `;

      // 모달 열기
      modal.classList.add("open");
    },

    // 🔥 서버에서 전체 프로젝트 일정 가져오기
    events: function (fetchInfo, successCallback, failureCallback) {
      fetch("/locationRent")
        .then(function (response) {
          return response.json();
        })
        .then(function (data) {
          console.log("서버에서 온 data:", data, "Array?", Array.isArray(data));

          if (!Array.isArray(data)) {
            console.error("배열이 아닌 응답입니다:", data);
            successCallback([]);
            return;
          }

          // CalendarDTO -> FullCalendar 이벤트로 변환
          var events = data
            .filter(function (item) {
              return item != null;
            })
            .map(function (item) {
              return {
                title: item.locationName, // "광화문타워 카페공간" 같은 이름
                start: item.startDate, // p.start_date
                end: item.endPlenDate, // p.end_date
                 extendedProps: {
                            bookingId: item.bookingId,
                            locationId: item.locationId,

                            landlordId: item.landlordId,
                            landlordName: item.landlordName,
                            landlordPhone: item.landlordPhone,
                            landlordEmail: item.landlordEmail,

                            tenantId: item.tenantId,
                            tenantName: item.tenantName,
                            tenantPhone: item.tenantPhone,
                            tenantEmail: item.tenantEmail,

                            bookingDays: item.bookingDays,
                            bookingFee: item.bookingFee,
                            transportFee: item.transportFee,
                            itemFee: item.itemFee
                          }
              };
            });

          console.log("FullCalendar에 넘길 events:", events);
          successCallback(events);
        })
        .catch(function (error) {
          console.error("이벤트 로딩 실패:", error);
          failureCallback(error);
        });
    },
  });

  calendar.render();

  // ✅ 모달 닫기 로직 (X 버튼 + 배경 클릭)
  var rentModal = document.getElementById("rentModal");
  var rentModalClose = document.getElementById("rentModalClose");

  if (rentModal && rentModalClose) {
    // 닫기(X) 클릭
    rentModalClose.addEventListener("click", function () {
      rentModal.classList.remove("open");
    });

    // 배경 클릭 시 닫기
    rentModal.addEventListener("click", function (e) {
      if (e.target === rentModal) {
        rentModal.classList.remove("open");
      }
    });
  }
});
