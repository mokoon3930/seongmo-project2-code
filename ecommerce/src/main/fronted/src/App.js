import { BrowserRouter as Router, Route, Routes } from "react-router-dom";

// --- 스타일 및 외부 라이브러리 ---
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import "./css/common.css";
import Swiper from "swiper/bundle"; // (사용되지 않지만 요청에 의해 포함)

// --- Contexts ---
import { UserProvider } from "./context/userContext.jsx";

// --- Route Guards & Layouts ---
import PrivateRoute from "./route/PrivateRoute.jsx";
import AdminRoute from "./route/AdminRoute.jsx";
import TypeRoute from "./route/TypeRoute.jsx";
import MainLayout from "./layouts/mainLayout.jsx";

// --- Pages & Components ---
import LoginPage from "./pages/loginPage.jsx";
import Register from "./pages/register.jsx";
import DevLink from "./pages/devLink.jsx";
import SearchResult from "./pages/searchResult.jsx";
import LocationDetail from "./pages/locationDetail.jsx";
import LocationReservation from "./pages/locationReservation.jsx";
import LocationRegister from "./pages/locationRegister.jsx";
import RentRegister from "./pages/rentRegister.jsx";
import LocationList from "./pages/locationList.jsx";
import Payment from "./pages/payment.jsx";
import MyPage from "./pages/myPage.jsx";
import PaymentRentalRegister from "./pages/paymentRentalRegister.jsx";
import PaymentRentalReservation from "./pages/paymentRentalReservation.jsx";
import PaymentSuccess from "./components/payment/PaymentSuccess.jsx";
import PaymentFail from "./components/payment/PaymentFail.jsx";
import FindIdAndPwd from "./pages/findIdAndPwd.jsx";
// ⚠️ 사용되지 않는 Import (요청에 의해 포함)
import LocationContent from "./components/locationContent.jsx";
import SeleectRegion from "./components/selectRegion.jsx";
import Test from "./pages/test.jsx";
import Review from "./pages/review.jsx";
import LocationModifyForm from "./components/locationRegister/locationModifyForm.jsx";
import LocationModify from "./pages/locationModify.jsx";
import MainPage from "./pages/mainPage.jsx";
import FaqRegister from "./pages/faqRegister.jsx";
import AnnouncementRegister from "./pages/announcementRegister.jsx";
import HelpModify from "./pages/helpModify.jsx";
import AdminPage from "./pages/adminPage.jsx";

function App() {
  return (
    <Router>
      <ToastContainer position="bottom-right" autoClose={3000} />
      <UserProvider>
        <Routes>
          {/* 메인레이아웃을 따르지 않는 독자 페이지 */}
          <Route path="/account/login" element={<LoginPage />} />
          <Route path="/account/register" element={<Register />} />
          <Route path="/account/findIdAndPwd" element={<FindIdAndPwd />} />

          {/* 메인레이아웃 필요한 곳(=헤더 필요한 페이지) */}
          <Route element={<MainLayout />}>
            <Route path="/" element={<MainPage />} />
            <Route path="/review" element={<Review />} />
            <Route path="/search" element={<SearchResult />} />
            <Route path="/search/:rrId" element={<LocationDetail />} />

            {/* 임대인 임차인 권한 상관X (로그인은 필요) */}
            <Route element={<TypeRoute customerType={["임대인", "임차인"]} />}>
              <Route path="/myPage" element={<MyPage />} />

              <Route path="/payment" element={<Payment />} />
              <Route path="/payment/success" element={<PaymentSuccess />} />
              <Route path="/payment/fail" element={<PaymentFail />} />
              <Route
                path="/payment/rentalRegister"
                element={<PaymentRentalRegister />}
              />
              <Route
                path="payment/rentReservation"
                element={<PaymentRentalReservation />}
              />

              <Route
                path="/reservation/:rrId"
                element={<LocationReservation />}
              />
            </Route>

            {/* 임대인만 접근가능 (로그인은 필요) */}
            <Route element={<TypeRoute customerType={["임대인"]} />}>
              <Route path="/locationRegister" element={<LocationRegister />} />
              <Route path="/locationList" element={<LocationList />} />
              <Route path="/locationModify" element={<LocationModify />} />
            </Route>

            {/* 관리자만 접근가능 */}
            <Route element={<AdminRoute />}>
              <Route path="/faqRegister" element={<FaqRegister />} />
              <Route
                path="/announcementRegister"
                element={<AnnouncementRegister />}
              />
              <Route path="/adminPage" element={<AdminPage />} />
            </Route>
            {/* <Route path="/rentRegister" element={<RentRegister />} /> */}
            <Route path="/helpModify" element={<HelpModify />} />
          </Route>
        </Routes>
      </UserProvider>
    </Router>
  );
}

export default App;
