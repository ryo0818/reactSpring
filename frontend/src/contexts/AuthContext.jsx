// AuthContext.js
import React, { createContext, useEffect, useState, useContext } from "react";
import { auth } from "../api/firebase";
import { onAuthStateChanged } from "firebase/auth";
import axios from "axios";

const API_BASE_URL = import.meta.env.VITE_API_HOST;
const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [currentUser, setCurrentUser] = useState(null);  // Firebaseの認証情報
  const [dbUser, setDbUser] = useState(null);            // DBのユーザー情報
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const unsubscribe = onAuthStateChanged(auth, async (user) => {
      setCurrentUser(user);
      setLoading(false);
    });
    return unsubscribe;
  }, []);

  if (loading) return <div>Loading...</div>;

  return (
    <AuthContext.Provider value={{ currentUser, dbUser,setDbUser }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
