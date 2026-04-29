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
データ変換（saleFunnelAggList対応）
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
    let label;

    if (timeUnit === 1) {
      label = item.aggregatedDateTime?.slice(11, 16);
    } else {
      label = item.aggregatedDateTime?.slice(5, 10);
    }

    const org = {};
    const personal = {};

    Object.entries(mapping).forEach(([jpKey, apiKey]) => {
      org[jpKey] = item[apiKey] ?? 0;
      personal[jpKey] = 0; // 個人は未対応（API来たら差し替え）
    });

    return {
      label,
      org,
      personal,
    };
  });
}

/*
API取得
*/
async function fetchStats(timeUnit, dbUser, currentUser) {
  const now = new Date();
  const targetDate = formatDateTime(now); 
  const res = await axios.post(
    `${API_BASE_URL}/achievment/search-sales-achievment`,
    {
      userId: currentUser.uid,
      userCompanyCode:  dbUser.myCompanyCode,
      userTeamCode:  dbUser.myteamcode,
      timeUnit: timeUnit,
      //targetDate: "2026-04-01 00:00" ,
      targetDate: targetDate ,
    }
  );
  return transformData(res.data, timeUnit);
}

function formatDateTime(date) {
  const yyyy = date.getFullYear();
  const MM = String(date.getMonth() + 1).padStart(2, "0");
  const dd = String(date.getDate()).padStart(2, "0");
  const HH = String(date.getHours()).padStart(2, "0");
  const mm = String(date.getMinutes()).padStart(2, "0");

  return `${yyyy}-${MM}-${dd} ${HH}:${mm}`;
}

/*
テーブル + グラフ
*/
function TableSection({ title, color, data, type }) {
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
                    {row[type][m]}
                  </td>
                ))}
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      <div className="h-64 mt-4">
        <ResponsiveContainer width="100%" height="100%">
          <BarChart data={data.map((d) => ({ label: d.label, ...d[type] }))}>
            <XAxis dataKey="label" />
            <YAxis />
            <Tooltip />
            <Legend />

            {metrics.map((m, i) => (
              <Bar
                key={m}
                dataKey={m}
                fill={
                  type === "org"
                    ? `rgba(59,130,246,${0.3 + i * 0.1})`
                    : `rgba(34,197,94,${0.3 + i * 0.1})`
                }
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
  const { dbUser ,currentUser} = useAuth();
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    load();
  }, [timeUnit, dbUser]);

  async function load() {
    try {
      setLoading(true);
      const result = await fetchStats(timeUnit, dbUser,currentUser);
      const now = new Date();
      const targetDate = formatDateTime(now);
      console.log("dbUser:", dbUser);
      console.log("APIリクエスト日時:", targetDate);
      console.log(timeUnit)
      console.log(result)
      console.log("currentUser:", currentUser);
      setData(result);
    } catch (e) {
      console.error(e);
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
        data={data}
        type="org"
      />

      <TableSection
        title="個人"
        color="text-green-600"
        data={data}
        type="personal"
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