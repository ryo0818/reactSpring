import React, { useState, useEffect } from "react";
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  Tooltip,
  ResponsiveContainer,
  Legend,
} from "recharts";
import axios from "axios";
import { useAuth } from "../contexts/AuthContext";

const API_BASE_URL = import.meta.env.VITE_API_HOST;

const metrics = ["架電数", "接続数", "オーナ数", "フル", "アポ数"];

/*
データ変換
*/
function transformData(apiData, timeUnit) {
  if (!apiData || !Array.isArray(apiData.saleFunnelAggList)) {
    return [];
  }

  const mapping = {
    "架電数": "callCount",
    "接続数": "connectCount",
    "オーナ数": "ownerCount",
    "フル": "fullCount",
    "アポ数": "apoCount",
  };

  return apiData.saleFunnelAggList.map((item) => {
    const label =
      timeUnit === 1
        ? item.aggregatedDateTime?.slice(11, 16)
        : item.aggregatedDateTime?.slice(5, 10);

    const result = { label };

    Object.entries(mapping).forEach(([jpKey, apiKey]) => {
      result[jpKey] = item[apiKey] ?? 0;
    });

    return result;
  });
}

/*
日付フォーマット
*/
function formatDateTime(date) {
  const yyyy = date.getFullYear();
  const MM = String(date.getMonth() + 1).padStart(2, "0");
  const dd = String(date.getDate()).padStart(2, "0");
  const HH = String(date.getHours()).padStart(2, "0");
  const mm = String(date.getMinutes()).padStart(2, "0");

  return `${yyyy}-${MM}-${dd} ${HH}:${mm}`;
}

/*
API取得（共通）
*/
async function fetchStats(timeUnit, userid, companyCode) {
  const targetDate = formatDateTime(new Date());

  const res = await axios.post(
    `${API_BASE_URL}/achievment/search-sales-achievment`,
    {
      userId:  userid, 
      userCompanyCode: companyCode,
      //userTeamCode: dbUser.myteamcode,
      timeUnit: timeUnit,
      targetDate: targetDate,
    }
  );

  return transformData(res.data, timeUnit);
}

/*
テーブル + グラフ
*/
function TableSection({ title, color, data }) {
  return (
    <div className="mb-8">
      <h3 className={`text-lg font-bold mb-2 ${color}`}>{title}</h3>

      <div className="overflow-x-auto">
        <table className="min-w-full border border-gray-300 text-sm">
          <thead className="bg-gray-100">
            <tr>
              <th className="border p-2">単位</th>
              {metrics.map((m) => (
                <th key={m} className="border p-2">
                  {m}
                </th>
              ))}
            </tr>
          </thead>

          <tbody>
            {data.map((row) => (
              <tr key={row.label}>
                <td className="border p-2">{row.label}</td>

                {metrics.map((m) => (
                  <td key={m} className="border p-2 text-right">
                    {row[m]}
                  </td>
                ))}
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      <div className="h-64 mt-4">
        <ResponsiveContainer width="100%" height="100%">
          <BarChart data={data}>
            <XAxis dataKey="label" />
            <YAxis />
            <Tooltip />
            <Legend />

            {metrics.map((m, i) => (
              <Bar
                key={m}
                dataKey={m}
                fill={`rgba(59,130,246,${0.3 + i * 0.1})`}
              />
            ))}
          </BarChart>
        </ResponsiveContainer>
      </div>
    </div>
  );
}

/*
データ取得コンポーネント
*/
function StatsView({ timeUnit }) {
  const { dbUser, currentUser } = useAuth();

  const [orgData, setOrgData] = useState([]);
  const [personalData, setPersonalData] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!dbUser || !currentUser) return;
    load();
  }, [timeUnit, dbUser, currentUser]);

  async function load() {
    try {
      setLoading(true);

      const [orgResult, personalResult] = await Promise.all([
        fetchStats(timeUnit, null, dbUser.myCompanyCode), // 組織
        fetchStats(timeUnit, currentUser.uid, null), // 個人
      ]);

      setOrgData(orgResult);
      setPersonalData(personalResult);
    } catch (e) {
      console.error("APIエラー:", e);
    } finally {
      setLoading(false);
    }
  }

  if (loading) {
    return <div className="p-4">Loading...</div>;
  }

  return (
    <>
      <TableSection
        title="組織全体"
        color="text-blue-600"
        data={orgData}
      />

      <TableSection
        title="個人"
        color="text-green-600"
        data={personalData}
      />
    </>
  );
}

/*
メイン
*/
export default function SalesDashboard() {
  const tabs = [
    { label: "時間別", value: 1 },
    { label: "日別", value: 2 },
    { label: "週別", value: 3 },
    { label: "月別", value: 4 },
  ];

  const [timeUnit, setTimeUnit] = useState(1);

  return (
    <div className="p-6 space-y-4">
      <h1 className="text-2xl font-bold">営業実績ダッシュボード</h1>

      <div className="flex space-x-2 border-b">
        {tabs.map((t) => (
          <button
            key={t.value}
            onClick={() => setTimeUnit(t.value)}
            className={`px-4 py-2 font-medium border-b-2 ${
              timeUnit === t.value
                ? "border-blue-500 text-blue-600"
                : "border-transparent text-gray-500"
            }`}
          >
            {t.label}
          </button>
        ))}
      </div>

      <StatsView timeUnit={timeUnit} />
    </div>
  );
}