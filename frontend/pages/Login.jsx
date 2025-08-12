// src/pages/Login.jsx
import React, { useState } from "react";
import axios from "axios";
import { signInWithPopup } from "firebase/auth";
import { auth, provider } from "../src/api/firebase";
import { useNavigate } from "react-router-dom";

const validCompanyCodes = ["ABC123", "XYZ999", "TOKYO001"];

const Login = () => {
  const [companyCode, setCompanyCode] = useState("");
  const [error, setError] = useState("");
  const [isCompanyCodeValid, setIsCompanyCodeValid] = useState(false);
  const navigate = useNavigate();

  // 会社コードチェック
  const handleCompanyCodeSubmit = async() => {
      // localStorage.setItem("companyCode", companyCode.trim());
      try {
        // サーバーに会社コードを送信して検証
        //const res = await axios.post(`${API_BASE_URL}/login/companyCode`, { code: companyCode });
        //if (res.data.valid) {
        if (validCompanyCodes.includes(companyCode.trim())) {
          console.log("会社コードOK");
          setIsCompanyCodeValid(true); // Googleログインステップへ
          setError("");
        } else {
          setError("会社コードが正しくありません1");
        }
        } catch (err) {
          console.error(err);
          setError("サーバーエラーが発生しました")
        }
  };

  // Googleログイン
  const handleGoogleLogin = async () => {
    try {
      await signInWithPopup(auth, provider);
      navigate("/"); // トップへ遷移
    } catch (err) {
      console.error("Googleログイン失敗:", err);
      setError("Googleログインに失敗しました。");
    }
  };

  return (
    <div className="flex flex-col items-center justify-center h-screen bg-gray-100">
      <div className="bg-white p-6 rounded shadow-md w-full max-w-sm text-center">
        {!isCompanyCodeValid ? (
          <>
            <h3 className="text-lg font-semibold mb-2">会社コードを入力</h3>
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
          </>
        ) : (
          <>
            <h2 className="text-xl font-bold mb-4">Googleアカウントでログイン</h2>
            <button
              onClick={handleGoogleLogin}
              className="bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded mb-4"
            >
              Googleでログイン
            </button>
          </>
        )}
        {error && <p className="text-red-500 mt-3">{error}</p>}
      </div>
    </div>
  );
};

export default Login;
