import React, { useState, useEffect } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";
import { signOut } from "firebase/auth";
import { auth } from "../api/firebase";
import {
  Menu,
  X,
  Home,
  List,
  PhoneCall,
  BarChart3,
  Database,
  User,
} from "lucide-react";
const Header = () => {
  const navigate = useNavigate();
  const { currentUser } = useAuth(); // ここで認証ユーザー取得
  const [showSummaryTabs, setShowSummaryTabs] = useState(false);
  const [activeTab, setActiveTab] = useState("personal");
  const location = useLocation(); // ① 現在のパスを取得
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const navItems = [
    { to: "/", label: "ホーム", icon: <Home size={18} /> },
    { to: "/sales", label: "営業リスト", icon: <List size={18} /> },
    {
      to: "/sales-second",
      label: "再架電リスト",
      icon: <PhoneCall size={18} />,
    },
   // { action: "summary", label: "集計結果", icon: <BarChart3 size={18} /> },
    { to: "/edit-page", label: "データ編集", icon: <Database size={18} /> },
      {
    to: "/sales-performance",
    label: "営業実績",
    icon: <BarChart3 size={18} />,
  },
  ];

  useEffect(() => {
    setShowSummaryTabs(false);
  }, [location.pathname]);

  const handleLogout = async () => {
    try {
      await signOut(auth);
      navigate("/login");
    } catch (error) {
      console.error("ログアウトに失敗しました:", error);
    }
  };

  return (
    <>
      {/* ヘッダー */}
      <header className="bg-gradient-to-r from-white via-gray-50 to-white shadow-md sticky top-0 z-40">
        <nav className="flex items-center justify-between px-6 py-3">
          {/* ロゴ */}
          <Link to="/" className="text-2xl font-bold text-gray-800">
            営業進捗アプリ
          </Link>

          {/* ハンバーガー（SP用） */}
          <button
            className="md:hidden text-gray-700 hover:text-gray-900"
            onClick={() => setIsMenuOpen(true)}
          >
            <Menu size={28} />
          </button>

          {/* PCナビ */}
          <div className="hidden md:flex gap-6 items-center">
            {navItems.map((item, i) =>
              item.action === "summary" ? (
                <button
                  key={i}
                  onClick={() => setShowSummaryTabs(!showSummaryTabs)}
                  className="flex items-center gap-1 text-gray-700 hover:text-blue-600 transition-colors"
                >
                  {item.icon} {item.label}
                </button>
              ) : (
                <Link
                  key={i}
                  to={item.to}
                  className="flex items-center gap-1 text-gray-700 hover:text-blue-600 transition-colors"
                >
                  {item.icon} {item.label}
                </Link>
              )
            )}
            <Link
              to={currentUser ? "/profile" : "/login"}
              className="text-gray-700 hover:text-blue-600 transition-colors"
            >
              {currentUser ? (
                <button
                  onClick={handleLogout}
                  className="text-red-600 hover:text-red-700 font-semibold"
                >
                  ログアウト
                </button>
              ) : (
                "ログイン"
              )}
            </Link>
            {currentUser && (
              <img
                src={currentUser.photoURL}
                alt="プロフィール"
                className="w-9 h-9 rounded-full object-cover border border-gray-300 shadow-sm"
              />
            )}
          </div>
        </nav>
      </header>

      {/* スライドインメニュー（SP用） */}
      <div
        className={`fixed inset-0 z-50 bg-black/40 backdrop-blur-sm transition-opacity ${
          isMenuOpen ? "opacity-100 visible" : "opacity-0 invisible"
        }`}
        onClick={() => setIsMenuOpen(false)}
      >
        <div
          className={`fixed top-0 left-0 h-full w-72 bg-white rounded-r-2xl shadow-2xl border-r border-gray-100 transform transition-transform duration-300 ${
            isMenuOpen ? "translate-x-0" : "-translate-x-full"
          }`}
          onClick={(e) => e.stopPropagation()}
        >
          <div className="flex items-center justify-between p-4 border-b">
            <h2 className="text-lg font-bold text-gray-700">メニュー</h2>
            <button
              onClick={() => setIsMenuOpen(false)}
              className="text-gray-600 hover:text-gray-900"
            >
              <X size={28} />
            </button>
          </div>
          <nav className="flex flex-col gap-2 p-4">
            {navItems.map((item, i) =>
              item.action === "summary" ? (
                <button
                  key={i}
                  onClick={() => {
                    setShowSummaryTabs(!showSummaryTabs);
                    setIsMenuOpen(false);
                  }}
                  className="flex items-center gap-2 px-3 py-2 rounded-lg hover:bg-blue-50 text-gray-700 hover:text-blue-600 transition"
                >
                  {item.icon} {item.label}
                </button>
              ) : (
                <Link
                  key={i}
                  to={item.to}
                  className="flex items-center gap-2 px-3 py-2 rounded-lg hover:bg-blue-50 text-gray-700 hover:text-blue-600 transition"
                >
                  {item.icon} {item.label}
                </Link>
              )
            )}
            <div className="border-t pt-3 mt-3">
              <Link
                to={currentUser ? "/profile" : "/login"}
                className="flex items-center gap-2 px-3 py-2 rounded-lg hover:bg-blue-50 text-gray-700 hover:text-blue-600 transition"
              >
                <User size={18} />
                {currentUser ? (
                  <button
                    onClick={handleLogout}
                    className="text-red-600 hover:text-red-700 font-semibold"
                  >
                    ログアウト
                  </button>
                ) : (
                  "ログイン"
                )}
              </Link>
              {currentUser && (
                <div className="mt-3 flex items-center gap-3 px-3">
                  <img
                    src={currentUser.photoURL}
                    alt="プロフィール"
                    className="w-10 h-10 rounded-full border border-gray-300 shadow-sm"
                  />
                  <span className="text-sm text-gray-600">マイページ</span>
                </div>
              )}
            </div>
          </nav>
        </div>
      </div>

      {/* 集計タブ */}
      {showSummaryTabs && (
        <div className="p-6 bg-gray-50 border-t">
          <div className="flex gap-6 border-b pb-2 mb-4">
            <button
              className={`pb-2 transition-colors ${
                activeTab === "personal"
                  ? "border-b-2 border-blue-500 font-semibold text-blue-600"
                  : "text-gray-500 hover:text-gray-700"
              }`}
              onClick={() => setActiveTab("personal")}
            >
              個人集計
            </button>
            <button
              className={`pb-2 transition-colors ${
                activeTab === "monthly"
                  ? "border-b-2 border-blue-500 font-semibold text-blue-600"
                  : "text-gray-500 hover:text-gray-700"
              }`}
              onClick={() => setActiveTab("monthly")}
            >
              月間集計
            </button>
          </div>
          {activeTab === "personal" && <p>個人ごとの営業結果をここに表示</p>}
          {activeTab === "monthly" && <p>月間の営業集計結果をここに表示</p>}
        </div>
      )}
    </>
  );
};

export default Header;
