// src/components/CompanyAuthForm.jsx
import React, { useState } from "react";

const CompanyAuthForm = ({ onSubmit, buttonLabel }) => {
  const [companyCode, setCompanyCode] = useState("");
  const [email, setEmail] = useState("");

  const handleSubmit = () => {
    onSubmit({ companyCode });
  };

  return (
    <div>
      <input
        type="text"
        value={companyCode}
        onChange={(e) => setCompanyCode(e.target.value)}
        placeholder="会社コード"
        className="w-full px-3 py-2 border border-gray-300 rounded mb-2"
      />
      <button
        onClick={handleSubmit}
        className="w-full bg-green-500 hover:bg-green-600 text-white py-2 rounded"
      >
        {buttonLabel}
      </button>
    </div>
  );
};

export default CompanyAuthForm;
