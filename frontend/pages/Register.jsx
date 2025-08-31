import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import CompanyAuthForm from "../src/components/CompanyAuthForm";
import { signInWithPopup } from "firebase/auth";
import { auth, provider } from "../src/api/firebase";
import { signOut } from "firebase/auth";
import axios from "axios";
import { useAuth } from '../src/contexts/AuthContext';

const API_BASE_URL = import.meta.env.VITE_API_HOST;

const Register = () => {
  const { setDbUser } = useAuth();
  const [error, setError] = useState("");
  const [companyOk, setCompanyOk] = useState(""); // 会社コードがOKか
  const navigate = useNavigate();
  const [userNameOk, setUserNameOk] = useState(""); 
  
  // 会社コード認証
  const handleRegister = async ({ companyCode,userName }) => {
    setError("");
    try {
      console.log("会社コード:", companyCode, "登録氏名:", userName);
      const res = await axios.post(`${API_BASE_URL}/login/check-cmpcode`, {companyCode :companyCode,userName :userName});
      if (res.data == 1) {
        setCompanyOk(companyCode); // Googleログイン画面を表示
        setUserNameOk(userName); // ユーザ名を保存
      } else {
        setError("会社コードが正しくありません");
      }
    } catch (err) {
      console.error(err);
      setError("サーバーエラーが発生しました");
      
    }
  };

  // Googleログイン
  const handleGoogleLogin = async () => {
    setError("");
    try {
      const result = await signInWithPopup(auth, provider);
      const user = result.user;
      console.log("Googleログイン成功:", user.email, user.uid);
        console.log("会社コード:", companyOk);
      // バックエンドに登録リクエスト
      const res = await axios.post(`${API_BASE_URL}/login/insert-user`, {
        email: user.email,
        id: user.uid,
        myCompanyCode :companyOk,
        userName : userNameOk
      });
      console.log("res:", res);
      setDbUser({
        myCompanyCode :companyOk,
        userName : userNameOk
      }); // DBユーザー情報を更新
      navigate("/"); // トップページへ
    } catch (err) {
     setCompanyOk(""); // Googleログイン画面を表示
      console.error(err);
      await signOut(auth);
      setError("Googleログインに失敗しました");
    }
  };

  return (
    <div className="flex flex-col items-center justify-center h-screen bg-gray-100">
      <div className="bg-white p-6 rounded shadow-md w-full max-w-sm text-center">
        {!companyOk ? (
          <>
            <h3 className="text-lg font-semibold mb-2">新規登録</h3>
            <CompanyAuthForm onSubmit={handleRegister} buttonLabel="登録" />
            <button
            onClick={() => navigate("/login")}
            className="w-full mt-2 bg-gray-500 hover:bg-gray-600 text-white py-2 rounded"
            >
            ログイン
            </button>
          </>
        ) : (
          <>
            <h2 className="text-xl font-bold mb-4">Googleアカウントでログイン</h2>
            <button
            onClick={handleGoogleLogin}
            className="bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded mb-4 w-full"
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

export default Register;
