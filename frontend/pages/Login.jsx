import React, { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { signInWithRedirect } from "firebase/auth";
import CompanyAuthForm from "../src/components/CompanyAuthForm";
import { signInWithPopup } from "firebase/auth";
import { auth, provider } from "../src/api/firebase";
import { useAuth } from "../src/contexts/AuthContext"; 
import axios from 'axios';
const validCompanyCodes = ["ABC123", "XYZ999", "TOKYO001"];
const API_BASE_URL = import.meta.env.VITE_API_HOST;
const Login = () => {
  const { currentUser } = useAuth();
  const [error, setError] = useState("");
  const [isCompanyCodeValid, setIsCompanyCodeValid] = useState(false);
  const navigate = useNavigate();
  const handleGoogleLogin = async () => {
    console.log("currentUser"); //
    try {
      const result = await signInWithPopup(auth, provider);
      const user = result.user;
      const email = user.email;
      const id = user.uid;
      const res = await axios.post(`${API_BASE_URL}/login/company-code`, { email,id });
        if (res.data.exists) {
        navigate("/"); // ユーザ存在 → トップページ
        } else {
          await signOut(auth);
          navigate("/login");
        navigate("/register"); // ユーザ未登録 → 登録画面へ
        }
      } catch (err) {
        console.error(err);
        await signOut(auth);
        setError("Googleログインに失敗しました");
     }
  };

  return (
    <div className="flex flex-col items-center justify-center h-screen bg-gray-100">
      <div className="bg-white p-6 rounded shadow-md w-full max-w-sm text-center">
          <>
            <h2 className="text-xl font-bold mb-4">Googleアカウントでログイン</h2>
            <button
              onClick={handleGoogleLogin}
              className="bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded mb-4 w-full"
            >
              Googleでログイン
            </button>
                        <button
              onClick={() => navigate("/register")}
              className="w-full mt-2 bg-gray-500 hover:bg-gray-600 text-white py-2 rounded"
            >
            新規登録
            </button>
          </>
        {error && <p className="text-red-500 mt-3">{error}</p>}
      </div>
    </div>
  );
};

export default Login;
