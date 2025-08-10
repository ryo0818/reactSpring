import React, { useEffect, useState } from 'react';
import { format } from 'date-fns'; // 日付のフォーマット用ライブラリ
import axios from 'axios';

const API_BASE_URL = import.meta.env.VITE_API_HOST;
const today = format(new Date(), 'yyyy-MM-dd');

const statusMap = {
  A: '対応済み',
  B: '要対応',
  C: '折返し待ち',
  未対応: '未対応'
};

function ClientList() {
  const [clients, setClients] = useState([]);
  const [newClient, setNewClient] = useState({
    id: null,
    companyName: '',
    phoneNumber: '',
    callDate: today,
    callCount: 1,
    status: '未対応',
    staff: '',
    remarks: '',
    url: '',
    address: ''
  });
  const [editIndex, setEditIndex] = useState(null);

  useEffect(() => {
    fetchClients();
  }, []);

  const fetchClients = async () => {
    try {
      const res = await axios.post(`${API_BASE_URL}/sales/list-view`);
      const formatted = res.data.map((item, index) => ({
        id: index + 1,
        companyName: item.companyName,
        phoneNumber: item.phoneNumber,
        callDate: item.callDate,
        callCount: item.callCount,
        status: statusMap[item.status] || '未対応',
        staff: item.staffName,
        remarks: item.note,
        url: item.url || '',
        address: item.address || ''
      }));
      setClients(formatted);
    } catch (err) {
      console.error('Error fetching client data:', err);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setNewClient(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = () => {
    if (editIndex !== null) {
      const updated = [...clients];
      updated[editIndex] = newClient;
      setClients(updated);
      setEditIndex(null);
      axios.post(`${API_BASE_URL}/sales/list-edit-form`, {...newClient,});

    } else {
      console.log("確認2",newClient)
      setClients(prev => [...prev, { ...newClient, id: prev.length + 1 }]);
      axios.post(`${API_BASE_URL}/sales/list-add-new`, {...newClient,});
    }
    setNewClient({
      id: null,
      companyName: '',
      phoneNumber: '',
      callDate: '',
      callCount: 1,
      status: '未対応',
      staff: '',
      remarks: '',
      url: '',
      address: ''
    });
  };

  const handleEdit = (index) => {
    setNewClient(clients[index]);
    setEditIndex(index);
  };

  return (
    <div className="p-4">
      <h2 className="text-lg font-bold mb-4">クライアント一覧</h2>
      <div className="grid grid-cols-2 gap-2 mb-4">
        <input className="border p-2 flex-1" name="companyName" value={newClient.companyName} onChange={handleChange} placeholder="会社名" />
        <input className="border p-2 flex-1" name="phoneNumber" value={newClient.phoneNumber} onChange={handleChange} placeholder="電話番号" />
        <input className="border p-2 flex-1" name="callDate" type="date" value={newClient.callDate} onChange={handleChange} />
        <input className="border p-2 flex-1" name="callCount" type="number" value={newClient.callCount} onChange={handleChange} placeholder="架電回数" />
        <select className="border p-2 flex-1" name="status" value={newClient.status} onChange={handleChange}>
          <option value="未対応">未対応</option>
          <option value="対応済み">対応済み</option>
          <option value="要対応">要対応</option>
          <option value="折返し待ち">折返し待ち</option>
        </select>
        <input className="border p-2 flex-1" name="staff" value={newClient.staff} onChange={handleChange} placeholder="担当者" />
        <input className="border p-2 flex-1" name="url" value={newClient.url} onChange={handleChange} placeholder="会社URL" />
        <input className="border p-2 flex-1" name="address" value={newClient.address} onChange={handleChange} placeholder="住所" />
        <input className="border p-2 col-span-2" name="remarks" value={newClient.remarks} onChange={handleChange} placeholder="備考" />
      </div>
      <button className="bg-blue-500 text-white px-4 py-2 rounded" onClick={handleSubmit}>
        {editIndex !== null ? '更新' : '追加'}
      </button>

      <table className="min-w-full mt-6 border-collapse border border-gray-200">
        <thead>
          <tr className="bg-gray-100">
            <th className="px-4 py-2 border text-left">会社名</th>
            <th className="px-4 py-2 border text-left">電話番号</th>
            <th className="px-4 py-2 border text-left">架電日</th>
            <th className="px-4 py-2 border text-left">回数</th>
            <th className="px-4 py-2 border text-left">ステータス</th>
            <th className="px-4 py-2 border text-left">担当</th>
            <th className="px-4 py-2 border text-left">URL</th>
            <th className="px-4 py-2 border text-left">住所</th>
            <th className="px-4 py-2 border text-left">備考</th>
            <th className="px-4 py-2 border text-left">操作</th>
          </tr>
        </thead>
        <tbody>
          {clients.map((client, index) => (
            <tr key={index} className="hover:bg-gray-50">
              <td className="px-4 py-2 border">{client.companyName}</td>
              <td className="px-4 py-2 border">{client.phoneNumber}</td>
              <td className="px-4 py-2 border">{client.callDate}</td>
              <td className="px-4 py-2 border">{client.callCount}</td>
              <td className="px-4 py-2 border">{client.status}</td>
              <td className="px-4 py-2 border">{client.staff}</td>
              <td className="px-4 py-2 border">
                {client.url ? (
                  <a href={client.url} className="text-blue-600 underline" target="_blank" rel="noopener noreferrer">
                    リンク
                  </a>
                ) : (
                  '-'
                )}
              </td>
              <td className="px-4 py-2 border">{client.address}</td>
              <td className="px-4 py-2 border">{client.remarks}</td>
              <td className="px-4 py-2 border">
                <button className="text-blue-500 hover:underline" onClick={() => handleEdit(index)}>編集</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default ClientList;
