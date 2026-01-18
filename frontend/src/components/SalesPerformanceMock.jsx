import React, { useState } from "react";
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  Tooltip,
  ResponsiveContainer,
  Legend,
} from "recharts";

const metrics = ["架電数", "接続数", "オーナ数", "フル", "アポ数"];

function generateData(labels) {
  return labels.map((label) => {
    const org = {};
    const personal = {};
    metrics.forEach((m) => {
      org[m] = Math.floor(Math.random() * 50) + 20;
      personal[m] = Math.floor(Math.random() * 30) + 10;
    });
    return { label, org, personal };
  });
}

function kpiRate(actual, target = 100) {
  return Math.round((actual / target) * 100);
}

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
                <th key={m} className="border p-2">{m}</th>
              ))}
              <th className="border p-2">KPI達成率</th>
            </tr>
          </thead>
          <tbody>
            {data.map((row) => {
              const total = metrics.reduce((s, m) => s + row[type][m], 0);
              return (
                <tr key={row.label}>
                  <td className="border p-2">{row.label}</td>
                  {metrics.map((m) => (
                    <td key={m} className="border p-2 text-right">{row[type][m]}</td>
                  ))}
                  <td className="border p-2 text-right font-semibold">
                    {kpiRate(total)}%
                  </td>
                </tr>
              );
            })}
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
              <Bar key={m} dataKey={m} fill={type === "org" ? `rgba(59,130,246,${0.3 + i * 0.1})` : `rgba(34,197,94,${0.3 + i * 0.1})`} />
            ))}
          </BarChart>
        </ResponsiveContainer>
      </div>
    </div>
  );
}

function TimeTab() {
  const labels = Array.from({ length: 11 }, (_, i) => `${9 + i}:00`);
  const data = generateData(labels);
  return (
    <>
      <TableSection title="組織全体" color="text-blue-600" data={data} type="org" />
      <TableSection title="個人" color="text-green-600" data={data} type="personal" />
    </>
  );
}

function DayTab() {
  const labels = Array.from({ length: 30 }, (_, i) => `${i + 1}日`);
  const data = generateData(labels);
  return (
    <>
      <TableSection title="組織全体" color="text-blue-600" data={data} type="org" />
      <TableSection title="個人" color="text-green-600" data={data} type="personal" />
    </>
  );
}

function WeekTab() {
  const labels = ["第1週", "第2週", "第3週", "第4週", "第5週"];
  const data = generateData(labels);
  return (
    <>
      <TableSection title="組織全体" color="text-blue-600" data={data} type="org" />
      <TableSection title="個人" color="text-green-600" data={data} type="personal" />
    </>
  );
}

function MonthTab() {
  const labels = Array.from({ length: 12 }, (_, i) => `${i + 1}月`);
  const data = generateData(labels);
  return (
    <>
      <TableSection title="組織全体" color="text-blue-600" data={data} type="org" />
      <TableSection title="個人" color="text-green-600" data={data} type="personal" />
    </>
  );
}

// 年間（月別）データ
const yearlyMonthlyData = Array.from({ length: 12 }, (_, i) => `${i + 1}月`).map((m) => ({
  label: m,
  calls: Math.floor(Math.random() * 900),
  connects: Math.floor(Math.random() * 600),
  owners: Math.floor(Math.random() * 300),
  full: Math.floor(Math.random() * 250),
  appointments: Math.floor(Math.random() * 180),
}));

export default function SalesDashboardMock() {
  const tabs = ["時間別", "日別", "週別", "月別"];
  const [active, setActive] = useState("時間別");

  return (
    <div className="p-6 space-y-4">
      <h1 className="text-2xl font-bold">営業実績ダッシュボード（モック）</h1>

      <div className="flex space-x-2 border-b">
        {tabs.map((t) => (
          <button
            key={t}
            onClick={() => setActive(t)}
            className={`px-4 py-2 font-medium border-b-2 ${
              active === t
                ? "border-blue-500 text-blue-600"
                : "border-transparent text-gray-500"
            }`}
          >
            {t}
          </button>
        ))}
      </div>

      <div>
        {active === "時間別" && <TimeTab />}
        {active === "日別" && <DayTab />}
        {active === "週別" && <WeekTab />}
        {active === "月別" && <MonthTab />}
      </div>
    </div>
  );
}
