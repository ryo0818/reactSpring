import React from "react";

const Header = () => {
  // 仮のユーザー状態（将来的に状態管理に置き換えてOK）
  const user = "";

  return (
    <header className="text-gray-100 shadow-lg  bg-gray-100 ">
      <nav className="flex items-center justify-between p-4">
        <a href="/" className="text-xl font-bold">
          Book Commerce
        </a>
        <div className="flex items-center gap-2">
          <a
            href="/"
            className="text-gray-300 hover:text-white px-3 py-2 rounded-md text-sm font-medium"
          >
            ホーム
          </a>
          <a
            href={user ? "/profile" : "/login"}
            className="text-gray-300 hover:text-white px-3 py-2 rounded-md text-sm font-medium"
          >
            {user ? "プロフィール" : "ログイン"}
          </a>
          {user && (
            <button
              onClick={() => alert("ログアウト処理")}
              className="text-gray-300 hover:text-white px-3 py-2 rounded-md text-sm font-medium"
            >
              ログアウト
            </button>
          )}
          <a href="/profile">
            <img
              width={40}
              height={40}
              alt="profile_icon"
              src="/default_icon.png"
              className="rounded-full"
            />
          </a>
        </div>
      </nav>
    </header>
  );
};

export default Header;
