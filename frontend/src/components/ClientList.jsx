import React, { useState } from 'react';
import axios from "axios";

const getSalesLists = () => {
  axios
  .get("http://localhost:8080/api/sales/list-view")
  .then((res) => console.log(res.data))
      .catch((err) => {
        console.error(err);
      });
}
const test = getSalesLists();

// ステータスバッジ
const getStatusBadge = (status) => {
  const baseClass = "px-2 py-1 rounded-full text-sm font-semibold";
  switch (status) {
    case '対応中':
      return <span className={`${baseClass} bg-yellow-100 text-yellow-800`}>対応中</span>;
    case '完了':
      return <span className={`${baseClass} bg-green-100 text-green-800`}>完了</span>;
    case '未対応':
      return <span className={`${baseClass} bg-red-100 text-red-800`}>未対応</span>;
    default:
      return <span className={`${baseClass} bg-gray-200 text-gray-700`}>{status}</span>;
  }
};

// 初期クライアントデータ
const initialClients = [
  {
    id: 1,
    companyName: '株式会社ABC',
    phoneNumber: '03-1234-5678',
    callDate: '2025-07-03',
    callCount: 2,
    status: '対応中',
    staff: '田中',
    remarks: '次回は担当者変更予定'
  },
  {
    id: 2,
    companyName: 'XYZコンサル',
    phoneNumber: '06-9876-5432',
    callDate: '2025-07-02',
    callCount: 1,
    status: '完了',
    staff: '佐藤',
    remarks: ''
  }
];

const ClientList = () => {
  const [clients, setClients] = useState(initialClients);
  const [newClient, setNewClient] = useState({
    id: null,
    companyName: '',
    phoneNumber: '',
    callDate: '',
    callCount: 1,
    status: '未対応',
    staff: '',
    remarks: ''
  });
  const [editId, setEditId] = useState(null);

  // 入力変更
  const handleChange = (e) => {
    const { name, value } = e.target;
    setNewClient({ ...newClient, [name]: value });
  };

  // 追加・更新
  const handleSubmit = (e) => {
    e.preventDefault();
    if (editId !== null) {
      setClients(clients.map(client =>
        client.id === editId ? { ...newClient, id: editId } : client
      ));
      setEditId(null);
    } else {
      const newId = Math.max(...clients.map(c => c.id), 0) + 1;
      setClients([...clients, { ...newClient, id: newId }]);
    }

    // 初期化
    setNewClient({
      id: null,
      companyName: '',
      phoneNumber: '',
      callDate: '',
      callCount: 1,
      status: '未対応',
      staff: '',
      remarks: ''
    });
  };

  // 編集モード
  const handleEdit = (client) => {
    setNewClient(client);
    setEditId(client.id);
  };

  return (
    <div className="p-6 bg-gray-50 min-h-screen">
      {/* フォーム */}
      <form onSubmit={handleSubmit} className="mb-6 bg-white p-4 rounded shadow space-y-2">
        <div className="flex flex-wrap gap-4">
          <input className="border p-2 flex-1" name="companyName" value={newClient.companyName} onChange={handleChange} placeholder="会社名" required />
          <input className="border p-2 flex-1" name="phoneNumber" value={newClient.phoneNumber} onChange={handleChange} placeholder="電話番号" required />
          <input type="date" className="border p-2" name="callDate" value={newClient.callDate} onChange={handleChange} required />
          <input type="number" min="1" className="border p-2 w-20" name="callCount" value={newClient.callCount} onChange={handleChange} />
          <select className="border p-2" name="status" value={newClient.status} onChange={handleChange}>
            <option value="未対応">未対応</option>
            <option value="対応中">対応中</option>
            <option value="完了">完了</option>
          </select>
          <input className="border p-2 flex-1" name="staff" value={newClient.staff} onChange={handleChange} placeholder="担当者" required />
        </div>
        <textarea
          name="remarks"
          value={newClient.remarks}
          onChange={handleChange}
          placeholder="備考"
          className="border p-2 w-full mt-2 rounded"
        />
        <button type="submit" className="bg-blue-500 text-white px-4 py-2 rounded">
          {editId ? '更新' : '追加'}
        </button>
      </form>

      {/* テーブル */}
      <div className="overflow-x-auto">
        <table className="min-w-full bg-white border rounded-lg shadow">
          <thead className="bg-blue-100">
            <tr>
              <th className="px-4 py-2 border text-left">会社名</th>
              <th className="px-4 py-2 border text-left">電話番号</th>
              <th className="px-4 py-2 border text-left">架電日付</th>
              <th className="px-4 py-2 border text-center">架電数</th>
              <th className="px-4 py-2 border text-center">状況</th>
              <th className="px-4 py-2 border text-left">担当者</th>
              <th className="px-4 py-2 border text-left">備考</th>
              <th className="px-4 py-2 border text-center">操作</th>
            </tr>
          </thead>
          <tbody>
            {clients.map(client => (
              <tr key={client.id} className="hover:bg-gray-100">
                <td className="px-4 py-2 border">{client.companyName}</td>
                <td className="px-4 py-2 border">{client.phoneNumber}</td>
                <td className="px-4 py-2 border">{client.callDate}</td>
                <td className="px-4 py-2 border text-center">{client.callCount}</td>
                <td className="px-4 py-2 border text-center">{getStatusBadge(client.status)}</td>
                <td className="px-4 py-2 border">{client.staff}</td>
                <td className="px-4 py-2 border">{client.remarks}</td>
                <td className="px-4 py-2 border text-center">
                  <button onClick={() => handleEdit(client)} className="text-blue-500 hover:underline">編集</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default ClientList;
