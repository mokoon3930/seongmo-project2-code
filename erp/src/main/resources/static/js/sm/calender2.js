document.addEventListener("DOMContentLoaded", function () {
  var calendarEl = document.getElementById("calendar");

  if (!calendarEl) {
    console.error("#calendar 요소 없음");
    return;
  }

  var calendar = new FullCalendar.Calendar(calendarEl, {
    initialView: "dayGridMonth",
    locale: "ko",

    // 🔥 여기다 너가 쓴 events 함수 넣기
    events: function (fetchInfo, successCallback, failureCallback) {
      fetch("/myLeaved")   // 위 컨트롤러
        .then(function (response) {
          return response.json();
        })
        .then(function (data) {
          console.log("서버에서 온 data:", data);

          if (!Array.isArray(data)) {
            console.error("배열이 아닌 응답입니다:", data);
            successCallback([]);
            return;
          }

          var events = data
            .filter(function (item) {
              return item && item.status === "승인"; // 승인만
            })
            .map(function (item) {
              var endPlusOne = null;
              if (item.endDate) {
                var endDateObj = new Date(item.endDate);
                endDateObj.setDate(endDateObj.getDate() + 1);
                endPlusOne = endDateObj.toISOString().slice(0, 10);
              }

              return {
                id: item.leavedId,
                title: item.leaveType + " - " + item.reason,
                start: item.startDate,
                end: endPlusOne,
                allDay: true,
                extendedProps: {
                  empId: item.empId,
                  leaveType: item.leaveType,
                  reason: item.reason,
                  status: item.status
                }
              };
            });

          successCallback(events);
        })
        .catch(function (error) {
          console.error("이벤트 로딩 실패:", error);
          failureCallback(error);
        });
    }
  });

  calendar.render();
});
