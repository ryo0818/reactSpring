// AuthContext.js
import React, { createContext, useEffect, useState, useContext } from "react";
import { auth } from "../api/firebase";
import { onAuthStateChanged } from "firebase/auth";

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [currentUser, setCurrentUser] = useState(null);  // Firebaseの認証情報
  const [dbUser, setDbUserState] = useState(() => {
    // 初期値を localStorage から復元
    const saved = localStorage.getItem("dbUser");
    return saved ? JSON.parse(saved) : null;
  });
  const [loading, setLoading] = useState(true);

  // Firebase の認証状態監視
  useEffect(() => {
    const unsubscribe = onAuthStateChanged(auth, (user) => {
      setCurrentUser(user);

      // ログアウトしたら dbUser もクリア
      if (!user) {
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
