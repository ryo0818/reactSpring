import React, { useState, useEffect } from 'react';
import { DataGrid, GridToolbar } from '@mui/x-data-grid';
import axios from 'axios';
import { format, parseISO } from 'date-fns';
import { useAuth } from '../contexts/AuthContext';

const API_BASE_URL = import.meta.env.VITE_API_HOST;

export default function ClientList() {
  const { dbUser, currentUser } = useAuth();
  const [rows, setRows] = useState([]);
  const [modifiedRows, setModifiedRows] = useState({});
  const [statusOptions, setStatusOptions] = useState([]);
  const [editingId, setEditingId] = useState(null);

  const [newClient, setNewClient] = useState({
    companyName: '',
    phoneNumber: '',
    callDate: format(new Date(), "yyyy-MM-dd'T'HH:mm"),
    callCount: 1,
    status: '',
    staff: '',
    remarks: '',
    url: '',
    address: '',
    industry: ''
  });

  // フォームリセット関数
const handleClearForm = () => {
  setNewClient({
    companyName: '',
    phoneNumber: '',
    callDate: format(new Date(), "yyyy-MM-dd'T'HH:mm"),
    callCount: 1,
    status: statusOptions[0] || '',
    staff: dbUser ? dbUser.userName : '',
    remarks: '',
    url: '',
    address: '',
    industry: ''
  });
  setEditingId(null); // 編集状態解除
};

  // データ取得
  useEffect(() => {
    if (!currentUser) return;

    const fetchStatusOptions = async () => {
      try {
        const res = await axios.post(`${API_BASE_URL}/sales/get-statslist`, { mycompanycode: dbUser.myCompanyCode });
        const statusNames = res.data.map(item => item.statusName);
        setStatusOptions(statusNames || []);
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
        const res = await axios.post(`${API_BASE_URL}/sales/list-view`, { mycompanycode: dbUser.myCompanyCode });
        console.log("Fetched clients:", res.data);
        const formatted = res.data.map((item, index) => ({
          id: item.id,
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
        setRows(formatted);
      } catch (err) {
        console.error(err);
      }
    };

    fetchStatusOptions();
    fetchClients();
  }, [currentUser, dbUser]);

  // フォーム変更
  const handleNewChange = (e) => {
    const { name, value } = e.target;
    setNewClient(prev => ({ ...prev, [name]: value }));
  };

  // 編集ボタン
  const handleEdit = (row) => {
    setNewClient({
      companyName: row.companyName,
      phoneNumber: row.phoneNumber,
      callDate: format(new Date(row.callDate), "yyyy-MM-dd'T'HH:mm"),
      callCount: row.callCount,
      status: row.status,
      staff: row.staff,
      remarks: row.remarks,
      url: row.url,
      address: row.address,
      industry: row.industry
    });
    setEditingId(row.id);
  };

  // 追加・更新
  const handleAddOrUpdate = async () => {
      try {
    if (editingId) {
      // 更新
      console.log("Updating client ID:", editingId, newClient);
      setRows(prev =>
        prev.map(r =>
          r.id === editingId ? { ...r, ...newClient, callDate: parseISO(newClient.callDate) } : r
        )
      );
      setModifiedRows(prev => ({
        ...prev,
        [editingId]: { ...newClient, id: editingId }
      }));
    } else {
       // 新規追加処理
      const payload = {
        ...newClient,
        callDate: format(parseISO(newClient.callDate), 'yyyy-MM-dd HH:mm')
      };
      console.log("Adding new client:", payload);

      // バックエンドにPOST（DB採番id付き）
      const res = await axios.post(`${API_BASE_URL}/sales/insert-sales`, payload);

      // state更新（レスポンスを追加）
      setRows(prev => [...prev, res.data]);
    }

    // フォームリセット
    setNewClient({
      companyName: '',
      phoneNumber: '',
      callDate: format(new Date(), "yyyy-MM-dd'T'HH:mm"),
      callCount: 1,
      status: statusOptions[0] || '',
      staff: dbUser ? dbUser.userName : '',
      remarks: '',
      url: '',
      address: '',
      industry: ''
    });
    setEditingId(null);
      } catch (err) {
    console.error("handleAddOrUpdate error:", err);
  }
  };

  // 削除・復元
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

  // DataGrid上の直接編集
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
    await axios.post(`${API_BASE_URL}/sales/list-edit-form`, updates);
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
      type: 'dateTime'
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
    {
      field: 'actions',
      headerName: '操作',
      flex: 0.5,
      renderCell: (params) => (
        <div className="flex gap-1">
          <button
            className="px-2 py-1 bg-yellow-500 text-white rounded"
            onClick={() => handleEdit(params.row)}
          >
            編集
          </button>
          <button
            className={`px-2 py-1 rounded ${params.row.isDeleted ? 'bg-gray-400 text-white' : 'bg-red-500 text-white'}`}
            onClick={() => handleToggleDelete(params.row.id)}
          >
            {params.row.isDeleted ? '復元' : '削除'}
          </button>
        </div>
      )
    }
  ];

  return (
    <div className="p-4">
      <h2 className="text-lg font-bold mb-4">クライアント一覧</h2>

      {/* 新規追加 / 編集フォーム */}
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

      <button className="bg-green-500 text-white px-4 py-2 rounded mb-4" onClick={handleAddOrUpdate}>
        {editingId ? '更新' : '追加'}
      </button>
        <button className="bg-gray-500 text-white px-4 py-2 rounded  mb-4 ml-2" onClick={handleClearForm}>
        クリア
        </button>
      {Object.keys(modifiedRows).length > 0 && (
        <button className="bg-blue-500 text-white px-4 py-2 rounded mb-4 ml-2" onClick={handleSaveAll}>
          変更を保存
        </button>
      )}

      <div style={{ height: 600, width: '100%' }}>
        <DataGrid
          rows={rows}
          columns={columns}
          processRowUpdate={handleProcessRowUpdate}
          experimentalFeatures={{ newEditingApi: true }}
          getRowClassName={(params) =>
            params.row.isDeleted ? 'bg-gray-200 line-through text-gray-500' :
              modifiedRows[params.id] ? 'bg-yellow-100' : ''
          }
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
