// AuthContext.js
import React, { createContext, useEffect, useState, useContext } from "react";
import { auth } from "../api/firebase";
import { onAuthStateChanged } from "firebase/auth";
import axiosInstance from "../api/axiosInstance";

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [currentUser, setCurrentUser] = useState(null); // Firebaseの認証情報
  const [dbUser, setDbUserState] = useState(() => {
    // 初期値を localStorage から復元
    const saved = localStorage.getItem("dbUser");
    return saved ? JSON.parse(saved) : null;
  });
  const [loading, setLoading] = useState(true);

  // Firebase の認証状態監視
  useEffect(() => {
    const unsubscribe = onAuthStateChanged(auth, async (user) => {
      setCurrentUser(user);

      if (user) {
        // ページリロード時にサーバーサイドセッションを再確立する
        try {
          const res = await axiosInstance.post("/login/get-user-info", {
            userEmail: user.email,
            userId: user.uid,
          });
          if (res.data.resultStatus === true) {
            // セッション再確立成功：localStorage の dbUser も最新に更新
            setDbUserState({
              myCompanyCode: res.data.userCompanyCode,
              userName: res.data.userName,
              myteamcode: res.data.userTeamCode,
            });
            localStorage.setItem("dbUser", JSON.stringify({
              myCompanyCode: res.data.userCompanyCode,
              userName: res.data.userName,
              myteamcode: res.data.userTeamCode,
            }));
          } else {
            // DBにユーザーが存在しない場合はクリア
            setDbUserState(null);
            localStorage.removeItem("dbUser");
          }
        } catch (e) {
          console.error("セッション再確立エラー:", e);
        }
      } else {
        // ログアウトしたら dbUser もクリア
        setDbUserState(null);
        localStorage.removeItem("dbUser");
      }

      setLoading(false);
    });
    return unsubscribe;
  }, []);

  // dbUser を更新したときに localStorage に保存する
  const setDbUser = (user) => {
    setDbUserState(user);
    if (user) {
      localStorage.setItem("dbUser", JSON.stringify(user));
    } else {
      localStorage.removeItem("dbUser");
    }
  };

  if (loading) return <div>Loading...</div>;

  return (
    <AuthContext.Provider value={{ currentUser, dbUser, setDbUser }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
