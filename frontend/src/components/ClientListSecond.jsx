import React, { useState, useEffect, use } from "react";
import { DataGrid, GridToolbar } from "@mui/x-data-grid";
import axios from "axios";
import { format, parseISO } from "date-fns";
import { useAuth } from "../contexts/AuthContext";
import { parse } from "date-fns";
const API_BASE_URL = import.meta.env.VITE_API_HOST;

export default function ClientListSecond() {
  const { dbUser, currentUser } = useAuth();
  const [rows, setRows] = useState([]);
  const [modifiedRows, setModifiedRows] = useState({});
  const [statusOptions, setStatusOptions] = useState([]);
  const [editingId, setEditingId] = useState(null);
  const [statusMap, setStatusMap] = useState({});
  const [isDirty, setIsDirty] = useState(false);
  const [isForceDisabled, setIsForceDisabled] = useState(false);
  const [pastRemarks, setPastRemarks] = useState("");

  const [newClient, setNewClient] = useState({
    companyName: "",
    phoneNumber: "",
    callDate: format(new Date(), "yyyy-MM-dd'T'HH:mm"),
    callCount: 1,
    status: "",
    staff: "",
    remarks: "",
    url: "",
    address: "",
    industry: "",
    priority: false,
    media: "",
    nextCallDate: "",
  });

  // フォーム変更
  const handleNewChange = (e) => {
    const { name, value, type, checked } = e.target;
    // ステータス変更時の監視
    if (name === "status" && editingId) {
      const oldRow = rows.find((r) => r.id === editingId);
      if (oldRow) {
        const statusChanged = oldRow.status !== value;
        setIsForceDisabled(statusChanged);
      }
    }

    setNewClient((prev) => ({
      ...prev,
      [name]: type === "checkbox" ? checked : value,
    }));
    setIsDirty(true);
  };

  // フォームリセット
  const handleClearForm = () => {
    setNewClient({
      companyName: "",
      phoneNumber: "",
      callDate: format(new Date(), "yyyy-MM-dd'T'HH:mm"),
      callCount: 1,
      status: statusOptions[0] || "",
      staff: dbUser ? dbUser.userName : "",
      remarks: "",
      url: "",
      address: "",
      industry: "",
      priority: false,
      media: "",
      nextCallDate: "",
    });
    setEditingId(null);
    setIsDirty(false);
  };

  // データ取得
  useEffect(() => {
    if (!currentUser) return;

    const fetchStatusOptions = async () => {
      try {
        console.log(
          "Fetching status options for company code:",
          dbUser.myCompanyCode
        );
        const res = await axios.post(`${API_BASE_URL}/login/get-statslist`, {
          userCompanyCode: dbUser.myCompanyCode,
        });
        console.log("Fetched status options:", res.data);
        const statusNames = res.data.map((item) => item.statusName);
        const statusList = res.data.map((s) => ({
          id: s.statusId,
          name: s.statusName,
        }));
        const sMap = Object.fromEntries(statusList.map((s) => [s.id, s.name]));
        setStatusMap(sMap);
        setStatusOptions(statusNames || []);
        if (statusNames.length > 0) {
          setNewClient((prev) => ({
            ...prev,
            status: statusNames[0],
            staff: dbUser.userName || "",
          }));
        }
        // ✅ ステータスマップができたタイミングで顧客一覧を取得
        fetchClients(sMap);
      } catch (err) {
        console.error("ステータス取得失敗:", err);
      }
    };

    const fetchClients = async (sMap) => {
      try {
        const res = await axios.post(`${API_BASE_URL}/sales/list-view`, {
          userCompanyCode: dbUser.myCompanyCode,
        });
        const hotdataMap = res.data.filter((task) => {
          return task.hotflg == true;
        });
        console.log("hotdataMap", hotdataMap);
        const formatted = hotdataMap.map((item) => ({
          id: item.saleId,
          companyName: item.clientCompanyName,
          phoneNumber: item.clientPhoneNumber,
          callDate: item.callDateTime
            ? parse(item.callDateTime, "yyyy-MM-dd HH:mm", new Date())
            : new Date(),
          callCount: item.callCount,
          status: sMap[item.statusId] || "未対応",
          staff: item.userStaff,
          remarks: item.remarks,
          url: item.url || "",
          address: item.clientAddress || "",
          industry: item.clientIndustry || "",
          priority: item.hotflg ?? false,
          isDeleted: false,
          media: item.media || "",
          nextCallDate: item.nextCallDate || "",
        }));
        setRows(formatted);
      } catch (err) {
        console.error(err);
      }
    };
    fetchStatusOptions();
    fetchClients();
  }, [currentUser, dbUser]);

  const handleProcessRowUpdate = (newRow) => {
    setRows((prev) => prev.map((r) => (r.id === newRow.id ? newRow : r)));
    setModifiedRows((prev) => ({ ...prev, [newRow.id]: newRow }));
    return newRow;
  };

  const injectMap = (data, user) => {
    const statusId = Object.entries(statusMap).find(
      ([id, name]) => name === data.status
    )?.[0];
    return {
      userTeamCode: user.myteamcode,
      saleId: user.id ?? data.id ?? null,
      userId: user.userId,
      userCompanyCode: user.myCompanyCode,
      clientIndustry: data.industry,
      clientCompanyName: data.companyName,
      clientPhoneNumber: data.phoneNumber,
      callDateTime: data.callDate,
      callCount: data.callCount,
      statusId: statusId,
      userStaff: data.staff,
      remarks: data.remarks,
      clientUrl: data.url,
      clientAddress: data.address,
      hotflg: data.priority ?? false,
      validFlg: data.isDeleted,
      media: data.media,
      nextCallDateTime: data.nextCallDate ?? null,
      history_flg: false,
    };
  };

  // 行クリック
  const handleRowClick = async (row) => {
        if (editingId && isDirty) {
      await handleAddOrUpdate();
    }
    setIsForceDisabled(false);
    setEditingId(row.id);
    setPastRemarks(row.remarks || "");
    setNewClient({
      companyName: row.companyName,
      phoneNumber: row.phoneNumber,
      callDate: format(new Date(), "yyyy-MM-dd'T'HH:mm"),
      callCount: row.callCount,
      status: row.status,
      staff: row.staff,
      remarks: "",
      url: row.url,
      address: row.address,
      industry: row.industry,
      priority: row.priority ?? false,
      media: row.media ?? "",
      nextCallDate: row.nextCallDate ?? "",
    });
    setEditingId(row.id);
    setIsDirty(false);
  };

  const handleEdit = (row) => {
    handleRowClick(row);
  };

  // 通常更新
  const handleAddOrUpdate = async () => {
    await updateRow(false);
  };

  // 架電数のみインクリメント
  const handleIncrementCallCount = async () => {
    await updateRow(true);
  };

  const updateRow = async (forceIncrement) => {
    if (!editingId) return;
    try {
      const history_flg = forceIncrement ? true : false;
      let mergedRemarks = pastRemarks;
      console.log("架電数+1:", forceIncrement);
      if (newClient.remarks?.trim()) {
        const now = format(new Date(), "yyyy/MM/dd HH:mm");
        mergedRemarks = `${pastRemarks}\n[${now}] ${newClient.remarks}`.trim();
      }
      const payload = {
        ...newClient,
        remarks: mergedRemarks,
        callDate: format(parseISO(newClient.callDate), "yyyy-MM-dd HH:mm"),
      };
      const submitData = injectMap(payload, {
        myCompanyCode: dbUser.myCompanyCode,
        userId: currentUser.uid,
        id: editingId,
        myteamcode: dbUser.myteamcode,
        history_flg: history_flg,
      });

      const oldRow = rows.find((r) => r.id === editingId);
      if (oldRow) {
        const newStatusId = Object.entries(statusMap).find(
          ([id, name]) => name === newClient.status
        )?.[0];
        const oldStatusId = Object.entries(statusMap).find(
          ([id, name]) => name === oldRow.status
        )?.[0];

        if (!forceIncrement) {
          // 通常更新
          if (newStatusId === oldStatusId) {
            submitData.callCount = oldRow.callCount; // 架電数は増やさない
            submitData.statusId = 0;
          } else {
            submitData.callCount = oldRow.callCount + 1;
            submitData.history_flg = true;
          }
        } else {
          // 強制インクリメント
          submitData.callCount = oldRow.callCount + 1;
          submitData.statusId = 0;
          submitData.history_flg = true;
        }
      }
      await axios.post(`${API_BASE_URL}/sales/update-salse`, [submitData]);
      setRows((prev) =>
        prev.map((r) =>
          r.id === editingId
            ? {
                ...r,
                ...newClient,
                callDate: parseISO(newClient.callDate),
                remarks: mergedRemarks,
                callCount: submitData.callCount,
              }
            : r
        )
      );
      handleClearForm();
    } catch (err) {
      console.error("handleAddOrUpdate error:", err);
    }
  };
  // 削除・復元
  const handleToggleDelete = (id) => {
    setRows((prev) =>
      prev.map((row) =>
        row.id === id ? { ...row, isDeleted: !row.isDeleted } : row
      )
    );
    const targetRow = rows.find((r) => r.id === id);
    if (!targetRow) return;
    if (targetRow.isDeleted) {
      // 復元 → modifiedRowsから削除（通常行に戻す）
      setModifiedRows((prev) => {
        const updated = { ...prev };
        delete updated[id];
        return updated;
      });
    } else {
      // 削除 → modifiedRowsに追加
      setModifiedRows((prev) => ({
        ...prev,
        [id]: { ...targetRow, isDeleted: true },
      }));
    }
  };

  const handleSaveAll = async () => {
    const updates = Object.values(modifiedRows).map((row) => ({
      ...row,
      callDate: format(new Date(row.callDate), "yyyy-MM-dd HH:mm"),
      isDeleted: row.isDeleted ?? false,
    }));

    const mappedList = updates.map((data) =>
      injectMap(data, {
        myCompanyCode: dbUser.myCompanyCode,
        userId: currentUser.uid,
        id: editingId ? editingId : null,
        myteamcode: dbUser.myteamcode,
      })
    );
    console.log("injectMap結果:", mappedList);
    await axios.post(`${API_BASE_URL}/sales/update-salse`, [mappedList]);
    setModifiedRows({});
  };

  const columns = [
    { field: "companyName", headerName: "会社名", flex: 1, editable: false },
    { field: "media", headerName: "媒体", flex: 1, editable: false },
    { field: "phoneNumber", headerName: "電話番号", flex: 1, editable: false },
    {
      field: "callDate",
      headerName: "架電日",
      flex: 1,
      editable: false,
      type: "dateTime",
    },
        {
      field: "nextCallDate",
      headerName: "再架電日",
      flex: 1,
      editable: false,
      type: "dateTime",
    },
    {
      field: "callCount",
      headerName: "回数",
      flex: 0.5,
      editable: false,
      type: "number",
    },
    {
      field: "status",
      headerName: "ステータス",
      flex: 1,
      editable: false,
      type: "singleSelect",
      valueOptions: statusOptions,
    },
    { field: "staff", headerName: "担当", flex: 1, editable: false },
    {
      field: "priority",
      headerName: "優先度",
      flex: 0.5,
      type: "boolean",
      editable: false,
    },
    {
      field: "url",
      headerName: "URL",
      flex: 1,
      editable: false,
      renderCell: (params) =>
        params.value ? (
          <a
            href={params.value}
            target="_blank"
            rel="noopener noreferrer"
            className="text-blue-600 underline"
          >
            リンク
          </a>
        ) : (
          "-"
        ),
    },
    { field: "remarks", headerName: "備考", flex: 1, editable: false },
  ];

  return (
      <div className="p-4">
        <h2 className="text-lg font-bold mb-4">クライアント一覧</h2>
        <div className="flex gap-4">
          <div className="w-1/3 bg-white p-4 rounded shadow">
            <div className="grid grid-cols-1 gap-2">
              <input
                className="border p-2 rounded"
                name="companyName"
                value={newClient.companyName}
                onChange={handleNewChange}
                placeholder="会社名"
              />
              <input
                className="border p-2 rounded"
                name="media"
                value={newClient.media}
                onChange={handleNewChange}
                placeholder="媒体"
              />
              <input
                className="border p-2 rounded"
                name="phoneNumber"
                value={newClient.phoneNumber}
                onChange={handleNewChange}
                placeholder="電話番号"
              />
              {!editingId && (
                <input
                  className="border p-2 rounded"
                  name="industry"
                  value={newClient.industry}
                  onChange={handleNewChange}
                  placeholder="業界"
                />
              )}
              <input
                className="border p-2 rounded"
                type="datetime-local"
                name="callDate"
                value={newClient.callDate}
                onChange={handleNewChange}
              />
              <input
                className="border p-2 rounded"
                type="datetime-local"
                name="nextCallDate"
                value={newClient.nextCallDate}
                onChange={handleNewChange}
              />
              {editingId ? null : (
                <input
                  className="border p-2 rounded"
                  type="number"
                  name="callCount"
                  value={newClient.callCount}
                  onChange={handleNewChange}
                  placeholder="架電回数"
                />
              )}
              <select
                className="border p-2 rounded"
                name="status"
                value={newClient.status}
                onChange={handleNewChange}
              >
                {statusOptions.map((s) => (
                  <option key={s} value={s}>
                    {s}
                  </option>
                ))}
              </select>
              <input
                className="border p-2 rounded"
                name="staff"
                value={newClient.staff}
                onChange={handleNewChange}
                placeholder="担当"
              />
              <input
                className="border p-2 rounded"
                name="url"
                value={newClient.url}
                onChange={handleNewChange}
                placeholder="会社URL"
              />
              {!editingId && (
                <input
                  className="border p-2 rounded"
                  name="address"
                  value={newClient.address}
                  onChange={handleNewChange}
                  placeholder="住所"
                />
              )}
              <textarea
                className="border p-2 rounded"
                name="remarks"
                value={newClient.remarks}
                onChange={handleNewChange}
                placeholder="備考"
              />
              <label className="flex items-center gap-2">
                <input
                  type="checkbox"
                  name="priority"
                  checked={newClient.priority}
                  onChange={handleNewChange}
                />
                優先度
              </label>
              {/* 過去備考（編集不可） */}
                {editingId && pastRemarks && (
                  <div className="border p-2 rounded bg-gray-100 text-sm">
                    <p className="font-semibold mb-1">過去の備考</p>
                    <pre className="whitespace-pre-wrap">{pastRemarks}</pre>
                  </div>
                )}
              <button
                className="bg-blue-600 text-white px-4 py-2 rounded"
                onClick={handleAddOrUpdate}
              >
                {editingId ? "更新" : "追加"}
              </button>
              {editingId && (
                <button
                  onClick={handleIncrementCallCount}
                  disabled={isForceDisabled}
                  className={`px-4 py-2 rounded text-white
        ${
          isForceDisabled
            ? "bg-gray-400 cursor-not-allowed text-gray-200"
            : "bg-indigo-600 hover:bg-indigo-700"
        }
      `}
                >
                  架電数 +1
                </button>
              )}
              <button
                className="bg-gray-400 text-white px-4 py-2 rounded"
                onClick={handleClearForm}
              >
                クリア
              </button>
            </div>
          </div>
          <div className="w-2/3">
            <div style={{ height: 700, width: "100%" }}>
              <DataGrid
                rows={rows}
                columns={columns}
                processRowUpdate={handleProcessRowUpdate}
                slots={{ toolbar: GridToolbar }}
                onRowClick={(params) => handleRowClick(params.row)}
              />
            </div>
            <button
              className="mt-2 bg-green-600 text-white px-4 py-2 rounded"
              onClick={handleSaveAll}
            >
              一括保存
            </button>
          </div>
        </div>
      </div>
    );
  }
  