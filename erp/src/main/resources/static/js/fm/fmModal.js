window.openGraphModal = function () {
  // 🔥 [수정] 범인 검거! 변수를 여기서 확실하게 정의해야 한다.
  const modal = document.getElementById("graphModal");
  const contentArea = document.getElementById("modal-content-area");

  if (!modal || !contentArea) {
    console.error("모달 요소를 찾을 수 없다! ID를 확인해라.");
    return;
  }

  modal.classList.remove("hidden");
  contentArea.innerHTML =
    '<div style="text-align:center; padding:50px;">로딩중...</div>';

  fetch("/financialGraph")
    .then((response) => response.text())
    .then((html) => {
      // 1. HTML 삽입
      contentArea.innerHTML = html;

      // 2. HTML 문자열 안에 있는 <script> 태그(chartData 세팅) 실행
      const scripts = contentArea.querySelectorAll("script");
      scripts.forEach((oldScript) => {
        const newScript = document.createElement("script");
        newScript.textContent = oldScript.textContent;
        document.body.appendChild(newScript);
        // 실행 후 바로 제거 (깔끔하게)
        document.body.removeChild(newScript);
      });

      // 3. 차트 그리기 함수 호출!
      if (typeof window.initFinancialChart === "function") {
        window.initFinancialChart();
      }
    })
    .catch((error) => {
      console.error(error);
      contentArea.innerHTML =
        '<div style="text-align:center; padding:50px; color:red;">그래프를 불러오지 못했다! ☠️</div>';
    });
};

window.closeGraphModal = function () {
  // 🔥 [수정] 닫을 때도 변수를 찾아야지!
  const modal = document.getElementById("graphModal");
  const contentArea = document.getElementById("modal-content-area");

  if (modal) modal.classList.add("hidden");

  // 내용 비우기
  if (contentArea) contentArea.innerHTML = "";
};
