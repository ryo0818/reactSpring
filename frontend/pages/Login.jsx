// src/pages/Login.jsx
import React, { useState } from "react";
import { signInWithPopup } from "firebase/auth";
import { auth, provider } from "../src/api/firebase";
import { useNavigate } from "react-router-dom";

const validCompanyCodes = ["ABC123", "XYZ999", "TOKYO001"]; // 仮の会社コード一覧

const Login = () => {
  const [companyCode, setCompanyCode] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleGoogleLogin = async () => {
    try {
      const result = await signInWithPopup(auth, provider);
      const user = result.user;

      // 認証OK → 会社コード確認ステップへ
      console.log("Googleログイン成功:", user);
    } catch (err) {
      console.error("Googleログイン失敗:", err);
      setError("Googleログインに失敗しました。");
    }
  };

  const handleCompanyCodeSubmit = () => {
    if (validCompanyCodes.includes(companyCode.trim())) {
      localStorage.setItem("companyCode", companyCode);
      navigate("/"); // トップへ遷移
    } else {
      setError("会社コードが正しくありません。");
    }
  };

  return (
    <div className="flex flex-col items-center justify-center h-screen bg-gray-100">
      <div className="bg-white p-6 rounded shadow-md w-full max-w-sm text-center">
        <h2 className="text-xl font-bold mb-4">Googleアカウントでログイン</h2>
        <button
          onClick={handleGoogleLogin}
          className="bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded mb-4"
        >
          Googleでログイン
        </button>

        <h3 className="text-lg font-semibold mt-6 mb-2">会社コードを入力</h3>
        <input
          type="text"
          value={companyCode}
          onChange={(e) => setCompanyCode(e.target.value)}
          className="w-full px-3 py-2 border border-gray-300 rounded mb-2"
          placeholder="会社コード"
        />
        <button
          onClick={handleCompanyCodeSubmit}
          className="w-full bg-green-500 hover:bg-green-600 text-white py-2 rounded"
        >
          送信
        </button>

        {error && <p className="text-red-500 mt-3">{error}</p>}
      </div>
    </div>
  );
};

export default Login;
