import React, { createContext, useEffect, useState, useContext } from "react";
import { auth } from "../api/firebase";
import { onAuthStateChanged } from "firebase/auth";
import axios from "axios";

const API_BASE_URL = import.meta.env.VITE_API_HOST;

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [currentUser, setCurrentUser] = useState(null); // Firebaseユーザー
  const [dbUser, setDbUser] = useState(null);           // DBユーザー
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const unsubscribe = onAuthStateChanged(auth, async (user) => {
      setCurrentUser(user);

      if (user) {
        try {
          // uidやemailを使ってDBのユーザー情報を取得
          const res = await axios.get(`${API_BASE_URL}/login/get-user`, {
            params: { id: user.uid },
          });
          setDbUser(res.data); // DBのユーザー情報を保存
        } catch (err) {
          console.error("DBユーザー取得エラー:", err);
          setDbUser(null);
        }
      } else {
        setDbUser(null);
      }

      setLoading(false);
    });

    return unsubscribe;
  }, []);

  if (loading) return <div>Loading...</div>;

  return (
    <AuthContext.Provider value={{ currentUser, dbUser, setDbUser }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
