import React, { useState, useEffect } from "react";
import Papa from "papaparse";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";

const API_BASE_URL = import.meta.env.VITE_API_HOST;

// バックエンド側で必要なフィールド
const targetFields = [
  { key: "clientCompanyName", label: "会社名" },
  { key: "clientPhoneNumber", label: "電話番号" },
  { key: "clientIndustry", label: "業種" },
  { key: "media", label: "媒体" },
  { key: "callDateTime", label: "架電日" },
  { key: "nextCallDateTime", label: "再架電日" },
  { key: "callCount", label: "架電回数" },
  { key: "statusId", label: "ステータス" }, 
  { key: "userStaff", label: "担当者" },
  { key: "remarks", label: "備考" },
  { key: "clientUrl", label: "URL" },
  { key: "clientAddress", label: "住所" },
  { key: "hotflg", label: "優先度" },
  { key: "validFlg", label: "削除フラグ" },
];

export default function CsvUploader() {
  const { dbUser, currentUser } = useAuth();

  const [headers, setHeaders] = useState([]);
  const [csvData, setCsvData] = useState([]);
  const [mapping, setMapping] = useState({});
  const [mappedData, setMappedData] = useState([]);
  const [statusList, setStatusList] = useState([]);

  const navigate = useNavigate();

  // ✅ ステータスマスタ取得
  useEffect(() => {
    const fetchStatus = async () => {
      try {
        const res = await axios.post(`${API_BASE_URL}/login/get-statslist`, {
          userCompanyCode: dbUser.myCompanyCode,
        });
        setStatusList(res.data);
      } catch (err) {
        console.error("ステータス取得失敗", err);
      }
    };
    fetchStatus();
  }, []);

  // ✅ name → id のMap作成
  const statusMap = React.useMemo(() => {
    const map = {};
    statusList.forEach((s) => {
      map[s.statusName] = s.statusId;
    });
    return map;
  }, [statusList]);

  // CSV読み込み
  const handleFileUpload = (e) => {
    const file = e.target.files[0];
    if (!file) return;

    Papa.parse(file, {
      header: true,
      skipEmptyLines: true,
      complete: (result) => {
        const cols = Object.keys(result.data[0] || {});
        setHeaders(cols);
        setCsvData(result.data);
      },
    });
  };

  // マッピング変更
  const handleMappingChange = (targetKey, csvColumn) => {
    setMapping((prev) => ({
      ...prev,
      [targetKey]: csvColumn,
    }));
  };

  // ✅ データ変換（ステータスID変換込み）
  const handleTransform = () => {
    const transformed = csvData.map((row, index) => {
      const obj = {};

      for (const field of targetFields) {
        const csvCol = mapping[field.key];
        let value = csvCol ? row[csvCol] : null;

        // 🎯 ステータス変換
        if (field.key === "statusId") {
          const statusId = statusMap[value];

          if (!statusId) {
            console.warn(`行${index + 1}: 不正なステータス → ${value}`);
            value = null; // ← 必要に応じてデフォルトIDに変更OK
          } else {
            value = statusId;
          }
        }

        obj[field.key] = value;
      }

      return obj;
    });

    setMappedData(transformed);
  };

  // 送信
  const handleSubmit = async () => {
    try {
      const payload = mappedData.map((row) => ({
        ...row,
        userId: currentUser?.uid || null,
        userTeamCode: dbUser?.myteamcode || null,
        userCompanyCode: dbUser?.myCompanyCode || null,
      }));

      console.log("Payload:", payload);

      await axios.post(`${API_BASE_URL}/sales/insert-salse-csv`, payload);

      alert("送信成功！");
      navigate("/sales");
    } catch (err) {
      console.error(err);
      alert("送信失敗...");
    }
  };

  return (
    <div className="max-w-4xl mx-auto p-6">
      <div className="bg-white shadow-lg rounded-2xl p-6">
        <h2 className="text-2xl font-bold text-gray-800 mb-4">
          CSVアップロード & マッピング
        </h2>

        <label className="block mb-4">
          <span className="text-gray-700 font-medium">CSVファイルを選択</span>
          <input
            type="file"
            accept=".csv"
            onChange={handleFileUpload}
            className="mt-2 block w-full text-sm text-gray-600 file:mr-4 file:py-2 file:px-4 
                       file:rounded-lg file:border-0 file:text-sm file:font-semibold
                       file:bg-blue-50 file:text-blue-600 hover:file:bg-blue-100"
          />
        </label>

        {headers.length > 0 && (
          <>
            <h3 className="mt-6 text-lg font-semibold text-gray-700">
              列マッピング
            </h3>
            <div className="mt-4 grid grid-cols-1 md:grid-cols-2 gap-4">
              {targetFields.map((field) => (
                <div
                  key={field.key}
                  className="flex flex-col bg-gray-50 rounded-lg p-3 shadow-sm"
                >
                  <label className="text-sm font-medium text-gray-700 mb-1">
                    {field.label}
                  </label>
                  <select
                    value={mapping[field.key] || ""}
                    onChange={(e) =>
                      handleMappingChange(field.key, e.target.value)
                    }
                    className="p-2 border rounded-lg focus:ring-2 focus:ring-blue-400"
                  >
                    <option value="">未設定</option>
                    {headers.map((h) => (
                      <option key={h} value={h}>
                        {h}
                      </option>
                    ))}
                  </select>
                </div>
              ))}
            </div>

            <button
              onClick={handleTransform}
              className="mt-6 w-full py-2 px-4 bg-green-600 text-white font-semibold rounded-lg hover:bg-green-700"
            >
              データを整形
            </button>
          </>
        )}

        {mappedData.length > 0 && (
          <div className="mt-6">
            <h3 className="text-lg font-semibold text-gray-700">プレビュー</h3>
            <div className="mt-2 bg-gray-900 text-green-300 p-3 rounded-lg overflow-x-auto text-sm max-h-60 overflow-y-scroll">
              <pre>{JSON.stringify(mappedData.slice(0, 5), null, 2)}</pre>
            </div>

            <button
              onClick={handleSubmit}
              className="mt-4 w-full py-2 px-4 bg-blue-600 text-white font-semibold rounded-lg hover:bg-blue-700"
            >
              サーバーに送信
            </button>
          </div>
        )}
      </div>
    </div>
  );
}
