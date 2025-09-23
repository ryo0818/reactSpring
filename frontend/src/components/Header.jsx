import React, { useState, useEffect } from "react";
import { Link, useLocation,useNavigate } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";
import { signOut } from "firebase/auth";
import { auth } from "../api/firebase";
const Header = () => {
  const navigate = useNavigate();
  const { currentUser } = useAuth();  // ここで認証ユーザー取得
  const [showSummaryTabs, setShowSummaryTabs] = useState(false);
  const [activeTab, setActiveTab] = useState("personal");
  const location = useLocation(); // ① 現在のパスを取得
  // ② パスが変わったらタブを非表示にする
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
      <header className="bg-white shadow-md">
        
        <nav className="flex items-center justify-between p-4">
          <Link to="/" className="text-xl font-bold text-black">
            営業進捗アプリ
          </Link>
          <div className="flex gap-4">
            <Link to="/" className="text-gray-700 hover:text-black">
              ホーム
            </Link>
            <Link to="/sales" className="text-gray-700 hover:text-black">
              営業リスト
            </Link>
            <Link to="/sales-second" className="text-gray-700 hover:text-black">
              再架電リスト
            </Link>
            <button
              onClick={() => setShowSummaryTabs(!showSummaryTabs)}
              className="text-gray-700 hover:text-black"
            >
              集計結果
            </button>
            <Link to="/edit-page" className="text-gray-700 hover:text-black">
              データ編集
            </Link>
            <Link
              to={currentUser ? "/profile" : "/login"}
              className="text-gray-700 hover:text-black"
            >
              {currentUser ?  
              <>
              <button
                  onClick={handleLogout}
                  className="text-red-600 hover:text-red-800 font-semibold">ログアウト </button>
                  </> : "ログイン"}
            </Link>
            {currentUser ? <img src={currentUser.photoURL} alt="プロフィール" className="w-8 h-8 rounded-full object-cover" /> :""}
          </div>
        </nav>
      </header>

      {showSummaryTabs && (
        <div className="p-4 bg-gray-100">
          <div className="flex gap-4 border-b mb-4">
            <button
              className={`pb-2 ${
                activeTab === "personal"
                  ? "border-b-2 border-blue-500 font-semibold"
                  : ""
              }`}
              onClick={() => setActiveTab("personal")}
            >
              個人集計
            </button>
            <button
              className={`pb-2 ${
                activeTab === "monthly"
                  ? "border-b-2 border-blue-500 font-semibold"
                  : ""
              }`}
              onClick={() => setActiveTab("monthly")}
            >
              月間集計
            </button>
          </div>

          {activeTab === "personal" && (
            <div>
              <p>個人ごとの営業結果をここに表示</p>
            </div>
          )}
          {activeTab === "monthly" && (
            <div>
              <p>月間の営業集計結果をここに表示</p>
            </div>
          )}
        </div>
      )}
    </>
  );
};

export default Header;
