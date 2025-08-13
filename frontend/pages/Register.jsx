import React, { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import CompanyAuthForm from "../src/components/CompanyAuthForm";

const validCompanyCodes = ["ABC123", "XYZ999", "TOKYO001"];

const Register = () => {
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleRegister = ({ companyCode, email }) => {
    if (validCompanyCodes.includes(companyCode.trim()) && email.includes("@")) {
      console.log("新規登録処理:", companyCode, email);
    //const res = await axios.post(`${API_BASE_URL}/register/companyCode`, { code: companyCode, email });
    //if (res.data.valid) {
      navigate("/login");
    } else {
      setError("会社コードまたはメールアドレスが正しくありません");
    }
  };

  return (
    <div className="flex flex-col items-center justify-center h-screen bg-gray-100">
      <div className="bg-white p-6 rounded shadow-md w-full max-w-sm text-center">
        <h3 className="text-lg font-semibold mb-2">新規登録</h3>
        <CompanyAuthForm onSubmit={handleRegister} buttonLabel="登録" />
            <button
              onClick={() => navigate("/login")}
              className="w-full mt-2 bg-gray-500 hover:bg-gray-600 text-white py-2 rounded"
            >
            ログイン
            </button>
        {error && <p className="text-red-500 mt-3">{error}</p>}
      </div>
    </div>
  );
};

export default Register;
