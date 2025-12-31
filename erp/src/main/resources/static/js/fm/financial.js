// -----------------------------------------------------------
// 전역 변수: 차트 인스턴스를 저장해두는 곳
// (파일이 로드될 때 한 번만 선언된다)
// -----------------------------------------------------------
let myFinancialChart = null;

// -----------------------------------------------------------
// 초기화 함수: 모달이 열릴 때마다 이 함수가 호출된다!
// -----------------------------------------------------------
window.initFinancialChart = function () {
  console.log("차트 초기화 함수가 실행되었다!");

  // 1. DOM 요소 가져오기 (모달 안에 새로 생긴 요소들)
  const ctxCanvas = document.getElementById("line-chart");
  if (!ctxCanvas) {
    console.warn("캔버스 요소를 찾을 수 없다! 차트 그리기를 중단한다.");
    return;
  }
  const ctx = ctxCanvas.getContext("2d");

  const typeSelect = document.getElementById("typeSelect");
  const categorySelect = document.getElementById("categorySelect");
  const dateMode = document.getElementById("dateMode");
  const fromDateInput = document.getElementById("fromDate");
  const toDateInput = document.getElementById("toDate");
  const searchBtn = document.getElementById("searchBtn");

  // 2. 데이터 가져오기 (financialGraph.html에서 window.chartData에 심어둠)
  const rawData = window.chartData || [];

  // 3. 색상 상수 정의
  const INCOME_BORDER = "rgba(54, 162, 235, 1)"; // 파란 선
  const INCOME_BG = "rgba(54, 162, 235, 0.3)"; // 파란 채우기
  const EXPENSE_BORDER = "rgba(255, 99, 132, 1)"; // 빨간 선
  const EXPENSE_BG = "rgba(255, 99, 132, 0.3)"; // 빨간 채우기

  const CATEGORY_COLORS = [
    { border: "rgba(255, 99, 132, 1)", bg: "rgba(255, 99, 132, 0.3)" },
    { border: "rgba(54, 162, 235, 1)", bg: "rgba(54, 162, 235, 0.3)" },
    { border: "rgba(255, 206, 86, 1)", bg: "rgba(255, 206, 86, 0.3)" },
    { border: "rgba(75, 192, 192, 1)", bg: "rgba(75, 192, 192, 0.3)" },
    { border: "rgba(153, 102, 255, 1)", bg: "rgba(153, 102, 255, 0.3)" },
    { border: "rgba(255, 159, 64, 1)", bg: "rgba(255, 159, 64, 0.3)" },
    { border: "rgba(99, 255, 132, 1)", bg: "rgba(99, 255, 132, 0.3)" },
    { border: "rgba(201, 203, 207, 1)", bg: "rgba(201, 203, 207, 0.3)" },
  ];

  // -----------------------------------------------------------
  // 내부 헬퍼 함수들 (재선언 충돌 방지를 위해 init 함수 내부에 위치)
  // -----------------------------------------------------------

  // 카테고리 옵션 채우기
  function fillCategoryOptions() {
    const type = typeSelect.value; // ALL / INCOME / EXPENSE
    let filtered = rawData;

    if (type === "INCOME") {
      filtered = rawData.filter((r) => r.transType === "수입");
    } else if (type === "EXPENSE") {
      filtered = rawData.filter((r) => r.transType === "지출");
    }

    // 중복 제거된 카테고리 목록
    const categories = [...new Set(filtered.map((r) => r.category))];

    categorySelect.innerHTML = "";

    // 기본 옵션
    const allOpt = document.createElement("option");
    allOpt.value = "";
    allOpt.textContent = "전체 카테고리";
    categorySelect.appendChild(allOpt);

    categories.forEach((c) => {
      const opt = document.createElement("option");
      opt.value = c;
      opt.textContent = c;
      categorySelect.appendChild(opt);
    });
  }

  // 날짜 범위 배열 생성
  function makeDateRange(from, to) {
    const result = [];
    if (!from || !to) return result;
    let cur = new Date(from);
    const end = new Date(to);

    while (cur <= end) {
      const y = cur.getFullYear();
      const m = String(cur.getMonth() + 1).padStart(2, "0");
      const d = String(cur.getDate()).padStart(2, "0");
      result.push(`${y}-${m}-${d}`);
      cur.setDate(cur.getDate() + 1);
    }
    return result;
  }

  // 일별 합계 (범위)
  function groupDailyWithRange(data, fromDate, toDate, useIncome, useExpense) {
    const labels = makeDateRange(fromDate, toDate);
    const incomeData = [];
    const expenseData = [];

    labels.forEach((date) => {
      const rows = data.filter((r) => r.transDate === date);
      let incomeSum = 0;
      let expenseSum = 0;

      if (useIncome) {
        incomeSum = rows
          .filter((r) => r.transType === "수입")
          .reduce((sum, r) => sum + r.transAmount, 0);
      }
      if (useExpense) {
        expenseSum = rows
          .filter((r) => r.transType === "지출")
          .reduce((sum, r) => sum + r.transAmount, 0);
      }
      incomeData.push(incomeSum);
      expenseData.push(expenseSum);
    });
    return { labels, incomeData, expenseData };
  }

  // 월별 합계 (전체)
  function groupMonthly(data, useIncome, useExpense) {
    const monthlyMap = {};
    data.forEach((r) => {
      const ym = r.transDate.substring(0, 7);
      if (!monthlyMap[ym]) monthlyMap[ym] = { income: 0, expense: 0 };
      if (useIncome && r.transType === "수입")
        monthlyMap[ym].income += r.transAmount;
      if (useExpense && r.transType === "지출")
        monthlyMap[ym].expense += r.transAmount;
    });

    const labels = Object.keys(monthlyMap).sort();
    const incomeData = labels.map((ym) => monthlyMap[ym].income);
    const expenseData = labels.map((ym) => monthlyMap[ym].expense);
    return { labels, incomeData, expenseData };
  }

  // 카테고리별 시계열 데이터
  function buildCategoryTimeSeries(data, type, mode, fromDate, toDate) {
    const isIncomeMode = type === "INCOME";
    const targetType = isIncomeMode ? "수입" : "지출";
    const filtered = data.filter((r) => r.transType === targetType);

    let labels = [];
    let isDaily = false;

    if (mode === "RANGE" && fromDate && toDate) {
      labels = makeDateRange(fromDate, toDate);
      isDaily = true;
    } else {
      const months = new Set(filtered.map((r) => r.transDate.substring(0, 7)));
      labels = [...months].sort();
      isDaily = false;
    }

    const categories = [...new Set(filtered.map((r) => r.category || "기타"))];
    const datasets = [];

    categories.forEach((cat, index) => {
      const values = labels.map((label) => {
        let rows;
        if (isDaily) {
          rows = filtered.filter(
            (r) => (r.category || "기타") === cat && r.transDate === label
          );
        } else {
          rows = filtered.filter(
            (r) =>
              (r.category || "기타") === cat &&
              r.transDate.substring(0, 7) === label
          );
        }
        return rows.reduce((sum, r) => sum + r.transAmount, 0);
      });

      const color = CATEGORY_COLORS[index % CATEGORY_COLORS.length];
      datasets.push({
        label: cat,
        data: values,
        borderColor: color.border,
        backgroundColor: color.bg,
        pointStyle: "circle",
        tension: 0.2,
      });
    });

    return { labels, datasets };
  }

  // -----------------------------------------------------------
  // 핵심 함수: 차트 그리기
  // -----------------------------------------------------------
  function drawChart() {
    const type = typeSelect.value;
    const category = categorySelect.value;
    const mode = dateMode.value;
    const fromDate = fromDateInput.value;
    const toDate = toDateInput.value;

    let data = rawData;
    const useIncome = type === "ALL" || type === "INCOME";
    const useExpense = type === "ALL" || type === "EXPENSE";

    let labels = [];
    let datasets = [];

    // 1) 수익/지출 선택 + 카테고리 미선택 -> 카테고리별 라인 차트
    if (!category && type !== "ALL") {
      if (mode === "RANGE" && fromDate && toDate) {
        data = data.filter(
          (r) => r.transDate >= fromDate && r.transDate <= toDate
        );
      }
      const categorySeries = buildCategoryTimeSeries(
        data,
        type,
        mode,
        fromDate,
        toDate
      );
      labels = categorySeries.labels;
      datasets = categorySeries.datasets;
    } else {
      // 2) 나머지는 합계(수입/지출) 차트
      if (category) {
        data = data.filter((r) => r.category === category);
      }

      if (mode === "RANGE" && fromDate && toDate) {
        const grouped = groupDailyWithRange(
          data,
          fromDate,
          toDate,
          useIncome,
          useExpense
        );
        labels = grouped.labels;
        if (useIncome) {
          datasets.push({
            label: "수입",
            data: grouped.incomeData,
            borderColor: INCOME_BORDER,
            backgroundColor: INCOME_BG,
          });
        }
        if (useExpense) {
          datasets.push({
            label: "지출",
            data: grouped.expenseData,
            borderColor: EXPENSE_BORDER,
            backgroundColor: EXPENSE_BG,
          });
        }
      } else {
        const grouped = groupMonthly(data, useIncome, useExpense);
        labels = grouped.labels;
        if (useIncome) {
          datasets.push({
            label: "수입",
            data: grouped.incomeData,
            borderColor: INCOME_BORDER,
            backgroundColor: INCOME_BG,
          });
        }
        if (useExpense) {
          datasets.push({
            label: "지출",
            data: grouped.expenseData,
            borderColor: EXPENSE_BORDER,
            backgroundColor: EXPENSE_BG,
          });
        }
      }
    }

    // 차트 타입 결정 (전체 요약일 때만 Bar, 나머지는 Line)
    let chartType;
    if (type === "ALL" && mode === "ALL" && !category) {
      chartType = "bar";
    } else {
      chartType = "line";
    }

    const config = {
      type: chartType,
      data: {
        labels: labels,
        datasets: datasets,
      },
      options: {
        responsive: true,
        maintainAspectRatio: false, // 모달 크기에 맞춰 늘어나게!
        scales: {
          y: {
            beginAtZero: true,
          },
        },
      },
    };

    // 🔥 기존 차트가 살아있다면 파괴한다! (좀비 방지)
    if (myFinancialChart) {
      myFinancialChart.destroy();
      myFinancialChart = null;
    }

    // 새 차트 생성
    myFinancialChart = new Chart(ctx, config);
  }

  // -----------------------------------------------------------
  // 이벤트 리스너 연결
  // (모달이 열릴 때마다 요소가 새로 생기므로, 이벤트도 다시 걸어야 함)
  // -----------------------------------------------------------

  function updateCategoryEnabled() {
    if (typeSelect.value === "ALL") {
      categorySelect.disabled = true;
      categorySelect.value = "";
    } else {
      categorySelect.disabled = false;
    }
  }

  dateMode.onchange = () => {
    const useRange = dateMode.value === "RANGE";
    fromDateInput.disabled = !useRange;
    toDateInput.disabled = !useRange;
    if (!useRange) {
      fromDateInput.value = "";
      toDateInput.value = "";
    }
    drawChart();
  };

  typeSelect.onchange = () => {
    fillCategoryOptions();
    updateCategoryEnabled();
    drawChart();
  };

  // 카테고리 변경 시에도 다시 그려야지!
  categorySelect.onchange = () => {
    drawChart();
  };

  searchBtn.onclick = () => {
    drawChart();
  };

  // -----------------------------------------------------------
  // 초기 실행
  // -----------------------------------------------------------
  fillCategoryOptions();
  updateCategoryEnabled();
  drawChart();
};
