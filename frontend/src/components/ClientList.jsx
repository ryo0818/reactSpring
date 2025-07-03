import React from 'react';

const clients = [
  {
    id: 1,
    companyName: '株式会社ABC',
    phoneNumber: '03-1234-5678',
    callDate: '2025-07-03',
    callCount: 2,
    status: '対応中',
    staff: '田中'
  },
  {
    id: 2,
    companyName: 'XYZコンサル',
    phoneNumber: '06-9876-5432',
    callDate: '2025-07-02',
    callCount: 1,
    status: '完了',
    staff: '佐藤'
  }
];

// 状況に応じた色付きバッジを返す関数
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

const ClientList = () => {
  return (
    <div className="p-6 bg-gray-50 min-h-screen">
      <h1 className="text-2xl font-bold mb-6">📞 営業先リスト</h1>
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
            </tr>
          </thead>
          <tbody>
            {clients.map(client => (
              <tr key={client.id} className="hover:bg-gray-100">
                <td className="px-4 py-2 border">{client.companyName}</td>
                <td className="px-4 py-2 border">{client.phoneNumber}</td>
                <td className="px-4 py-2 border">{client.callDate}</td>
                <td className="px-4 py-2 border text-center">{client.callCount}</td>
                <td className="px-4 py-2 border text-center">
                  {getStatusBadge(client.status)}
                </td>
                <td className="px-4 py-2 border">{client.staff}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default ClientList;
