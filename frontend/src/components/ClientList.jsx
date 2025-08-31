import React, { useState, useEffect } from 'react';
import { DataGrid, GridToolbar } from '@mui/x-data-grid';
import axios from 'axios';
import { format, parseISO } from 'date-fns';
import { useAuth } from '../contexts/AuthContext';

const API_BASE_URL = import.meta.env.VITE_API_HOST;

//const statusOptions = ['未対応', '対応済み', '要対応', '折返し待ち'];

export default function ClientList() {
  const { dbUser,currentUser } = useAuth();
  const [rows, setRows] = useState([]);
  const [modifiedRows, setModifiedRows] = useState({});
  const [statusOptions, setStatusOptions] = useState([]);
  const [newClient, setNewClient] = useState({
    companyName: '',
    phoneNumber: '',
    callDate: format(new Date(), "yyyy-MM-dd'T'HH:mm"), // 分まで表示
    callCount: 1,
    status: '',
    staff: '',
    remarks: '',
    url: '',
    address: '',
    industry: ''
  });

  // データ取得
  useEffect(() => {
  if (!currentUser) return;  // dbUser が null なら実行しない

  const fetchStatusOptions = async () => {
    try {
      console.log("DBユーザ情報:", dbUser);
      console.log("dd",dbUser.myCompanyCode)
      console.log("google:",currentUser);
      const res = await axios.post(`${API_BASE_URL}/sales/get-statslist`, {
        mycompanycode: dbUser.myCompanyCode
      });
      console.log("ステータスオプション:", res.data);

      const statusNames = res.data.map(item => item.statusName);
      setStatusOptions(statusNames || []);

      // 最初のステータスをデフォルト値にする
      if (statusNames.length > 0) {
        setNewClient(prev => ({
          ...prev,
          status: statusNames[0],
          staff: dbUser.userName || ''
        }));
      }
    } catch (err) {
      console.error("ステータス取得失敗:", err);
    }
  };

  const fetchClients = async () => {
    try {
      const res = await axios.post(`${API_BASE_URL}/sales/list-view`);
      const formatted = res.data.map((item, index) => ({
        id: index + 1,
        companyName: item.companyName,
        phoneNumber: item.phoneNumber,
        callDate: item.callDate
          ? parseISO(item.callDate.includes('T') ? item.callDate : item.callDate + 'T00:00')
          : new Date(),
        callCount: item.callCount,
        status: item.status || '未対応',
        staff: item.staffName,
        remarks: item.note,
        url: item.url || '',
        address: item.address || '',
        industry: item.industry || '',
        isDeleted: false
      }));
      console.log(formatted);
      setRows(formatted);
    } catch (err) {
      console.error(err);
    }
  };

  fetchClients();
  fetchStatusOptions();

}, []);
  // 新規追加フォーム
  const handleNewChange = (e) => {
    const { name, value } = e.target;
    setNewClient(prev => ({ ...prev, [name]: value }));
  };

  const handleAddNew = async () => {
    const newId = rows.length ? rows[rows.length - 1].id + 1 : 1;
    const clientToAdd = { ...newClient, id: newId, callDate: parseISO(newClient.callDate) };
    setRows(prev => [...prev, clientToAdd]);

    // リセット
    setNewClient({
      companyName: '',
      phoneNumber: '',
      callDate: format(new Date(), "yyyy-MM-dd'T'HH:mm"),
      callCount: 1,
      status: '未対応',
      staff: dbUser ? dbUser.userName : '',
      remarks: '',
      url: '',
      address: '',
      industry: ''
    });

    await axios.post(`${API_BASE_URL}/sales/list-add-new`, {
      ...clientToAdd,
      callDate: format(clientToAdd.callDate, 'yyyy-MM-dd HH:mm') // 保存時は分まで
    });
  };

  // 編集時の更新
  const handleProcessRowUpdate = (newRow) => {
    setRows(prev => prev.map(r => (r.id === newRow.id ? newRow : r)));
    setModifiedRows(prev => ({ ...prev, [newRow.id]: newRow }));
    return newRow;
  };

  // まとめて保存
  const handleSaveAll = async () => {
    const updates = Object.values(modifiedRows).map(row => ({
      ...row,
      callDate: format(new Date(row.callDate), 'yyyy-MM-dd HH:mm'),
      isDeleted: row.isDeleted ?? false
    }));
    axios.post(`${API_BASE_URL}/sales/list-edit-form`, updates);
    setModifiedRows({});
  };

  const columns = [
    { field: 'companyName', headerName: '会社名', flex: 1, editable: true },
    { field: 'phoneNumber', headerName: '電話番号', flex: 1, editable: true },
    { field: 'industry', headerName: '業界', flex: 1, editable: true },
    { 
      field: 'callDate',
      headerName: '架電日',
      flex: 1,
      editable: true,
      type: 'dateTime',
    },
    { field: 'callCount', headerName: '回数', flex: 0.5, editable: true, type: 'number' },
    {
      field: 'status',
      headerName: 'ステータス',
      flex: 1,
      editable: true,
      type: 'singleSelect',
      valueOptions: statusOptions
    },
    { field: 'staff', headerName: '担当', flex: 1, editable: true },
    {
      field: 'url',
      headerName: 'URL',
      flex: 1,
      editable: true,
      renderCell: (params) =>
        params.value ? (
          <a href={params.value} target="_blank" rel="noopener noreferrer" className="text-blue-600 underline">
            リンク
          </a>
        ) : '-'
    },
    { field: 'address', headerName: '住所', flex: 1, editable: true },
    { field: 'remarks', headerName: '備考', flex: 1, editable: true },
    {field: 'actions',headerName: '操作',flex: 0.5,
    renderCell: (params) => (
    <button
      className={`px-2 py-1 text-sm rounded ${params.row.isDeleted ? 'bg-gray-400 text-white' : 'bg-red-500 text-white'}`}
      onClick={() => handleToggleDelete(params.row.id)}
    >{params.row.isDeleted ? '復元' : '削除'}</button>)}
  ];
  const handleToggleDelete = (id) => {
  setRows(prev =>
    prev.map(row =>
      row.id === id ? { ...row, isDeleted: !row.isDeleted } : row
    )
  );
  setModifiedRows(prev => ({
    ...prev,
    [id]: {
      ...rows.find(r => r.id === id),
      isDeleted: !rows.find(r => r.id === id)?.isDeleted
    }
  }));
};

  return (
    <div className="p-4">
      <h2 className="text-lg font-bold mb-4">クライアント一覧</h2>

      {/* 新規追加フォーム */}
      <div className="grid grid-cols-2 gap-2 mb-4">
        <input className="border p-2 flex-1 rounded" name="companyName" value={newClient.companyName} onChange={handleNewChange} placeholder="会社名" />
        <input className="border p-2 flex-1 rounded" name="phoneNumber" value={newClient.phoneNumber} onChange={handleNewChange} placeholder="電話番号" />
        <input className="border p-2 flex-1 rounded" name="industry" value={newClient.industry} onChange={handleNewChange} placeholder="業界" />
        <input className="border p-2 flex-1 rounded" type="datetime-local" name="callDate" value={newClient.callDate} onChange={handleNewChange} />
        <input className="border p-2 flex-1 rounded" type="number" name="callCount" value={newClient.callCount} onChange={handleNewChange} placeholder="架電回数" />
        <select className="border p-2 flex-1 rounded" name="status" value={newClient.status} onChange={handleNewChange}>
          {statusOptions.map(s => (<option key={s} value={s}>{s}</option>))}
        </select>
        <input className="border p-2 flex-1 rounded" name="staff" value={newClient.staff} onChange={handleNewChange} placeholder="担当" />
        <input className="border p-2 flex-1 rounded" name="url" value={newClient.url} onChange={handleNewChange} placeholder="会社URL" />
        <input className="border p-2 flex-1 rounded" name="address" value={newClient.address} onChange={handleNewChange} placeholder="住所" />
        <input className="border p-2 col-span-2 rounded" name="remarks" value={newClient.remarks} onChange={handleNewChange} placeholder="備考" />
      </div>

      <button className="bg-green-500 text-white px-4 py-2 rounded mb-4" onClick={handleAddNew}>追加</button>
      {Object.keys(modifiedRows).length > 0 && (
        <button className="bg-blue-500 text-white px-4 py-2 rounded mb-4 ml-2" onClick={handleSaveAll}>変更を保存</button>
      )}

      <div style={{ height: 600, width: '100%' }}>
        <DataGrid
          rows={rows}
          columns={columns}
          processRowUpdate={handleProcessRowUpdate}
          experimentalFeatures={{ newEditingApi: true }}
          getRowClassName={(params) => 
            params.row.isDeleted ? 'bg-gray-200 line-through text-gray-500' :
            modifiedRows[params.id] ? 'bg-yellow-100' : ''}
          disableSelectionOnClick
          components={{ Toolbar: GridToolbar }}
          pageSizeOptions={[50, 100, 200]}
          initialState={{ pagination: { paginationModel: { pageSize: 50, page: 0 } } }}
          pagination
        />
      </div>
    </div>
  );
}
