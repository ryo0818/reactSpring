import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Header from "./components/Header";
import Footer from "./components/Footer";
import Home from "./../pages/Home";
import SalesList from "./../pages/SalesList";
import Login from "../pages/Login";
import { AuthProvider } from "./contexts/AuthContext";
import PrivateRoute from "./contexts/PrivateRoute";
function App() {
  return (
    <div className="min-h-screen flex flex-col">
    <AuthProvider>
      <Router>
        <Header />
        <Routes>
          {/* ホームは誰でもアクセスOK */}
          <Route path="/" element={<Home />} />
          <Route path="/login" element={<Login />} />
          {/* 以下はログインしていないとアクセス不可 */}
          <Route path="/sales" element={<PrivateRoute><SalesList /></PrivateRoute>}/>
        </Routes>
        <Footer />
      </Router>
    </AuthProvider>
    </div>
  );
}

export default App;
