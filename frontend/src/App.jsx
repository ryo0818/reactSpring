import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Header from "./components/Header";
import Footer from "./components/Footer";
import Home from "./../pages/Home";
import SalesList from "./../pages/SalesList";
import Login from "../pages/Login";

function App() {
  return (
    <div className="min-h-screen flex flex-col">
      <Router>
        <Header />

        {/* 🔽 ここでルーティングの定義を追加 */}
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/sales" element={<SalesList />} />
          <Route path="/login" element={<Login />} />
        </Routes>

        <Footer />
      </Router>
    </div>
  );
}

export default App;
