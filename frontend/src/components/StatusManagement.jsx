import React, { useEffect, useState } from "react";
import axios from "axios";
import { useAuth } from "../contexts/AuthContext";

const API_BASE_URL = import.meta.env.VITE_API_HOST;

export default function StatusManagement() {
  const { dbUser } = useAuth();
  const [statuses, setStatuses] = useState([]);
  const [newStatusName, setNewStatusName] = useState("");
  const [newStatusLevel, setNewStatusLevel] = useState(0);

  // ステータス一覧取得
  const fetchStatuses = async () => {
    if (!dbUser) return;
    try {
      const res = await axios.post(`${API_BASE_URL}/login/get-statslist`, {
        userCompanyCode: dbUser.myCompanyCode,
      });
      setStatuses(res.data);
    } catch (err) {
      console.error("ステータス取得失敗:", err);
    }
  };

  useEffect(() => {
    fetchStatuses();
  }, [dbUser]);

  // 追加
  const handleAdd = async () => {
    if (!newStatusName) return;
    await axios.post(`${API_BASE_URL}/login/add-status`, {
      userCompanyCode: dbUser.myCompanyCode,
      statusName: newStatusName,
      statusLevel: newStatusLevel,
    });
    setNewStatusName("");
    setNewStatusLevel(0);
    fetchStatuses();
  };

  // 削除
  const handleDelete = async (id) => {
    await axios.post(`${API_BASE_URL}/login/delete-status`, { statusId: id });
    fetchStatuses();
  };

  // 編集
  const handleSave = async (status) => {
    if (!status.statusName) return;
    await axios.post(`${API_BASE_URL}/login/update-status`, {
      statusId: status.statusId,
      statusName: status.statusName,
      statusLevel: status.statusLevel,
    });
    fetchStatuses();
  };

  return (
    <div className="p-4 max-w-3xl mx-auto">
      <h2 className="text-xl font-bold mb-4">ステータス管理</h2>

      {/* 新規追加 */}
      <div className="grid grid-cols-1 sm:grid-cols-2 gap-2 mb-4">
        <input
          className="border p-2 rounded"
          placeholder="新規ステータス名"
          value={newStatusName}
          onChange={(e) => setNewStatusName(e.target.value)}
        />
        <input
          className="border p-2 rounded"
          type="number"
          placeholder="レベル"
          value={newStatusLevel}
          onChange={(e) => setNewStatusLevel(Number(e.target.value))}
        />
        <button
          className="bg-green-500 text-white px-4 py-2 rounded sm:col-span-2"
          onClick={handleAdd}
        >
          追加
        </button>
      </div>

      {/* ステータス一覧 */}
      <ul className="grid grid-cols-1 sm:grid-cols-2 gap-2">
        {statuses.map((s) => (
          <li
            key={s.statusId}
            className="flex flex-col sm:flex-row items-start sm:items-center justify-between p-2 border rounded gap-2"
          >
            <input
              type="text"
              className="border p-1 flex-1 rounded"
              value={s.statusName}
              onChange={(e) => {
                const updated = [...statuses];
                const idx = updated.findIndex((item) => item.statusId === s.statusId);
                updated[idx].statusName = e.target.value;
                setStatuses(updated);
              }}
            />
            <input
              type="number"
              className="border p-1 w-20 rounded"
              value={s.statusLevel}
              onChange={(e) => {
                const updated = [...statuses];
                const idx = updated.findIndex((item) => item.statusId === s.statusId);
                updated[idx].statusLevel = Number(e.target.value);
                setStatuses(updated);
              }}
            />
            <div className="flex gap-2 mt-2 sm:mt-0">
              <button
                className="bg-blue-500 text-white px-2 py-1 rounded"
                onClick={() => handleSave(s)}
              >
                保存
              </button>
              <button
                className="bg-red-500 text-white px-2 py-1 rounded"
                onClick={() => handleDelete(s.statusId)}
              >
                削除
              </button>
            </div>
          </li>
        ))}
      </ul>
    </div>
  );
}
