import React from "react";
import { Routes, Route, Link } from "react-router-dom";
import Home from "../../pages/Home";
import SalesList from "../../pages/SalesList";
//import Login from "../pages/Con";
//import Profile from "../pages/Profile";

const Header = () => {
  const user = "";

  return (
    <>
      <header className="bg-white shadow-md">
        <nav className="flex items-center justify-between p-4">
          <Link to="/" className="text-xl font-bold text-black">
            営業進捗アプリ
          </Link>
          <div className="flex gap-4">
            <Link to="/" className="text-gray-700 hover:text-black">ホーム</Link>
            <Link to="/sales" className="text-gray-700 hover:text-black">営業リスト</Link>
            <Link to="/sales" className="text-gray-700 hover:text-black">再架電リスト</Link>
            <Link to="/sales" className="text-gray-700 hover:text-black">集計結果</Link>
            <Link to={user ? "/profile" : "/login"} className="text-gray-700 hover:text-black">
              {user ? "プロフィール" : "ログイン"}
            </Link>
          </div>
        </nav>
      </header>

      {/* ルーティング定義（非推奨） */}
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/sales" element={<SalesList />} />
        {/* <Route path="/login" element={<Login />} /> */}
        {/* <Route path="/profile" element={<Profile />} /> */}
      </Routes>
    </>
  );
};

export default Header;
