import React, { useState, useEffect } from "react";
import { Link, useLocation } from "react-router-dom";

const Header = () => {
  const user = "";
  const [showSummaryTabs, setShowSummaryTabs] = useState(false);
  const [activeTab, setActiveTab] = useState("personal");

  const location = useLocation(); // ① 現在のパスを取得

  // ② パスが変わったらタブを非表示にする
  useEffect(() => {
    setShowSummaryTabs(false);
  }, [location.pathname]);

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
            <Link to="/sales" className="text-gray-700 hover:text-black">
              再架電リスト
            </Link>
            <button
              onClick={() => setShowSummaryTabs(!showSummaryTabs)}
              className="text-gray-700 hover:text-black"
            >
              集計結果
            </button>
            <Link
              to={user ? "/profile" : "/login"}
              className="text-gray-700 hover:text-black"
            >
              {user ? "プロフィール" : "ログイン"}
            </Link>
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
